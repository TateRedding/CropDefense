package enemies;

import static helps.Constants.Enemies.CROW_BOSS;

import java.awt.Point;
import java.util.ArrayList;

public class CrowBoss extends Enemy {

	private static final long serialVersionUID = -1167840212560122781L;

	public CrowBoss(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, CROW_BOSS, enemyHandler);
	}

}
