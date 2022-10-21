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

import crops.Crop;
import gamestates.GameStates;
import gamestates.Play;
import helps.ImageLoader;
import main.Game;

public class ActionBar extends UIBar {

	private Crop selectedCrop;
	private Play play;
	private Random random = new Random();
	private SquareButton bellPepper, corn, chili, tomato;
	private TextButton menu, pause, save;
	private TextButton harvest, upgradeDamage, upgradeRange;
	private ArrayList<TextButton> mainButtons = new ArrayList<>();
	private ArrayList<SquareButton> cropButtons = new ArrayList<>();

	private int displayXStart = Game.SCREEN_WIDTH - ImageLoader.cropDisplayBG.getWidth() - 14;
	private int displayYStart = Game.SCREEN_HEIGHT + 10;
	private int[] cropOrder = { CORN, TOMATO, CHILI, BELL_PEPPER };

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

		int xOffset = 5;
		int totalButtonWidth = getButtonWidth(SQUARE) * 4 + xOffset * 3;
		int menuEndX = menu.getBounds().x + menu.getBounds().width;
		xStart = menuEndX + (displayXStart - menuEndX) / 2 - totalButtonWidth / 2;
		yStart = Game.SCREEN_HEIGHT + (UI_HEIGHT / 2 - getButtonHeight(SQUARE) / 2);

