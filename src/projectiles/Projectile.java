package projectiles;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

import helps.ImageLoader;

public abstract class Projectile implements Serializable {

	private static final long serialVersionUID = -5724194940325394945L;

	protected ProjectileHandler projectileHandler;
	protected Point2D.Float position;
	protected Rectangle bounds;

	protected float xSpeed, ySpeed, damage, rotation;

	protected int id, projectileType, colorIndex;

	protected boolean active = true;

	public Projectile(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int id,
			int projectileType, int colorIndex, ProjectileHandler projectileHandler) {

		position = new Point2D.Float(x, y);
		int width = ImageLoader.projectileSprites[0][0].getWidth();
		int height = ImageLoader.projectileSprites[0][0].getHeight();
		this.bounds = new Rectangle((int) position.x, (int) position.y, width, height);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.damage = damage;
		this.rotation = rotation;
		this.id = id;
		this.projectileType = projectileType;
		this.colorIndex = colorIndex;
		this.projectileHandler = projectileHandler;

	}

	public void reuse(int x, int y, float xSpeed, float ySpeed, float damage, float rotation, int colorIndex) {

		position = new Point2D.Float(x, y);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.damage = damage;
		this.rotation = rotation;
		this.colorIndex = colorIndex;
		active = true;

	}

	public void move() {

		position.x += xSpeed;
		position.y += ySpeed;

		this.bounds.x = (int) position.x;
		this.bounds.y = (int) position.y;

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

	public Rectangle getBounds() {
		return bounds;
	}

	public float getDamage() {
		return damage;
	}

	public float getRotation() {
		return rotation;
	}

	public int getProjectileType() {
		return projectileType;
	}

	public int getColorIndex() {
		return colorIndex;
	}

}
