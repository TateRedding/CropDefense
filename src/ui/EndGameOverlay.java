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
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;

public class EndGameOverlay {

	public static final int WIN = 0;
	public static final int LOSE = 1;

	private Play play;
	private TextButton menu, reload, credits;

	private int x, y, width, height;
	private int endCode;

	public EndGameOverlay(Play play, int endCode) {

		this.play = play;
		this.endCode = endCode;
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

		g.drawImage(ImageLoader.overlayBG, x, y, null);

		g.setColor(Color.BLACK);
		String text = "You ";

		if (endCode == WIN) {
			text += "Win!";
			credits.draw(g);
		} else if (endCode == LOSE) {
			text += "Lose!";
			reload.draw(g);
		}

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 55));
		int xStart = x + width / 2 - g.getFontMetrics().stringWidth(text) / 2;
		int yStart = y + 43 + g.getFontMetrics().getHeight() / 2;
		g.drawString(text, xStart, yStart);

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

		menu.setMousePressed(false);
		credits.setMousePressed(false);
		reload.setMousePressed(false);

	}

}
