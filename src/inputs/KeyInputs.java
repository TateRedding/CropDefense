package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.GameStates;
import main.GameScreen;

public class KeyInputs implements KeyListener {

	private GameScreen gameScreen;

	public KeyInputs(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (GameStates.gameState) {
		case CREDITS:
			gameScreen.getGame().getCredits().keyPressed(e);
			break;
		case EDIT:
			gameScreen.getGame().getEdit().keyPressed(e);
			break;
		case EDIT_MAP:
			gameScreen.getGame().getEditMap().keyPressed(e);
			break;
		case PLAY:
			gameScreen.getGame().getPlay().keyPressed(e);
			break;
		case SAVE_GAME:
			gameScreen.getGame().getSaveGame().keyPressed(e);
		default:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
