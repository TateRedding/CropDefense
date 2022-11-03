package ui;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import helps.DrawText;
import helps.LoadSave;

public class OverwriteOverlay extends Overlay {

	private NameFileOverlay nameFileOverlay;
	private TextButton okay, cancel;

	public OverwriteOverlay(NameFileOverlay nameFileOverlay, int y) {

		super(y);

		this.nameFileOverlay = nameFileOverlay;
		initButtons();

	}

	private void initButtons() {

		int xStart = mainX + mainW / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 5;
		int totalButtonHeight = getButtonHeight(TEXT_LARGE) * 2 + yOffset;
		int yStart = mainY + mainH / 2 - totalButtonHeight / 2;
		okay = new TextButton(TEXT_LARGE, "Okay", BLUE, xStart, yStart);
		cancel = new TextButton(TEXT_LARGE, "Cancel", BLUE, xStart, yStart += yOffset + getButtonHeight(TEXT_LARGE));

	}

	public void update() {

		okay.update();
		cancel.update();

	}

	public void draw(Graphics g) {

		super.draw(g);

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(28f));
		String[] lines = new String[] { "A file with that name already exists!", "Overwrite file?" };

		DrawText.drawTextCentered(g, lines, 5, titleX, titleY, titleW, titleH);

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

		okay.reset();
		cancel.reset();

	}

	public void mouseMoved(int x, int y) {

		okay.setMouseOver(false);
		cancel.setMouseOver(false);

		if (okay.getBounds().contains(x, y))
			okay.setMouseOver(true);
		if (cancel.getBounds().contains(x, y))
			cancel.setMouseOver(true);

	}

}
