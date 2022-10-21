package crops;

import static helps.Constants.Crops.TOMATO;

public class Tomato extends Crop {

	private static final long serialVersionUID = -3977040170172591679L;

	public Tomato(int x, int y, int id, int colorIndex, CropHandler cropHandler) {
		super(x, y, id, TOMATO, colorIndex, cropHandler);
	}

}
