package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;

import helps.ImageLoader;
import main.Game;
import ui.TextButton;

public abstract class FileSelect extends State implements StateMethods {

	protected TextButton menu, delete, yes, no;
	protected File selectedFile;

	protected int maxSaves = 5;
	protected int maxMaps = 3;

	protected boolean deleting;

	public FileSelect(Game game) {

		super(game);
		initButtons();

	}

	private void initButtons() {

		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);

		int xStart = Game.SCREEN_WIDTH / 4 * 3 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 + (yOffset + getButtonHeight(TEXT_LARGE)) / 2;

		delete = new TextButton(TEXT_LARGE, "Delete", BROWN, xStart, yStart);
		yStart += yOffset + getButtonHeight(TEXT_LARGE);

		yes = new TextButton(TEXT_SMALL, "Yes", BEIGE, xStart, yStart);
		xStart = delete.getBounds().x + delete.getBounds().width - getButtonWidth(TEXT_SMALL);

		no = new TextButton(TEXT_SMALL, "No", BEIGE, xStart, yStart);

	}

	@Override
	public void update() {

		menu.update();
		if (selectedFile != null) {
			delete.update();
			if (deleting) {
				yes.update();
				no.update();
			}
		}

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, null);

		menu.draw(g);

		if (selectedFile != null) {
			delete.draw(g);
			drawSelectedFileBG(g);

			if (deleting) {
				int xStart = delete.getBounds().x + delete.getBounds().width / 2
						- ImageLoader.textBGSmall.getWidth() / 2;
				int yStart = delete.getBounds().y - ImageLoader.textBGSmall.getHeight() - getButtonHeight(TEXT_LARGE)
						- 20;
				g.drawImage(ImageLoader.textBGSmall, xStart, yStart, null);
				g.setColor(Color.BLACK);
				g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 23));
				String text = "Are you sure you want";
				xStart = delete.getBounds().x + delete.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				yStart += ImageLoader.textBGSmall.getHeight() / 2 - 5;
				g.drawString(text, xStart, yStart);

				text = "to delete this file?";
				xStart = delete.getBounds().x + delete.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				yStart += g.getFontMetrics().getHeight() / 5 * 4;
				g.drawString(text, xStart, yStart);
				yes.draw(g);
				no.draw(g);
			}

		}

	}

	private void drawSelectedFileBG(Graphics g) {

		int xStart = Game.SCREEN_WIDTH / 4 - ImageLoader.textBGSmall.getWidth() / 2;
		int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.textBGSmall.getHeight() / 2;
		g.drawImage(ImageLoader.textBGSmall, xStart, yStart, null);

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else if (selectedFile != null) {
			if (delete.getBounds().contains(x, y))
				delete.setMousePressed(true);
			if (deleting) {
				if (yes.getBounds().contains(x, y))
					yes.setMousePressed(true);
				else if (no.getBounds().contains(x, y))
					no.setMousePressed(true);
			}
		}

	}

	@Override
	public void mouseReleased(int x, int y) {

		menu.setMousePressed(false);
		delete.setMousePressed(false);
		yes.setMousePressed(false);
		no.setMousePressed(false);

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

	}

}
