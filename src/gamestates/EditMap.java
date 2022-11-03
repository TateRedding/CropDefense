package gamestates;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.MAP;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import ui.MapButton;
import ui.NameFileOverlay;
import ui.TextButton;
import ui.UIBar;

public class EditMap extends MapSelect {

	private NameFileOverlay nameFileOverlay;
	private TextButton tutorial, load;

	private boolean namingFile;

	public EditMap(Game game) {

		super(game);
		initMapButtons();
		initTextButtons();

	}

	public void initMapButtons() {

		buttons.clear();
		mapNames = new String[mapHandler.getMapFiles().size()];

		int x = Game.SCREEN_WIDTH / 2 - getButtonWidth(MAP) / 2;
		int yOffset = ImageLoader.mapNameBG.getHeight() + 5;
		int y = (Game.SCREEN_HEIGHT + UI_HEIGHT - (getButtonHeight(MAP) * maxMaps + yOffset * maxMaps - 1)) / 2;

		for (int i = 0; i < maxMaps; i++) {
			BufferedImage thumbnail = null;
			if (mapHandler.getMaps().size() >= i + 1) {
				Map currMap = mapHandler.getMaps().get(i);
				thumbnail = getThumbnail(currMap);
				String fileName = mapHandler.getMapFiles().get(i).getName();
				String mapName = fileName.substring(0, fileName.length() - LoadSave.mapFileExtension.length());
				mapNames[i] = mapName;
			} else
				thumbnail = ImageLoader.newMapThumbnail;

			buttons.add(new MapButton(BLUE, x, y, thumbnail));
			y += getButtonHeight(MAP) + yOffset;

		}

	}

	private void initTextButtons() {

		tutorial = new TextButton(TEXT_LARGE, "Tutorial", BROWN, Game.SCREEN_WIDTH - 10 - getButtonWidth(TEXT_LARGE),
				10);

		int xStart = Game.SCREEN_WIDTH / 4 * 3 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 - (yOffset + getButtonHeight(TEXT_LARGE)) / 2;
		load = new TextButton(TEXT_LARGE, "Load", BROWN, xStart, yStart);

	}

	public void update() {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.update();

		super.update();
		tutorial.update();
		if (selectedFile != null)
			load.update();

	}

	public void render(Graphics g) {

		super.render(g);
		tutorial.draw(g);
		if (selectedFile != null)
			load.draw(g);

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.draw(g);

	}

	protected void switchAndReset(GameStates gameState) {

		super.switchAndReset(gameState);
		nameFileOverlay = null;
		namingFile = false;

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mousePressed(x, y);
		else {

			if (tutorial.getBounds().contains(x, y))
				tutorial.setMousePressed(true);
			if (selectedFile != null)
				if (load.getBounds().contains(x, y))
					load.setMousePressed(true);

		}

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			initMapButtons();
			game.getPlayNewGame().initMapButtons();
			switchAndReset(GameStates.MENU);
		} else if (tutorial.getBounds().contains(x, y) && tutorial.isMousePressed()) {
			switchAndReset(GameStates.EDIT_TUTORIAL);
		}

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mouseReleased(x, y);
		else {
			if (selectedFile != null) {
				if (load.getBounds().contains(x, y) && load.isMousePressed()) {
					game.editMap(LoadSave.loadMap(selectedFile));
					switchAndReset(GameStates.EDIT);
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
					if (mapHandler.getMaps().size() >= i + 1)
						selectedFile = mapHandler.getMapFiles().get(i);
					else {
						int yStart = (Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT) / 2 - ImageLoader.overlayBG.getHeight() / 2;
						this.nameFileOverlay = new NameFileOverlay(this, yStart);
						namingFile = true;
						selectedFile = null;
					}
				}
		}

		super.mouseReleased(x, y);
		tutorial.reset();
		load.reset();

	}

	public void mouseMoved(int x, int y) {

		tutorial.setMouseOver(false);
		load.setMouseOver(false);

		super.mouseMoved(x, y);

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mouseMoved(x, y);
		else {

			if (tutorial.getBounds().contains(x, y))
				tutorial.setMouseOver(true);
			if (selectedFile != null)
				if (load.getBounds().contains(x, y))
					load.setMouseOver(true);

		}

	}

	public void keyPressed(KeyEvent e) {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.keyPressed(e);

	}

	public void setNameFileOverlay(NameFileOverlay nameFileOverlay) {
		this.nameFileOverlay = nameFileOverlay;
	}

	public void setNamingMap(boolean namingMap) {
		this.namingFile = namingMap;
	}

}
