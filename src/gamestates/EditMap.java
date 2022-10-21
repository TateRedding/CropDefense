package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.MAP;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import ui.MapButton;
import ui.NameFileOverlay;
import ui.TextButton;

public class EditMap extends MapSelect {

	private TextButton tutorial, load, delete, yes, no;
	private File selectedFile;

	private int maxMaps = 3;

	private boolean deleting;

	public EditMap(Game game) {

		super(game);
		initMapButtons();
		initTextButtons();

	}

	public void initMapButtons() {

		buttons.clear();

		int x = Game.SCREEN_WIDTH / 2 - getButtonWidth(MAP) / 2;
		int yOffset = 30;
		int y = (Game.SCREEN_HEIGHT + UI_HEIGHT - (getButtonHeight(MAP) * maxMaps + yOffset * maxMaps - 1)) / 2;

		for (int i = 0; i < maxMaps; i++) {
			BufferedImage thumbnail = null;
			if (mapHandler.getMaps().size() >= i + 1) {
				Map currMap = mapHandler.getMaps().get(i);
				thumbnail = getThumbnail(currMap);
			} else
				thumbnail = ImageLoader.newMapThumbnail;

			buttons.add(new MapButton(BLUE, x, y, thumbnail));
			y += getButtonHeight(MAP) + yOffset;

		}

	}

	private void initTextButtons() {

		tutorial = new TextButton(TEXT_LARGE, "Tutorial", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE),
				10);

		int mapButtonRightEdge = buttons.get(0).getBounds().x + buttons.get(0).getBounds().width;
		int xStart = mapButtonRightEdge + (Game.SCREEN_WIDTH - mapButtonRightEdge) / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 - (yOffset + getButtonHeight(TEXT_LARGE)) / 2;
		load = new TextButton(TEXT_LARGE, "Load", BROWN, xStart, yStart);
		yStart += yOffset + getButtonHeight(TEXT_LARGE);
		delete = new TextButton(TEXT_LARGE, "Delete", BROWN, xStart, yStart);

		yStart += yOffset + getButtonHeight(TEXT_LARGE);
		yes = new TextButton(TEXT_SMALL, "Yes", BEIGE, xStart, yStart);
		xStart = delete.getBounds().x + delete.getBounds().width - getButtonWidth(TEXT_SMALL);
		no = new TextButton(TEXT_SMALL, "No", BEIGE, xStart, yStart);

	}

	public void update() {

		super.update();

		tutorial.update();

		if (selectedFile != null) {
			load.update();
			delete.update();
			if (deleting) {
				yes.update();
				no.update();
			}
		}

	}

	public void render(Graphics g) {

		super.render(g);

		tutorial.draw(g);

		if (selectedFile != null) {
			load.draw(g);
			delete.draw(g);
			if (deleting) {
				g.setColor(Color.BLACK);
				g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));
				String text = "Are you sure you want to delete this file?";
				int xStart = load.getBounds().x + load.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				int yStart = load.getBounds().y - 10;
				g.drawString(text, xStart, yStart);
				yes.draw(g);
				no.draw(g);
			}
		}

	}

	private void deleteSelectedFile() {

		String fileName = selectedFile.getName();
		File thumbnail = new File(LoadSave.mapPath + File.separator
				+ fileName.substring(0, fileName.length() - LoadSave.mapFileExtension.length()) + "_thumbnail.png");
		thumbnail.delete();
		selectedFile.delete();
		mapHandler.loadMaps();
		initMapButtons();
		game.getPlayNewGame().initMapButtons();
		selectedFile = null;
		deleting = false;

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (tutorial.getBounds().contains(x, y))
			tutorial.setMousePressed(true);

		if (selectedFile != null) {
			if (load.getBounds().contains(x, y))
				load.setMousePressed(true);
			else if (delete.getBounds().contains(x, y))
				delete.setMousePressed(true);
			if (deleting) {
				if (yes.getBounds().contains(x, y))
					yes.setMousePressed(true);
				else if (no.getBounds().contains(x, y))
					no.setMousePressed(true);
			}
		}

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			super.mouseReleased(x, y);
			deleting = false;
		} else if (tutorial.getBounds().contains(x, y) && tutorial.isMousePressed()) {
			game.setTutorial(new Tutorial(game, Tutorial.EDIT_TUTORIAL));
			GameStates.setGameState(GameStates.TUTORIAL);
		}
		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mouseReleased(x, y);
		else {
			if (selectedFile != null) {
				if (load.getBounds().contains(x, y) && load.isMousePressed()) {
					game.editMap(LoadSave.loadMap(selectedFile));
					selectedFile = null;
					GameStates.setGameState(GameStates.EDIT);
					deleting = false;
				} else if (delete.getBounds().contains(x, y) && delete.isMousePressed())
					deleting = true;
				if (deleting) {
					if (yes.getBounds().contains(x, y) && yes.isMousePressed())
						deleteSelectedFile();
					else if (no.getBounds().contains(x, y) && no.isMousePressed())
						deleting = false;
				}

			}

			for (int i = 0; i < maxMaps; i++)
				if (buttons.get(i).getBounds().contains(x, y) && buttons.get(i).isMousePressed()) {
					if (mapHandler.getMaps().size() >= i + 1) {
						selectedFile = mapHandler.getMapFiles()[i];
					} else {
						this.nameFileOverlay = new NameFileOverlay(this, i);
						namingFile = true;
						selectedFile = null;
					}
				}

		}

		tutorial.setMousePressed(false);
		load.setMousePressed(false);
		delete.setMousePressed(false);
		yes.setMousePressed(false);
		no.setMousePressed(false);

		for (MapButton b : buttons)
			b.setMousePressed(false);

	}

}
