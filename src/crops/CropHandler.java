package crops;

import static helps.Constants.Crops.BELL_PEPPER;
import static helps.Constants.Crops.CHILI;
import static helps.Constants.Crops.CORN;
import static helps.Constants.Crops.TOMATO;
import static main.Game.TILE_SIZE;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import enemies.Enemy;
import gamestates.Play;
import helps.ImageLoader;

public class CropHandler implements Serializable {

	private static final long serialVersionUID = -7182985894419125398L;

	private Play play;
	private ArrayList<Crop> crops = new ArrayList<Crop>();

	private int id;

	public CropHandler(Play play) {
		this.play = play;
	}

	public void update() {

		for (Crop c : crops)
			c.update();

	}

	public void draw(Graphics g) {

		for (Crop c : crops)
			drawCrop(c, g);

	}

	private void drawCrop(Crop c, Graphics g) {
		g.drawImage(ImageLoader.getCropSprites(c.getCropType())[c.getColorIndex()], c.getX(), c.getY(), null);
	}

	public void plantCrop(int cropType, int colorIndex, int x, int y) {

		int xPos = x / TILE_SIZE * TILE_SIZE;
		int yPos = y / TILE_SIZE * TILE_SIZE;

		switch (cropType) {
		case BELL_PEPPER:
			crops.add(new BellPepper(xPos, yPos, id++, colorIndex, this));
			break;
		case CHILI:
			crops.add(new Chili(xPos, yPos, id++, colorIndex, this));
			break;
		case CORN:
			crops.add(new Corn(xPos, yPos, id++, colorIndex, this));
			break;
		case TOMATO:
			crops.add(new Tomato(xPos, yPos, id++, colorIndex, this));
			break;
		}

	}

	public void attackEnemy(Crop c, Enemy e) {
		play.attackEnemy(c, e);
	}

	public void removeCrop(Crop c) {

		for (int i = 0; i < crops.size(); i++)
			if (crops.get(i).getId() == c.getId()) {
				crops.remove(i);
				return;
			}

	}

	public Play getPlay() {
		return play;
	}

	public ArrayList<Crop> getCrops() {
		return crops;
	}

}
