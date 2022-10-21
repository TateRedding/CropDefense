package handlers;

import static helps.Constants.Enemies.CROW;
import static helps.Constants.Enemies.MOLD;
import static helps.Constants.Enemies.WORM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import enemies.Enemy;
import gamestates.Play;
import objects.Wave;
import ui.EndGameOverlay;

public class WaveHandler implements Serializable {

	private static final long serialVersionUID = -7985085213176327119L;

	private Play play;
	private ArrayList<Wave> waves = new ArrayList<>();
	private int enemySpawnTickLimit = 250;
	private int enemySpawnTick = enemySpawnTickLimit;
	private int breakTickLimit = 300;
	private int breakTick = 0;
	private boolean onBreak = true;

	public WaveHandler(Play play) {

		this.play = play;
		createWaves();

	}

	private void createWaves() {

		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM, WORM, WORM))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM, CROW))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, CROW, CROW))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(CROW, CROW, CROW))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM, CROW, MOLD))));
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM, WORM, WORM, CROW, CROW))));
		waves.add(
				new Wave(new ArrayList<Integer>(Arrays.asList(WORM, WORM, WORM, WORM, WORM, CROW, CROW, MOLD, MOLD))));

	}

	public void update() {

		if (waves.size() > 0) {

			if (onBreak)
				if (breakTick >= breakTickLimit)
					startNextWave();
				else
					breakTick++;
			else {
				if (moreEnemiesLeftToSpawn())
					if (enemySpawnTick >= enemySpawnTickLimit)
						spawnNextEnemy();
					else
						enemySpawnTick++;
				else if (allEnemiesAreDead())
					endWave();
			}

		}

	}

	private void startNextWave() {

		play.getEnemyHandler().getEnemies().clear();
		play.setCurrentPathIndex(0);
		onBreak = false;
		breakTick = 0;

	}

	private boolean moreEnemiesLeftToSpawn() {

		if (waves.size() > 0)
			if (waves.get(0).getEnemyList().size() > 0)
				return true;
		return false;

	}

	private void spawnNextEnemy() {

		play.getEnemyHandler().spawnEnemy(waves.get(0).getEnemyList().get(0));
		waves.get(0).getEnemyList().remove(0);
		enemySpawnTick = 0;

	}

	private boolean allEnemiesAreDead() {

		for (Enemy e : play.getEnemyHandler().getEnemies())
			if (e.isAlive())
				return false;
		return true;

	}

	private void endWave() {

		if (waves.size() > 0)
			waves.remove(0);
		if (waves.size() > 0)
			onBreak = true;
		else {
			play.setGameOver(true);
			play.setEndGameOverlay(new EndGameOverlay(play, EndGameOverlay.WIN));
		}

	}

	public float getTimeLeft() {
		float ticksLeft = breakTickLimit - breakTick;
		return ticksLeft / 60.0f;
	}

	public boolean isOnBreak() {
		return onBreak;
	}

}
