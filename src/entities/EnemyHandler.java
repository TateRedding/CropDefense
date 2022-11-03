package entities;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import gamestates.Play;

public class EnemyHandler implements Serializable {

	private static final long serialVersionUID = -3384663047989900176L;

	private Play play;
	private ArrayList<Enemy> enemies = new ArrayList<>();

	public EnemyHandler(Play play) {
		this.play = play;

	}

	public void update() {

		for (Enemy e : enemies)
			if (e.isAlive() || e.isDying())
				e.update();

	}

	public void draw(Graphics g) {

		for (Enemy e : enemies) {
			if (e.isAlive() || e.isDying()) {
				e.draw(g);
				if (!e.isDying())
					e.drawHealthBar(g);
			}
		}

	}

	public void spawnEnemy(int enemyType) {

		if (play.getPaths().size() == 0)
			return;

		enemies.add(new Enemy(play.getNextPath(), enemyType, this));

	}

	public Play getPlay() {
		return play;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

}
