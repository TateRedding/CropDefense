package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import handlers.MapHandler;
import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import ui.MapButton;

public abstract class MapSelect extends FileSelect {

	protected MapHandler mapHandler;
	protected ArrayList<MapButton> buttons = new ArrayList<>();
	protected String[] mapNames;

	public MapSelect(Game game) {

		super(game);
		mapHandler = game.getMapHandler();

	}

	public void update() {

		super.update();
		for (MapButton b : buttons)
			b.update();

	}

	public void render(Graphics g) {

		super.render(g);
		for (MapButton b : buttons)
			b.draw(g);

		drawMapNames(g);

	}

	private void drawMapNames(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));

		ArrayList<File> mapFiles = mapHandler.getMapFiles();
		for (int i = 0; i < mapFiles.size(); i++) {

			int xStart = (buttons.get(i).getBounds().x + buttons.get(i).getBounds().width / 2)
					- ImageLoader.mapNameBG.getWidth() / 2;
			int yStart = buttons.get(i).getBounds().y - ImageLoader.mapNameBG.getHeight() - 1;
			g.drawImage(ImageLoader.mapNameBG, xStart, yStart, null);

			DrawText.drawTextCentered(g, mapNames[i], xStart, yStart, ImageLoader.mapNameBG.getWidth(),
					ImageLoader.mapNameBG.getHeight());

		}

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

	protected void deleteSelectedFile() {

		String fileName = selectedFile.getName();
		File thumbnail = new File(LoadSave.mapPath + File.separator
				+ fileName.substring(0, fileName.length() - LoadSave.mapFileExtension.length()) + "_thumbnail.png");
		thumbnail.delete();
		selectedFile.delete();
		mapHandler.loadMaps();
		game.getEditMap().initMapButtons();
		game.getPlayNewGame().initMapButtons();
		selectedFile = null;
		deleting = false;

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);
		for (MapButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);
		for (MapButton b : buttons)
			b.reset();

	}

	public void mouseMoved(int x, int y) {

		super.mouseMoved(x, y);

		for (MapButton b : buttons)
			b.setMouseOver(false);

		for (MapButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMouseOver(true);

	}

}
