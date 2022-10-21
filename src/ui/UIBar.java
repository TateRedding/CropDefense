package ui;

import static main.Game.SCREEN_HEIGHT;
import static main.Game.SCREEN_WIDTH;

import java.awt.Rectangle;

public class UIBar {

	public static final int X = 0;
	public static final int Y = SCREEN_HEIGHT;
	public static final int UI_WIDTH = SCREEN_WIDTH;
	public static final int UI_HEIGHT = 160;

	protected Rectangle bounds;

	public UIBar() {
		bounds = new Rectangle(X, Y, UI_WIDTH, UI_HEIGHT);
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
