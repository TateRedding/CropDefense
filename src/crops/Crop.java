package crops;

import static helps.Constants.Crops.getCropCost;
import static helps.Constants.Crops.getDefaultCooldown;
import static helps.Constants.Crops.getDefaultDamage;
import static helps.Constants.Crops.getDefaultProjectilesPerAttack;
import static helps.Constants.Crops.getDefaultRange;
import static helps.Constants.Crops.getDefaultTicksBetweenAttacks;
import static main.Game.TILE_SIZE;

import java.io.Serializable;

import enemies.Enemy;
import main.Game;

public class Crop implements Serializable {

	private static final long serialVersionUID = -6991147755069659746L;

	protected CropHandler cropHandler;
	protected Enemy target;

	protected float range, damage;

	protected int x, y, id, cropType, colorIndex;
	protected int damageTier = 1;
	protected int rangeTier = 1;
	protected int betweenAttackTick, betweenAttackTickLimit;
	protected int cooldownTickLimit, cooldownTick;
	protected int projectileCount, projectileLimit;

	protected boolean attacking;

	public Crop(int x, int y, int id, int cropType, int colorIndex, CropHandler cropHandler) {

		this.x = x;
		this.y = y;
		this.id = id;
		this.cropType = cropType;
		this.colorIndex = colorIndex;
		this.cropHandler = cropHandler;
		this.damage = getDefaultDamage(cropType);
		this.range = getDefaultRange(cropType);
		this.betweenAttackTickLimit = getDefaultTicksBetweenAttacks(cropType);
		this.betweenAttackTick = betweenAttackTickLimit;
		this.cooldownTickLimit = getDefaultCooldown(cropType);
		this.cooldownTick = cooldownTickLimit;
		this.projectileLimit = getDefaultProjectilesPerAttack(cropType);

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

	public Enemy getEnemyInRange() {

		int cropXMid = (x - TILE_SIZE / 2);
		int cropYMid = (y - TILE_SIZE / 2);

		for (Enemy e : cropHandler.getPlay().getEnemyHandler().getEnemies()) {
			if (e.isAlive()) {
				int enemyXMid = ((int) e.getBounds().x - TILE_SIZE / 2);
				int enemyYMid = ((int) e.getBounds().y - TILE_SIZE / 2);

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

	public int getId() {
		return id;
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

}
