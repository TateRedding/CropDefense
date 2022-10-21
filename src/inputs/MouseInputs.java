package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.GameStates;
import main.GameScreen;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GameScreen gameScreen;

	public MouseInputs(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		switch (GameStates.gameState) {
		case EDIT:
			gameScreen.getGame().getEdit().mouseDragged(e.getX(), e.getY());
			break;
		case PLAY:
			gameScreen.getGame().getPlay().mouseDragged(e.getX(), e.getY());
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

		switch (GameStates.gameState) {
		case EDIT:
			gameScreen.getGame().getEdit().mouseMoved(e.getX(), e.getY());
			break;
		case PLAY:
			gameScreen.getGame().getPlay().mouseMoved(e.getX(), e.getY());
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {

			switch (GameStates.gameState) {
			case CREDITS:
				gameScreen.getGame().getCredits().mousePressed(e.getX(), e.getY());
				break;
			case EDIT:
				gameScreen.getGame().getEdit().mousePressed(e.getX(), e.getY());
				break;
			case EDIT_MAP:
				gameScreen.getGame().getEditMap().mousePressed(e.getX(), e.getY());
				break;
			case LOAD_GAME:
				gameScreen.getGame().getLoadGame().mousePressed(e.getX(), e.getY());
				break;
			case MENU:
				gameScreen.getGame().getMenu().mousePressed(e.getX(), e.getY());
				break;
			case PLAY:
				gameScreen.getGame().getPlay().mousePressed(e.getX(), e.getY());
				break;
			case PLAY_NEW_GAME:
				gameScreen.getGame().getPlayNewGame().mousePressed(e.getX(), e.getY());
				break;
			case TUTORIAL:
				gameScreen.getGame().getTutorial().mousePressed(e.getX(), e.getY());
				break;
			default:
				break;
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1) {

			switch (GameStates.gameState) {
			case CREDITS:
				gameScreen.getGame().getCredits().mouseReleased(e.getX(), e.getY());
				break;
			case EDIT:
				gameScreen.getGame().getEdit().mouseReleased(e.getX(), e.getY());
				break;
			case EDIT_MAP:
				gameScreen.getGame().getEditMap().mouseReleased(e.getX(), e.getY());
				break;
			case LOAD_GAME:
				gameScreen.getGame().getLoadGame().mouseReleased(e.getX(), e.getY());
				break;
			case MENU:
				gameScreen.getGame().getMenu().mouseReleased(e.getX(), e.getY());
				break;
			case PLAY:
				gameScreen.getGame().getPlay().mouseReleased(e.getX(), e.getY());
				break;
			case PLAY_NEW_GAME:
				gameScreen.getGame().getPlayNewGame().mouseReleased(e.getX(), e.getY());
				break;
			case TUTORIAL:
				gameScreen.getGame().getTutorial().mouseReleased(e.getX(), e.getY());
				break;
			default:
				break;
			}

		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {

		switch (GameStates.gameState) {
		case EDIT:
			gameScreen.getGame().getEdit().mouseEntered(e.getX(), e.getY());
			break;
		case PLAY:
			gameScreen.getGame().getPlay().mouseEntered(e.getX(), e.getY());
			break;
		default:
			break;
		}

	}

	@Override
	public void mouseExited(MouseEvent e) {

		switch (GameStates.gameState) {
		case EDIT:
			gameScreen.getGame().getEdit().mouseExited(e.getX(), e.getY());
			break;
		case PLAY:
			gameScreen.getGame().getPlay().mouseExited(e.getX(), e.getY());
			break;
		default:
			break;
		}

	}

}
