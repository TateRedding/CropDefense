package enemies;

import static helps.Constants.Enemies.CROW;
import static helps.Constants.Enemies.MOLD;
import static helps.Constants.Enemies.WORM;
import static main.Game.TILE_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import gamestates.Play;
import helps.ImageLoader;

public class EnemyHandler implements Serializable {

	private static final long serialVersionUID = -3384663047989900176L;

	private Play play;
	private ArrayList<Enemy> enemies = new ArrayList<>();

	private int hpBarWidth = 20;
	private int id;

	public EnemyHandler(Play play) {
		this.play = play;
	}

	public void update() {

		for (Enemy e : enemies) {
			if (e.isAlive() || e.isDying())
				e.update();
			if (e.isAlive() && !e.isDying())
				e.move();
		}

	}

	public void draw(Graphics g) {

		for (Enemy e : enemies) {
			if (e.isAlive() || e.isDying())
				drawEnemy(e, g);
			if (e.isAlive() && !e.isDying()) {
				drawHealthBar(e, g);
			}
		}

	}

	private void drawEnemy(Enemy e, Graphics g) {
		g.drawImage(ImageLoader.getEnemySprites(e.getEnemyType(), e.getDirection())[e.getAnimation()][e
				.getAnimationIndex()], e.getBounds().x, e.getBounds().y, TILE_SIZE, TILE_SIZE, null);
	}

	private void drawHealthBar(Enemy e, Graphics g) {

		int healthWidth = getHealthWidth(e);

		int x = e.getBounds().x + (TILE_SIZE - hpBarWidth) / 2;
		int y = e.getBounds().y;

		g.setColor(Color.RED);
		g.fillRect(x, y, healthWidth, 3);

		g.setColor(Color.BLACK);
		g.drawRect(x - 1, y - 1, hpBarWidth, 4);

		g.fillRect(x + healthWidth, y, hpBarWidth - healthWidth, 3);

	}

	public void spawnEnemy(int enemyType) {

		if (play.getPaths().size() == 0)
			return;

		switch (enemyType) {
		case CROW:
			enemies.add(new Crow(play.getNextPath(), id++, this));
			break;
		case MOLD:
			enemies.add(new Mold(play.getNextPath(), id++, this));
			break;
		case WORM:
			enemies.add(new Worm(play.getNextPath(), id++, this));
			break;
		default:
			break;
		}
	}

	private int getHealthWidth(Enemy e) {
		return (int) (hpBarWidth * e.getHealthPercentage());
	}

	public Play getPlay() {
		return play;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

}
