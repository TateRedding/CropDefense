package gamestates;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import helps.DrawText;
import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import ui.UIBar;

public class PlayTutorial extends Tutorial {

	public PlayTutorial(Game game) {

		super(game);
		images = ImageLoader.tutorialPlay;

	}

	public void render(Graphics g) {

		super.render(g);

		switch (imageIndex) {
		case 0 -> drawFirstImageMarkings(g);
		case 1 -> drawSecondImageMarkings(g);
		case 2 -> drawThirdImageMarkings(g);
		case 3 -> drawFourthImageMarkings(g);
		case 4 -> drawFifthImageMarkings(g);
		}

	}

	private void drawFirstImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// Oval
		g2.setColor(Color.RED);

		int xStart = 205;
		int yStart = 644;
		int width = 405;
		int height = 152;

		g2.setStroke(new BasicStroke(3));
		g2.drawOval(xStart, yStart, width, height);

		// Text
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		String l1 = "These are your crops.";
		String l2 = "Plant them to defend ";
		String l3 = "your farm against enemies!";
		String[] lines = new String[] { l1, l2, l3 };

		xStart = Game.SCREEN_WIDTH / 3;
		yStart = Game.SCREEN_HEIGHT - 100;
		int areaWidth = Game.SCREEN_WIDTH / 3;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

	}

	private void drawSecondImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		// Oval
		g2.setColor(Color.RED);

		int xStart = 131;
		int yStart = 669;
		int width = 96;
		int height = 102;

		g2.setStroke(new BasicStroke(3));
		g2.drawOval(xStart, yStart, width, height);

		// Text

		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		String l1 = "This is your seed bag and your life counter.";
		String l2 = "Crops cost seeds to plant.";

		yStart = Game.SCREEN_HEIGHT - 50;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, l1, 0, yStart, Game.SCREEN_WIDTH / 2, areaHeight);

		xStart = Game.SCREEN_WIDTH / 2 + 50;
		areaHeight = 50;
		yStart = Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT - areaHeight;

		DrawText.drawTextCenteredRightAlign(g2, l2, xStart, yStart, areaHeight);

	}

	private void drawThirdImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));

		String l1 = "Earn seeds by killing enemies.";
		String l2 = "Different enemies reward different amounts of seeds.";
		String[] lines = new String[] { l1, l2 };

		int xStart = 400;
		int yStart = Game.SCREEN_HEIGHT - 300;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;
		int areaWidth = g.getFontMetrics().stringWidth(l2);

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, Game.SCREEN_WIDTH / 2, areaHeight);

		g2.setColor(Color.RED);

		l1 = "If an enemy reaches";
		l2 = "the end of it's path,";
		String l3 = "you lose a life!";
		lines = new String[] { l1, l2, l3 };

		xStart = Game.SCREEN_WIDTH - 250;
		yStart = Game.SCREEN_HEIGHT / 2;
		areaWidth = g2.getFontMetrics().stringWidth(l1);
		areaHeight = (DrawText.getPixelHeight(g2) + textLineOffset) * (lines.length + 1);

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

	}

	private void drawFourthImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));

		String l1 = "Select a planted crop to see its stats.";
		String l2 = "You can also upgrade its damage and range,";
		String l3 = "or you can harvest it to get some seeds back.";
		String[] lines = new String[] { l1, l2, l3 };

		int yStart = Game.SCREEN_HEIGHT - 200;
		int areaHeight = Game.SCREEN_HEIGHT - yStart;

		DrawText.drawTextCentered(g2, lines, textLineOffset, Game.SCREEN_WIDTH / 2, yStart, Game.SCREEN_WIDTH / 2,
				areaHeight);

	}

	private void drawFifthImageMarkings(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLUE);
		g2.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));

		String l1 = "Earn lives by killing";
		String l2 = "every enemy in a single wave.";
		String[] lines = new String[] { l1, l2 };

		int xStart = Game.SCREEN_WIDTH / 2;
		int yStart = Game.SCREEN_HEIGHT / 2;
		int areaWidth = Game.SCREEN_WIDTH / 2;
		int areaHeight = (DrawText.getPixelHeight(g2) + textLineOffset) * (lines.length + 1);
		int yOffset = 25;

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

		l1 = "Kill every enemy in every wave";
		l2 = "and you win!";
		lines = new String[] { l1, l2 };

		yStart += areaHeight + yOffset;
		areaHeight = (DrawText.getPixelHeight(g2) + textLineOffset) * (lines.length + 1);

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

		g.setColor(Color.RED);
		l1 = "Run out of lives,";
		l2 = "and you lose!";
		lines = new String[] { l1, l2 };

		yStart += areaHeight + yOffset;
		areaHeight = (DrawText.getPixelHeight(g2) + textLineOffset) * (lines.length + 1);

		DrawText.drawTextCentered(g2, lines, textLineOffset, xStart, yStart, areaWidth, areaHeight);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);

		if (start.getBounds().contains(x, y) && start.isMousePressed()) {
			imageIndex = 0;
			GameStates.setGameState(GameStates.PLAY_NEW_GAME);
		}

		start.reset();

	}

}
