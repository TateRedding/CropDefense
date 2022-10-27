package ui;

import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import helps.ImageLoader;
import main.Game;

public class TextButton extends Button {

	private String text;
	private int buttonSize;

	public TextButton(int buttonSize, String text, int buttonColor, int x, int y) {

		super(buttonColor, x, y, getButtonWidth(buttonSize), getButtonHeight(buttonSize));
		this.buttonSize = buttonSize;
		this.text = text;

	}

	public void draw(Graphics g) {

		drawBackground(g);
		drawText(g);

	}

	private void drawBackground(Graphics g) {

		if (buttonSize == TEXT_SMALL) {
			g.drawImage(ImageLoader.textButtonsSmall[buttonColor][index], x, y, null);
		} else
			g.drawImage(ImageLoader.textButtonsLarge[buttonColor][index], x, y, null);

	}

	private void drawText(Graphics g) {

		int fontSize = 20;

		if (buttonSize == TEXT_SMALL)
			fontSize = 12;

		g.setFont(new Font(Game.FONT_NAME, Font.BOLD, fontSize));
		g.setColor(Color.BLACK);

		int w = g.getFontMetrics().stringWidth(text);
		int yOffset = 10;
		if (buttonSize == TEXT_SMALL)
			yOffset = 6;
		int xStart = x - w / 2 + width / 2;
		int yStart = y + yOffset + height / 2;

		if (!mousePressed) {
			if (buttonSize == TEXT_SMALL)
				yStart -= 2;
			else
				yStart -= 4;
		}

		g.drawString(text, xStart, yStart);

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
