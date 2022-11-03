package ui;

import java.awt.Graphics;

import helps.ImageLoader;
import main.Game;

public abstract class Overlay {

	protected int x, y, width, height;
	protected int titleX, titleY, titleW, titleH;
	protected int mainX, mainY, mainW, mainH;

	public Overlay(int y) {

		this.width = ImageLoader.overlayBG.getWidth();
		this.height = ImageLoader.overlayBG.getHeight();
		this.x = Game.SCREEN_WIDTH / 2 - width / 2;
		this.y = y;

		this.titleX = x + 18;
		this.titleY = y + 22;
		this.titleW = 388;
		this.titleH = 81;

		this.mainX = x + 18;
		this.mainY = y + 150;
		this.mainW = 388;
		this.mainH = 336;

	}

	public void draw(Graphics g) {
		g.drawImage(ImageLoader.overlayBG, x, y, null);
	}

}
