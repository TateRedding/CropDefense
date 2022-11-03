package helps;

import static helps.Constants.Buttons.MAP;
import static helps.Constants.Buttons.SQUARE;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static helps.Constants.Enemies.CROW;
import static helps.Constants.Enemies.CROW_BOSS;
import static helps.Constants.Enemies.MOLD;
import static helps.Constants.Enemies.MOLD_BOSS;
import static helps.Constants.Enemies.WORM;
import static helps.Constants.Enemies.WORM_BOSS;
import static helps.Constants.Tiles.GRASS;
import static helps.Constants.Tiles.ROAD;
import static helps.Constants.Tiles.WATER;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import objects.Map;
import objects.Tile;

/**
 * 
 * This class is used to load all images used in the game into BufferdImage
 * static objects, as BufferedImage is not serializable and therefore can not be
 * saved along with other objects using the ObjectInputStream to create save
 * files for Map or Play objects.
 *
 */

public class ImageLoader {

	private static int spriteSize = 32;

	public static ArrayList<ArrayList<BufferedImage>> tileSprites;
	public static BufferedImage background, creditsBG, cropDisplayBG, heart, newMapThumbnail, mapNameBG,
			missingThumbnail, overlayBG, seedPacket, seeds, textBGLarge, textBGMed, textBGSmall, uiBGBeige, uiBGBlue;

	public static BufferedImage[] bellPepperSprites, chiliSprites, cornSprites, pathPointSprites, tomatoSprites,
			tutorialEdit, tutorialPlay, waterFrames;

	public static BufferedImage[][] mapButtons, projectileSprites, squareButtons, textButtonsLarge, textButtonsSmall;
	public static BufferedImage[][] crowSprites, moldSprites, wormSprites;

	public static void loadImages() {

		createThumbnailImages();
		loadButtonImages();
		loadBackgroundImages();
		loadCropSprites();
		loadEnemySprites();
		loadIconSprites();
		loadPathPointSprites();
		loadTileSprites();
		loadTutorialImages();

	}

	private static void createThumbnailImages() {

		String[] lines = new String[] { "Create", "new map" };
		newMapThumbnail = createThumbnail(lines, Color.BLACK);

		lines = new String[] { "Missing", "thumbnail!" };
		missingThumbnail = createThumbnail(lines, Color.RED);

	}

	private static BufferedImage createThumbnail(String[] lines, Color color) {

		BufferedImage temp = new BufferedImage(128, 80, TYPE_INT_ARGB);

		Graphics g = temp.getGraphics();
		g.setColor(new Color(141, 196, 53));
		g.fillRect(0, 0, temp.getWidth(), temp.getHeight());

		g.setColor(color);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		DrawText.drawTextCentered(g, lines, 2, 0, 0, temp.getWidth(), temp.getHeight());

		return temp;

	}

	private static void loadButtonImages() {

		mapButtons = get2DImageArray(LoadSave.MAP_BUTTONS, getButtonWidth(MAP), getButtonHeight(MAP), 4, 3);
		squareButtons = get2DImageArray(LoadSave.SQUARE_BUTTONS, getButtonWidth(SQUARE), getButtonHeight(SQUARE), 4, 3);
		textButtonsLarge = get2DImageArray(LoadSave.TEXT_BUTTONS_LARGE, getButtonWidth(TEXT_LARGE),
				getButtonHeight(TEXT_LARGE), 4, 3);
		textButtonsSmall = get2DImageArray(LoadSave.TEXT_BUTTONS_SMALL, getButtonWidth(TEXT_SMALL),
				getButtonHeight(TEXT_SMALL), 4, 3);

	}

	private static void loadBackgroundImages() {

		background = LoadSave.loadImage(LoadSave.BACKGROUND);
		cropDisplayBG = LoadSave.loadImage(LoadSave.CROP_DISPLAY_BG);
		creditsBG = LoadSave.loadImage(LoadSave.CREDITS_BG);
		mapNameBG = LoadSave.loadImage(LoadSave.MAP_NAME_BG);
		overlayBG = LoadSave.loadImage(LoadSave.OVERLAY_BG);
		textBGLarge = LoadSave.loadImage(LoadSave.TEXT_BG_LARGE);
		textBGMed = LoadSave.loadImage(LoadSave.TEXT_BG_MED);
		textBGSmall = LoadSave.loadImage(LoadSave.TEXT_BG_SMALL);
		uiBGBeige = LoadSave.loadImage(LoadSave.UI_BG_BEIGE);
		uiBGBlue = LoadSave.loadImage(LoadSave.UI_BG_BLUE);

	}

