package main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import gamestates.Edit;
import gamestates.GameStates;
import gamestates.Play;
import helps.ImageLoader;
import ui.UnsavedChangesOverlay;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -5213241656256282279L;

	public GameFrame(GameScreen gameScreen) {

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
				int yStart = Game.SCREEN_HEIGHT / 2 - ImageLoader.overlayBG.getHeight() / 2;

				switch (GameStates.gameState) {

				case PLAY, SAVE_GAME -> {

					if (play == null || !play.isUnsavedChanges() || play.isGameOver())
						System.exit(0);

					play.setUnsavedChangesOverlay(new UnsavedChangesOverlay(gameScreen.getGame().getPlay(),
							UnsavedChangesOverlay.QUIT, yStart));
					play.setUnsavedOverlayActive(true);
					play.setPaused(true);

					if (GameStates.gameState == GameStates.SAVE_GAME)
						GameStates.setGameState(GameStates.PLAY);
				}

				case EDIT -> {
					if (edit == null || !edit.isUnsavedChanges())
						System.exit(0);

					edit.setUnsavedChangesOverlay(new UnsavedChangesOverlay(gameScreen.getGame().getEdit(),
							UnsavedChangesOverlay.QUIT, yStart));
					edit.setUnsavedOverlayActive(true);
				}

				default -> System.exit(0);

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
