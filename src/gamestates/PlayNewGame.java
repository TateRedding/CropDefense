package gamestates;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.MAP;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MapButton;
import ui.NameFileOverlay;
import ui.TextButton;

public class PlayNewGame extends MapSelect {

	private TextButton tutorial;

	private int maxSaves;

	public PlayNewGame(Game game) {

		super(game);
		this.maxSaves = game.getLoadGame().getMaxSaves();
		initMapButtons();
		initTextButtons();

	}

	public void initMapButtons() {

		buttons.clear();

		int x = Game.SCREEN_WIDTH / 2 - getButtonWidth(MAP) / 2;
		int yOffset = 30;
		int y = (Game.SCREEN_HEIGHT + UI_HEIGHT) / 2
				- (getButtonHeight(MAP) * mapHandler.getMaps().size() + yOffset * (mapHandler.getMaps().size() - 1))
						/ 2;

		for (int i = 0; i < mapHandler.getMaps().size(); i++) {
			BufferedImage thumbnail = getThumbnail(mapHandler.getMaps().get(i));
			buttons.add(new MapButton(BLUE, x, y, thumbnail));
			y += getButtonHeight(MAP) + yOffset;
		}

	}

	private void initTextButtons() {
		tutorial = new TextButton(TEXT_LARGE, "Tutorial", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE),
				10);
	}

	public void update() {

		super.update();
		tutorial.update();

	}

	public void render(Graphics g) {

		if (game.getLoadGame().getSaveFiles().length >= maxSaves) {
			drawTooManySavesMessage(g);
			menu.draw(g);
		} else
			super.render(g);

		if (buttons.size() == 0)
			drawNoMapsMessage(g);

		tutorial.draw(g);

	}

	private void drawTooManySavesMessage(Graphics g) {

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 30));
		g.setColor(Color.BLACK);
		String text = "Too many save files!";
		int xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
		int yStart = Game.SCREEN_HEIGHT / 2;
		g.drawString(text, xStart, yStart);

		text = "Please delete one in order to start a new game.";
		xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart += g.getFontMetrics().getHeight();
		g.drawString(text, xStart, yStart);

	}

	private void drawNoMapsMessage(Graphics g) {

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 30));
		g.setColor(Color.BLACK);
		String text = "No maps to play on!";
		int xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
		int yStart = Game.SCREEN_HEIGHT / 2;
		g.drawString(text, xStart, yStart);

		text = "Please create a map to start a new game.";
		xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart += g.getFontMetrics().getHeight();
		g.drawString(text, xStart, yStart);

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (tutorial.getBounds().contains(x, y))
			tutorial.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mouseReleased(x, y);
		else if (tutorial.getBounds().contains(x, y) && tutorial.isMousePressed()) {
			game.setTutorial(new Tutorial(game, Tutorial.PLAY_TUTORIAL));
			GameStates.setGameState(GameStates.TUTORIAL);
		} else {
			for (int i = 0; i < buttons.size(); i++)
				if (buttons.get(i).getBounds().contains(x, y) && buttons.get(i).isMousePressed()) {
					this.nameFileOverlay = new NameFileOverlay(this, i);
					namingFile = true;
				}
		}

		tutorial.setMousePressed(false);
		for (MapButton mb : buttons)
			mb.setMousePressed(false);

	}

}
