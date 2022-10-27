package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import ui.NameFileOverlay;
import ui.TextButton;
import ui.UnsavedChangesOverlay;

public class SaveGame extends SaveSelect {

	private NameFileOverlay nameFileOverlay;

	private boolean namingFile;

	public SaveGame(Game game) {
		super(game);
	}

	public void render(Graphics g) {

		super.render(g);

		int xStart = Game.SCREEN_WIDTH / 2 - ImageLoader.textBGLarge.getWidth() / 2;
		int yStart = buttons.get(0).getBounds().y - ImageLoader.textBGLarge.getHeight() - 25;
		g.drawImage(ImageLoader.textBGLarge, xStart, yStart, null);

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, 30));
		g.setColor(Color.BLACK);
		String text = "Select a slot to save your game.";
		xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
		yStart += ImageLoader.textBGLarge.getHeight() / 2 + g.getFontMetrics().getHeight() / 5 * 2;
		g.drawString(text, xStart, yStart);

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.draw(g);

	}

	public void mousePressed(int x, int y) {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mousePressed(x, y);
		else
			super.mousePressed(x, y);

	}

	public void mouseReleased(int x, int y) {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.mouseReleased(x, y);
		else {

			if (menu.getBounds().contains(x, y) && menu.isMousePressed()) {
				if (game.getPlay().isUnsavedChanges()) {
					game.getPlay().setUnsavedOverlayActive(true);
					game.getPlay().setUnsavedChangesOverlay(
							new UnsavedChangesOverlay(game.getPlay(), UnsavedChangesOverlay.EXIT_TO_MENU));
					GameStates.setGameState(GameStates.PLAY);
					menu.setMousePressed(false);
					return;
				} else {
					nameFileOverlay = null;
					namingFile = false;
				}
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
					} else {
						nameFileOverlay = new NameFileOverlay(this);
						namingFile = true;
					}

		}

		super.mouseReleased(x, y);

	}

	public void keyPressed(KeyEvent e) {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.keyPressed(e);

	}

	public void setNameFileOverlay(NameFileOverlay nameFileOverlay) {
		this.nameFileOverlay = nameFileOverlay;
	}

	public void setNamingFile(boolean namingFile) {
		this.namingFile = namingFile;
	}

}
