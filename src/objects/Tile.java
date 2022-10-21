package objects;

import java.io.Serializable;

public class Tile implements Serializable {

	private static final long serialVersionUID = 6489810601028720842L;

	private int binaryId, tileType;

	public Tile(int tileType, int binaryId) {

		this.tileType = tileType;
		this.binaryId = binaryId;

	}

	public int getBinaryId() {
		return binaryId;
	}

	public int getTileType() {
		return tileType;
	}

}
