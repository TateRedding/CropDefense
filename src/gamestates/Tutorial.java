package gamestates;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helps.ImageLoader;
import main.Game;
import ui.TextButton;

public class Tutorial extends State implements StateMethods {

	public static final int PLAY_TUTORIAL = 0;
	public static final int EDIT_TUTORIAL = 1;

	private TextButton menu, next, start;
	private BufferedImage[] screenshots;

	private int screenIndex;
	private int tutorialType;

	public Tutorial(Game game, int tutorialType) {

		super(game);
		this.tutorialType = tutorialType;
		initButtons();

		if (tutorialType == EDIT_TUTORIAL)
			screenshots = ImageLoader.tutorialEdit;
		else if (tutorialType == PLAY_TUTORIAL)
			screenshots = ImageLoader.tutorialPlay;

	}

	private void initButtons() {

		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);
		next = new TextButton(TEXT_LARGE, "Next", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE), 10);
		start = new TextButton(TEXT_LARGE, "Let's go!", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE), 10);

	}

	@Override
	public void update() {

		menu.update();

		if (screenshots != null && screenIndex < screenshots.length - 1)
			next.update();
		else
			start.update();

	}

	@Override
	public void render(Graphics g) {

		menu.draw(g);

		if (screenshots != null) {
			g.drawImage(screenshots[screenIndex], 0, 0, screenshots[screenIndex].getWidth(),
					screenshots[screenIndex].getHeight(), null);
			if (screenIndex < screenshots.length - 1)
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
		else if (screenIndex < screenshots.length - 1) {
			if (next.getBounds().contains(x, y))
				next.setMousePressed(true);
		} else if (start.getBounds().contains(x, y))
			start.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			GameStates.setGameState(GameStates.MENU);
			screenIndex = 0;
		} else if (screenIndex < screenshots.length - 1) {
			if (next.getBounds().contains(x, y) && next.isMousePressed())
				screenIndex++;
		} else if (start.getBounds().contains(x, y) && start.isMousePressed()) {
			screenIndex = 0;
			if (tutorialType == PLAY_TUTORIAL)
				GameStates.setGameState(GameStates.PLAY_NEW_GAME);
			else if (tutorialType == EDIT_TUTORIAL)
				GameStates.setGameState(GameStates.EDIT_MAP);
		}

		menu.setMousePressed(false);
		next.setMousePressed(false);
		start.setMousePressed(false);

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

	}

}
