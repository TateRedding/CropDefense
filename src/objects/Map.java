package objects;

import static helps.Constants.Tiles.GRASS;
import static helps.Constants.Tiles.ROAD;
import static helps.Constants.Tiles.PathPoints.END;
import static helps.Constants.Tiles.PathPoints.START;
import static main.Game.GRID_HEIGHT;
import static main.Game.GRID_WIDTH;

import java.awt.Point;
import java.io.Serializable;
import java.util.Random;

import helps.ImageLoader;
import helps.LoadSave;
import pathfinding.PathPoint;

public class Map implements Serializable {

	private static final long serialVersionUID = 3560927649515403266L;

	private PathPoint[] startPoints, endPoints;
	private Tile[][] tileData;
	private String mapName;

	public Map(String mapName) {

		this.mapName = mapName;
		createDefaultMap();

	}

	private void createDefaultMap() {

		tileData = new Tile[GRID_HEIGHT][GRID_WIDTH];
		for (int j = 0; j < tileData.length; j++)
			for (int i = 0; i < tileData[j].length; i++)
				if (j == GRID_HEIGHT / 2)
					tileData[j][i] = new Tile(ROAD, 6);
				else {
					Random r = new Random();
					tileData[j][i] = new Tile(GRASS, r.nextInt(ImageLoader.tileSprites.get(GRASS).size()));
				}

		startPoints = new PathPoint[3];
		endPoints = new PathPoint[3];

		for (int i = 0; i < startPoints.length; i++)
			startPoints[i] = new PathPoint(i, START);

		for (int i = 0; i < endPoints.length; i++)
			endPoints[i] = new PathPoint(i, END);

		startPoints[0].setPoint(new Point(0, GRID_HEIGHT / 2));
		endPoints[0].setPoint(new Point(GRID_WIDTH - 1, GRID_HEIGHT / 2));

		LoadSave.saveMap(this, mapName);

	}

	public PathPoint[] getStartPoints() {
		return startPoints;
	}

	public PathPoint[] getEndPoints() {
		return endPoints;
	}

	public Tile[][] getTileData() {
		return tileData;
	}

	public void setTileData(Tile[][] tileData) {
		this.tileData = tileData;
	}

	public String getMapName() {
		return mapName;
	}

}
