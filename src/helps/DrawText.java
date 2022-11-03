package helps;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

public class DrawText {

	public static void drawTextCentered(Graphics g, String[] lines, int lineOffset, int x, int y, int width,
			int height) {

		int lineHeight = getPixelHeight(g);
		int descent = g.getFontMetrics().getDescent();
		int textHeight = lineHeight * lines.length + (lineOffset + descent) * (lines.length - 1);
		int yOffset = (height - textHeight) / 2;
		int yStart = y + yOffset + lineHeight;

		for (String line : lines) {
			int xStart = x + width / 2 - g.getFontMetrics().stringWidth(line) / 2;
			g.drawString(line, xStart, yStart);
			yStart += lineHeight + lineOffset + descent;
		}

	}

	public static void drawTextCentered(Graphics g, String text, int x, int y, int width, int height) {

		String[] lines = new String[] { text };
		drawTextCentered(g, lines, 0, x, y, width, height);

	}

	public static void drawTextCenteredRightAlign(Graphics g, String[] lines, int lineOffset, int x, int y,
			int height) {

		int lineHeight = getPixelHeight(g);
		int descent = g.getFontMetrics().getDescent();
		int textHeight = lineHeight * lines.length + (lineOffset + descent) * (lines.length - 1);
		int yOffset = (height - textHeight) / 2;
		int yStart = y + yOffset + lineHeight;

		for (String line : lines) {
			g.drawString(line, x, yStart);
			yStart += lineHeight + lineOffset + descent;
		}

	}

	public static void drawTextCenteredRightAlign(Graphics g, String text, int x, int y, int height) {

		String[] lines = new String[] { text };
		drawTextCenteredRightAlign(g, lines, 0, x, y, height);

	}

	// Returns the number of pixels in height of the character "d" in the font style
	// and size of the Graphics object.
	public static int getPixelHeight(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, "d");
		Rectangle bounds = gv.getPixelBounds(null, 0, 0);
		return (int) bounds.getHeight();

	}

	// Creates a temporary Graphics object to use in getPixelHeight(Graphics g)
	public static int getPixelHeight(Font font) {

		BufferedImage temp = new BufferedImage(1, 1, TYPE_INT_ARGB);
		Graphics g = temp.getGraphics();
		g.setFont(font);
		return getPixelHeight(g);

	}

}
