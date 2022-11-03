package gamestates;

import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;
import java.io.File;

import helps.LoadSave;
import main.Game;
import ui.TextButton;

public class LoadGame extends SaveSelect {

	private TextButton load;

	public LoadGame(Game game) {

		super(game);
		initLoadButton();

	}

	private void initLoadButton() {

		int xStart = Game.SCREEN_WIDTH / 4 * 3 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 - (yOffset + getButtonHeight(TEXT_LARGE)) / 2;
		load = new TextButton(TEXT_LARGE, "Load", BROWN, xStart, yStart);

	}

	public void update() {

		super.update();
		if (selectedFile != null)
			load.update();

	}

	public void render(Graphics g) {

		super.render(g);
		if (selectedFile != null)
			load.draw(g);

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (selectedFile != null) {
			if (load.getBounds().contains(x, y))
				load.setMousePressed(true);
		}

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed())
			switchAndReset(GameStates.MENU);
		else if (selectedFile != null) {
			if (load.getBounds().contains(x, y) && load.isMousePressed()) {
				game.loadGame(selectedFile);
				switchAndReset(GameStates.PLAY);
			} else if (delete.getBounds().contains(x, y) && delete.isMousePressed())
				deleting = true;
			if (deleting) {
				if (yes.getBounds().contains(x, y) && yes.isMousePressed()) {
					selectedFile.delete();
					initSaveButtons();
					game.getSaveGame().initSaveButtons();
					selectedFile = null;
					deleting = false;
				} else if (no.getBounds().contains(x, y) && no.isMousePressed())
					deleting = false;
			}

		}
		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y) && b.isMousePressed())
				if (!b.getText().equals("Empty")) {
					File saveFile = new File(
							LoadSave.savePath + File.separator + b.getText() + LoadSave.saveFileExtension);
					selectedFile = saveFile;
				}

		super.mouseReleased(x, y);
		load.reset();

	}

	public void mouseOver(int x, int y) {

		load.setMouseOver(false);

		super.mousePressed(x, y);

		if (selectedFile != null) {
			if (load.getBounds().contains(x, y))
				load.setMouseOver(true);
		}

	}

}
