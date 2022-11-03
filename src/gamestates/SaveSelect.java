package gamestates;

import static helps.Constants.Buttons.GRAY;
import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;
import static ui.UIBar.UI_HEIGHT;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

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
			b.reset();

	}

	public void mouseMoved(int x, int y) {

		super.mouseMoved(x, y);

		for (TextButton b : buttons)
			b.setMouseOver(false);

		for (TextButton b : buttons)
			if (b.getBounds().contains(x, y))
				b.setMouseOver(true);

	}

	public File[] getSaveFiles() {
		return saveFiles;
	}

	public int getMaxSaves() {
		return maxSaves;
	}

}
