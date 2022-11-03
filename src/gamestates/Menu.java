package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import helps.ImageLoader;
import main.Game;
import ui.TextButton;

public class Menu extends State implements StateMethods {

	private TextButton newGame, loadGame, editMap, credits, quit;
	private ArrayList<TextButton> buttons = new ArrayList<>();

	public Menu(Game game) {

		super(game);
		initButtons();

	}

	private void initButtons() {

		int x = Game.SCREEN_WIDTH / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 30;
		int y = (Game.SCREEN_HEIGHT + UI_HEIGHT - (getButtonHeight(TEXT_LARGE) * 5 + yOffset * 4)) / 2;

		newGame = new TextButton(TEXT_LARGE, "New Game", BLUE, x, y);
		loadGame = new TextButton(TEXT_LARGE, "Load Game", BLUE, x, y += (getButtonHeight(TEXT_LARGE) + yOffset));
		editMap = new TextButton(TEXT_LARGE, "Edit Map", BROWN, x, y += (getButtonHeight(TEXT_LARGE) + yOffset));
		credits = new TextButton(TEXT_LARGE, "Credits", BEIGE, x, y += (getButtonHeight(TEXT_LARGE) + yOffset));
		quit = new TextButton(TEXT_LARGE, "Quit", GRAY, x, y += (getButtonHeight(TEXT_LARGE) + yOffset));

		buttons.addAll(Arrays.asList(newGame, loadGame, editMap, credits, quit));

	}

	@Override
	public void update() {

		for (TextButton b : buttons)
			b.update();

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, null);

		for (TextButton b : buttons)
			b.draw(g);

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (newGame.getBounds().contains(x, y) && newGame.isMousePressed()) {
			GameStates.setGameState(GameStates.PLAY_NEW_GAME);
		} else if (loadGame.getBounds().contains(x, y) && loadGame.isMousePressed())
			GameStates.setGameState(GameStates.LOAD_GAME);
		else if (editMap.getBounds().contains(x, y) && editMap.isMousePressed())
			GameStates.setGameState(GameStates.EDIT_MAP);
		else if (credits.getBounds().contains(x, y) && credits.isMousePressed())
			GameStates.setGameState(GameStates.CREDITS);
		else if (quit.getBounds().contains(x, y) && quit.isMousePressed()) {
			System.exit(0);
		}

		for (TextButton b : buttons)
			b.reset();

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

		for (TextButton b : buttons)
			b.setMouseOver(false);

		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMouseOver(true);

	}

}
