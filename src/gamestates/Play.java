package gamestates;

import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static helps.Constants.Crops.getCooldown;
import static helps.Constants.Crops.getCropCost;
import static helps.Constants.Crops.getDefaultRange;
import static helps.Constants.Enemies.getReward;
import static helps.Constants.Tiles.GRASS;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entities.Crop;
import entities.CropHandler;
import entities.Enemy;
import entities.EnemyHandler;
import entities.ProjectileHandler;
import handlers.WaveHandler;
import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import objects.Tile;
import pathfinding.AStar;
import ui.ActionBar;
import ui.EndGameOverlay;

public class Play extends MapState implements StateMethods, Serializable {

	private static final long serialVersionUID = -1858111861612350590L;

	private ActionBar actionBar;
	private Crop selectedCrop;
	private CropHandler cropHandler;
	private EnemyHandler enemyHandler;
	private EndGameOverlay endGameOverlay;
	private ProjectileHandler projectileHandler;
	private WaveHandler waveHandler;
	private ArrayList<ArrayList<Point>> paths = new ArrayList<ArrayList<Point>>();

	private String saveName;

	private int selectedCropType = -1;
	private int selectedCropColorIndex = -1;
	private int currentPathIndex;
	private int currentWave = 1, totalWaves;
	private int seeds = 100;
	private int lives = 5;

	private boolean paused, gameOver, lifeLostThisWave;

	public Play(Game game, Map map) {

		super(game, map);
		cropHandler = new CropHandler(this);
		enemyHandler = new EnemyHandler(this);
		projectileHandler = new ProjectileHandler(this);
		waveHandler = new WaveHandler(this);
		actionBar = new ActionBar(this);
		this.totalWaves = waveHandler.getWaves().size();

		initPaths();

	}

	private void initPaths() {

		for (int i = 0; i < startPoints.length; i++) {
			if (startPoints[i].getPoint() != null) {
				ArrayList<Point> shortestPath = new ArrayList<Point>();
				for (int j = 0; j < endPoints.length; j++) {
					if (endPoints[j].getPoint() != null) {
						ArrayList<Point> path = AStar.pathFind(tileData, startPoints[i].getPoint(),
								endPoints[j].getPoint());
						if (path != null)
							if (shortestPath.size() == 0 || path.size() < shortestPath.size())
								shortestPath = path;
					}
				}
				if (shortestPath.size() > 0) {
					// Add off-screen point to beginning of path.
					if (shortestPath.get(0).x == 0)
						shortestPath.add(0, new Point(-1, shortestPath.get(0).y));
					else if (shortestPath.get(0).y == 0)
						shortestPath.add(0, new Point(shortestPath.get(0).x, -1));
					// Add off-screen point to end of path.
					if (shortestPath.get(shortestPath.size() - 1).x == Game.GRID_WIDTH - 1)
						shortestPath.add(new Point(Game.GRID_WIDTH, shortestPath.get(shortestPath.size() - 1).y));
					else if (shortestPath.get(shortestPath.size() - 1).y == Game.GRID_HEIGHT - 1)
						shortestPath.add(new Point(shortestPath.get(shortestPath.size() - 1).x, Game.GRID_HEIGHT));

					paths.add(shortestPath);
				}

			}
		}

	}

	public void update() {

		super.update();
		if (actionBar != null)
			actionBar.update();

		if (!paused) {
			game.getMapHandler().update();
			cropHandler.update();
			enemyHandler.update();
			projectileHandler.update();
			waveHandler.update();
			actionBar.getPause().setText("Pause");
			unsavedChanges = true;
		} else if (actionBar != null)
			actionBar.getPause().setText("Unpause");

		if (justSaved) {
			justSavedTick++;
			if (justSavedTick >= justSavedTickLimit) {
				justSavedTick = 0;
				justSaved = false;
			}
		}

		if (gameOver && endGameOverlay != null)
			endGameOverlay.update();

	}

	public void render(Graphics g) {

		game.getTileHandler().drawTileData(tileData, g);
		cropHandler.draw(g);
		enemyHandler.draw(g);
		projectileHandler.draw(g);

		drawSelectedCrop(g);

		if (selectedCrop != null) {
			drawSelectedCropHighlight(g);
			drawSelectedCropRange(g);
		}

		actionBar.draw(g);

		drawWaveTimer(g);

		if (justSaved)
			drawSavedMessage(g);

		if (!gameOver) {
			if (paused && !unsavedOverlayActive)
				drawPausedMessage(g);
		} else {
			if (endGameOverlay != null)
				endGameOverlay.draw(g);
		}

		super.render(g);

	}

