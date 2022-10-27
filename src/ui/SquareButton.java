package ui;

import static helps.Constants.Buttons.SQUARE;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helps.ImageLoader;

public class SquareButton extends Button {

	private BufferedImage sprite;

	public SquareButton(int buttonColor, int x, int y, BufferedImage sprite) {

		super(buttonColor, x, y, getButtonWidth(SQUARE), getButtonHeight(SQUARE));
		this.sprite = sprite;

	}

	public void draw(Graphics g) {

		drawBackground(g);
		drawSprite(g);

	}

	private void drawBackground(Graphics g) {
		g.drawImage(ImageLoader.squareButtons[buttonColor][index], x, y, null);

	}

	private void drawSprite(Graphics g) {

		int xStart = x + 4;
		int yStart = y + 4;
		if (mousePressed)
			yStart += 4;

		g.drawImage(sprite, xStart, yStart, sprite.getWidth() * 2, sprite.getHeight() * 2, null);

	}

}
