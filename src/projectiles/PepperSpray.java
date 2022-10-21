package projectiles;

import static helps.Constants.Projectiles.SPRAY;

public class PepperSpray extends Projectile {

	private static final long serialVersionUID = 3551354832940947270L;

	public PepperSpray(float x, float y, float xSpeed, float ySpeed, float damage, float rotation, int id,
			int colorIndex, ProjectileHandler projectileHandler) {
		super(x, y, xSpeed, ySpeed, damage, rotation, id, SPRAY, colorIndex, projectileHandler);
	}

}
