package objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Wave implements Serializable {

	private static final long serialVersionUID = -1594180275988509187L;

	private ArrayList<Integer> enemyList;

	public Wave(ArrayList<Integer> enemyList) {
		this.enemyList = enemyList;
	}

	public ArrayList<Integer> getEnemyList() {
		return enemyList;
	}

}
