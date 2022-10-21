package ui;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;

import gamestates.EditMap;
import gamestates.GameStates;
import gamestates.PlayNewGame;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;

public class NameFileOverlay {

	private EditMap editMap;
	private OverwriteOverlay overwriteOverlay;
	private PlayNewGame playNewGame;
	private TextBox textBox;
	private TextButton start, cancel;

	private int mapID;
	private int x, y, width, height;

	private boolean overwriting, invalidSaveName;

	public NameFileOverlay(EditMap editMap, int mapID) {

		this.editMap = editMap;
		this.mapID = mapID;
		this.overwriteOverlay = new OverwriteOverlay(this);
		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = (Game.SCREEN_HEIGHT + UI_HEIGHT) / 2 - height / 2;

		initElements();

	}

	public NameFileOverlay(PlayNewGame playNewMap, int mapID) {

		this.playNewGame = playNewMap;
		this.mapID = mapID;
		this.overwriteOverlay = new OverwriteOverlay(this);
		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = (Game.SCREEN_HEIGHT + UI_HEIGHT) / 2 - height / 2;

		initElements();

	}

	private void initElements() {

		int textBoxWidth = width - 100;
		int xStart = x + width / 2 - textBoxWidth / 2;
		int yStart = y + 200;

		textBox = new TextBox(xStart, yStart, textBoxWidth, new Font(Game.FONT_NAME, Font.PLAIN, 25));
		textBox.setCharLimit(15);

		xStart = x + width / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 5;
		start = new TextButton(TEXT_LARGE, "Start", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));
		cancel = new TextButton(TEXT_LARGE, "Cancel", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

	}

	public void update() {

		if (overwriting)
			overwriteOverlay.update();
		else {

			textBox.update();
			start.update();
			cancel.update();

		}

	}

	public void draw(Graphics g) {

		if (overwriting)
			overwriteOverlay.draw(g);
		else {

			g.drawImage(ImageLoader.overlayBG, x, y, width, height, null);

			g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 30));

			String text = "";
			if (editMap != null)
				text = "Name your new map";
			else if (playNewGame != null)
				text = "Name your save file";

			int xStart = x + width / 2 - g.getFontMetrics().stringWidth(text) / 2;
			int yStart = y + 62 + g.getFontMetrics().getHeight() / 5 * 2;
			g.drawString(text, xStart, yStart);

			textBox.draw(g);
			start.draw(g);
			cancel.draw(g);

			if (invalidSaveName) {
				text = "Invalid file name!";
				g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 25));
				g.setColor(Color.RED);
				xStart = x + (width / 2 - g.getFontMetrics().stringWidth(text) / 2);
				yStart = y + height - 90;
				g.drawString(text, xStart, yStart);

				text = "Please choose another name.";
				g.setFont(new Font(Game.FONT_NAME, Font.PLAIN, 22));
				g.setColor(Color.BLACK);
				xStart = x + (width / 2 - g.getFontMetrics().stringWidth(text) / 2);
				yStart = y + height - 65;
				g.drawString(text, xStart, yStart);
			}

		}

	}

	public void startNewMap() {

		Map newMap = new Map(textBox.getText());
		LoadSave.saveMap(newMap, textBox.getText());
		editMap.getGame().getMapHandler().getMaps().add(mapID, newMap);
		editMap.getGame().editMap(newMap);
		editMap.initMapButtons();
		editMap.getGame().getPlayNewGame().initMapButtons();
		GameStates.setGameState(GameStates.EDIT);

	}

	public void startNewGame() {

		Map map = playNewGame.getGame().getMapHandler().getMaps().get(mapID);
		playNewGame.getGame().startNewGame(map, textBox.getText());
		LoadSave.saveGame(playNewGame.getGame().getPlay(), textBox.getText());
		playNewGame.getGame().getLoadGame().initSaveButtons();
		GameStates.setGameState(GameStates.PLAY);

	}

	public void mousePressed(int x, int y) {

		if (overwriting)
			overwriteOverlay.mousePressed(x, y);
		else {

			if (start.getBounds().contains(x, y))
				start.setMousePressed(true);
			else if (cancel.getBounds().contains(x, y))
				cancel.setMousePressed(true);
		}

	}

	public void mouseReleased(int x, int y) {

		if (overwriting)
			overwriteOverlay.mouseReleased(x, y);
		else {

			if (start.getBounds().contains(x, y) && start.isMousePressed()) {
				if (textBox.getText().length() > 0) {
					if (editMap != null) {
						File mapFile = new File(
								LoadSave.mapPath + File.separator + textBox.getText() + LoadSave.mapFileExtension);
						if (mapFile.exists()) {
							overwriting = true;
							start.setMousePressed(false);
							return;
						}
						startNewMap();
						editMap.setNameFileOverlay(null);
					} else if (playNewGame != null) {
						if (textBox.getText().equals("Empty")) {
							invalidSaveName = true;
						} else {
							File saveFile = new File(LoadSave.savePath + File.separator + textBox.getText()
									+ LoadSave.saveFileExtension);
							if (saveFile.exists()) {
								overwriting = true;
								start.setMousePressed(false);
								return;
							}
							startNewGame();
							playNewGame.setNameFileOverlay(null);
						}
					}
				}
			} else if (cancel.getBounds().contains(x, y) && cancel.isMousePressed()) {
				if (editMap != null) {
					editMap.setNameFileOverlay(null);
					editMap.setNamingMap(false);
				}
				if (playNewGame != null) {
					playNewGame.setNameFileOverlay(null);
					playNewGame.setNamingMap(false);
				}
			}

			start.setMousePressed(false);
			cancel.setMousePressed(false);

		}

	}

	public void keyPressed(KeyEvent e) {
		textBox.keyPressed(e);
	}

	public EditMap getEditMap() {
		return editMap;
	}

	public PlayNewGame getPlayNewGame() {
		return playNewGame;
	}

	public void setOverwriting(boolean overwriting) {
		this.overwriting = overwriting;
	}

}
