package projectiles;

import static helps.Constants.Projectiles.SEED;

public class Seed extends Projectile {

	private static final long serialVersionUID = -6514897426877504307L;

	public Seed(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int id, int colorIndex,
			ProjectileHandler projectileHandler) {
		super(x, y, xSpeed, ySpeed, damage, rotation, id, SEED, colorIndex, projectileHandler);
	}

}
