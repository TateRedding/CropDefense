package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.TEXT_LARGE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import helps.ImageLoader;
import main.Game;
import ui.TextButton;
import ui.UIBar;

public class Credits extends State implements StateMethods {

	private TextButton menu;

	public Credits(Game game) {

		super(game);
		menu = new TextButton(TEXT_LARGE, "Main Menu", BEIGE, 10, 10);

	}

	@Override
	public void update() {

		menu.update();

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, null);

		int xStart = Game.SCREEN_WIDTH / 2 - ImageLoader.credits.getWidth() / 2;
		int yStart = (Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT) / 2 - ImageLoader.credits.getHeight() / 2;

		g.drawImage(ImageLoader.creditsBG, xStart, yStart, null);
		g.drawImage(ImageLoader.credits, xStart, yStart, null);

		menu.draw(g);

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed())
			GameStates.setGameState(GameStates.MENU);
	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE)
			GameStates.setGameState(GameStates.MENU);
	}

}
