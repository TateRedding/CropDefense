package projectiles;

import static helps.Constants.Projectiles.KERNEL;

public class Kernel extends Projectile {

	private static final long serialVersionUID = 9172761033319430824L;

	public Kernel(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int id, int colorIndex,
			ProjectileHandler projectileHandler) {
		super(x, y, xSpeed, ySpeed, damage, rotation, id, KERNEL, colorIndex, projectileHandler);
	}

}
