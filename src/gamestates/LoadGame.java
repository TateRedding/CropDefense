package gamestates;

import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;

import main.Game;
import ui.TextButton;

public class LoadGame extends SaveSelect {

	private TextButton load;

	public LoadGame(Game game) {

		super(game);
		initLoadButton();

	}

	private void initLoadButton() {

		int saveButtonRightEdge = buttons.get(0).getBounds().x + buttons.get(0).getBounds().width;
		int xStart = saveButtonRightEdge + (Game.SCREEN_WIDTH - saveButtonRightEdge) / 2
				- getButtonWidth(TEXT_LARGE) / 2;
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

		super.mouseReleased(x, y);

		if (selectedFile != null)
			if (load.getBounds().contains(x, y) && load.isMousePressed()) {
				game.loadGame(selectedFile);
				selectedFile = null;
				GameStates.setGameState(GameStates.PLAY);
				deleting = false;
			}

		load.setMousePressed(false);

	}

}
