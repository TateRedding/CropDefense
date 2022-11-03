package ui;

import static helps.Constants.Crops.getCropCost;

import java.awt.image.BufferedImage;

public class CropButton extends SquareButton {

	private int cropType;
	private ActionBar actionBar;

	public CropButton(int buttonColor, int x, int y, BufferedImage sprite, int cropType, ActionBar actionBar) {

		super(buttonColor, x, y, sprite);
		this.cropType = cropType;
		this.actionBar = actionBar;

	}

	public void update() {

		index = 0;
		if (mouseOver && getCropCost(cropType) <= actionBar.getPlay().getSeeds())
			index = 1;
		if (mousePressed)
			index = 2;

	}

	public int getCropType() {
		return cropType;
	}

}
