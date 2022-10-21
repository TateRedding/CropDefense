package enemies;

import static helps.Constants.Enemies.MOLD;

import java.awt.Point;
import java.util.ArrayList;

public class Mold extends Enemy {

	private static final long serialVersionUID = -7084227522356397836L;

	public Mold(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, MOLD, enemyHandler);
	}

}
