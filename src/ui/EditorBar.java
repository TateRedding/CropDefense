package ui;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.SQUARE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static helps.Constants.Tiles.GRASS;
import static helps.Constants.Tiles.ROAD;
import static helps.Constants.Tiles.WATER;
import static helps.Constants.Tiles.PathPoints.END;
import static helps.Constants.Tiles.PathPoints.START;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

import gamestates.Edit;
import gamestates.GameStates;
import helps.ImageLoader;
import main.Game;

public class EditorBar extends UIBar {

	private Edit edit;
	private SquareButton grass, water, road, start, end;
	private TextButton menu, save;
	private ArrayList<SquareButton> tileButtons = new ArrayList<>();

	private int buttonOffset = 5;

	public EditorBar(Edit edit) {

		this.edit = edit;
		initButtons();

	}

	private void initButtons() {

		int xStart = Game.SCREEN_WIDTH / 2 - (getButtonWidth(SQUARE) * 6) / 2;
		int yStart = Game.SCREEN_HEIGHT + (UI_HEIGHT / 2 - getButtonHeight(SQUARE) / 2);

		menu = new TextButton(TEXT_SMALL, "Menu", GRAY, 10, Game.SCREEN_HEIGHT + 20);
		save = new TextButton(TEXT_SMALL, "Save", GRAY, 10, Game.SCREEN_HEIGHT + 20 + getButtonHeight(TEXT_SMALL) + 2);

		grass = new SquareButton(BEIGE, xStart, yStart, ImageLoader.tileSprites.get(GRASS).get(0));
		water = new SquareButton(BEIGE, xStart += getButtonWidth(SQUARE) + buttonOffset, yStart,
				ImageLoader.waterFrames[0]);
		road = new SquareButton(BEIGE, xStart += getButtonWidth(SQUARE) + buttonOffset, yStart,
				ImageLoader.tileSprites.get(ROAD).get(0));
		start = new SquareButton(BEIGE, xStart += getButtonWidth(SQUARE) * 2 + buttonOffset, yStart,
				ImageLoader.pathPointSprites[0]);
		end = new SquareButton(BEIGE, xStart += getButtonWidth(SQUARE) + buttonOffset, yStart,
				ImageLoader.pathPointSprites[1]);

		tileButtons.addAll(Arrays.asList(grass, water, road, start, end));

	}

	public void update() {

		menu.update();
		save.update();

		for (SquareButton tb : tileButtons)
			tb.update();

	}

	public void draw(Graphics g) {

		g.setColor(Color.BLACK);
		g.fillRect(0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT);
		g.drawImage(ImageLoader.tileSprites.get(0).get(0), 0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT, null);
		g.drawImage(ImageLoader.uiBGBlue, 0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT, null);

		menu.draw(g);
		save.draw(g);

		for (SquareButton tb : tileButtons)
			tb.draw(g);

		drawLabels(g);

	}

	private void drawLabels(Graphics g) {

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 30));
		g.setColor(Color.BLACK);

		String label = "Terrain";
		int xStart = (water.getBounds().x + water.getBounds().width / 2) - g.getFontMetrics().stringWidth(label) / 2;
		int yStart = water.getBounds().y - (buttonOffset + 3);

		g.drawString(label, xStart, yStart);

		label = "Pathing";
		xStart = (end.getBounds().x - buttonOffset / 2) - g.getFontMetrics().stringWidth(label) / 2;

		g.drawString(label, xStart, yStart);

		g.setFont(new Font(Game.FONT_NAME, Font.PLAIN, 15));

		String[] labels = { "Grass", "Water", "Road", "Start", "End" };

		yStart = grass.getBounds().y + grass.getBounds().height + g.getFontMetrics().getHeight();

		for (int i = 0; i < tileButtons.size(); i++) {
			xStart = (tileButtons.get(i).getBounds().x + tileButtons.get(i).getBounds().width / 2)
					- g.getFontMetrics().stringWidth(labels[i]) / 2;
			g.drawString(labels[i], xStart, yStart);
		}

	}

	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else if (save.getBounds().contains(x, y))
			save.setMousePressed(true);
		else
			for (SquareButton tb : tileButtons)
				if (tb.getBounds().contains(x, y))
					tb.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			if (edit.isUnsavedChanges()) {
				edit.setUnsavedOverlayActive(true);
				edit.setUnsavedChangesOverlay(new UnsavedChangesOverlay(edit, UnsavedChangesOverlay.EXIT_TO_MENU));
			} else {
				GameStates.setGameState(GameStates.MENU);
				edit.getGame().getMapHandler().loadMaps();
			}
		} else if (save.getBounds().contains(x, y) && save.isMousePressed())
			edit.saveMap();
		else if (grass.getBounds().contains(x, y) && grass.isMousePressed())
			edit.setSelectedTileType(GRASS);
		else if (water.getBounds().contains(x, y) && water.isMousePressed())
			edit.setSelectedTileType(WATER);
		else if (road.getBounds().contains(x, y) && road.isMousePressed())
			edit.setSelectedTileType(ROAD);
		else if (start.getBounds().contains(x, y) && start.isMousePressed())
			edit.setSelectedPointType(START);
		else if (end.getBounds().contains(x, y) && end.isMousePressed())
			edit.setSelectedPointType(END);

		menu.setMousePressed(false);
		save.setMousePressed(false);

		for (SquareButton tb : tileButtons)
			tb.setMousePressed(false);
	}

}