	private static void loadCropSprites() {

		bellPepperSprites = getImageArray(LoadSave.BELL_PEPPER_SPRITES, spriteSize, spriteSize, 3);
		chiliSprites = getImageArray(LoadSave.CHILI_SPRITES, spriteSize, spriteSize, 2);
		cornSprites = getImageArray(LoadSave.CORN_SPRITES, spriteSize, spriteSize, 3);
		tomatoSprites = getImageArray(LoadSave.TOMATO_SPRITES, spriteSize, spriteSize, 3);

		projectileSprites = get2DImageArray(LoadSave.PROJECTILE_SPRITES, spriteSize / 4, spriteSize / 4, 3, 3);

	}

	private static void loadEnemySprites() {

		crowSprites = get2DImageArray(LoadSave.CROW_SPRITES, spriteSize, spriteSize, 2, 4);
		moldSprites = get2DImageArray(LoadSave.MOLD_SPRITES, spriteSize, spriteSize, 2, 4);
		wormSprites = get2DImageArray(LoadSave.WORM_SPRITES, spriteSize, spriteSize, 2, 4);

	}

	private static void loadIconSprites() {

		BufferedImage atlas = LoadSave.loadImage(LoadSave.ICON_SPRITES);

		heart = atlas.getSubimage(spriteSize, 0, spriteSize, spriteSize);
		seedPacket = atlas.getSubimage(0, 0, spriteSize, spriteSize);
		seeds = atlas.getSubimage(spriteSize * 2, 0, spriteSize / 2, spriteSize / 2);

	}

	private static void loadPathPointSprites() {

		pathPointSprites = new BufferedImage[2];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.PATH_POINTS);

