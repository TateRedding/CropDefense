package gamestates;

import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.TEXT_LARGE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import handlers.MapHandler;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import ui.MapButton;
import ui.TextButton;

public abstract class MapSelect extends State implements StateMethods {

	protected MapHandler mapHandler;
	protected TextButton menu;
	protected ArrayList<MapButton> buttons = new ArrayList<>();

	protected MapSelect(Game game) {

		super(game);
		this.mapHandler = game.getMapHandler();
		initButtons();

	}

	protected void initButtons() {
		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);
	}

	@Override
	public void update() {

		menu.update();
		for (MapButton b : buttons)
			b.update();

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, ImageLoader.background.getWidth(), ImageLoader.background.getHeight(),
				null);

		menu.draw(g);
		for (MapButton b : buttons)
			b.draw(g);

	}

	protected BufferedImage getThumbnail(Map map) {

		BufferedImage thumbnail = null;

		String path = LoadSave.mapPath + File.separator + map.getMapName() + "_thumbnail.png";
		File thumbnailFile = new File(path);
		if (thumbnailFile.exists()) {
			try {
				thumbnail = ImageIO.read(thumbnailFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			thumbnail = ImageLoader.missingThumbnail;

		return thumbnail;

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else
			for (MapButton b : buttons)
				if (b.getBounds().contains(x, y))
					b.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			GameStates.setGameState(GameStates.MENU);
			if (game.getEditMap() != null)
				game.getEditMap().initMapButtons();
			else if (game.getPlayNewGame() != null)
				game.getPlayNewGame().initMapButtons();
		}

		menu.setMousePressed(false);

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

	}

}
