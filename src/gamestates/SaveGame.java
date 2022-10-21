package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

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

	public void update() {

		if (namingFile && nameFileOverlay != null)
			nameFileOverlay.update();

		super.update();

	}

	public void render(Graphics g) {

		super.render(g);

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
			}

			for (TextButton b : buttons)
				if (b.getBounds().contains(x, y) && b.isMousePressed())
					if (b.getText().equals("Empty")) {
						nameFileOverlay = new NameFileOverlay(this);
						namingFile = true;
					}

			super.mouseReleased(x, y);

		}

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
