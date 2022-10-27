package handlers;

import static helps.Constants.Tiles.GRASS;
import static helps.Constants.Tiles.ROAD;
import static helps.Constants.Tiles.WATER;

import java.awt.Graphics;
import java.util.HashMap;

import helps.ImageLoader;
import main.Game;
import objects.Tile;

public class TileHandler {

	private Game game;
	private HashMap<Integer, Integer> binaryMap = new HashMap<>();

	public TileHandler(Game game) {

		this.game = game;

		int[] binaryIds = new int[] { 0, 2, 8, 10, 11, 16, 18, 22, 24, 26, 27, 30, 31, 64, 66, 72, 74, 75, 80, 82, 86,
				88, 90, 91, 94, 95, 104, 106, 107, 120, 122, 123, 126, 127, 208, 210, 214, 216, 218, 219, 222, 223, 248,
				250, 251, 254 };

		for (int i = 0; i < binaryIds.length; i++)
			binaryMap.put(binaryIds[i], i);

	}

	public void drawTileData(Tile[][] tileData, Graphics g) {

		for (int y = 0; y < tileData.length; y++)
			for (int x = 0; x < tileData[y].length; x++) {
				Tile currTile = tileData[y][x];
				if (currTile.getTileType() == WATER) {
					g.drawImage(ImageLoader.waterFrames[game.getMapHandler().getAnimationIndex()], x * Game.TILE_SIZE,
							y * Game.TILE_SIZE, null);
					if (currTile.getBinaryId() != 255) {
						int index = binaryMap.get(currTile.getBinaryId());
						g.drawImage(ImageLoader.tileSprites.get(WATER).get(index), x * Game.TILE_SIZE,
								y * Game.TILE_SIZE, null);
					}
				} else {
					if (currTile.getTileType() == ROAD)
						g.drawImage(ImageLoader.tileSprites.get(GRASS).get(0), x * Game.TILE_SIZE, y * Game.TILE_SIZE,
								null);
					g.drawImage(ImageLoader.tileSprites.get(currTile.getTileType()).get(currTile.getBinaryId()),
							x * Game.TILE_SIZE, y * Game.TILE_SIZE, null);
				}
			}

	}

}
