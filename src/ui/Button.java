package ui;

import java.awt.Rectangle;

public class Button {

	protected Rectangle bounds;

	protected int buttonColor, index;
	protected int x, y, width, height;

	protected boolean mouseOver, mousePressed;

	public Button(int buttonColor, int x, int y, int width, int height) {

		this.buttonColor = buttonColor;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(x, y, width, height);

	}

	public void update() {

		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;

	}

	public void reset() {

		mouseOver = false;
		mousePressed = false;

	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
