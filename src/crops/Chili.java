package crops;

import static helps.Constants.Crops.CHILI;

public class Chili extends Crop {

	private static final long serialVersionUID = -7316887504680105577L;

	public Chili(int x, int y, int id, int colorIndex, CropHandler cropHandler) {
		super(x, y, id, CHILI, colorIndex, cropHandler);
	}

}
