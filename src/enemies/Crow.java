package enemies;

import static helps.Constants.Enemies.CROW;

import java.awt.Point;
import java.util.ArrayList;

public class Crow extends Enemy {

	private static final long serialVersionUID = -2689622603763409924L;

	public Crow(ArrayList<Point> path, int id, EnemyHandler enemyHandler) {
		super(path, id, CROW, enemyHandler);
	}

}
