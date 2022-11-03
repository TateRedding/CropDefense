package ui;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.SQUARE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static helps.Constants.Crops.getCropCost;
import static helps.Constants.Crops.getCropName;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import entities.Crop;
import entities.Enemy;
import gamestates.GameStates;
import gamestates.Play;
import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;

public class ActionBar extends UIBar {

	private Crop selectedCrop;
	private Play play;
	private Random random = new Random();
	private CropButton bellPepper, corn, chili, tomato;
	private TextButton menu, pause, save;
	private TextButton harvest, upgradeDamage, upgradeRange;
	private ArrayList<TextButton> mainButtons = new ArrayList<>();
	private ArrayList<CropButton> cropButtons = new ArrayList<>();

	private int displayXStart = Game.SCREEN_WIDTH - ImageLoader.cropDisplayBG.getWidth() - 14;
	private int displayYStart = Y + 10;
	private int[] cropOrder = { CORN, TOMATO, CHILI, BELL_PEPPER };

	private int selectedCropXStart = displayXStart + 18;
	private int selectedCropYStart = displayYStart + 10;
	private int selectedCropAreaWidth = 72;
	private int selectedCropAreaHeight = 72;

	public ActionBar(Play play) {

		this.play = play;
		initButtons();

	}

	private void initButtons() {

		int xStart = 14;
		int yStart = Game.SCREEN_HEIGHT + 20;
		int yOffset = 2;

		menu = new TextButton(TEXT_SMALL, "Menu", GRAY, xStart, yStart);
		pause = new TextButton(TEXT_SMALL, "Pause", GRAY, xStart, yStart += getButtonHeight(TEXT_SMALL) + yOffset);
		save = new TextButton(TEXT_SMALL, "Save", GRAY, xStart, yStart += getButtonHeight(TEXT_SMALL) + yOffset);

		mainButtons.addAll(Arrays.asList(menu, pause, save));

		int xOffset = 15;
		int totalButtonWidth = getButtonWidth(SQUARE) * 4 + xOffset * 3;
		int menuEndX = menu.getBounds().x + menu.getBounds().width;
		xStart = menuEndX + (displayXStart - menuEndX) / 2 - totalButtonWidth / 2;
		yStart = Game.SCREEN_HEIGHT + (UI_HEIGHT / 2 - getButtonHeight(SQUARE) / 2);

		corn = new CropButton(BLUE, xStart, yStart, ImageLoader.getCropSprites(CORN)[0], CORN, this);
		tomato = new CropButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(TOMATO)[0], TOMATO, this);
		chili = new CropButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(CHILI)[0], CHILI, this);
		bellPepper = new CropButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(BELL_PEPPER)[0], BELL_PEPPER, this);

		cropButtons.addAll(Arrays.asList(corn, tomato, chili, bellPepper));

		int selectedCropYEnd = (selectedCropYStart + selectedCropAreaHeight);
		yStart = selectedCropYEnd + (displayYStart + ImageLoader.cropDisplayBG.getHeight() - selectedCropYEnd) / 2
				- getButtonHeight(TEXT_SMALL) / 2;

		harvest = new TextButton(TEXT_SMALL, "Harvest", BEIGE, displayXStart + 6, yStart);

		int displayBoxXEnd = selectedCropXStart + selectedCropAreaWidth;
		int areaWidth = (displayXStart + ImageLoader.cropDisplayBG.getWidth() - displayBoxXEnd) / 2;

		xStart = displayBoxXEnd + areaWidth / 2 - getButtonWidth(TEXT_SMALL) / 2;
		yStart = selectedCropYStart + selectedCropAreaHeight - 12;
		upgradeDamage = new TextButton(TEXT_SMALL, "Upgrade", BEIGE, xStart, yStart);
		upgradeRange = new TextButton(TEXT_SMALL, "Upgrade", BEIGE, xStart + areaWidth, yStart);

	}

	public void update() {

		for (TextButton mb : mainButtons)
			mb.update();

		for (CropButton cb : cropButtons)
			cb.update();

		if (selectedCrop != null) {
			harvest.update();
			if (selectedCrop.getDamageTier() < 3)
				upgradeDamage.update();
			if (selectedCrop.getRangeTier() < 3)
				upgradeRange.update();
		}

	}

	public void draw(Graphics g) {

		// Main UI Body
		g.setColor(new Color(141, 196, 53));
		g.fillRect(X, Y, UI_WIDTH, UI_HEIGHT);
		g.drawImage(ImageLoader.uiBGBeige, X, Y, null);

		for (TextButton mb : mainButtons)
			mb.draw(g);

		for (CropButton cb : cropButtons) {
			cb.draw(g);
			if (cb.isMouseOver())
				play.drawCropToolTip(g, cb.getCropType());

		}

		drawButtonOverlays(g);

		drawLabels(g);
		drawCropPrices(g);

		// Selected Crop Information
		if (selectedCrop != null) {
			drawSelectedCrop(g);
			harvest.draw(g);
			if (selectedCrop.getDamageTier() < 3)
				upgradeDamage.draw(g);
			if (selectedCrop.getRangeTier() < 3)
				upgradeRange.draw(g);
		}

		// Player Information
		drawPlayerInformation(g);

		// Wave Information
		drawWaveInformation(g);

	}

	private void drawButtonOverlays(Graphics g) {

		g.setColor(new Color(255, 0, 0, 100));

		int seeds = play.getSeeds();

		for (CropButton cb : cropButtons)
			if (getCropCost(cb.getCropType()) > seeds)
				g.fillRect(cb.getBounds().x, cb.getBounds().y, cb.getBounds().width, cb.getBounds().height);

	}

	private void drawLabels(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));

		int yStart = corn.getBounds().y - 6;

		for (int i = 0; i < cropButtons.size(); i++) {
			String label = getCropName(cropOrder[i]);
			int xStart = (cropButtons.get(i).getBounds().x + cropButtons.get(i).getBounds().width / 2)
					- g.getFontMetrics().stringWidth(label) / 2;
			g.drawString(label, xStart, yStart);
		}

	}

	private void drawCropPrices(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(16f));

		for (int i = 0; i < cropButtons.size(); i++) {

			String cost = "" + getCropCost(cropOrder[i]);
			int xOffset = getButtonWidth(SQUARE) / 2 - ImageLoader.seeds.getWidth() / 2
					- g.getFontMetrics().stringWidth(cost) / 2 - 3;
			int x = cropButtons.get(i).getBounds().x + xOffset;
			int y = cropButtons.get(i).getBounds().y + cropButtons.get(i).getBounds().height + 3;
			g.drawImage(ImageLoader.seeds, x, y, null);
			DrawText.drawTextCenteredRightAlign(g, cost, x + ImageLoader.seeds.getWidth() + 6, y,
					ImageLoader.seeds.getHeight());

		}

	}

	private void drawSelectedCrop(Graphics g) {

		if (selectedCrop != null) {
			// Main Body
			BufferedImage bg = ImageLoader.cropDisplayBG;
			g.drawImage(bg, displayXStart, displayYStart, null);

			// Selected Crop
			BufferedImage sprite = ImageLoader.getCropSprites(selectedCrop.getCropType())[selectedCrop.getColorIndex()];

			g.drawImage(sprite, selectedCropXStart + 4, selectedCropYStart + 4, sprite.getWidth() * 2,
					sprite.getHeight() * 2, null);

			// Crop Stats
			int displayBoxXEnd = selectedCropXStart + selectedCropAreaWidth;
			int areaWidth = (displayXStart + ImageLoader.cropDisplayBG.getWidth() - displayBoxXEnd) / 2;
			int areaHeight = selectedCropAreaHeight - 12;
			;

			g.setColor(Color.BLACK);
			g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));

			String damageTier = "Tier: " + selectedCrop.getDamageTier() + "/3";
			String damageStr = "" + (int) selectedCrop.getDamage();
			String[] damageLines = new String[] { "Damage", damageTier, damageStr };
			DrawText.drawTextCentered(g, damageLines, 5, displayBoxXEnd, selectedCropYStart, areaWidth, areaHeight);

			String rangeTier = "Tier: " + selectedCrop.getRangeTier() + "/3";
			String rangeStr = "" + Math.round(selectedCrop.getRange() / Game.TILE_SIZE * 100) / 100.0f;
			String[] rangeLines = new String[] { "Range", rangeTier, rangeStr };
			DrawText.drawTextCentered(g, rangeLines, 5, displayBoxXEnd + areaWidth, selectedCropYStart, areaWidth,
					areaHeight);

			// Price Messages
			g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(20f));
			int xStart = harvest.getBounds().x + harvest.getBounds().width;
			int yStart = harvest.getBounds().y;
			int height = harvest.getBounds().height;
			int width = displayXStart + ImageLoader.cropDisplayBG.getWidth() - xStart;
			if (harvest.isMouseOver()) {
				g.setColor(Color.RED);
				DrawText.drawTextCentered(g, "Sell for: " + selectedCrop.getHarvestAmount() + " seeds", xStart, yStart,
						width, height);
			} else if (upgradeDamage.isMouseOver() || upgradeRange.isMouseOver()) {
				g.setColor(Color.BLUE);
				DrawText.drawTextCentered(g, "Upgrade cost: " + selectedCrop.getUpgradeCost() + " seeds", xStart,
						yStart, width, height);
			}
		}

	}

	private void drawPlayerInformation(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));

		String seeds = "" + play.getSeeds();
		String lives = "" + play.getLives();

		int width = ImageLoader.seedPacket.getWidth();
		int height = ImageLoader.seedPacket.getHeight();
		int xOffset = 3;
		int yOffset = 6;

		int menuEndX = menu.getBounds().x + menu.getBounds().width;
		int x = menuEndX + (corn.getBounds().x - menuEndX) / 2
				- (width + xOffset + g.getFontMetrics().stringWidth("55")) / 2;
		int y = Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT / 2 - height - yOffset / 2;

		g.drawImage(ImageLoader.seedPacket, x, y, null);
		DrawText.drawTextCenteredRightAlign(g, seeds, x + width + xOffset, y, height);

		y += height + yOffset;
		g.drawImage(ImageLoader.heart, x, y, null);
		DrawText.drawTextCenteredRightAlign(g, lives, x + width + xOffset, y, height);

	}

	private void drawWaveInformation(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(24f));

		int enemies = 0;
		for (Enemy e : play.getEnemyHandler().getEnemies())
			if (e.isAlive())
				enemies++;

		String waveInfo = "Wave " + play.getCurrentWave() + "/" + play.getTotalWaves();
		String enemyInfo = "Enemies: " + enemies;
		String[] lines = new String[] { waveInfo, enemyInfo };

		int xStart = bellPepper.getBounds().x + bellPepper.getBounds().width + 15;

		DrawText.drawTextCenteredRightAlign(g, lines, 5, xStart, Game.SCREEN_HEIGHT, UI_HEIGHT);

	}

	public void mousePressed(int x, int y) {

		for (TextButton mb : mainButtons)
			if (mb.getBounds().contains(x, y))
				mb.setMousePressed(true);

		for (CropButton cb : cropButtons)
			if (cb.getBounds().contains(x, y) && getCropCost(cb.getCropType()) <= play.getSeeds())
				cb.setMousePressed(true);

		if (selectedCrop != null) {
			if (harvest.getBounds().contains(x, y))
				harvest.setMousePressed(true);
			if (upgradeDamage.getBounds().contains(x, y))
				if (selectedCrop.getDamageTier() < 3)
					upgradeDamage.setMousePressed(true);
			if (upgradeRange.getBounds().contains(x, y))
				if (selectedCrop.getRangeTier() < 3)
					upgradeRange.setMousePressed(true);
		}

	}

	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			if (play.isUnsavedChanges()) {
				play.setPaused(true);
				play.setUnsavedOverlayActive(true);
				int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.overlayBG.getHeight() / 2;
				play.setUnsavedChangesOverlay(
						new UnsavedChangesOverlay(play, UnsavedChangesOverlay.EXIT_TO_MENU, yStart));
			} else {
				GameStates.setGameState(GameStates.MENU);
				play.getGame().getMapHandler().loadMaps();
			}
		} else if (pause.getBounds().contains(x, y) && pause.isMousePressed())
			play.togglePause();
		else if (save.getBounds().contains(x, y) && save.isMousePressed()) {
			play.saveGame();
			selectedCrop = null;
		} else
			for (CropButton cb : cropButtons) {
				int cropType = cb.getCropType();
				if (cb.getBounds().contains(x, y) && cb.isMousePressed() && getCropCost(cropType) <= play.getSeeds()) {
					play.setSelectedCropType(cropType);
					play.setSelectedCropColorIndex(random.nextInt(ImageLoader.getCropSprites(cropType).length));
				}
			}
		if (selectedCrop != null) {
			if (harvest.getBounds().contains(x, y) && harvest.isMousePressed()) {
				play.harvestCrop(selectedCrop);
				selectedCrop = null;
			} else if (upgradeDamage.getBounds().contains(x, y) && upgradeDamage.isMousePressed()) {
				if (selectedCrop.getDamageTier() < 3)
					play.upgradeDamage(selectedCrop);
			} else if (upgradeRange.getBounds().contains(x, y) && upgradeRange.isMousePressed())
				if (selectedCrop.getRangeTier() < 3)
					play.upgradeRange(selectedCrop);
		}

		for (TextButton mb : mainButtons)
			mb.reset();
		for (SquareButton cb : cropButtons)
			cb.reset();
		harvest.reset();
		upgradeDamage.reset();
		upgradeRange.reset();

	}

	public void mouseMoved(int x, int y) {

		for (TextButton mb : mainButtons)
			mb.setMouseOver(false);
		for (CropButton cb : cropButtons)
			cb.setMouseOver(false);
		harvest.setMouseOver(false);
		upgradeDamage.setMouseOver(false);
		upgradeRange.setMouseOver(false);

		for (TextButton mb : mainButtons)
			if (mb.getBounds().contains(x, y))
				mb.setMouseOver(true);

		for (CropButton cb : cropButtons)
			if (cb.getBounds().contains(x, y))
				cb.setMouseOver(true);

		if (selectedCrop != null) {
			if (harvest.getBounds().contains(x, y))
				harvest.setMouseOver(true);
			if (selectedCrop.getDamageTier() < 3)
				if (upgradeDamage.getBounds().contains(x, y))
					upgradeDamage.setMouseOver(true);
			if (selectedCrop.getRangeTier() < 3)
				if (upgradeRange.getBounds().contains(x, y))
					upgradeRange.setMouseOver(true);
		}

	}

	public Play getPlay() {
		return play;
	}

	public TextButton getPause() {
		return pause;
	}

	public void setSelectedCrop(Crop selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

}
