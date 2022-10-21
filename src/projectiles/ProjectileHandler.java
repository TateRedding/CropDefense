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
				if (e.getBounds().contains(p.getPosition())) {
					e.hurt(p.getDamage());
					return true;
				}

		return false;

	}

	private boolean isOutsideBounds(Projectile p) {

		if (p.getPosition().x >= 0 && p.getPosition().x <= SCREEN_WIDTH && p.getPosition().y >= 0
				&& p.getPosition().y <= SCREEN_HEIGHT)
			return false;

		return true;

	}

	public void draw(Graphics g) {

		for (Projectile p : projectiles)
			if (p.isActive())
				drawProjectile(p, g);

	}

	private void drawProjectile(Projectile p, Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.translate(p.getPosition().x, p.getPosition().y);
		g2d.rotate(Math.toRadians(p.getRotation()));

		g2d.drawImage(ImageLoader.projectileSprites[p.getProjectileType()][p.getColorIndex()], -(TILE_SIZE / 4),
				-(TILE_SIZE / 4), TILE_SIZE / 2, TILE_SIZE / 2, null);
		g2d.rotate(-Math.toRadians(p.getRotation()));
		g2d.translate(-p.getPosition().x, -p.getPosition().y);

	}

	public void newProjectile(Crop c, Enemy e) {

		int projectileType = getProjectileType(c);

		int xDist = (int) (c.getX() - e.getBounds().x);
		int yDist = (int) (c.getY() - e.getBounds().y);
		int totDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totDist;

		float xSpeed = xPer * helps.Constants.Projectiles.getSpeed(projectileType);
		float ySpeed = helps.Constants.Projectiles.getSpeed(projectileType) - xSpeed;

		if (c.getX() > e.getBounds().x)
			xSpeed *= -1;
		if (c.getY() > e.getBounds().y)
			ySpeed *= -1;

		float rotation = 0;
		float arcValue = (float) Math.atan(yDist / (float) xDist);
		rotation = (float) Math.toDegrees(arcValue);

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

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

}