		corn = new SquareButton(BLUE, xStart, yStart, ImageLoader.getCropSprites(CORN)[0]);
		tomato = new SquareButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(TOMATO)[0]);
		chili = new SquareButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(CHILI)[0]);
		bellPepper = new SquareButton(BLUE, xStart += getButtonWidth(SQUARE) + xOffset, yStart,
				ImageLoader.getCropSprites(BELL_PEPPER)[0]);

		cropButtons.addAll(Arrays.asList(corn, tomato, chili, bellPepper));

		xStart = displayXStart + ImageLoader.cropDisplayBG.getWidth() - getButtonWidth(TEXT_SMALL) - 6;
		yStart = displayYStart + 10;

		harvest = new TextButton(TEXT_SMALL, "Harvest", BEIGE, xStart, yStart);
		upgradeDamage = new TextButton(TEXT_SMALL, "Upgrade", BEIGE, xStart, yStart += 31);
		upgradeRange = new TextButton(TEXT_SMALL, "Upgrade", BEIGE, xStart, yStart += 25);

	}

	public void update() {

		for (TextButton mb : mainButtons)
			mb.update();

		for (SquareButton cb : cropButtons)
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
		g.setColor(Color.BLACK);
		g.fillRect(0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT);
		g.drawImage(ImageLoader.tileSprites.get(0).get(0), 0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT, null);
		g.drawImage(ImageLoader.uiBGBeige, 0, Game.SCREEN_HEIGHT, UI_WIDTH, UI_HEIGHT, null);

		for (TextButton mb : mainButtons)
			mb.draw(g);

		for (SquareButton cb : cropButtons)
			cb.draw(g);

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

	}

	private void drawLabels(Graphics g) {

		g.setFont(new Font(Game.FONT_NAME, Font.PLAIN, 15));

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
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 12));

		for (int i = 0; i < cropButtons.size(); i++) {
			String cost = "" + getCropCost(cropOrder[i]);
			int xOffset = getButtonWidth(SQUARE) / 2 - ImageLoader.seeds.getWidth() / 2
					- g.getFontMetrics().stringWidth(cost) / 2 - 3;
			int x = cropButtons.get(i).getBounds().x + xOffset;
			int y = cropButtons.get(i).getBounds().y + cropButtons.get(i).getBounds().height + 3;
			g.drawImage(ImageLoader.seeds, x, y, ImageLoader.seeds.getWidth(), ImageLoader.seeds.getHeight(), null);
			g.drawString(cost, x + ImageLoader.seeds.getWidth() + 6, y + ImageLoader.seeds.getHeight() / 4 * 3);

		}

	}

	private void drawSelectedCrop(Graphics g) {

		if (selectedCrop != null) {
			// Main Body
			BufferedImage bg = ImageLoader.cropDisplayBG;
			g.drawImage(bg, displayXStart, displayYStart, bg.getWidth(), bg.getHeight(), null);

			// Selected Crop
			BufferedImage sprite = ImageLoader.getCropSprites(selectedCrop.getCropType())[selectedCrop.getColorIndex()];
			g.drawImage(sprite, displayXStart + 10, displayYStart + 14, sprite.getWidth() * 2, sprite.getHeight() * 2,
					null);

			// Crop Stats
			int xStart = displayXStart + 82;
			int yStart = displayYStart + 21;
			int yOffset = 20;
			g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 15));
			g.drawString("" + getCropName(selectedCrop.getCropType()), xStart, yStart);
			g.drawString("ID: " + selectedCrop.getId(), xStart, yStart += yOffset);
			g.drawString("Damage: " + (int) selectedCrop.getDamage(), xStart, yStart += yOffset);
			float range = Math.round((selectedCrop.getRange() / Game.TILE_SIZE * 100) / 100.0f);
			g.drawString("Range: " + range, xStart, yStart += yOffset);

			// Price Messages
			xStart = displayXStart + 6;
			yStart = displayYStart + ImageLoader.cropDisplayBG.getHeight() - 21;
			if (harvest.isMouseOver()) {
				g.setColor(Color.red);
				g.drawString("Sell for: " + selectedCrop.getHarvestAmount() + " seeds", xStart, yStart);
			} else if (upgradeDamage.isMouseOver()) {
				g.setColor(Color.blue);
				g.drawString("Increase Damage for: " + selectedCrop.getUpgradeCost() + " seeds", xStart, yStart);
			} else if (upgradeRange.isMouseOver()) {
				g.setColor(Color.blue);
				g.drawString("Increase Range for: " + selectedCrop.getUpgradeCost() + " seeds", xStart, yStart);
			}
		}

	}

	private void drawPlayerInformation(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		String seeds = "" + play.getSeeds();
		String lives = "" + play.getLives();

		int width = ImageLoader.seedPacket.getWidth();
		int height = ImageLoader.seedPacket.getHeight();
		int xOffset = 3;
		int yOffset = 6;

		int menuEndX = menu.getBounds().x + menu.getBounds().width;
		int x = menuEndX + (corn.getBounds().x - menuEndX) / 2
				- (width + xOffset + g.getFontMetrics().stringWidth("55")) / 2;
		int y = Game.SCREEN_HEIGHT + UI_HEIGHT / 2 - height - yOffset / 2;

		g.drawImage(ImageLoader.seedPacket, x, y, width, height, null);
		g.drawString(seeds, x + width + xOffset, y + height / 4 * 3);
		g.drawImage(ImageLoader.heart, x, y += height + yOffset, width, height, null);
		g.drawString(lives, x + width + xOffset, y + height / 4 * 3);

	}

	public void mousePressed(int x, int y) {

		for (TextButton mb : mainButtons)
			if (mb.getBounds().contains(x, y))
				mb.setMousePressed(true);

		for (SquareButton cb : cropButtons)
			if (cb.getBounds().contains(x, y))
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
				play.setUnsavedChangesOverlay(new UnsavedChangesOverlay(play, UnsavedChangesOverlay.EXIT_TO_MENU));
			} else {
				GameStates.setGameState(GameStates.MENU);
				play.getGame().getMapHandler().loadMaps();
			}
		} else if (pause.getBounds().contains(x, y) && pause.isMousePressed())
			play.togglePause();
		else if (save.getBounds().contains(x, y) && save.isMousePressed()) {
			play.saveGame();
			selectedCrop = null;
		} else if (corn.getBounds().contains(x, y) && corn.isMousePressed()) {
			play.setSelectedCropType(CORN);
			play.setSelectedCropColorIndex(random.nextInt(ImageLoader.getCropSprites(CORN).length));
		} else if (tomato.getBounds().contains(x, y) && tomato.isMousePressed()) {
			play.setSelectedCropType(TOMATO);
			play.setSelectedCropColorIndex(random.nextInt(ImageLoader.getCropSprites(TOMATO).length));
		} else if (chili.getBounds().contains(x, y) && chili.isMousePressed()) {
			play.setSelectedCropType(CHILI);
			play.setSelectedCropColorIndex(random.nextInt(ImageLoader.getCropSprites(CHILI).length));
		} else if (bellPepper.getBounds().contains(x, y) && bellPepper.isMousePressed()) {
			play.setSelectedCropType(BELL_PEPPER);
			play.setSelectedCropColorIndex(random.nextInt(ImageLoader.getCropSprites(BELL_PEPPER).length));
		} else if (selectedCrop != null) {
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
			mb.setMousePressed(false);
		for (SquareButton cb : cropButtons)
			cb.setMousePressed(false);
		harvest.setMousePressed(false);
		harvest.setMouseOver(false);
		upgradeDamage.setMousePressed(false);
		upgradeDamage.setMouseOver(false);
		upgradeRange.setMousePressed(false);
		upgradeRange.setMouseOver(false);

	}

	public void mouseMoved(int x, int y) {

		harvest.setMouseOver(false);
		upgradeDamage.setMouseOver(false);
		upgradeRange.setMouseOver(false);

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

	public TextButton getPause() {
		return pause;
	}

	public void setSelectedCrop(Crop selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

}
