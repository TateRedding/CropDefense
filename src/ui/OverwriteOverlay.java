package ui;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import helps.ImageLoader;
import main.Game;

public class OverwriteOverlay {

	private NameFileOverlay nameFileOverlay;
	private TextButton okay, cancel;

	private int x, y, width, height;

	public OverwriteOverlay(NameFileOverlay nameFileOverlay) {

		this.nameFileOverlay = nameFileOverlay;
		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = (Game.SCREEN_HEIGHT + UI_HEIGHT) / 2 - height / 2;
		initButtons();

	}

	private void initButtons() {

		int xStart = x + width / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yStart = y + 250;
		int yOffset = 5;
		okay = new TextButton(TEXT_LARGE, "Okay", BLUE, xStart, yStart);
		cancel = new TextButton(TEXT_LARGE, "Cancel", BLUE, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

	}

	public void update() {

		okay.update();
		cancel.update();

	}

	public void draw(Graphics g) {

		g.drawImage(ImageLoader.overlayBG, x, y, width, height, null);

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 22));
		String lineOne = "A file with that name already exists!";
		String lineTwo = "Overwrite file?";
		int xStart = x + width / 2 - g.getFontMetrics().stringWidth(lineOne) / 2;
		int yStart = y + 43 + g.getFontMetrics().getHeight() / 5 * 2;
		g.drawString(lineOne, xStart, yStart);
		xStart = x + width / 2 - g.getFontMetrics().stringWidth(lineTwo) / 2;
		yStart += g.getFontMetrics().getHeight();
		g.drawString(lineTwo, xStart, yStart);

		okay.draw(g);
		cancel.draw(g);

	}

	public void mousePressed(int x, int y) {

		if (okay.getBounds().contains(x, y))
			okay.setMousePressed(true);
		if (cancel.getBounds().contains(x, y))
			cancel.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		if (okay.getBounds().contains(x, y) && okay.isMousePressed()) {
			if (nameFileOverlay.getEditMap() != null)
				nameFileOverlay.startNewMap();
			else if (nameFileOverlay.getSaveGame() != null)
				nameFileOverlay.saveGame();
		} else if (cancel.getBounds().contains(x, y) && cancel.isMousePressed())
			nameFileOverlay.setOverwriting(false);

		okay.setMousePressed(false);
		cancel.setMousePressed(false);

	}

}
