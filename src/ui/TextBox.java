package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import helps.DrawText;

public class TextBox {

	private Font font;
	private String text = "";

	private int x, y, width, height;
	private int charLimit = 25;
	private int cursorTick, cursorTickMax = 25;
	private int cursorIndex;
	private int textX, textOffset;

	private boolean showCursor = true;

	public TextBox(int x, int y, int width, Font font) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.font = font;
		this.textOffset = 8;
		this.height = DrawText.getPixelHeight(font) + textOffset;
		this.textX = x + textOffset;

	}

	public void update() {

		cursorTick++;
		if (cursorTick >= cursorTickMax) {
			cursorTick = 0;
			showCursor = !showCursor;
		}

	}

	public void draw(Graphics g) {

		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);

		g.setFont(font);
		int yStart = y + height - textOffset / 2;
		g.drawString(text, textX, yStart);

		if (showCursor)
			drawCursor(g);

	}

	private void drawCursor(Graphics g) {

		int cursorX = textX + 1;
		if (text != "")
			cursorX = textX + g.getFontMetrics().stringWidth(text.substring(0, cursorIndex)) + 1;
		int cursorOffset = 4;
		int cursorY = y + cursorOffset;

		g.drawRect(cursorX, cursorY, 1, height - cursorOffset * 2);

	}

	private boolean isAccepted(KeyEvent e) {

		char[] accepted = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', ' ', '!', '@', '#', '$', '%', '^', '&', '(', ')', '-', '_', '.', ',' };

		for (char c : accepted)
			if (e.getKeyChar() == c)
				return true;

		return false;
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (cursorIndex > 0) {
				text = text.substring(0, cursorIndex - 1) + text.substring(cursorIndex, text.length());
				if (cursorIndex > 0)
					cursorIndex--;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && cursorIndex > 0)
			cursorIndex--;
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT && cursorIndex < text.length())
			cursorIndex++;

		if (isAccepted(e) && text.length() < charLimit) {
			text = text.substring(0, cursorIndex) + e.getKeyChar() + text.substring(cursorIndex, text.length());
			cursorIndex++;
		}

	}

	public String getText() {
		return text;
	}

	public void setCharLimit(int charLimit) {
		this.charLimit = charLimit;
	}

}
