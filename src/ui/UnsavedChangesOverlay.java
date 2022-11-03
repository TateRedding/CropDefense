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
import helps.DrawText;
import helps.LoadSave;

public class UnsavedChangesOverlay extends Overlay {

	public static final int QUIT = 0;
	public static final int EXIT_TO_MENU = 1;

	private Edit edit;
	private Play play;
	private TextButton save, cont, cancel;

	private int exitCode;

	public UnsavedChangesOverlay(Edit edit, int exitCode, int y) {

		super(y);

		this.edit = edit;
		this.exitCode = exitCode;

		initButtons();

	}

	public UnsavedChangesOverlay(Play play, int exitCode, int y) {

		super(y);

		this.play = play;
		this.exitCode = exitCode;

		initButtons();

	}

	private void initButtons() {

		int xStart = mainX + mainW / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 5;
		int totalButtonHeight = getButtonHeight(TEXT_LARGE) * 3 + yOffset * 2;
		int yStart = mainY + mainH / 2 - totalButtonHeight / 2;
		save = new TextButton(TEXT_LARGE, "Save & Continue", GRAY, xStart, yStart);
		cont = new TextButton(TEXT_LARGE, "Continue", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));
		cancel = new TextButton(TEXT_LARGE, "Cancel", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

	}

	public void update() {

		save.update();
		cont.update();
		cancel.update();

	}

	public void draw(Graphics g) {

		super.draw(g);

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(36f));
		String lineOne = "";
		if (edit != null)
			lineOne = "You have unsaved changes!";
		else if (play != null)
			lineOne = "You haven't saved your game!";
		String[] lines = new String[] { lineOne, "Continue without saving?" };

		DrawText.drawTextCentered(g, lines, 5, titleX, titleY, titleW, titleH);

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

		save.reset();
		cont.reset();
		cancel.reset();

	}

	public void mouseMoved(int x, int y) {

		save.setMouseOver(false);
		cont.setMouseOver(false);
		cancel.setMouseOver(false);

		if (save.getBounds().contains(x, y))
			save.setMouseOver(true);
		else if (cont.getBounds().contains(x, y))
			cont.setMouseOver(true);
		else if (cancel.getBounds().contains(x, y))
			cancel.setMouseOver(true);

	}

}
