package main;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import gamestates.Credits;
import gamestates.Edit;
import gamestates.EditMap;
import gamestates.EditTutorial;
import gamestates.GameStates;
import gamestates.LoadGame;
import gamestates.Menu;
import gamestates.Play;
import gamestates.PlayNewGame;
import gamestates.PlayTutorial;
import gamestates.SaveGame;
import handlers.MapHandler;
import handlers.TileHandler;
import helps.ImageLoader;
import helps.LoadSave;
import objects.Map;
import ui.ActionBar;

public class Game implements Runnable {

	public static final int TILE_SIZE = 32;
	public static final int GRID_WIDTH = 32;
	public static final int GRID_HEIGHT = 20;
	public static final int SCREEN_WIDTH = TILE_SIZE * GRID_WIDTH;
	public static final int SCREEN_HEIGHT = TILE_SIZE * GRID_HEIGHT;

	private static final double FPS_SET = 120.0;
	public static final double UPS_SET = 60.0;

	private GameScreen gameScreen;
	private Thread gameThread;

	private Credits credits;
	private Edit edit;
	private EditMap editMap;
	private EditTutorial editTutorial;
	private LoadGame loadGame;
	private MapHandler mapHandler;
	private Menu menu;
	private Play play;
	private PlayNewGame playNewGame;
	private PlayTutorial playTutorial;
	private SaveGame saveGame;
	private TileHandler tileHandler;

	public static void main(String[] args) {

		Game game = new Game();
		game.start();

	}

	private void start() {

		gameThread = new Thread(this);
		gameThread.start();

	}

	public Game() {

		LoadSave.createFolders();
		LoadSave.loadFont();
		ImageLoader.loadImages();

		initClasses();

		gameScreen = new GameScreen(this);
		new GameFrame(gameScreen);
		gameScreen.requestFocus();

	}

	private void initClasses() {

		mapHandler = new MapHandler();
		tileHandler = new TileHandler(this);

		credits = new Credits(this);
		editMap = new EditMap(this);
		editTutorial = new EditTutorial(this);
		loadGame = new LoadGame(this);
		menu = new Menu(this);
		playNewGame = new PlayNewGame(this);
		playTutorial = new PlayTutorial(this);
		saveGame = new SaveGame(this);

	}

	@Override
	public void run() {

		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		long lastCheck = System.currentTimeMillis();
		long previousTime = System.nanoTime();

		int frames = 0;
		int updates = 0;

		double deltaU = 0;
		double deltaF = 0;

		while (true) {

			long currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				updateGame();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gameScreen.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}

	}

	private void updateGame() {

		switch (GameStates.gameState) {

		case CREDITS -> credits.update();
		case EDIT -> edit.update();
		case EDIT_MAP -> editMap.update();
		case EDIT_TUTORIAL -> editTutorial.update();
		case LOAD_GAME -> loadGame.update();
		case MENU -> menu.update();
		case PLAY -> play.update();
		case PLAY_NEW_GAME -> playNewGame.update();
		case PLAY_TUTORIAL -> playTutorial.update();
		case SAVE_GAME -> saveGame.update();

		}

	}

	public void render(Graphics g) {

		switch (GameStates.gameState) {

		case CREDITS -> credits.render(g);
		case EDIT -> edit.render(g);
		case EDIT_MAP -> editMap.render(g);
		case EDIT_TUTORIAL -> editTutorial.render(g);
		case LOAD_GAME -> loadGame.render(g);
		case MENU -> menu.render(g);
		case PLAY -> play.render(g);
		case PLAY_NEW_GAME -> playNewGame.render(g);
		case PLAY_TUTORIAL -> playTutorial.render(g);
		case SAVE_GAME -> saveGame.render(g);

		}
	}

	public void startNewGame(Map map) {
		play = new Play(this, map);
	}

	public void editMap(Map map) {
		edit = new Edit(this, map);
	}

	public void loadGame(File saveFile) {

		try {
			FileInputStream fis = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			play = (Play) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		play.setJustSaved(false);
		play.setJustSavedTick(0);
		play.setGame(this);
		play.setActionBar(new ActionBar(play));

	}

	public void windowFocusLost() {

		if (play != null)
			play.setPaused(true);

	}

	public TileHandler getTileHandler() {
		return tileHandler;
	}

	public Credits getCredits() {
		return credits;
	}

	public Edit getEdit() {
		return edit;
	}

	public EditMap getEditMap() {
		return editMap;
	}

	public EditTutorial getEditTutorial() {
		return editTutorial;
	}

	public LoadGame getLoadGame() {
		return loadGame;
	}

	public MapHandler getMapHandler() {
		return mapHandler;
	}

	public Menu getMenu() {
		return menu;
	}

	public Play getPlay() {
		return play;
	}

	public PlayNewGame getPlayNewGame() {
		return playNewGame;
	}

	public PlayTutorial getPlayTutorial() {
		return playTutorial;
	}

	public SaveGame getSaveGame() {
		return saveGame;
	}

}
