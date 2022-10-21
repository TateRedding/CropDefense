package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import gamestates.Edit;
import gamestates.GameStates;
import gamestates.Play;
import ui.UnsavedChangesOverlay;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -5213241656256282279L;

	@SuppressWarnings("unused")
	private GameScreen gameScreen;

	public GameFrame(GameScreen gameScreen) {

		this.gameScreen = gameScreen;

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		add(gameScreen);
		pack();
		setResizable(false);
		setTitle("Crop Defense");
		setLocationRelativeTo(null);
		setVisible(true);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {

				Play play = gameScreen.getGame().getPlay();
				Edit edit = gameScreen.getGame().getEdit();

				if (GameStates.gameState == GameStates.PLAY && play != null && play.isUnsavedChanges()
						&& !play.isGameOver()) {
					play.setPaused(true);
					play.setUnsavedChangesOverlay(
							new UnsavedChangesOverlay(gameScreen.getGame().getPlay(), UnsavedChangesOverlay.QUIT));
					play.setUnsavedOverlayActive(true);
				} else if (GameStates.gameState == GameStates.EDIT && edit != null && edit.isUnsavedChanges()) {
					edit.setUnsavedChangesOverlay(
							new UnsavedChangesOverlay(gameScreen.getGame().getEdit(), UnsavedChangesOverlay.QUIT));
					edit.setUnsavedOverlayActive(true);
				} else {
					System.exit(0);
				}

			}
		});
		addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gameScreen.getGame().windowFocusLost();
			}

		});

	}

}
