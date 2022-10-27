package enemies;

import static helps.Constants.Enemies.MOLD_BOSS;

import java.awt.Point;
import java.util.ArrayList;

public class MoldBoss extends Enemy {

	private static final long serialVersionUID = -8570989094853864536L;

	public MoldBoss(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, MOLD_BOSS, enemyHandler);
	}

}
