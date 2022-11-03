package entities;

import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static helps.Constants.Projectiles.KERNEL;
import static helps.Constants.Projectiles.SEED;
import static helps.Constants.Projectiles.SPRAY;
import static main.Game.TILE_SIZE;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import gamestates.Play;

public class ProjectileHandler implements Serializable {

	private static final long serialVersionUID = 8255547914164262424L;

	private Play play;
	private ArrayList<Projectile> projectiles = new ArrayList<>();

	public ProjectileHandler(Play play) {
		this.play = play;
	}

	public void update() {

		for (Projectile p : projectiles)
			if (p.isActive())
				p.update();

	}

	public void draw(Graphics g) {

		for (Projectile p : projectiles)
			if (p.isActive())
				p.draw(g);

	}

	public void newProjectile(Crop c, Enemy e) {

		int projectileType = getProjectileType(c);

		int xDist = ((int) (c.getX()) + TILE_SIZE / 2) - (e.getBounds().x + e.getBounds().width / 2);
		int yDist = ((int) (c.getY()) + TILE_SIZE / 2) - (e.getBounds().y + e.getBounds().height / 2);
		int totDist = Math.abs(xDist) + Math.abs(yDist);

		float xPer = (float) Math.abs(xDist) / totDist;
		if (projectileType == SPRAY) {
			if (c.getProjectileCount() % 3 == 1)
				xPer += .15;
			else if (c.getProjectileCount() % 3 == 2)
				xPer -= .15;
		}

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

		float xPos = (float) (c.getX() + TILE_SIZE / 2);
		float yPos = (float) (c.getY() + TILE_SIZE / 2);

		for (Projectile p : projectiles)
			if (!p.isActive())
				if (p.getProjectileType() == projectileType) {
					int colorIndex = 0;
					if (c.getCropType() == CORN)
						colorIndex = c.getColorIndex();
					p.reuse(xPos, yPos, xSpeed, ySpeed, c.getDamage(), rotation, colorIndex);
					return;
				}

		projectiles.add(new Projectile(xPos, yPos, xSpeed, ySpeed, c.getDamage(), rotation, projectileType,
				c.getColorIndex(), this));

	}

	private int getProjectileType(Crop c) {

		switch (c.getCropType()) {

		case BELL_PEPPER, TOMATO:
			return SEED;
		case CHILI:
			return SPRAY;
		case CORN:
			return KERNEL;
		}

		return -1;

	}

	public Play getPlay() {
		return play;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

}
