package projectiles;

import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static helps.Constants.Projectiles.KERNEL;
import static helps.Constants.Projectiles.SEED;
import static helps.Constants.Projectiles.SPRAY;
import static main.Game.SCREEN_HEIGHT;
import static main.Game.SCREEN_WIDTH;
import static main.Game.TILE_SIZE;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import crops.Crop;
import enemies.Enemy;
import gamestates.Play;
import helps.ImageLoader;

public class ProjectileHandler implements Serializable {

	private static final long serialVersionUID = 8255547914164262424L;

	private Play play;
	private ArrayList<Projectile> projectiles = new ArrayList<>();

	private int id;

	public ProjectileHandler(Play play) {
		this.play = play;
	}

	public void update() {

		for (Projectile p : projectiles)
			if (p.isActive()) {
				p.move();
				if (isHittingEnemy(p)) {
					p.setActive(false);
				} else if (isOutsideBounds(p)) {
					p.setActive(false);
				}
			}

	}

	private boolean isHittingEnemy(Projectile p) {

		for (Enemy e : play.getEnemyHandler().getEnemies())
			if (e.isAlive())
				if (e.getBounds().intersects(p.getBounds())) {
					e.hurt(p.getDamage());
					return true;
				}

		return false;

	}

	private boolean isOutsideBounds(Projectile p) {

		if (p.getBounds().x >= 0 && p.getBounds().x <= SCREEN_WIDTH && p.getBounds().y >= 0
				&& p.getBounds().y <= SCREEN_HEIGHT)
			return false;

		return true;

	}

	public void draw(Graphics g) {

		for (Projectile p : projectiles)
			if (p.isActive()) {
				drawProjectile(p, g);
			}

	}

	private void drawProjectile(Projectile p, Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xMid = p.getBounds().x + p.getBounds().width / 2;
		int yMid = p.getBounds().y + p.getBounds().height / 2;
		g2d.rotate(Math.toRadians(p.getRotation()), xMid, yMid);

		g2d.drawImage(ImageLoader.projectileSprites[p.getProjectileType()][p.getColorIndex()], p.getBounds().x,
				p.getBounds().y, null);
		g2d.rotate(-Math.toRadians(p.getRotation()), xMid, yMid);

	}

	public void newProjectile(Crop c, Enemy e) {

		int projectileType = getProjectileType(c);

		int xDist = ((int) (c.getX()) + TILE_SIZE / 2) - (e.getBounds().x + e.getBounds().width / 2);
		int yDist = ((int) (c.getY()) + TILE_SIZE / 2) - (e.getBounds().y + e.getBounds().height / 2);
		int totDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totDist;

		float xSpeed = xPer * helps.Constants.Projectiles.getSpeed(projectileType);
		float ySpeed = helps.Constants.Projectiles.getSpeed(projectileType) - xSpeed;

		if ((c.getX() + TILE_SIZE / 2) > (e.getBounds().x + e.getBounds().width / 2))
			xSpeed *= -1;
		if ((c.getY() + TILE_SIZE / 2) > (e.getBounds().y + e.getBounds().height / 2))
			ySpeed *= -1;

		float arcValue = (float) Math.atan(yDist / (float) xDist);
		float rotation = (float) Math.toDegrees(arcValue);

		if (xDist < 0)
			rotation += 180;

		for (Projectile p : projectiles)
			if (!p.isActive())
				if (p.getProjectileType() == projectileType) {
					int colorIndex = 0;
					if (c.getCropType() == CORN)
						colorIndex = c.getColorIndex();
					p.reuse(c.getX() + (TILE_SIZE / 2), c.getY() + (TILE_SIZE / 2), xSpeed, ySpeed, c.getDamage(),
							rotation, colorIndex);
					return;
				}

		switch (projectileType) {
		case KERNEL:
			projectiles.add(new Kernel((float) c.getX() + (TILE_SIZE / 2), (float) c.getY() + (TILE_SIZE / 2), xSpeed,
					ySpeed, c.getDamage(), rotation, id++, c.getColorIndex(), this));
			break;
		case SEED:
			projectiles.add(new Seed((float) c.getX() + (TILE_SIZE / 2), (float) c.getY() + (TILE_SIZE / 2), xSpeed,
					ySpeed, c.getDamage(), rotation, id++, 0, this));
			break;
		case SPRAY:
			projectiles.add(new PepperSpray((float) c.getX() + (TILE_SIZE / 2), (float) c.getY() + (TILE_SIZE / 2),
					xSpeed, ySpeed, c.getDamage(), rotation, id++, 0, this));
			break;
		}

	}

	private int getProjectileType(Crop c) {

		switch (c.getCropType()) {

		case BELL_PEPPER:
			return SEED;
		case TOMATO:
			return SEED;
		case CHILI:
			return SPRAY;
		case CORN:
			return KERNEL;
		}

		return -1;

	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

}
