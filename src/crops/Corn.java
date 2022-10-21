package crops;

import static helps.Constants.Crops.CORN;

public class Corn extends Crop {

	private static final long serialVersionUID = -7153947385590892992L;

	public Corn(int x, int y, int id, int colorIndex, CropHandler cropHandler) {
		super(x, y, id, CORN, colorIndex, cropHandler);
	}

}
