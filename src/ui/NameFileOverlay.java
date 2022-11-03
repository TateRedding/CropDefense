package ui;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;

import gamestates.EditMap;
import gamestates.GameStates;
import gamestates.SaveGame;
import helps.DrawText;
import helps.LoadSave;
import objects.Map;

public class NameFileOverlay extends Overlay {

	private EditMap editMap;
	private OverwriteOverlay overwriteOverlay;
	private SaveGame saveGame;
	private TextBox textBox;
	private TextButton save, cancel;

	private boolean overwriting, invalidSaveName;

	public NameFileOverlay(EditMap editMap, int y) {

		super(y);

		this.editMap = editMap;
		this.overwriteOverlay = new OverwriteOverlay(this, y);

		initElements();

	}

	public NameFileOverlay(SaveGame saveGame, int y) {

		super(y);

		this.saveGame = saveGame;
		this.overwriteOverlay = new OverwriteOverlay(this, y);

		initElements();

	}

	private void initElements() {

		int xStart = x + width / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 5;
		int totalButtonHeight = getButtonHeight(TEXT_LARGE) * 2 + yOffset;
		int yStart = mainY + mainH / 2 - totalButtonHeight / 2;
		String text = "Save";
		if (editMap != null)
			text = "Start";
		save = new TextButton(TEXT_LARGE, text, GRAY, xStart, yStart);
		cancel = new TextButton(TEXT_LARGE, "Cancel", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

		int textBoxWidth = width - 100;
		Font font = LoadSave.gameFont.deriveFont(48f);
		int textBoxHeight = DrawText.getPixelHeight(font) + 4;
		xStart = mainX + mainW / 2 - textBoxWidth / 2;
		yStart = mainY + (save.getBounds().y - mainY) / 2 - textBoxHeight / 2;

		textBox = new TextBox(xStart, yStart, textBoxWidth, font);
		textBox.setCharLimit(15);

	}

	public void update() {

		if (overwriting)
			overwriteOverlay.update();
		else {

			textBox.update();
			save.update();
			cancel.update();

		}

	}

	public void draw(Graphics g) {

		if (overwriting)
			overwriteOverlay.draw(g);
		else {

			super.draw(g);

			g.setColor(Color.BLACK);
			g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(48f));
			String text = "";
			if (editMap != null)
				text = "Name your new map";
			else if (saveGame != null)
				text = "Name your save file";

			DrawText.drawTextCentered(g, text, titleX, titleY, titleW, titleH);

			textBox.draw(g);
			save.draw(g);
			cancel.draw(g);

			if (invalidSaveName) {

				g.setColor(Color.RED);
				g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
				String[] lines = new String[] { "Invalid file name!", "Please choose another name." };
				int areaYStart = cancel.getBounds().y + cancel.getBounds().height;
				int areaHeight = mainY + mainH - areaYStart;

				DrawText.drawTextCentered(g, lines, 5, mainX, areaYStart, mainW, areaHeight);

			}

		}

	}

	public void startNewMap() {

		Map newMap = new Map(textBox.getText());
		LoadSave.saveMap(newMap, textBox.getText());
		editMap.getGame().getMapHandler().loadMaps();
		editMap.getGame().editMap(newMap);
		editMap.initMapButtons();
		editMap.getGame().getPlayNewGame().initMapButtons();
		GameStates.setGameState(GameStates.EDIT);

	}

	public void saveGame() {

		saveGame.getGame().getPlay().setSaveName(textBox.getText());
		saveGame.getGame().getPlay().saveGame();
		saveGame.setNameFileOverlay(null);
		GameStates.setGameState(GameStates.PLAY);

	}

	public void mousePressed(int x, int y) {

		if (overwriting)
			overwriteOverlay.mousePressed(x, y);
		else {

			if (save.getBounds().contains(x, y))
				save.setMousePressed(true);
			else if (cancel.getBounds().contains(x, y))
				cancel.setMousePressed(true);
		}

	}

	public void mouseReleased(int x, int y) {

		if (overwriting)
			overwriteOverlay.mouseReleased(x, y);
		else {
			if (save.getBounds().contains(x, y) && save.isMousePressed()) {
				if (textBox.getText().length() > 0) {
					if (editMap != null) {
						File mapFile = new File(
								LoadSave.mapPath + File.separator + textBox.getText() + LoadSave.mapFileExtension);
						if (mapFile.exists()) {
							overwriting = true;
							save.setMousePressed(false);
							return;
						}
						startNewMap();
						editMap.setNameFileOverlay(null);
					} else if (saveGame != null) {
						if (textBox.getText().equals("Empty")) {
							invalidSaveName = true;
						} else {
							File saveFile = new File(LoadSave.savePath + File.separator + textBox.getText()
									+ LoadSave.saveFileExtension);
							if (saveFile.exists()) {
								overwriting = true;
								save.setMousePressed(false);
								return;
							}
							saveGame();
						}
					}
				}
			} else if (cancel.getBounds().contains(x, y) && cancel.isMousePressed()) {
				if (editMap != null) {
					editMap.setNameFileOverlay(null);
					editMap.setNamingMap(false);
				}
				if (saveGame != null) {
					saveGame.setNameFileOverlay(null);
					saveGame.setNamingFile(false);
				}
			}

			save.reset();
			cancel.reset();

		}

	}

	public void mouseMoved(int x, int y) {

		save.setMouseOver(false);
		cancel.setMouseOver(false);

		if (overwriting)
			overwriteOverlay.mouseMoved(x, y);
		else {

			if (save.getBounds().contains(x, y))
				save.setMouseOver(true);
			else if (cancel.getBounds().contains(x, y))
				cancel.setMouseOver(true);
		}

	}

	public void keyPressed(KeyEvent e) {
		textBox.keyPressed(e);
	}

	public EditMap getEditMap() {
		return editMap;
	}

	public SaveGame getSaveGame() {
		return saveGame;
	}

	public void setOverwriting(boolean overwriting) {
		this.overwriting = overwriting;
	}

}
