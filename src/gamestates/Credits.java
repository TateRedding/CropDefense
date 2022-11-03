package gamestates;

import static helps.Constants.Buttons.BEIGE;
import static helps.Constants.Buttons.TEXT_LARGE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import ui.TextButton;
import ui.UIBar;

public class Credits extends State implements StateMethods {

	private TextButton menu;

	public Credits(Game game) {

		super(game);
		menu = new TextButton(TEXT_LARGE, "Main Menu", BEIGE, 10, 10);

	}

	@Override
	public void update() {

		menu.update();

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(ImageLoader.background, 0, 0, null);

		int xStart = Game.SCREEN_WIDTH / 2 - ImageLoader.creditsBG.getWidth() / 2;
		int yStart = (Game.SCREEN_HEIGHT + UIBar.UI_HEIGHT) / 2 - ImageLoader.creditsBG.getHeight() / 2;

		g.drawImage(ImageLoader.creditsBG, xStart, yStart, null);
		drawCredits(g);

		menu.draw(g);

	}

	private void drawCredits(Graphics g) {

		Font header = LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(40f);
		Font name = LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f);
		Font url = LoadSave.gameFont.deriveFont(32f);

		String l0 = "Created and developed by";
		String l1 = "Tate Redding";

		String l2 = "Concept and code based on work by";
		String l3 = "Kaarin Gaming";
		String l4 = "kaaringaming.com";
		String l5 = "youtube.com/c/KaarinGaming";
		String l6 = "github.com/KaarinGaming";

		String l7 = "Terrain, user interface, and font";
		String l8 = "Kenney via OpenGameArt.org";
		String l9 = "opengameart.org/users/Kenney";
		String l10 = "kenney.nl";

		String l11 = "Crop images";
		String l12 = "ARoachIFoundOnMyPillow via OpenGameArt.org";
		String l13 = "opengameart.org/users/aroachifoundonmypillow";

		String l14 = "Crow images based on work by";
		String l15 = "Revangale via OpenGameArt.org";
		String l16 = "opengameart.org/users/revangale";
		String l17 = "revangale.wordpress.com";

		String l18 = "All other artwork";
		String l19 = "Tate Redding";

		String l20 = "Thanks for playing!";

		String[] lines = { l0, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14, l15, l16, l17, l18, l19,
				l20 };

		int yStart = 88;
		int yOffset = 20;
		g.setColor(Color.BLACK);
		for (int i = 0; i < lines.length; i++) {
			if (i == 0 || i == 2 || i == 7 || i == 11 || i == 14 || i == 18 || i == 20) {
				g.setFont(header);
				yOffset = 28;
			} else if (i == 1 || i == 3 || i == 8 || i == 12 || i == 15 || i == 19) {
				g.setFont(name);
				yOffset = 24;
			} else {
				g.setFont(url);
			}
			int xStart = Game.SCREEN_WIDTH / 2 - g.getFontMetrics().stringWidth(lines[i]) / 2;
			g.drawString(lines[i], xStart, yStart);
			yStart += yOffset;
			if (i == 1 || i == 6 || i == 10 || i == 13 || i == 17 || i == 19)
				yStart += yOffset;
		}

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (menu.getBounds().contains(x, y))
			menu.setMousePressed(true);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (menu.getBounds().contains(x, y) && menu.isMousePressed())
			GameStates.setGameState(GameStates.MENU);

		menu.reset();

	}

	@Override
	public void mouseDragged(int x, int y) {

	}

	@Override
	public void mouseMoved(int x, int y) {

		menu.setMouseOver(false);

		if (menu.getBounds().contains(x, y))
			menu.setMouseOver(true);

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE)
			GameStates.setGameState(GameStates.MENU);
	}

}
