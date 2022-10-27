package enemies;

import static helps.Constants.Directions.DOWN;
import static helps.Constants.Directions.LEFT;
import static helps.Constants.Directions.RIGHT;
import static helps.Constants.Directions.UP;
import static helps.Constants.Enemies.CROW_BOSS;
import static helps.Constants.Enemies.MOLD_BOSS;
import static helps.Constants.Enemies.WORM_BOSS;
import static helps.Constants.Enemies.getSpeed;
import static helps.Constants.Enemies.getStartHealth;
import static helps.Constants.Enemies.Animations.DEATH;
import static helps.Constants.Enemies.Animations.MOVE;
import static main.Game.TILE_SIZE;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

import helps.ImageLoader;
import main.Game;

public abstract class Enemy implements Serializable {

	private static final long serialVersionUID = 7363623767719560422L;

	protected EnemyHandler enemyHandler;
	protected Rectangle bounds;
	protected ArrayList<Point> path;

	protected float x, y, speed;

	protected int xOffset, yOffset, id, enemyType;
	protected int health, maxHealth;
	protected int direction = -1;
	protected int animation;
	protected int animationTick, animationIndex;
	protected int maxAnimationTick = 10;

	protected boolean alive = true;
	protected boolean dying = false;

	public Enemy(ArrayList<Point> path, int id, int enemyType, EnemyHandler enemyHandler) {

		this.enemyType = enemyType;
		if (path != null) {
			this.path = initPath(path);
			this.x = (float) path.get(0).getX() * TILE_SIZE;
			this.y = (float) path.get(0).getY() * TILE_SIZE;
		}
		this.id = id;
		this.enemyHandler = enemyHandler;

		int width = ImageLoader.getEnemySprites(enemyType, RIGHT)[0][0].getWidth();
		int height = ImageLoader.getEnemySprites(enemyType, RIGHT)[0][0].getHeight();
		this.xOffset = width - TILE_SIZE;
		this.yOffset = height - TILE_SIZE;
		this.bounds = new Rectangle((int) x - xOffset, (int) y - yOffset, width, height);

		this.health = getStartHealth(enemyType);
		this.maxHealth = health;
		this.speed = getSpeed(enemyType);

	}

	protected ArrayList<Point> initPath(ArrayList<Point> path) {

		ArrayList<Point> newPath = new ArrayList<Point>();
		newPath.addAll(path);

		// Add extra off-screen point to end of path for boss type enemies.
		if (enemyType == CROW_BOSS || enemyType == MOLD_BOSS || enemyType == WORM_BOSS) {
			if (path.get(path.size() - 1).x == Game.GRID_WIDTH)
				newPath.add(new Point(Game.GRID_WIDTH + 1, path.get(path.size() - 1).y));
			else if (path.get(path.size() - 1).y == Game.GRID_HEIGHT)
				newPath.add(new Point(path.get(path.size() - 1).x, Game.GRID_HEIGHT + 1));
		}

		return newPath;

	}

	protected void update() {

		if (alive)
			animation = MOVE;
		else if (dying)
			animation = DEATH;

		if (alive || dying) {
			animationTick++;
			if (animationTick >= maxAnimationTick) {
				updateAnimationIndex();
				animationTick = 0;
			}
		}

	}

	public void hurt(float dmg) {

		this.health -= (int) dmg;

		if (health <= 0) {
			alive = false;
			dying = true;
			animationIndex = 0;
			enemyHandler.getPlay().rewardPlayer(enemyType);
		}

	}

	public void kill() {

		// Kill the enemy when it reaches the end.
		alive = false;
		dying = false;
		health = 0;

		int hurtAmount = 1;
		if (enemyType == CROW_BOSS || enemyType == MOLD_BOSS || enemyType == WORM_BOSS)
			hurtAmount = 3;
		enemyHandler.getPlay().hurtPlayer(hurtAmount);

	}

	protected void move() {

		if (path == null || path.size() <= 0)
			return;

		direction = getDirectionToMove(path.get(0));
		moveInDirection();

		// Check if Entity has reached the current path point based on movement speed
		float currentX = path.get(0).x * TILE_SIZE;
		float currentY = path.get(0).y * TILE_SIZE;

		// Round to two decimal places to get rid of any floating point errors
		float roundX = Math.round(x * 100) / 100.0f;
		float roundY = Math.round(y * 100) / 100.0f;

		if (roundX >= currentX - speed && roundX <= currentX + speed && roundY >= currentY - speed
				&& roundY <= currentY + speed) {
			x = currentX;
			y = currentY;
			path.remove(0);

			if (path.size() == 0)
				kill();

		}

	}

	private int getDirectionToMove(Point point) {

		int pX = point.x * TILE_SIZE;
		int pY = point.y * TILE_SIZE;

		int dir = -1;

		if (pX == x && pY < y)
			dir = UP;
		else if (pX < x && pY == y)
			dir = LEFT;
		else if (pX > x && pY == y)
			dir = RIGHT;
		else if (pX == x && pY > y)
			dir = DOWN;

		return dir;

	}

	private void moveInDirection() {

		switch (direction) {
		case UP:
			this.y -= speed;
			break;
		case LEFT:
			this.x -= speed;
			break;
		case RIGHT:
			this.x += speed;
			break;
		case DOWN:
			this.y += speed;
			break;
		default:
			break;
		}

		updateHitbox();

	}

	private void updateHitbox() {

		this.bounds.x = (int) x - xOffset;
		this.bounds.y = (int) y - yOffset;

	}

	public void updateAnimationIndex() {

		animationIndex++;

		if (dying)
			if (animationIndex >= ImageLoader.getEnemyAnimationFrameCount(enemyType, DEATH)) {
				dying = false;
				return;
			}

		if (animationIndex >= ImageLoader.getEnemyAnimationFrameCount(enemyType, MOVE))
			animationIndex = 0;

	}

	public float getHealthPercentage() {
		return (float) health / (float) maxHealth;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getEnemyType() {
		return enemyType;
	}

	public int getDirection() {
		return direction;
	}

	public int getAnimation() {
		return animation;
	}

	public int getAnimationIndex() {
		return animationIndex;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isDying() {
		return dying;
	}
}
