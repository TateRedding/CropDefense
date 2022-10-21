package crops;

import static helps.Constants.Crops.BELL_PEPPER;

public class BellPepper extends Crop {

	private static final long serialVersionUID = 1063777315438905721L;

	public BellPepper(int x, int y, int id, int colorIndex, CropHandler cropHandler) {
		super(x, y, id, BELL_PEPPER, colorIndex, cropHandler);
	}

}
