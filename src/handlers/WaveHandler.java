package handlers;

import static helps.Constants.Enemies.CROW;
import static helps.Constants.Enemies.CROW_BOSS;
import static helps.Constants.Enemies.MOLD;
import static helps.Constants.Enemies.MOLD_BOSS;
import static helps.Constants.Enemies.WORM;
import static helps.Constants.Enemies.WORM_BOSS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import entities.Enemy;
import gamestates.Play;
import helps.ImageLoader;
import main.Game;
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

		addWave(WORM, WORM, WORM);
		addWave(WORM, WORM, WORM, WORM, WORM);
		addWave(WORM, WORM, WORM, CROW);
		addWave(WORM, WORM, CROW, CROW);
		addWave(CROW, WORM, CROW, WORM, CROW);

		addWave(WORM, CROW, WORM, CROW, MOLD);
		addWave(WORM, CROW, MOLD, WORM, CROW, MOLD);
		addWave(WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM, WORM);
		addWave(WORM, WORM, WORM, CROW, CROW, CROW, MOLD, MOLD, MOLD);
		addWave(WORM, WORM_BOSS, WORM);

		addWave(WORM, WORM, WORM, WORM, WORM_BOSS);
		addWave(MOLD, MOLD, CROW, CROW, WORM, WORM, MOLD, MOLD, CROW, CROW, WORM, WORM);
		addWave(MOLD, MOLD, CROW, CROW, WORM_BOSS);
		addWave(WORM_BOSS, WORM_BOSS);
		addWave(CROW_BOSS);

		addWave(CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW, CROW);
		addWave(CROW, MOLD, CROW_BOSS, MOLD, CROW);
		addWave(WORM, WORM, CROW, CROW, CROW_BOSS, WORM, WORM);
		addWave(MOLD, CROW_BOSS, WORM_BOSS, MOLD);
		addWave(MOLD_BOSS);

		addWave(WORM_BOSS, WORM_BOSS, WORM_BOSS);
		addWave(WORM, CROW, MOLD, WORM_BOSS, CROW_BOSS, WORM_BOSS, MOLD, CROW, WORM);
		addWave(MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD, MOLD_BOSS);
		addWave(WORM_BOSS, CROW_BOSS, MOLD_BOSS);
		addWave(WORM_BOSS, WORM_BOSS, CROW_BOSS, CROW_BOSS, MOLD_BOSS, MOLD_BOSS);

	}

	private void addWave(Integer... enemies) {
		waves.add(new Wave(new ArrayList<Integer>(Arrays.asList(enemies))));
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
		play.setLifeLostThisWave(false);
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

		if (waves.size() > 0) {
			waves.remove(0);
			play.increaseWaveCount();
			if (!play.isLifeLostThisWave())
				play.gainLife();
		}

		if (waves.size() > 0)
			onBreak = true;
		else {
			play.setGameOver(true);
			int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.overlayBG.getHeight() / 2;
			play.setEndGameOverlay(new EndGameOverlay(play, EndGameOverlay.WIN, yStart));
		}

	}

	public ArrayList<Wave> getWaves() {
		return waves;
	}

	public float getTimeLeft() {
		float ticksLeft = breakTickLimit - breakTick;
		return ticksLeft / 60.0f;
	}

	public boolean isOnBreak() {
		return onBreak;
	}

}
