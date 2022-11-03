package gamestates;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import ui.TextButton;

public abstract class Tutorial extends State implements StateMethods {

	protected TextButton menu, next, start;
	protected BufferedImage[] images;

	protected int imageIndex, textLineOffset = 5;

	public Tutorial(Game game) {

		super(game);
		initButtons();

	}

	private void initButtons() {

		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);
		next = new TextButton(TEXT_LARGE, "Next", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE), 10);
		start = new TextButton(TEXT_LARGE, "Let's go!", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE), 10);

	}

	@Override
	public void update() {

		menu.update();

		if (images != null && imageIndex < images.length - 1)
			next.update();
		else
			start.update();

	}

	@Override
	public void render(Graphics g) {

		menu.draw(g);

		if (images != null) {
			g.drawImage(images[imageIndex], 0, 0, null);
			if (imageIndex < images.length - 1)
				next.draw(g);
			else
				start.draw(g);
		}

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else if (imageIndex < images.length - 1) {
			if (next.getBounds().contains(x, y))
				next.setMousePressed(true);
		} else if (start.getBounds().contains(x, y))
			start.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			GameStates.setGameState(GameStates.MENU);
			imageIndex = 0;
		} else if (imageIndex < images.length - 1) {
			if (next.getBounds().contains(x, y) && next.isMousePressed())
				imageIndex++;
		}

		menu.reset();
		next.reset();

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

		menu.setMouseOver(false);
		next.setMouseOver(false);
		start.setMouseOver(false);

		if (menu.getBounds().contains(x, y))
			menu.setMouseOver(true);
		else if (imageIndex < images.length - 1) {
			if (next.getBounds().contains(x, y))
				next.setMouseOver(true);
		} else if (start.getBounds().contains(x, y))
			start.setMouseOver(true);

	}

}
