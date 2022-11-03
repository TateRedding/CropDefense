package entities;

import static main.Game.TILE_SIZE;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import gamestates.Play;

public class CropHandler implements Serializable {

	private static final long serialVersionUID = -7182985894419125398L;

	private Play play;
	private ArrayList<Crop> crops = new ArrayList<Crop>();

	public CropHandler(Play play) {
		this.play = play;
	}

	public void update() {

		for (Crop c : crops)
			c.update();

	}

	public void draw(Graphics g) {

		for (Crop c : crops)
			c.draw(g);

	}

	public void plantCrop(int cropType, int colorIndex, int x, int y) {

		int xPos = x / TILE_SIZE * TILE_SIZE;
		int yPos = y / TILE_SIZE * TILE_SIZE;

		crops.add(new Crop(xPos, yPos, cropType, colorIndex, this));

	}

	public void attackEnemy(Crop c, Enemy e) {
		play.attackEnemy(c, e);
	}

	public void removeCrop(Crop c) {
		crops.remove(crops.indexOf(c));
	}

	public Play getPlay() {
		return play;
	}

	public ArrayList<Crop> getCrops() {
		return crops;
	}

}
