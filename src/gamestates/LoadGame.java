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

public class LoadGame extends State implements StateMethods {

	private TextButton menu, load, delete, yes, no;
	private ArrayList<TextButton> buttons = new ArrayList<>();
	private File[] saveFiles;
	private File selectedFile;

	private int maxSaves = 5;

	private boolean deleting;

	public LoadGame(Game game) {

		super(game);
		initSaveButtons();
		initTextButtons();

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

	private void initFiles() {

		File saveFolder = new File(LoadSave.savePath);
		this.saveFiles = saveFolder.listFiles();

	}

	private void initTextButtons() {

		menu = new TextButton(TEXT_LARGE, "Main Menu", BLUE, 10, 10);

		int saveButtonRightEdge = buttons.get(0).getBounds().x + buttons.get(0).getBounds().width;
		int xStart = saveButtonRightEdge + (Game.SCREEN_WIDTH - saveButtonRightEdge) / 2
				- getButtonWidth(TEXT_LARGE) / 2;
		int yOffset = 10;
		int yStart = Game.SCREEN_HEIGHT / 2 - (yOffset + getButtonHeight(TEXT_LARGE)) / 2;
		load = new TextButton(TEXT_LARGE, "Load", BROWN, xStart, yStart);
		yStart += yOffset + getButtonHeight(TEXT_LARGE);
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
			load.update();
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
			load.draw(g);
			delete.draw(g);
			drawLastPlayedInformation(g);

			if (deleting) {
				g.setColor(Color.BLACK);
				g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));
				String text = "Are you sure you want to delete this file?";
				int xStart = load.getBounds().x + load.getBounds().width / 2 - g.getFontMetrics().stringWidth(text) / 2;
				int yStart = load.getBounds().y - 10;
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

		String creationTime = "" + attr.creationTime();
		String month = creationTime.substring(5, 7);
		String day = creationTime.substring(8, 10);
		String year = creationTime.substring(0, 4);
		String lastSaved = "Last saved on: " + month + "/" + day + "/" + year;

		String saveName = "Selected game: " + selectedFile.getName().substring(0,
				selectedFile.getName().length() - LoadSave.saveFileExtension.length());

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		int xStart = 100;
		int yStart = Game.SCREEN_HEIGHT / 2;
		g.drawString(saveName, xStart, yStart);

		yStart += g.getFontMetrics().getHeight();
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
			if (load.getBounds().contains(x, y))
				load.setMousePressed(true);
			else if (delete.getBounds().contains(x, y))
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
			if (load.getBounds().contains(x, y) && load.isMousePressed()) {
				game.loadGame(selectedFile);
				selectedFile = null;
				GameStates.setGameState(GameStates.PLAY);
				deleting = false;
			} else if (delete.getBounds().contains(x, y) && delete.isMousePressed())
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
		load.setMousePressed(false);
		delete.setMousePressed(false);
		yes.setMousePressed(false);
		no.setMousePressed(false);
		for (TextButton b : buttons)
			b.setMousePressed(false);

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

	}

	public File[] getSaveFiles() {
		return saveFiles;
	}

	public int getMaxSaves() {
		return maxSaves;
	}

}
