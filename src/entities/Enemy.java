package entities;

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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import helps.ImageLoader;
import main.Game;

public class Enemy implements Serializable {

	private static final long serialVersionUID = 7363623767719560422L;

	private EnemyHandler enemyHandler;
	private Rectangle bounds;
	private ArrayList<Point> path;

	private float x, y, speed;

	private int xOffset, yOffset, enemyType;
	private int health, maxHealth;
	private int direction = -1;
	private int animation;
	private int animationTick, animationIndex;
	private int maxAnimationTick = 10;

	private boolean alive = true;
	private boolean dying = false;

	public Enemy(ArrayList<Point> path, int enemyType, EnemyHandler enemyHandler) {

		this.enemyType = enemyType;
		if (path != null) {
			this.path = initPath(path);
			this.x = (float) path.get(0).getX() * TILE_SIZE;
			this.y = (float) path.get(0).getY() * TILE_SIZE;
		}

		this.enemyHandler = enemyHandler;

		int width = ImageLoader.getEnemySprites(enemyType)[0][0].getWidth();
		int height = ImageLoader.getEnemySprites(enemyType)[0][0].getHeight();
		if (isBoss()) {
			width *= 2;
			height *= 2;
		}

		this.xOffset = width - TILE_SIZE;
		this.yOffset = height - TILE_SIZE;
		this.bounds = new Rectangle((int) x - xOffset, (int) y - yOffset, width, height);

		this.health = getStartHealth(enemyType);
		this.maxHealth = health;
		this.speed = getSpeed(enemyType);

	}

	private ArrayList<Point> initPath(ArrayList<Point> path) {

		ArrayList<Point> newPath = new ArrayList<Point>();
		newPath.addAll(path);

		// Add extra off-screen point to end of path for boss type enemies.
		if (isBoss()) {
			if (path.get(path.size() - 1).x == Game.GRID_WIDTH)
				newPath.add(new Point(Game.GRID_WIDTH + 1, path.get(path.size() - 1).y));
			else if (path.get(path.size() - 1).y == Game.GRID_HEIGHT)
				newPath.add(new Point(path.get(path.size() - 1).x, Game.GRID_HEIGHT + 1));
		}

		return newPath;

	}

	public void update() {

		if (alive)
			animation = MOVE;
		else
			animation = DEATH;

		animationTick++;
		if (animationTick >= maxAnimationTick) {
			updateAnimationIndex();
			animationTick = 0;
		}

		if (alive)
			move();

	}

	public void draw(Graphics g) {

		BufferedImage sprite = ImageLoader.getEnemySprites(enemyType)[animation][animationIndex];

		g.drawImage(sprite, bounds.x + flipX(), bounds.y, bounds.width * flipW(), bounds.height, null);

	}

	public void drawHealthBar(Graphics g) {

		int hpBarWidth = 20;
		if (isBoss())
			hpBarWidth = 50;

		int healthWidth = (int) (hpBarWidth * getHealthPercentage());

		int x = bounds.x + (bounds.width - hpBarWidth) / 2;
		int y = bounds.y;

		g.setColor(Color.RED);
		g.fillRect(x, y, healthWidth, 3);

		g.setColor(Color.BLACK);
		g.drawRect(x - 1, y - 1, hpBarWidth, 4);

		g.fillRect(x + healthWidth, y, hpBarWidth - healthWidth, 3);

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
		if (isBoss())
			hurtAmount = 3;
		enemyHandler.getPlay().hurtPlayer(hurtAmount);

	}

	private void move() {

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

	public int flipX() {

		if (direction == LEFT)
			return bounds.width;
		else
			return 0;

	}

	public int flipW() {

		if (direction == LEFT)
			return -1;
		else
			return 1;

	}

	public boolean isBoss() {

		if (enemyType == CROW_BOSS || enemyType == MOLD_BOSS || enemyType == WORM_BOSS)
			return true;
		return false;

	}

	public float getHealthPercentage() {
		return (float) health / (float) maxHealth;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isDying() {
		return dying;
	}
}
