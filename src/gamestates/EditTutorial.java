package gamestates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;

public class EditTutorial extends Tutorial {

	public EditTutorial(Game game) {

		super(game);
		images = ImageLoader.tutorialEdit;

	}

	public void render(Graphics g) {

		super.render(g);

		switch (imageIndex) {
		case 0 -> drawFirstImageMarkings(g);
		case 1 -> drawSecondImageMarkings(g);
		case 2 -> drawThirdImageMarkings(g);
		}

	}

	private void drawFirstImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// Ovals
		g2.setColor(Color.RED);

		int xStart = 260;
		int yStart = 644;
		int width = 298;
		int height = 152;

		g2.setStroke(new BasicStroke(3));
		g2.drawOval(xStart, yStart, width, height);

		g2.setColor(Color.BLUE);

		xStart = 563;
		yStart = 644;
		width = 221;
		height = 152;

		g2.drawOval(xStart, yStart, width, height);

		// Text
		g2.setColor(Color.RED);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));
		String l1 = "Select a terrain type to place on the map";
		String l2 = "Once selected, click and drag";
		String l3 = "anywhere on the map to draw terrain.";
		String[] lines = new String[] { l1, l2, l3 };

		yStart = Game.SCREEN_HEIGHT - 100;
		int areaWidth = Game.SCREEN_WIDTH / 2;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, 0, yStart, areaWidth, areaHeight);

		g.setColor(Color.BLUE);

		l1 = "Use the path points to set the";
		l2 = "start and end locations for the enemies";
		l3 = "Maps can contain up to three start and/or end points.";
		lines = new String[] { l1, l2, l3 };

		DrawText.drawTextCentered(g2, lines, textLineOffset, areaWidth, yStart, areaWidth, areaHeight);

	}

	private void drawSecondImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// Oval
		g2.setColor(Color.RED);

		int xStart = Game.TILE_SIZE * 29 - Game.TILE_SIZE / 2;
		int yStart = Game.TILE_SIZE * 19 - Game.TILE_SIZE / 2;
		int width = Game.TILE_SIZE * 2;
		int height = Game.TILE_SIZE * 2;

		g2.setStroke(new BasicStroke(3));
		g2.drawOval(xStart, yStart, width, height);

		// Text
		g2.setColor(Color.BLUE);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		String l1 = "Start points can be placed on road tiles";
		String l2 = "on the left and top sides of the map.";
		String l3 = "End points can be placed on road tiles";
		String l4 = "on the right and bottom sides of the map.";
		String[] lines = new String[] { l1, l2, l3, l4 };

		yStart = Game.SCREEN_HEIGHT - 250;
		int areaWidth = Game.SCREEN_WIDTH / 2;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, 0, yStart, areaWidth, areaHeight);

		g.setColor(Color.RED);

		l1 = "Enemies are smarter than you think!";
		l2 = "They will rotate through every start point,";
		l3 = "but they will always choose the shortest";
		l4 = "path to any end point they can find!";
		lines = new String[] { l1, l2, l3, l4 };

		yStart = 100;
		areaHeight = Game.SCREEN_HEIGHT / 4 - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, areaWidth, yStart, areaWidth, areaHeight);

		l1 = "This end point";
		l2 = "would never";
		l3 = "be used!";
		lines = new String[] { l1, l2, l3, };

		xStart = Game.SCREEN_WIDTH - 200;
		yStart = Game.SCREEN_HEIGHT - 220;
		areaWidth = Game.SCREEN_WIDTH / 5;
		areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

	}

	private void drawThirdImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		String l1 = "In order to save your map and play on it,";
		String l2 = "there must be at least one valid path from";
		String l3 = "any start point to any end point.";
		String l4 = "The map shown here would not be able to be saved!";
		String[] lines = new String[] { l1, l2, l3, l4 };

		DrawText.drawTextCentered(g2, lines, textLineOffset, 0, Game.SCREEN_HEIGHT / 2, Game.SCREEN_WIDTH,
				Game.SCREEN_HEIGHT / 2);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);

		if (start.getBounds().contains(x, y) && start.isMousePressed()) {
			imageIndex = 0;
			GameStates.setGameState(GameStates.EDIT_MAP);
		}

		start.reset();

	}

}
