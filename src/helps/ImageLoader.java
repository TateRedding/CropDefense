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
import static helps.Constants.Directions.LEFT;
import static helps.Constants.Enemies.CROW;
import static helps.Constants.Enemies.MOLD;
import static helps.Constants.Enemies.WORM;
import static helps.Constants.Enemies.Animations.DEATH;
import static helps.Constants.Enemies.Animations.MOVE;
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

import main.Game;
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

	public static ArrayList<ArrayList<BufferedImage>> tileSprites;
	public static BufferedImage background, credits, creditsBG, cropDisplayBG, heart, newMapThumbnail, missingThumbnail,
			overlayBG, seedPacket, seeds, textBGLarge, textBGSmall, uiBGBeige, uiBGBlue;
	public static BufferedImage[] bellPepperSprites, chiliSprites, cornSprites, pathPointSprites, tomatoSprites,
			tutorialEdit, tutorialPlay, waterFrames;
	public static BufferedImage[][] crowSprites, crowSpritesLeft, moldSprites, moldSpritesLeft, mapButtons,
			projectileSprites, squareButtons, textButtonsLarge, textButtonsSmall, wormSprites, wormSpritesLeft;

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

		newMapThumbnail = new BufferedImage(128, 80, TYPE_INT_ARGB);
		missingThumbnail = new BufferedImage(128, 80, TYPE_INT_ARGB);

		Graphics g = newMapThumbnail.getGraphics();
		g.setColor(new Color(141, 196, 53));
		g.fillRect(0, 0, newMapThumbnail.getWidth(), newMapThumbnail.getHeight());

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));
		String text = "Create";
		int xStart = newMapThumbnail.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2;
		int yStart = newMapThumbnail.getHeight() / 2;
		g.drawString(text, xStart, yStart);

		text = "new map";
		xStart = newMapThumbnail.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart += g.getFontMetrics().getHeight() * .75;
		g.drawString(text, xStart, yStart);

		g = missingThumbnail.getGraphics();
		g.setColor(new Color(141, 196, 53));
		g.fillRect(0, 0, missingThumbnail.getWidth(), missingThumbnail.getHeight());

		g.setColor(Color.RED);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));
		text = "Missing";
		xStart = missingThumbnail.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart = missingThumbnail.getHeight() / 2;
		g.drawString(text, xStart, yStart);

		text = "thumbnail!";
		xStart = missingThumbnail.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart += g.getFontMetrics().getHeight() * .75;
		g.drawString(text, xStart, yStart);

	}

	private static void loadButtonImages() {

		mapButtons = new BufferedImage[4][2];
		squareButtons = new BufferedImage[4][2];
		textButtonsLarge = new BufferedImage[4][2];
		textButtonsSmall = new BufferedImage[4][2];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.MAP_BUTTONS);
		int width = getButtonWidth(MAP);
		int height = getButtonHeight(MAP);

		for (int i = 0; i < mapButtons[0].length; i++)
			for (int j = 0; j < mapButtons.length; j++)
				mapButtons[j][i] = atlas.getSubimage(i * width, j * height, width, height);

		atlas = LoadSave.loadImage(LoadSave.SQUARE_BUTTONS);
		width = getButtonWidth(SQUARE);
		height = getButtonHeight(SQUARE);

		for (int i = 0; i < squareButtons[0].length; i++)
			for (int j = 0; j < squareButtons.length; j++)
				squareButtons[j][i] = atlas.getSubimage(i * width, j * height, width, height);

		atlas = LoadSave.loadImage(LoadSave.TEXT_BUTTONS_SMALL);
		width = getButtonWidth(TEXT_SMALL);
		height = getButtonHeight(TEXT_SMALL);

		for (int i = 0; i < textButtonsSmall[0].length; i++)
			for (int j = 0; j < textButtonsSmall.length; j++)
				textButtonsSmall[j][i] = atlas.getSubimage(i * width, j * height, width, height);

		atlas = LoadSave.loadImage(LoadSave.TEXT_BUTTONS_LARGE);
		width = getButtonWidth(TEXT_LARGE);
		height = getButtonHeight(TEXT_LARGE);

		for (int i = 0; i < textButtonsLarge[0].length; i++)
			for (int j = 0; j < textButtonsLarge.length; j++)
				textButtonsLarge[j][i] = atlas.getSubimage(i * width, j * height, width, height);

	}

	private static void loadBackgroundImages() {

		background = LoadSave.loadImage(LoadSave.BACKGROUND);
		cropDisplayBG = LoadSave.loadImage(LoadSave.CROP_DISPLAY_BG);
		credits = LoadSave.loadImage(LoadSave.CREDITS);
		creditsBG = LoadSave.loadImage(LoadSave.CREDITS_BG);
		overlayBG = LoadSave.loadImage(LoadSave.OVERLAY_BG);
		textBGLarge = LoadSave.loadImage(LoadSave.TEXT_BG_LARGE);
		textBGSmall = LoadSave.loadImage(LoadSave.TEXT_BG_SMALL);
		uiBGBeige = LoadSave.loadImage(LoadSave.UI_BG_BEIGE);
		uiBGBlue = LoadSave.loadImage(LoadSave.UI_BG_BLUE);

	}

	private static void loadCropSprites() {

		bellPepperSprites = new BufferedImage[3];
		chiliSprites = new BufferedImage[2];
		cornSprites = new BufferedImage[3];
		tomatoSprites = new BufferedImage[3];

		projectileSprites = new BufferedImage[3][3];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.BELL_PEPPER_SPRITES);
		int spriteSize = 32;

		for (int i = 0; i < bellPepperSprites.length; i++)
			bellPepperSprites[i] = getSprite(atlas, i, 0, spriteSize);

		atlas = LoadSave.loadImage(LoadSave.CHILI_SPRITES);

		for (int i = 0; i < chiliSprites.length; i++)
			chiliSprites[i] = getSprite(atlas, i, 0, spriteSize);

		atlas = LoadSave.loadImage(LoadSave.CORN_SPRITES);

		for (int i = 0; i < cornSprites.length; i++)
			cornSprites[i] = getSprite(atlas, i, 0, spriteSize);

		atlas = LoadSave.loadImage(LoadSave.TOMATO_SPRITES);

		for (int i = 0; i < tomatoSprites.length; i++)
			tomatoSprites[i] = getSprite(atlas, i, 0, spriteSize);

		atlas = LoadSave.loadImage(LoadSave.PROJECTILE_SPRITES);
		spriteSize = 8;

		for (int i = 0; i < projectileSprites[0].length; i++)
			for (int j = 0; j < projectileSprites.length; j++)
				projectileSprites[j][i] = getSprite(atlas, i, j, spriteSize);

	}

	private static void loadEnemySprites() {

		crowSprites = new BufferedImage[2][4];
		crowSpritesLeft = new BufferedImage[2][4];
		moldSprites = new BufferedImage[2][4];
		moldSpritesLeft = new BufferedImage[2][4];
		wormSprites = new BufferedImage[2][4];
		wormSpritesLeft = new BufferedImage[2][4];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.CROW_SPRITES);
		int spriteSize = 32;

		for (int i = 0; i < crowSprites[0].length; i++)
			for (int j = 0; j < crowSprites.length; j++) {
				crowSprites[j][i] = getSprite(atlas, i, j, spriteSize);
				crowSpritesLeft[j][i] = flipImageYAxis(crowSprites[j][i]);
			}

		atlas = LoadSave.loadImage(LoadSave.MOLD_SPRITES);

		for (int i = 0; i < moldSprites[0].length; i++)
			for (int j = 0; j < moldSprites.length; j++) {
				moldSprites[j][i] = getSprite(atlas, i, j, spriteSize);
				moldSpritesLeft[j][i] = flipImageYAxis(moldSprites[j][i]);
			}

		atlas = LoadSave.loadImage(LoadSave.WORM_SPRITES);

		for (int i = 0; i < wormSprites[0].length; i++)
			for (int j = 0; j < wormSprites.length; j++) {
				wormSprites[j][i] = getSprite(atlas, i, j, spriteSize);
				wormSpritesLeft[j][i] = flipImageYAxis(wormSprites[j][i]);
			}

	}

	private static void loadIconSprites() {

		BufferedImage atlas = LoadSave.loadImage(LoadSave.ICON_SPRITES);
		int spriteSize = 32;

		heart = atlas.getSubimage(spriteSize, 0, spriteSize, spriteSize);
		seedPacket = atlas.getSubimage(0, 0, spriteSize, spriteSize);
		seeds = atlas.getSubimage(spriteSize * 2, 0, spriteSize / 2, spriteSize / 2);

	}

	private static void loadPathPointSprites() {

		pathPointSprites = new BufferedImage[2];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.PATH_POINTS);
		int spriteSize = 32;

		for (int i = 0; i < pathPointSprites.length; i++) {
			pathPointSprites[i] = getSprite(atlas, i, 0, spriteSize);
		}

	}

	private static void loadTileSprites() {

		tileSprites = new ArrayList<>();
		waterFrames = new BufferedImage[4];

		BufferedImage atlas = LoadSave.loadImage(LoadSave.TILE_ATLAS);
		int spriteSize = 32;

		ArrayList<BufferedImage> grassSprites = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> waterSprites = new ArrayList<BufferedImage>();
		ArrayList<BufferedImage> roadSprites = new ArrayList<BufferedImage>();

		// Grass Sprites
		for (int i = 0; i < 4; i++) {
			grassSprites.add(getSprite(atlas, i, 0, spriteSize));

		}

		// Water Sprites
		BufferedImage edgeTop = getSprite(atlas, 0, 2, spriteSize);
		BufferedImage edgeRight = getSprite(atlas, 1, 2, spriteSize);
		BufferedImage edgeBottom = getSprite(atlas, 2, 2, spriteSize);
		BufferedImage edgeLeft = getSprite(atlas, 3, 2, spriteSize);

		BufferedImage twoEdgeTopLeft = getSprite(atlas, 0, 3, spriteSize);
		BufferedImage twoEdgeTopRight = getSprite(atlas, 1, 3, spriteSize);
		BufferedImage twoEdgeBottomRight = getSprite(atlas, 2, 3, spriteSize);
		BufferedImage twoEdgeBottomLeft = getSprite(atlas, 3, 3, spriteSize);

		BufferedImage cornerBottomRight = getSprite(atlas, 0, 6, spriteSize);
		BufferedImage cornerBottomLeft = getSprite(atlas, 1, 6, spriteSize);
		BufferedImage cornerTopLeft = getSprite(atlas, 2, 6, spriteSize);
		BufferedImage cornerTopRight = getSprite(atlas, 3, 6, spriteSize);

		waterSprites.add(getSprite(atlas, 0, 5, spriteSize));
		waterSprites.add(getSprite(atlas, 2, 4, spriteSize));
		waterSprites.add(getSprite(atlas, 1, 4, spriteSize));
		waterSprites.add(buildImage(twoEdgeBottomRight, cornerTopLeft));
		waterSprites.add(twoEdgeBottomRight);
		waterSprites.add(getSprite(atlas, 3, 4, spriteSize));
		waterSprites.add(buildImage(twoEdgeBottomLeft, cornerTopRight));
		waterSprites.add(twoEdgeBottomLeft);

		waterSprites.add(buildImage(edgeTop, edgeBottom));
		waterSprites.add(buildImage(buildImage(edgeBottom, cornerTopLeft), cornerTopRight));
		waterSprites.add(buildImage(edgeBottom, cornerTopRight));
		waterSprites.add(buildImage(edgeBottom, cornerTopLeft));
		waterSprites.add(edgeBottom);
		waterSprites.add(getSprite(atlas, 0, 4, spriteSize));
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
			waterFrames[i] = getSprite(atlas, i, 1, spriteSize);

		// Road Sprites
		roadSprites.add(getSprite(atlas, 0, 12, spriteSize));
		roadSprites.add(getSprite(atlas, 0, 9, spriteSize));
		roadSprites.add(getSprite(atlas, 3, 9, spriteSize));
		roadSprites.add(getSprite(atlas, 2, 8, spriteSize));
		roadSprites.add(getSprite(atlas, 1, 9, spriteSize));
		roadSprites.add(getSprite(atlas, 3, 8, spriteSize));
		roadSprites.add(getSprite(atlas, 1, 7, spriteSize));
		roadSprites.add(getSprite(atlas, 2, 10, spriteSize));

		roadSprites.add(getSprite(atlas, 2, 9, spriteSize));
		roadSprites.add(getSprite(atlas, 0, 7, spriteSize));
		roadSprites.add(getSprite(atlas, 1, 8, spriteSize));
		roadSprites.add(getSprite(atlas, 1, 10, spriteSize));
		roadSprites.add(getSprite(atlas, 0, 8, spriteSize));
		roadSprites.add(getSprite(atlas, 3, 10, spriteSize));
		roadSprites.add(getSprite(atlas, 0, 10, spriteSize));
		roadSprites.add(getSprite(atlas, 0, 11, spriteSize));

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

	private static BufferedImage getSprite(BufferedImage atlas, int x, int y, int spriteSize) {
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
				case WATER:
					g.setColor(new Color(99, 197, 207));
					break;
				case GRASS:
					g.setColor(new Color(141, 196, 53));
					break;
				case ROAD:
					g.setColor(new Color(180, 131, 85));
					break;
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

	public static BufferedImage flipImageYAxis(BufferedImage img) {

		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage newImg = new BufferedImage(w, h, img.getType());
		Graphics g = newImg.createGraphics();

		g.drawImage(img, w, 0, -w, h, null);
		g.dispose();

		return newImg;

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

	public static BufferedImage[][] getEnemySprites(int enemyType, int direction) {

		switch (enemyType) {
		case CROW:
			if (direction == LEFT)
				return crowSpritesLeft;
			else
				return crowSprites;
		case MOLD:
			if (direction == LEFT)
				return moldSpritesLeft;
			else
				return moldSprites;
		case WORM:
			if (direction == LEFT)
				return wormSpritesLeft;
			else
				return wormSprites;
		}

		return null;

	}

	public static int getEnemyAnimationFrameCount(int enemyType, int animation) {

		switch (enemyType) {
		case CROW:
			if (animation == MOVE)
				return crowSprites[MOVE].length;
			else if (animation == DEATH)
				return crowSprites[DEATH].length;
		case MOLD:
			if (animation == MOVE)
				return moldSprites[MOVE].length;
			else if (animation == DEATH)
				return moldSprites[DEATH].length;
		case WORM:
			if (animation == MOVE)
				return wormSprites[MOVE].length;
			else if (animation == DEATH)
				return wormSprites[DEATH].length;
		}

		return 0;

	}

}
