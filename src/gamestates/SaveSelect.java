package gamestates;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
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

public abstract class SaveSelect extends FileSelect {

	protected ArrayList<TextButton> buttons = new ArrayList<>();
	protected File[] saveFiles;

	public SaveSelect(Game game) {

		super(game);
		initSaveButtons();

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

	public void update() {

		super.update();
		for (TextButton b : buttons)
			b.update();

	}

	public void render(Graphics g) {

		super.render(g);
		for (TextButton b : buttons)
			b.draw(g);

		if (selectedFile != null)
			drawLastPlayedInformation(g);

	}

	private void drawLastPlayedInformation(Graphics g) {

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
		String saveName = "Selected game: " + selectedFile.getName().substring(0,
				selectedFile.getName().length() - LoadSave.saveFileExtension.length());
		String lastSaved = "Last saved on: " + month + "/" + day + "/" + year;

		g.setColor(Color.BLACK);
		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 20));

		g.drawString(saveName, xStart, yStart);

		yStart += g.getFontMetrics().getHeight() / 5 * 4;
		g.drawString(lastSaved, xStart, yStart);

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);
		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMousePressed(true);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);
		for (TextButton b : buttons)
			b.setMousePressed(false);

	}

	public File[] getSaveFiles() {
		return saveFiles;
	}

	public int getMaxSaves() {
		return maxSaves;
	}

}
