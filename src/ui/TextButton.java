package ui;

import static helps.Constants.Buttons.TEXT_LARGE;
import static helps.Constants.Buttons.TEXT_SMALL;
import static helps.Constants.Buttons.getButtonHeight;
import static helps.Constants.Buttons.getButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;

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

		float fontSize = 24f;
		int areaHeight = getButtonHeight(buttonSize) - 2;
		int yStart = y;

		if (buttonSize == TEXT_LARGE) {
			fontSize = 32f;
			areaHeight -= 2;
		}
		if (mousePressed) {
			yStart += 2;
			if (buttonSize == TEXT_LARGE)
				yStart += 2;
		}

		g.setFont(LoadSave.gameFont.deriveFont(fontSize).deriveFont(Font.BOLD));
		g.setColor(Color.BLACK);
		DrawText.drawTextCentered(g, text, x, yStart, width, areaHeight);

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}