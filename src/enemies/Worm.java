package enemies;

import static helps.Constants.Enemies.WORM;

import java.awt.Point;
import java.util.ArrayList;

public class Worm extends Enemy {

	private static final long serialVersionUID = -7489707470181701588L;

	public Worm(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, WORM, enemyHandler);
	}

}
