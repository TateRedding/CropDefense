package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.BLUE;
import static helps.Constants.Buttons.BROWN;
import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import ui.TextButton;

public abstract class SaveSelect extends State implements StateMethods {

	protected TextButton menu, delete, yes, no;
	protected ArrayList<TextButton> buttons = new ArrayList<>();
	protected File[] saveFiles;
	protected File selectedFile;

	protected int maxSaves = 5;

	protected boolean deleting;

	public SaveSelect(Game game) {

		super(game);
		initSaveButtons();
		initTextButtons();

	}

	private void initFiles() {

		File saveFolder = new File(LoadSave.savePath);
		this.saveFiles = saveFolder.listFiles();

	}

	public void initSaveButtons() {

		initFiles();
		buttons.clear();

		int x = Game.SCREEN_WIDTH / 2 - getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 25;
		int y = (Game.SCREEN_HEIGHT + UI_HEIGHT) / 2
				- (getButtonHeight(TEXT_LARGE) * maxSaves + yOffset * (maxSaves - 1)) / 2;

		for (int i = 0; i < maxSaves; i++) {
			String text = "";
			if (saveFiles != null)
				if (saveFiles.length >= i + 1)
					text = saveFiles[i].getName().substring(0,
							saveFiles[i].getName().length() - LoadSave.saveFileExtension.length());
				else
					text = "Empty";

			buttons.add(new TextButton(TEXT_LARGE, text, GRAY, x, y));
			y += getButtonHeight(TEXT_LARGE) + yOffset;

		}

	}

	private void initTextButtons() {

		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);

		int saveButtonRightEdge = buttons.get(0).getBounds().x + buttons.get(0).getBounds().width;
		int xStart = saveButtonRightEdge + (Game.SCREEN_WIDTH - saveButtonRightEdge) / 2
				- getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 + (yOffset + getButtonHeight(TEXT_LARGE)) / 2;
		delete = new TextButton(TEXT_LARGE, "Delete", BROWN, xStart, yStart);

		yStart += yOffset + getButtonHeight(TEXT_LARGE);
		yes = new TextButton(TEXT_SMALL, "Yes", BEIGE, xStart, yStart);
		xStart = delete.getBounds().x + delete.getBounds().width - getButtonWidth(TEXT_SMALL);
		no = new TextButton(TEXT_SMALL, "No", BEIGE, xStart, yStart);

	}

	@Override
	public void update() {

		menu.update();
		for (TextButton b : buttons)
			b.update();

		if (selectedFile != null) {
			delete.update();
			if (deleting) {
				yes.update();
				no.update();
			}
		}

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, ImageLoader.background.getWidth(), ImageLoader.background.getHeight(),
				null);

		menu.draw(g);
		for (TextButton b : buttons)
			b.draw(g);

		if (selectedFile != null) {
			delete.draw(g);
			drawLastPlayedInformation(g);

			if (deleting) {
				int xStart = delete.getBounds().x + delete.getBounds().width / 2
						- ImageLoader.textBGSmall.getWidth() / 2;
				int yStart = delete.getBounds().y - ImageLoader.textBGSmall.getHeight() - getButtonHeight(TEXT_LARGE)
						- 20;
				g.drawImage(ImageLoader.textBGSmall, xStart, yStart, ImageLoader.textBGSmall.getWidth(),
						ImageLoader.textBGSmall.getHeight(), null);
				g.setColor(Color.BLACK);
				g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 23));
				String text = "Are you sure you want";
				xStart = delete.getBounds().x + delete.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				yStart += ImageLoader.textBGSmall.getHeight() / 2 - 5;
				g.drawString(text, xStart, yStart);

				text = "to delete this file?";
				xStart = delete.getBounds().x + delete.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				yStart += g.getFontMetrics().getHeight() / 5 * 4;
				g.drawString(text, xStart, yStart);
				yes.draw(g);
				no.draw(g);
			}
		}

	}

	private void drawLastPlayedInformation(Graphics g) {

		Path filePath = Paths.get(selectedFile.getAbsolutePath());
		BasicFileAttributes attr = null;
		try {
			attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int xStart = 90;
		int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.textBGSmall.getHeight() / 2;
		g.drawImage(ImageLoader.textBGSmall, xStart, yStart, ImageLoader.textBGSmall.getWidth(),
				ImageLoader.textBGSmall.getHeight(), null);

		String creationTime = "" + attr.creationTime();
		String month = creationTime.substring(5, 7);
		String day = creationTime.substring(8, 10);
		String year = creationTime.substring(0, 4);
		String lastSaved = "Last saved on: " + month + "/" + day + "/" + year;

		String saveName = "Selected game: " + selectedFile.getName().substring(0,
				selectedFile.getName().length() - LoadSave.saveFileExtension.length());

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		xStart = 100;
		yStart = Game.SCREEN_HEIGHT / 2 - 2;
		g.drawString(saveName, xStart, yStart);

		yStart += g.getFontMetrics().getHeight() / 5 * 4;
		g.drawString(lastSaved, xStart, yStart);

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);
		else if (selectedFile != null) {
			if (delete.getBounds().contains(x, y))
				delete.setMousePressed(true);
			if (deleting) {
				if (yes.getBounds().contains(x, y))
					yes.setMousePressed(true);
				else if (no.getBounds().contains(x, y))
					no.setMousePressed(true);
			}
		}
		for (TextButton tb : buttons)
			if (tb.getBounds().contains(x, y))
				tb.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
			GameStates.setGameState(GameStates.MENU);
			deleting = false;
			selectedFile = null;
		} else if (selectedFile != null) {
			if (delete.getBounds().contains(x, y) && delete.isMousePressed())
				deleting = true;
			if (deleting) {
				if (yes.getBounds().contains(x, y) && yes.isMousePressed()) {
					selectedFile.delete();
					initSaveButtons();
					selectedFile = null;
					deleting = false;
				} else if (no.getBounds().contains(x, y) && no.isMousePressed())
					deleting = false;
			}

		}
		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y) && b.isMousePressed())
				if (!b.getText().equals("Empty")) {
					File saveFile = new File(
							LoadSave.savePath + File.separator + b.getText() + LoadSave.saveFileExtension);
					selectedFile = saveFile;
				}

		menu.setMousePressed(false);
		delete.setMousePressed(false);
		yes.setMousePressed(false);
		no.setMousePressed(false);
		for (TextButton b : buttons)
			b.setMousePressed(false);

	}

	@Override
	public void mouseDragged(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int x, int y) {
		// TODO Auto-generated method stub

	}

	public File[] getSaveFiles() {
		return saveFiles;
	}

	public int getMaxSaves() {
		return maxSaves;
	}

}