	private void drawSelectedCrop(Graphics g) {

		if (inGameArea && selectedCropType != -1) {
			int range = (int) getDefaultRange(selectedCropType);
			BufferedImage sprite = ImageLoader.getCropSprites(selectedCropType)[selectedCropColorIndex];
			g.drawImage(sprite, mouseX, mouseY, null);
			g.setColor(Color.RED);
			g.drawOval((mouseX + Game.TILE_SIZE / 2) - range, (mouseY + Game.TILE_SIZE / 2) - range, range * 2,
					range * 2);
		}

	}

	public void drawSelectedCropHighlight(Graphics g) {

		g.setColor(Color.CYAN);
		g.drawRect(selectedCrop.getX(), selectedCrop.getY(), 31, 31);
		g.drawRect(selectedCrop.getX() - 1, selectedCrop.getY() - 1, 33, 33);

	}

	public void drawSelectedCropRange(Graphics g) {

		int range = (int) selectedCrop.getRange();
		g.setColor(Color.RED);
		g.drawOval((selectedCrop.getX() + Game.TILE_SIZE / 2) - range,
				(selectedCrop.getY() + Game.TILE_SIZE / 2) - range, range * 2, range * 2);

	}

	private void drawWaveTimer(Graphics g) {

		if (waveHandler.isOnBreak()) {

			g.setColor(Color.BLACK);
			g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(56f));
			DecimalFormat formatter = new DecimalFormat("0.0");
			float timeLeft = waveHandler.getTimeLeft();
			String text = "Next Wave Starting in: " + formatter.format(timeLeft) + "s";

			int x = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
			int y = 150;
			g.drawString(text, x, y);

		}

	}

	private void drawSavedMessage(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(40f));

		String text = "Game saved succesfully!";

		int x = Game.SCREEN_WIDTH / 2 - (g.getFontMetrics().stringWidth(text) / 2);
		int y = Game.SCREEN_HEIGHT - 10;

		g.drawString(text, x, y);

	}

	private void drawPausedMessage(Graphics g) {

		int xStart = Game.SCREEN_WIDTH / 2 - ImageLoader.textBGLarge.getWidth() / 2;
		int yStart = 100;
		g.drawImage(ImageLoader.textBGLarge, xStart, yStart, null);

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(76f));

		DrawText.drawTextCentered(g, "GAME PAUSED", xStart, yStart, ImageLoader.textBGLarge.getWidth(),
				ImageLoader.textBGLarge.getHeight());

	}

	public void drawCropToolTip(Graphics g, int cropType) {

		int xStart = 200;
		int yStart = Game.SCREEN_HEIGHT - ImageLoader.textBGMed.getHeight() - 5;

		g.drawImage(ImageLoader.textBGMed, xStart, yStart, null);

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(28f));

		String[] lines = null;
		float seconds = Math.round(getCooldown(cropType) / (Game.UPS_SET) * 100) / 100.0f;

		switch (cropType) {
		case CORN -> lines = new String[] { "Corn. The most basic of crops.", "Shoots a single kernal",
				"every " + seconds + " seconds" };
		case TOMATO ->
			lines = new String[] { "Tomatos shoot in short", "bursts of three seeds", "every " + seconds + " seconds" };
		case CHILI -> lines = new String[] { "Pepper spray your enemies!", "Shoots a cone of pepper spray",
				"every " + seconds + " seconds" };
		case BELL_PEPPER ->
			lines = new String[] { "The heavy hitter.", "Shoots a high damage seed", "every " + seconds + " seconds" };
		}

		DrawText.drawTextCentered(g, lines, 5, xStart, yStart + 4, ImageLoader.textBGMed.getWidth(),
				ImageLoader.textBGMed.getHeight() - 4);

	}

	public ArrayList<Point> getNextPath() {

		if (paths.size() == 0)
			return null;

		ArrayList<Point> path = paths.get(currentPathIndex);

		currentPathIndex++;
		if (currentPathIndex >= paths.size())
			currentPathIndex = 0;

		return path;

	}

	public void increaseWaveCount() {
		currentWave++;
	}

	public boolean canAffordCrop(int cropType) {
		return seeds >= getCropCost(cropType);
	}

	public void harvestCrop(Crop c) {

		seeds += c.getHarvestAmount();
		cropHandler.removeCrop(c);
		selectedCrop = null;

	}

	public void attackEnemy(Crop c, Enemy e) {
		projectileHandler.newProjectile(c, e);
	}

	public void upgradeDamage(Crop c) {

		if (seeds >= c.getUpgradeCost()) {
			seeds -= c.getUpgradeCost();
			c.upgradeDamage();
		}

	}

	public void upgradeRange(Crop c) {

		if (seeds >= c.getUpgradeCost()) {
			seeds -= c.getUpgradeCost();
			c.upgradeRange();
		}

	}

	public void rewardPlayer(int enemyType) {
		seeds += getReward(enemyType);
	}

	public void gainLife() {
		lives++;
	}

	public void hurtPlayer(int hurtAmount) {

		lives -= hurtAmount;
		lifeLostThisWave = true;
		if (lives <= 0) {
			paused = true;
			int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.overlayBG.getHeight() / 2;
			endGameOverlay = new EndGameOverlay(this, EndGameOverlay.LOSE, yStart);
			gameOver = true;
		}

	}

	public void togglePause() {
		this.paused = !paused;
	}

	public void saveGame() {

		paused = true;

		if (unsavedChanges == false)
			return;

		if (saveName != null) {
			unsavedChanges = false;
			LoadSave.saveGame(this, saveName);
			game.getLoadGame().initSaveButtons();
			game.getSaveGame().initSaveButtons();
			justSaved = true;
		} else
			GameStates.setGameState(GameStates.SAVE_GAME);

	}

	private Tile getTileAt(int x, int y) {

		int tileX = x / Game.TILE_SIZE;
		int tileY = y / Game.TILE_SIZE;

		return tileData[tileY][tileX];

	}

	private Crop getCropAt(int x, int y) {

		for (Crop c : cropHandler.getCrops())
			if (c.getX() == x && c.getY() == y)
				return c;
		return null;

	}

	public void mouseClicked(int x, int y) {

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (gameOver && endGameOverlay != null)
				endGameOverlay.mousePressed(x, y);
			else if (actionBar.getBounds().contains(x, y))
				actionBar.mousePressed(x, y);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);

		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (gameOver && endGameOverlay != null)
				endGameOverlay.mouseReleased(x, y);
			else if (actionBar.getBounds().contains(x, y))
				actionBar.mouseReleased(x, y);
			else if (!paused) {
				if (selectedCropType != -1 && getTileAt(x, y).getTileType() == GRASS) {
					if (canAffordCrop(selectedCropType)) {
						cropHandler.plantCrop(selectedCropType, selectedCropColorIndex, x, y);
						seeds -= getCropCost(selectedCropType);
						selectedCropType = -1;
					}
				}
			}
		if (getCropAt(mouseX, mouseY) != null) {
			selectedCrop = getCropAt(mouseX, mouseY);
			actionBar.setSelectedCrop(selectedCrop);
		}

	}

	public void mouseMoved(int x, int y) {

		super.mouseMoved(x, y);
		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (actionBar.getBounds().contains(x, y))
				actionBar.mouseMoved(x, y);

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			selectedCrop = null;
			actionBar.setSelectedCrop(selectedCrop);
			selectedCropType = -1;
			selectedCropColorIndex = -1;
		}
	}

	public ActionBar getActionBar() {
		return actionBar;
	}

	public void setActionBar(ActionBar actionBar) {
		this.actionBar = actionBar;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean isLifeLostThisWave() {
		return lifeLostThisWave;
	}

	public void setLifeLostThisWave(boolean lifeLostThisWave) {
		this.lifeLostThisWave = lifeLostThisWave;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public EnemyHandler getEnemyHandler() {
		return enemyHandler;
	}

	public WaveHandler getWaveHandler() {
		return waveHandler;
	}

	public ArrayList<ArrayList<Point>> getPaths() {
		return paths;
	}

	public String getSaveName() {
		return saveName;
	}

	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}

	public int getCurrentWave() {
		return currentWave;
	}

	public int getTotalWaves() {
		return totalWaves;
	}

	public int getSeeds() {
		return seeds;
	}

	public int getLives() {
		return lives;
	}

	public void setSelectedCrop(Crop selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	public void setEndGameOverlay(EndGameOverlay endGameOverlay) {
		this.endGameOverlay = endGameOverlay;
	}

	public void setSelectedCropType(int selectedCropType) {
		this.selectedCropType = selectedCropType;
	}

	public void setSelectedCropColorIndex(int selectedCropColorIndex) {
		this.selectedCropColorIndex = selectedCropColorIndex;
	}

	public void setCurrentPathIndex(int currentPathIndex) {
		this.currentPathIndex = currentPathIndex;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

}
