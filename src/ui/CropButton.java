package ui;

import java.awt.image.BufferedImage;

public class CropButton extends SquareButton {

	private int cropType;

	public CropButton(int buttonColor, int x, int y, BufferedImage sprite, int cropType) {

		super(buttonColor, x, y, sprite);
		this.cropType = cropType;

	}

	public int getCropType() {
		return cropType;
	}

}
