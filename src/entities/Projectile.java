package entities;

import static main.Game.SCREEN_HEIGHT;
import static main.Game.SCREEN_WIDTH;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

import helps.ImageLoader;

public class Projectile implements Serializable {

	private static final long serialVersionUID = -5724194940325394945L;

	private ProjectileHandler projectileHandler;
	private Point2D.Float position;
	private Rectangle bounds;

	private float xSpeed, ySpeed, damage, rotation;

	private int projectileType, colorIndex;

	private boolean active = true;

	public Projectile(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int projectileType,
			int colorIndex, ProjectileHandler projectileHandler) {

		position = new Point2D.Float(x, y);
		int width = ImageLoader.projectileSprites[0][0].getWidth();
		int height = ImageLoader.projectileSprites[0][0].getHeight();
		this.bounds = new Rectangle((int) position.x, (int) position.y, width, height);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.damage = damage;
		this.rotation = rotation;
		this.projectileType = projectileType;
		this.colorIndex = colorIndex;
		this.projectileHandler = projectileHandler;

	}

	public void reuse(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int colorIndex) {

		position = new Point2D.Float(x, y);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.damage = damage;
		this.rotation = rotation;
		this.colorIndex = colorIndex;
		active = true;

	}

	public void update() {

		move();
		if (isHittingEnemy() || isOutsideBounds())
			active = false;

	}

	public void draw(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xMid = bounds.x + bounds.width / 2;
		int yMid = bounds.y + bounds.height / 2;
		g2d.rotate(Math.toRadians(rotation), xMid, yMid);

		g2d.drawImage(ImageLoader.projectileSprites[projectileType][colorIndex], bounds.x, bounds.y, null);
		g2d.rotate(-Math.toRadians(rotation), xMid, yMid);

	}

	public void move() {

		position.x += xSpeed;
		position.y += ySpeed;

		this.bounds.x = (int) position.x;
		this.bounds.y = (int) position.y;

	}

	private boolean isHittingEnemy() {

		for (Enemy e : projectileHandler.getPlay().getEnemyHandler().getEnemies())
			if (e.isAlive())
				if (e.getBounds().intersects(bounds)) {
					e.hurt(damage);
					return true;
				}

		return false;

	}

	private boolean isOutsideBounds() {

		if (bounds.x < 0 || bounds.x > SCREEN_WIDTH || bounds.y < 0 || bounds.y > SCREEN_HEIGHT)
			return true;

		return false;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Point2D.Float getPosition() {
		return position;
	}

	public float getDamage() {
		return damage;
	}

	public int getProjectileType() {
		return projectileType;
	}

}
