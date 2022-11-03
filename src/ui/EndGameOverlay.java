package ui;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;

import gamestates.GameStates;
import gamestates.Play;
import helps.DrawText;
import helps.LoadSave;

public class EndGameOverlay extends Overlay {

	public static final int WIN = 0;
	public static final int LOSE = 1;

	private Play play;
	private TextButton menu, reload, credits;

	private int endCode;

	public EndGameOverlay(Play play, int endCode, int y) {

		super(y);

		this.play = play;
		this.endCode = endCode;

		initButtons();

	}

	private void initButtons() {

		int xStart = mainX + mainW / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 5;
		int totalButtonHeight = getButtonHeight(TEXT_LARGE) * 2 + yOffset;
		int yStart = mainY + mainH / 2 - totalButtonHeight / 2;
		menu = new TextButton(TEXT_LARGE, "Main Menu", GRAY, xStart, yStart);
		reload = new TextButton(TEXT_LARGE, "Reload", GRAY, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));
		credits = new TextButton(TEXT_LARGE, "Credits", GRAY, xStart, yStart);

	}

	public void update() {

		menu.update();

		if (endCode == WIN)
			credits.update();
		else if (endCode == LOSE)
			reload.update();

	}

	public void draw(Graphics g) {

		super.draw(g);
		String text = "You ";
		if (endCode == WIN) {
			text += "Win!";
			credits.draw(g);
		} else if (endCode == LOSE) {
			text += "Lose!";
			reload.draw(g);
		}

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(64f));

		DrawText.drawTextCentered(g, text, titleX, titleY, titleW, titleH);

		menu.draw(g);

	}

	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else if (endCode == WIN) {
			if (credits.getBounds().contains(x, y))
				credits.setMousePressed(true);
		} else if (endCode == LOSE)
			if (reload.getBounds().contains(x, y))
				reload.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed())
			GameStates.setGameState(GameStates.MENU);
		else if (endCode == WIN) {
			if (credits.getBounds().contains(x, y) && credits.isMousePressed())
				GameStates.setGameState(GameStates.CREDITS);
		} else if (endCode == LOSE)
			if (reload.getBounds().contains(x, y) && reload.isMousePressed()) {
				File saveFile = new File(
						LoadSave.savePath + File.separator + play.getSaveName() + LoadSave.saveFileExtension);
				if (saveFile.exists())
					play.getGame().loadGame(saveFile);
				else
					play.getGame().startNewGame(play.getMap());
			}

		menu.reset();
		credits.reset();
		reload.reset();

	}

	public void mouseMoved(int x, int y) {

		menu.setMouseOver(false);
		credits.setMouseOver(false);
		reload.setMouseOver(false);

		if (menu.getBounds().contains(x, y))
			menu.setMouseOver(true);
		else if (endCode == WIN) {
			if (credits.getBounds().contains(x, y))
				credits.setMouseOver(true);
		} else if (endCode == LOSE)
			if (reload.getBounds().contains(x, y))
				reload.setMouseOver(true);

	}

}
