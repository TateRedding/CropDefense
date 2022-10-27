package enemies;

import static helps.Constants.Enemies.WORM_BOSS;

import java.awt.Point;
import java.util.ArrayList;

public class WormBoss extends Enemy {

	private static final long serialVersionUID = -7241971867397877472L;

	public WormBoss(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, WORM_BOSS, enemyHandler);
	}

}
