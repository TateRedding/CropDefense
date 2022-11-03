package entities;

import static helps.Constants.Crops.getCropCost;
import static helps.Constants.Crops.getCooldown;
import static helps.Constants.Crops.getDefaultDamage;
import static helps.Constants.Crops.getProjectilesPerAttack;
import static helps.Constants.Crops.getDefaultRange;
import static helps.Constants.Crops.getTicksBetweenAttacks;
import static main.Game.TILE_SIZE;

import java.awt.Graphics;
import java.io.Serializable;

import helps.ImageLoader;
import main.Game;

public class Crop implements Serializable {

	private static final long serialVersionUID = -6991147755069659746L;

	private CropHandler cropHandler;
	private Enemy target;

	private float range, damage;

	private int x, y, cropType, colorIndex;
	private int damageTier = 1;
	private int rangeTier = 1;
	private int betweenAttackTick, betweenAttackTickLimit;
	private int cooldownTickLimit, cooldownTick;
	private int projectileCount, projectileLimit;

	private boolean attacking;

	public Crop(int x, int y, int cropType, int colorIndex, CropHandler cropHandler) {

		this.x = x;
		this.y = y;
		this.cropType = cropType;
		this.colorIndex = colorIndex;
		this.cropHandler = cropHandler;
		this.damage = getDefaultDamage(cropType);
		this.range = getDefaultRange(cropType);
		this.betweenAttackTickLimit = getTicksBetweenAttacks(cropType);
		this.betweenAttackTick = betweenAttackTickLimit;
		this.cooldownTickLimit = getCooldown(cropType);
		this.cooldownTick = cooldownTickLimit;
		this.projectileLimit = getProjectilesPerAttack(cropType);

	}

	public void update() {

		cooldownTick++;
		if (cooldownTick >= cooldownTickLimit) {
			cooldownTick = cooldownTickLimit;
			if (getEnemyInRange() != null) {
				attacking = true;
				target = getEnemyInRange();
			}
		}

		if (attacking) {
			if (projectileCount < projectileLimit) {
				betweenAttackTick++;
				if (betweenAttackTick >= betweenAttackTickLimit) {
					cropHandler.attackEnemy(this, target);
					projectileCount++;
					betweenAttackTick = 0;
				}
			} else {
				attacking = false;
				projectileCount = 0;
				cooldownTick = 0;
			}
		}

	}

	public void draw(Graphics g) {
		g.drawImage(ImageLoader.getCropSprites(cropType)[colorIndex], x, y, null);
	}

	public Enemy getEnemyInRange() {

		int cropXMid = (x + TILE_SIZE / 2);
		int cropYMid = (y + TILE_SIZE / 2);

		for (Enemy e : cropHandler.getPlay().getEnemyHandler().getEnemies()) {
			if (e.isAlive()) {
				int enemyXMid = (((int) e.getBounds().x) + (e.getBounds().width / 2));
				int enemyYMid = (((int) e.getBounds().y) + (e.getBounds().height / 2));
				if (enemyXMid < 0 || enemyXMid > Game.SCREEN_WIDTH || enemyYMid < 0 || enemyYMid > Game.SCREEN_HEIGHT)
					return null;

				double xDist = cropXMid - enemyXMid;
				double yDist = cropYMid - enemyYMid;

				if (Math.sqrt((xDist * xDist) + (yDist * yDist)) <= (double) range)
					return e;

			}
		}

		return null;

	}

	public int getUpgradeCost() {

		int upgradesSoFar = (rangeTier - 1) + (damageTier - 1);
		return (int) ((getCropCost(cropType) * 0.3f) * (upgradesSoFar + 1));

	}

	public int getHarvestAmount() {

		int upgradeReturn = (rangeTier - 1) + (damageTier - 1) * getUpgradeCost() / 2;
		return getCropCost(cropType) / 2 + upgradeReturn;

	}

	public void upgradeDamage() {

		damageTier++;
		damage *= 1.25f;

	}

	public void upgradeRange() {

		this.rangeTier++;
		range *= 1.25f;

	}

	public float getRange() {
		return range;
	}

	public float getDamage() {
		return damage;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getCropType() {
		return cropType;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	public int getDamageTier() {
		return damageTier;
	}

	public int getRangeTier() {
		return rangeTier;
	}

	public int getProjectileCount() {
		return projectileCount;
	}

}
