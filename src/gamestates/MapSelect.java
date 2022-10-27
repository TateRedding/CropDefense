package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import handlers.MapHandler;
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

		if (selectedFile != null)
			drawLastSavedInformation(g);

		drawMapNames(g);

	}

	private void drawMapNames(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		ArrayList<File> mapFiles = mapHandler.getMapFiles();
		for (int i = 0; i < mapFiles.size(); i++) {

			int xStart = (buttons.get(i).getBounds().x + buttons.get(i).getBounds().width / 2)
					- ImageLoader.mapNameBG.getWidth() / 2;
			int yStart = buttons.get(i).getBounds().y - ImageLoader.mapNameBG.getHeight();
			g.drawImage(ImageLoader.mapNameBG, xStart, yStart, null);

			xStart = (buttons.get(i).getBounds().x + buttons.get(i).getBounds().width / 2)
					- g.getFontMetrics().stringWidth(mapNames[i]) / 2;
			yStart += ImageLoader.mapNameBG.getHeight() / 2 + g.getFontMetrics().getHeight() / 4;
			g.drawString(mapNames[i], xStart, yStart);

		}

	}

	private void drawLastSavedInformation(Graphics g) {

		Path filePath = Paths.get(selectedFile.getAbsolutePath());
		BasicFileAttributes attr = null;
		try {
			attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int xStart = Game.SCREEN_WIDTH / 4 - ImageLoader.textBGSmall.getWidth() / 2 + 10;
		int yStart = Game.SCREEN_HEIGHT / 2 - 2;

		String creationTime = "" + attr.creationTime();
		String month = creationTime.substring(5, 7);
		String day = creationTime.substring(8, 10);
		String year = creationTime.substring(0, 4);
		String saveName = "Selected map: " + selectedFile.getName().substring(0,
				selectedFile.getName().length() - LoadSave.saveFileExtension.length());
		String lastSaved = "Last saved on: " + month + "/" + day + "/" + year;

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		g.drawString(saveName, xStart, yStart);

		yStart += g.getFontMetrics().getHeight() / 5 * 4;
		g.drawString(lastSaved, xStart, yStart);

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
			b.setMousePressed(false);

	}

}
