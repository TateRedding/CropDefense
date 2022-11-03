package helps;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import gamestates.Play;
import main.Game;
import objects.Map;
import ui.ActionBar;

public class LoadSave {

	public static final String BACKGROUND = "background.png";
	public static final String BELL_PEPPER_SPRITES = "bell_pepper.png";
	public static final String CHILI_SPRITES = "chili.png";
	public static final String CORN_SPRITES = "corn.png";
	public static final String CREDITS_BG = "ui/credits_bg.png";
	public static final String CROP_DISPLAY_BG = "ui/crop_display_bg.png";
	public static final String CROW_SPRITES = "crow.png";
	public static final String ICON_SPRITES = "icons.png";
	public static final String MAP_BUTTONS = "ui/map_buttons.png";
	public static final String MAP_NAME_BG = "ui/map_name_bg.png";
	public static final String MOLD_SPRITES = "mold.png";
	public static final String OVERLAY_BG = "ui/overlay_background.png";
	public static final String PATH_POINTS = "path_points.png";
	public static final String PROJECTILE_SPRITES = "projectiles.png";
	public static final String SQUARE_BUTTONS = "ui/square_buttons.png";
	public static final String TEXT_BG_LARGE = "ui/text_bg_large.png";
	public static final String TEXT_BG_MED = "ui/text_bg_medium.png";
	public static final String TEXT_BG_SMALL = "ui/text_bg_small.png";
	public static final String TEXT_BUTTONS_LARGE = "ui/text_buttons_large.png";
	public static final String TEXT_BUTTONS_SMALL = "ui/text_buttons_small.png";
	public static final String TILE_ATLAS = "terrain.png";
	public static final String TOMATO_SPRITES = "tomato.png";
	public static final String UI_BG_BEIGE = "ui/ui_background_beige.png";
	public static final String UI_BG_BLUE = "ui/ui_background_blue.png";
	public static final String WORM_SPRITES = "worm.png";

	public static String homePath = System.getProperty("user.home");
	public static String gameFolder = "Crop Defense";
	public static String mapFolder = "maps";
	public static String mapFileExtension = ".cdmap";
	public static String mapPath = homePath + File.separator + gameFolder + File.separator + mapFolder;
	public static String saveFolder = "saves";
	public static String saveFileExtension = ".save";
	public static String savePath = homePath + File.separator + gameFolder + File.separator + saveFolder;

	public static Font gameFont;
	private static String fontName = "Kenney Pixel.ttf";

	public static void createFolders() {

		File folder = new File(homePath + File.separator + gameFolder);
		if (!folder.exists())
			folder.mkdir();

		folder = new File(mapPath);
		if (!folder.exists())
			folder.mkdir();

		folder = new File(savePath);
		if (!folder.exists())
			folder.mkdir();

	}

	public static void loadFont() {

		InputStream is = LoadSave.class.getClassLoader().getResourceAsStream(fontName);

		try {
			gameFont = Font.createFont(Font.TRUETYPE_FONT, is);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(gameFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static BufferedImage loadImage(String fileName) {

		BufferedImage img = null;
		InputStream is = LoadSave.class.getClassLoader().getResourceAsStream(fileName);

		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), TYPE_INT_ARGB);
		convertedImg.getGraphics().drawImage(img, 0, 0, null);

		return convertedImg;

	}

	public static Map loadMap(File mapFile) {

		Map map = null;

		try {
			FileInputStream fis = new FileInputStream(mapFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			map = (Map) ois.readObject();
			ois.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}

	public static void saveMap(Map map, String mapName) {

		File mapFile = new File(mapPath + File.separator + mapName + mapFileExtension);

		if (mapFile.exists()) {
			System.out.println("Saving Map...");
			writeMapToFile(map, mapFile);
		} else {
			System.out.println("Creating new map file");
			createMapFile(map, mapFile);
		}

		ImageLoader.createMapThumbnail(map, mapName);

	}

	private static void writeMapToFile(Map map, File mapFile) {

		try {

			FileOutputStream fileStream = new FileOutputStream(mapFile);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(map);
			objectStream.close();
			fileStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void createMapFile(Map map, File mapFile) {

		if (mapFile.exists()) {
			System.out.println("File: " + mapFile + " already exists");
			return;
		} else {
			try {
				mapFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			writeMapToFile(map, mapFile);

		}

	}

	public static void saveGame(Play play, String saveName) {

		File saveFile = new File(savePath + File.separator + saveName + saveFileExtension);

		if (saveFile.exists()) {
			System.out.println("Overwriting save...");
			writeGameToFile(play, saveFile);
		} else {
			System.out.println("Creating new save file");
			createGameFile(play, saveFile);
			return;
		}

	}

	private static void writeGameToFile(Play play, File saveFile) {

		Game game = play.getGame();
		ActionBar actionBar = play.getActionBar();
		play.setGame(null);
		play.setActionBar(null);
		play.setSelectedCrop(null);

		try {

			FileOutputStream fileStream = new FileOutputStream(saveFile);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
			objectStream.writeObject(play);
			objectStream.close();
			fileStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		play.setGame(game);
		play.setActionBar(actionBar);

	}

	public static void createGameFile(Play play, File saveFile) {

		if (saveFile.exists()) {
			System.out.println("File: " + saveFile + " already exists");
			return;
		} else {
			try {
				saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			writeGameToFile(play, saveFile);

		}

	}

}
