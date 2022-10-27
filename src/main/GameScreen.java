package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyInputs;
import inputs.MouseInputs;
import ui.UIBar;

public class GameScreen extends JPanel {

	private static final long serialVersionUID = -2843838240953849131L;

	private Game game;
	private MouseInputs mouseInputs;

	public GameScreen(Game game) {

		this.game = game;

		mouseInputs = new MouseInputs(this);

		setPanelSize();
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		addKeyListener(new KeyInputs(this));

	}

	private void setPanelSize() {

		Dimension size = new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT);
		setPreferredSize(size);

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		game.render(g);

	}

	public Game getGame() {
		return game;
	}

}