		for (int i = 0; i < pathPointSprites.length; i++) {
			pathPointSprites[i] = getSprite(atlas, i, 0);
		}

	}

	private static void loadTileSprites() {

		tileSprites = new ArrayList<>();
		waterFrames = new BufferedImage[4];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.TILE_ATLAS);

		ArrayList<BufferedImage> grassSprites = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> waterSprites = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> roadSprites = new ArrayList<BufferedImage>();

		// Grass Sprites
		for (int i = 0; i < 4; i++) {
			grassSprites.add(getSprite(atlas, i, 0));

		}

		// Water Sprites
		BufferedImage edgeTop = getSprite(atlas, 0, 2);
		BufferedImage edgeRight = getSprite(atlas, 1, 2);
		BufferedImage edgeBottom = getSprite(atlas, 2, 2);
		BufferedImage edgeLeft = getSprite(atlas, 3, 2);

		BufferedImage twoEdgeTopLeft = getSprite(atlas, 0, 3);
		BufferedImage twoEdgeTopRight = getSprite(atlas, 1, 3);
		BufferedImage twoEdgeBottomRight = getSprite(atlas, 2, 3);
		BufferedImage twoEdgeBottomLeft = getSprite(atlas, 3, 3);

		BufferedImage cornerBottomRight = getSprite(atlas, 0, 6);
		BufferedImage cornerBottomLeft = getSprite(atlas, 1, 6);
		BufferedImage cornerTopLeft = getSprite(atlas, 2, 6);
		BufferedImage cornerTopRight = getSprite(atlas, 3, 6);

		waterSprites.add(getSprite(atlas, 0, 5));
		waterSprites.add(getSprite(atlas, 2, 4));
		waterSprites.add(getSprite(atlas, 1, 4));
		waterSprites.add(buildImage(twoEdgeBottomRight, cornerTopLeft));
		waterSprites.add(twoEdgeBottomRight);
		waterSprites.add(getSprite(atlas, 3, 4));
		waterSprites.add(buildImage(twoEdgeBottomLeft, cornerTopRight));
		waterSprites.add(twoEdgeBottomLeft);

		waterSprites.add(buildImage(edgeTop, edgeBottom));
		waterSprites.add(buildImage(buildImage(edgeBottom, cornerTopLeft), cornerTopRight));
		waterSprites.add(buildImage(edgeBottom, cornerTopRight));
		waterSprites.add(buildImage(edgeBottom, cornerTopLeft));
		waterSprites.add(edgeBottom);
		waterSprites.add(getSprite(atlas, 0, 4));
		waterSprites.add(buildImage(edgeLeft, edgeRight));
		waterSprites.add(buildImage(twoEdgeTopRight, cornerBottomLeft));

		waterSprites.add(buildImage(buildImage(edgeRight, cornerBottomLeft), cornerTopLeft));
		waterSprites.add(buildImage(edgeRight, cornerBottomLeft));
		waterSprites.add(buildImage(twoEdgeTopLeft, cornerBottomRight));
		waterSprites.add(buildImage(buildImage(edgeLeft, cornerBottomRight), cornerTopRight));
		waterSprites.add(buildImage(edgeLeft, cornerBottomRight));
		waterSprites.add(buildImage(buildImage(edgeTop, cornerBottomRight), cornerBottomLeft));
		waterSprites.add(
				buildImage(buildImage(buildImage(cornerTopLeft, cornerTopRight), cornerBottomLeft), cornerBottomRight));
		waterSprites.add(buildImage(buildImage(cornerTopRight, cornerBottomRight), cornerBottomLeft));

		waterSprites.add(buildImage(buildImage(cornerBottomRight, cornerBottomLeft), cornerTopLeft));
		waterSprites.add(buildImage(cornerBottomLeft, cornerBottomRight));
		waterSprites.add(twoEdgeTopRight);
		waterSprites.add(buildImage(edgeRight, cornerTopLeft));
		waterSprites.add(edgeRight);
		waterSprites.add(buildImage(edgeTop, cornerBottomRight));
		waterSprites.add(buildImage(buildImage(cornerTopRight, cornerBottomRight), cornerTopLeft));
		waterSprites.add(buildImage(cornerTopRight, cornerBottomRight));

		waterSprites.add(buildImage(cornerTopLeft, cornerBottomRight));
		waterSprites.add(cornerBottomRight);
		waterSprites.add(twoEdgeTopLeft);
		waterSprites.add(buildImage(edgeLeft, cornerTopRight));
		waterSprites.add(edgeLeft);
		waterSprites.add(buildImage(edgeTop, cornerBottomLeft));
		waterSprites.add(buildImage(buildImage(cornerTopLeft, cornerBottomLeft), cornerTopRight));
		waterSprites.add(buildImage(cornerTopRight, cornerBottomLeft));

		waterSprites.add(buildImage(cornerTopLeft, cornerBottomLeft));
		waterSprites.add(cornerBottomLeft);
		waterSprites.add(edgeTop);
		waterSprites.add(buildImage(cornerTopLeft, cornerTopRight));
		waterSprites.add(cornerTopRight);
		waterSprites.add(cornerTopLeft);

		for (int i = 0; i < waterFrames.length; i++)
			waterFrames[i] = getSprite(atlas, i, 1);

		// Road Sprites
		roadSprites.add(getSprite(atlas, 0, 12));
		roadSprites.add(getSprite(atlas, 0, 9));
		roadSprites.add(getSprite(atlas, 3, 9));
		roadSprites.add(getSprite(atlas, 2, 8));
		roadSprites.add(getSprite(atlas, 1, 9));
		roadSprites.add(getSprite(atlas, 3, 8));
		roadSprites.add(getSprite(atlas, 1, 7));
		roadSprites.add(getSprite(atlas, 2, 10));

		roadSprites.add(getSprite(atlas, 2, 9));
		roadSprites.add(getSprite(atlas, 0, 7));
		roadSprites.add(getSprite(atlas, 1, 8));
		roadSprites.add(getSprite(atlas, 1, 10));
		roadSprites.add(getSprite(atlas, 0, 8));
		roadSprites.add(getSprite(atlas, 3, 10));
		roadSprites.add(getSprite(atlas, 0, 10));
		roadSprites.add(getSprite(atlas, 0, 11));

		tileSprites.add(grassSprites);
		tileSprites.add(waterSprites);
		tileSprites.add(roadSprites);

	}

	private static void loadTutorialImages() {

		tutorialEdit = new BufferedImage[3];
		tutorialPlay = new BufferedImage[5];

		for (int i = 0; i < tutorialEdit.length; i++)
			tutorialEdit[i] = LoadSave.loadImage("tutorial_images/edit_" + (i + 1) + ".png");

		for (int i = 0; i < tutorialPlay.length; i++)
			tutorialPlay[i] = LoadSave.loadImage("tutorial_images/play_" + (i + 1) + ".png");

	}

	private static BufferedImage[] getImageArray(String atlasName, int width, int height, int amount) {

		BufferedImage atlas = LoadSave.loadImage(atlasName);
		BufferedImage[] temp = new BufferedImage[amount];

		for (int i = 0; i < temp.length; i++)
			temp[i] = atlas.getSubimage(i * width, 0, width, height);

		return temp;

	}

	private static BufferedImage[][] get2DImageArray(String atlasName, int width, int height, int rows, int columns) {

		BufferedImage atlas = LoadSave.loadImage(atlasName);
		BufferedImage[][] temp = new BufferedImage[rows][columns];

		for (int i = 0; i < temp[0].length; i++)
			for (int j = 0; j < temp.length; j++)
				temp[j][i] = atlas.getSubimage(i * width, j * height, width, height);

		return temp;

	}

	private static BufferedImage getSprite(BufferedImage atlas, int x, int y) {
		return atlas.getSubimage(x * spriteSize, y * spriteSize, spriteSize, spriteSize);
	}

	private static BufferedImage buildImage(BufferedImage base, BufferedImage top) {

		int w = base.getWidth();
		int h = base.getHeight();

		BufferedImage newImg = new BufferedImage(w, h, base.getType());

		Graphics2D g2d = newImg.createGraphics();

		g2d.drawImage(base, 0, 0, null);
		g2d.drawImage(top, 0, 0, null);
		g2d.dispose();

		return newImg;

	}

	public static void createMapThumbnail(Map map, String mapName) {

		BufferedImage thumbnail = new BufferedImage(128, 80, TYPE_INT_ARGB);
		Graphics g = thumbnail.getGraphics();

		Tile[][] tileData = map.getTileData();

		for (int i = 0; i < tileData.length; i++)
			for (int j = 0; j < tileData[i].length; j++) {
				switch (tileData[i][j].getTileType()) {
				case WATER -> g.setColor(new Color(99, 197, 207));
				case GRASS -> g.setColor(new Color(141, 196, 53));
				case ROAD -> g.setColor(new Color(180, 131, 85));
				}

				g.fillRect(j * 4, i * 4, 4, 4);
			}

		File thumbnailFile = new File(LoadSave.mapPath + File.separator + mapName + "_thumbnail.png");

		try {
			ImageIO.write(thumbnail, "png", thumbnailFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static BufferedImage[] getCropSprites(int cropType) {

		switch (cropType) {
		case BELL_PEPPER:
			return bellPepperSprites;
		case CHILI:
			return chiliSprites;
		case CORN:
			return cornSprites;
		case TOMATO:
			return tomatoSprites;
		}

		return null;

	}

	public static BufferedImage[][] getEnemySprites(int enemyType) {

		switch (enemyType) {
		case CROW, CROW_BOSS:
			return crowSprites;
		case MOLD, MOLD_BOSS:
			return moldSprites;
		case WORM, WORM_BOSS:
			return wormSprites;
		}

		return null;

	}

	public static int getEnemyAnimationFrameCount(int enemyType, int animation) {

		switch (enemyType) {
		case CROW, CROW_BOSS:
			return crowSprites[animation].length;
		case MOLD, MOLD_BOSS:
			return moldSprites[animation].length;
		case WORM, WORM_BOSS:
			return wormSprites[animation].length;
		}

		return 0;

	}

}
