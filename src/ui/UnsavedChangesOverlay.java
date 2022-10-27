package ui;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gamestates.Edit;
import gamestates.GameStates;
import gamestates.Play;
import helps.ImageLoader;
import main.Game;

public class UnsavedChangesOverlay {

	public static final int QUIT = 0;
	public static final int EXIT_TO_MENU = 1;

	private Edit edit;
	private Play play;
	private TextButton save, cont, cancel;

	private int x, y, width, height;
	private int exitCode;

	public UnsavedChangesOverlay(Edit edit, int exitCode) {

		this.edit = edit;
		this.exitCode = exitCode;
		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = Game.SCREEN_HEIGHT / 2 - height / 2;

		initButtons();

	}

	public UnsavedChangesOverlay(Play play, int exitCode) {

		this.play = play;
		this.exitCode = exitCode;
		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = Game.SCREEN_HEIGHT / 2 - height / 2;

		initButtons();

	}

	private void initButtons() {

		int xStart = x + width / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yStart = y + 250;
		int yOffset = 5;
		save = new TextButton(TEXT_LARGE, "Save and Continue", GRAY, xStart, yStart);
		cont = new TextButton(TEXT_LARGE, "Continue", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));
		cancel = new TextButton(TEXT_LARGE, "Cancel", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

	}

	public void update() {

		save.update();
		cont.update();
		cancel.update();

	}

	public void draw(Graphics g) {

		g.drawImage(ImageLoader.overlayBG, x, y, null);

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 22));
		String lineOne = "";
		if (edit != null)
			lineOne = "You have unsaved changes!";
		else if (play != null)
			lineOne = "You haven't saved your game!";
		String lineTwo = "Continue without saving?";
		int xStart = x + width / 2 - g.getFontMetrics().stringWidth(lineOne) / 2;
		int yStart = y + 43 + g.getFontMetrics().getHeight() / 5 * 2;
		g.drawString(lineOne, xStart, yStart);
		xStart = x + width / 2 - g.getFontMetrics().stringWidth(lineTwo) / 2;
		yStart += g.getFontMetrics().getHeight();
		g.drawString(lineTwo, xStart, yStart);

		save.draw(g);
		cont.draw(g);
		cancel.draw(g);

	}

	public void mousePressed(int x, int y) {

		if (save.getBounds().contains(x, y))
			save.setMousePressed(true);
		else if (cont.getBounds().contains(x, y))
			cont.setMousePressed(true);
		else if (cancel.getBounds().contains(x, y))
			cancel.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		if (save.getBounds().contains(x, y) && save.isMousePressed()) {
			if (edit != null) {
				edit.saveMap();
				if (!edit.isInvalidMap()) {
					if (exitCode == EXIT_TO_MENU) {
						GameStates.setGameState(GameStates.MENU);
						edit.getGame().getMapHandler().loadMaps();
					} else if (exitCode == QUIT)
						System.exit(0);
				}
			} else if (play != null) {
				play.setUnsavedChangesOverlay(null);
				play.setUnsavedOverlayActive(false);
				if (play.getSaveName() == null) {
					GameStates.setGameState(GameStates.SAVE_GAME);
				} else {
					play.saveGame();
					if (exitCode == EXIT_TO_MENU)
						GameStates.setGameState(GameStates.MENU);
					else if (exitCode == QUIT)
						System.exit(0);
				}
			}
		} else if (cont.getBounds().contains(x, y) && cont.isMousePressed()) {
			if (exitCode == EXIT_TO_MENU) {
				GameStates.setGameState(GameStates.MENU);
				if (edit != null)
					edit.getGame().getMapHandler().loadMaps();
			} else if (exitCode == QUIT)
				System.exit(exitCode);
		} else if (cancel.getBounds().contains(x, y) && cancel.isMousePressed()) {
			if (edit != null) {
				edit.setUnsavedChangesOverlay(null);
				edit.setUnsavedOverlayActive(false);
			} else if (play != null) {
				play.setUnsavedChangesOverlay(null);
				play.setUnsavedOverlayActive(false);
			}
		}

		save.setMousePressed(false);
		cont.setMousePressed(false);
		cancel.setMousePressed(false);

	}

}
