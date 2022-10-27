package ui;

import static helps.Constants.Buttons.MAP;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helps.ImageLoader;

public class MapButton extends Button {

	private BufferedImage thumbnail;

	public MapButton(int buttonColor, int x, int y, BufferedImage thumbnail) {

		super(buttonColor, x, y, getButtonWidth(MAP), getButtonHeight(MAP));
		this.thumbnail = thumbnail;

	}

	public void draw(Graphics g) {

		drawBackground(g);
		drawThumbnail(g);

	}

	private void drawBackground(Graphics g) {
		g.drawImage(ImageLoader.mapButtons[buttonColor][index], x, y, null);
	}

	private void drawThumbnail(Graphics g) {

		int yStart = y + 4;
		if (mousePressed)
			yStart += 4;

		g.drawImage(thumbnail, x + 4, yStart, null);

	}

}
