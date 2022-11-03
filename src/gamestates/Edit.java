package gamestates;

import static helps.Constants.Tiles.GRASS;
import static helps.Constants.Tiles.ROAD;
import static helps.Constants.Tiles.WATER;
import static helps.Constants.Tiles.PathPoints.END;
import static helps.Constants.Tiles.PathPoints.START;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import helps.ImageLoader;
import helps.LoadSave;
import main.Game;
import objects.Map;
import objects.Tile;
import pathfinding.AStar;
import pathfinding.PathPoint;
import ui.EditorBar;

public class Edit extends MapState implements StateMethods {

	private static final long serialVersionUID = 9206568218356397631L;

	private EditorBar editorBar;

	private int selectedTileType = -1;
	private int selectedPointType = -1;
	private int lastTileX, lastTileY, lastTileType;
	private int startPointIndex, endPointIndex;
	private int showWarningTick, showWarningTickLimit = 300;

	private boolean showInvalidMapWarning;

	public Edit(Game game, Map map) {

		super(game, map);
		this.editorBar = new EditorBar(this);

		for (int i = 0; i < startPoints.length; i++)
			if (startPoints[i].getPoint() == null) {
				startPointIndex = i;
				break;
			}

		for (int j = 0; j < endPoints.length; j++)
			if (endPoints[j].getPoint() == null) {
				endPointIndex = j;
				break;
			}

	}

	public void update() {

		super.update();
		game.getMapHandler().update();
		editorBar.update();

		if (showInvalidMapWarning) {
			showWarningTick++;
			if (showWarningTick >= showWarningTickLimit) {
				showWarningTick = 0;
				showInvalidMapWarning = false;
			}
		}

		if (justSaved) {
			justSavedTick++;
			if (justSavedTick >= justSavedTickLimit) {
				justSavedTick = 0;
				justSaved = false;
			}
		}

	}

	public void render(Graphics g) {

		game.getTileHandler().drawTileData(tileData, g);
		editorBar.draw(g);

		drawPathPoints(g);

		if (!unsavedOverlayActive)
			drawSelectedObject(g);

		if (showInvalidMapWarning && !justSaved)
			drawInvalidMapWarning(g);

		if (!showInvalidMapWarning && justSaved)
			drawSavedMessage(g);

		super.render(g);

	}

	private void drawPathPoints(Graphics g) {

		for (PathPoint sp : startPoints)
			if (sp.getPoint() != null) {
				BufferedImage sprite = ImageLoader.pathPointSprites[START];
				int x = sp.getPoint().x * Game.TILE_SIZE;
				int y = sp.getPoint().y * Game.TILE_SIZE;
				g.drawImage(sprite, x, y, null);
			}

		for (PathPoint ep : endPoints)
			if (ep.getPoint() != null) {
				BufferedImage sprite = ImageLoader.pathPointSprites[END];
				int x = ep.getPoint().x * Game.TILE_SIZE;
				int y = ep.getPoint().y * Game.TILE_SIZE;
				g.drawImage(sprite, x, y, null);
			}

	}

	private void drawSelectedObject(Graphics g) {

		if (inGameArea) {
			BufferedImage sprite = null;
			if (selectedTileType != -1) {
				sprite = ImageLoader.tileSprites.get(selectedTileType).get(0);
				if (selectedTileType == WATER)
					sprite = ImageLoader.waterFrames[0];
			} else if (selectedPointType != -1) {
				drawValidSpotHighlights(g);
				sprite = ImageLoader.pathPointSprites[selectedPointType];
			}

			g.drawImage(sprite, mouseX, mouseY, null);
		}
	}

	private void drawValidSpotHighlights(Graphics g) {

		g.setColor(new Color(0, 255, 0, 100));

		for (int i = 0; i < tileData[0].length; i++) {
			int x = i * Game.TILE_SIZE;
			if (isSpotValid(selectedPointType, x, 0))
				g.fillRect(x, 0, Game.TILE_SIZE, Game.TILE_SIZE);
			if (isSpotValid(selectedPointType, x, (Game.GRID_HEIGHT - 1) * Game.TILE_SIZE))
				g.fillRect(x, (Game.GRID_HEIGHT - 1) * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE);
		}
		for (int j = 0; j < tileData.length; j++) {
			int y = j * Game.TILE_SIZE;
			if (isSpotValid(selectedPointType, 0, y))
				g.fillRect(0, y, Game.TILE_SIZE, Game.TILE_SIZE);
			if (isSpotValid(selectedPointType, (Game.GRID_WIDTH - 1) * Game.TILE_SIZE, y))
				g.fillRect((Game.GRID_WIDTH - 1) * Game.TILE_SIZE, y, Game.TILE_SIZE, Game.TILE_SIZE);
		}

	}

	private boolean isSpotValid(int selectedPointType, int x, int y) {

		int tileX = x / Game.TILE_SIZE;
		int tileY = y / Game.TILE_SIZE;

		if (selectedPointType == START) {
			if (tileX == 0 || tileY == 0)
				if (tileData[tileY][tileX].getTileType() == ROAD && getPathPointAt(x, y) == null)
					return true;
		} else if (selectedPointType == END)
			if (tileX == Game.GRID_WIDTH - 1 || tileY == Game.GRID_HEIGHT - 1)
				if (tileData[tileY][tileX].getTileType() == ROAD && getPathPointAt(x, y) == null)
					return true;

		return false;
	}

	private void drawInvalidMapWarning(Graphics g) {

		g.setColor(Color.RED);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(32f));
		String text = "Map must contain at least one valid path from a start point to an end point!";

		int x = Game.SCREEN_WIDTH / 2 - (g.getFontMetrics().stringWidth(text) / 2);
		int y = Game.SCREEN_HEIGHT - 10;

		g.drawString(text, x, y);

	}

	private void drawSavedMessage(Graphics g) {

		g.setColor(Color.BLACK);
		g.setFont(LoadSave.gameFont.deriveFont(Font.BOLD).deriveFont(40f));

		String text = "Map saved succesfully!";

		int x = Game.SCREEN_WIDTH / 2 - (g.getFontMetrics().stringWidth(text) / 2);
		int y = Game.SCREEN_HEIGHT - 10;

		g.drawString(text, x, y);

	}

	public void saveMap() {

		if (isMapValid()) {

			showInvalidMapWarning = false;

			LoadSave.saveMap(map, map.getMapName());
			game.getMapHandler().loadMaps();
			game.getEditMap().initMapButtons();
			game.getPlayNewGame().initMapButtons();
			unsavedChanges = false;
			justSaved = true;

		} else {
			showInvalidMapWarning = true;
			justSaved = false;
		}

	}

	private boolean isMapValid() {

		for (int i = 0; i < startPoints.length; i++)
			if (startPoints[i].getPoint() != null)
				for (int j = 0; j < endPoints.length; j++)
					if (endPoints[j].getPoint() != null) {
						ArrayList<Point> path = AStar.pathFind(tileData, startPoints[i].getPoint(),
								endPoints[j].getPoint());
						if (path != null)
							return true;
					}

		return false;

	}

	private void changeTile(int x, int y) {

		if (inGameArea && selectedTileType != -1) {
			int tileX = x / Game.TILE_SIZE;
			int tileY = y / Game.TILE_SIZE;

			if ((lastTileX == tileX && lastTileY == tileY && lastTileType == selectedTileType)
					|| selectedTileType == tileData[tileY][tileX].getTileType())
				return;
			lastTileX = tileX;
			lastTileY = tileY;
			lastTileType = selectedTileType;

			updateSprites(selectedTileType, tileX, tileY);
			if (getPathPointAt(x, y) != null && selectedTileType != ROAD)
				getPathPointAt(x, y).setPoint(null);

			unsavedChanges = true;

		}

	}

	private void updateSprites(int tileType, int tileX, int tileY) {

		ArrayList<Point> points = new ArrayList<Point>();
		int currentTileType = tileType;
		points.add(new Point(tileX, tileY));
		points.addAll(getSurroundingPoints(tileX, tileY));

		for (int i = 0; i < points.size(); i++) {
			Point currentPoint = points.get(i);
			if (i != 0) {
				currentTileType = tileData[currentPoint.y][currentPoint.x].getTileType();
				if (currentTileType == GRASS)
					continue;
			}
			ArrayList<Integer> surroundingTypes = getSurroundingTileTypes(currentPoint.x, currentPoint.y,
					currentTileType);
			int bitId = determineBitId(currentPoint, currentTileType, surroundingTypes);
			tileData[currentPoint.y][currentPoint.x] = new Tile(currentTileType, bitId);

		}

	}

	private ArrayList<Point> getSurroundingPoints(int tileX, int tileY) {

		ArrayList<Point> points = new ArrayList<Point>();

		for (int y = tileY - 1; y < tileY + 2; y++)
			for (int x = tileX - 1; x < tileX + 2; x++) {
				if (x == tileX && y == tileY)
					continue;
				if (y >= 0 && y < Game.GRID_HEIGHT && x >= 0 && x < Game.GRID_WIDTH)
					points.add(new Point(x, y));
			}

		return points;

	}

	private ArrayList<Integer> getSurroundingTileTypes(int tileX, int tileY, int tileType) {

		ArrayList<Integer> types = new ArrayList<Integer>();

		for (int y = tileY - 1; y < tileY + 2; y++)
			for (int x = tileX - 1; x < tileX + 2; x++) {
				if (x == tileX && y == tileY) // No need to add the tile we are on.
					continue;
				if (y < 0 || y >= Game.GRID_HEIGHT || x < 0 || x >= Game.GRID_WIDTH) {
					// Road tiles should connect to the edges of the screen if there is a pathpoint
					// present
					if (getPathPointAt(tileX * Game.TILE_SIZE, tileY * Game.TILE_SIZE) != null && tileType == ROAD
							&& (y == tileY || x == tileX))
						types.add(ROAD);
					else
						types.add(null);
				} else
					types.add(tileData[y][x].getTileType());
			}

		return types;

	}

	private int determineBitId(Point point, int tileType, ArrayList<Integer> types) {

		if (types.size() != 8)
			return 0;

		int bitId = 0;
		if (tileType == WATER) {
			if (types.get(0) != null && types.get(0) == WATER)
				if (types.get(1) != null && types.get(3) != null && types.get(1) == WATER && types.get(3) == WATER)
					bitId += 1;
			if (types.get(1) != null && types.get(1) == WATER)
				bitId += 2;
			if (types.get(2) != null && types.get(2) == WATER)
				if (types.get(1) != null && types.get(4) != null && types.get(1) == WATER && types.get(4) == WATER)
					bitId += 4;
			if (types.get(3) != null && types.get(3) == WATER)
				bitId += 8;
			if (types.get(4) != null && types.get(4) == WATER)
				bitId += 16;
			if (types.get(5) != null && types.get(5) == WATER)
				if (types.get(3) != null && types.get(6) != null && types.get(3) == WATER && types.get(6) == WATER)
					bitId += 32;
			if (types.get(6) != null && types.get(6) == WATER)
				bitId += 64;
			if (types.get(7) != null && types.get(7) == WATER)
				if (types.get(4) != null && types.get(6) != null && types.get(4) == WATER && types.get(6) == WATER)
					bitId += 128;
		} else if (tileType == ROAD) {
			if (types.get(1) != null && types.get(1) == ROAD)
				bitId += 1;
			if (types.get(3) != null && types.get(3) == ROAD)
				bitId += 2;
			if (types.get(4) != null && types.get(4) == ROAD)
				bitId += 4;
			if (types.get(6) != null && types.get(6) == ROAD)
				bitId += 8;
		} else if (tileType == GRASS) {
			Random r = new Random();
			bitId = r.nextInt(ImageLoader.tileSprites.get(GRASS).size());
		}

		return bitId;

	}

	private PathPoint getPathPointAt(int x, int y) {

		ArrayList<PathPoint> points = new ArrayList<>();
		Collections.addAll(points, startPoints);
		Collections.addAll(points, endPoints);

		for (PathPoint pp : points)
			if (pp.getPoint() != null)
				if (x / Game.TILE_SIZE == pp.getPoint().x && y / Game.TILE_SIZE == pp.getPoint().y)
					return pp;

		return null;

	}

	private void setPathPoint(int pointType, int x, int y) {

		int tileX = x / Game.TILE_SIZE;
		int tileY = y / Game.TILE_SIZE;

		int currTileType = tileData[tileY][tileX].getTileType();
		if (getPathPointAt(x, y) != null || currTileType != ROAD)
			return;

		if (selectedPointType == START) {
			startPoints[startPointIndex].setPoint(new Point(tileX, tileY));
			startPointIndex++;
			if (startPointIndex >= startPoints.length)
				startPointIndex = 0;
		} else if (selectedPointType == END) {
			endPoints[endPointIndex].setPoint(new Point(tileX, tileY));
			endPointIndex++;
			if (endPointIndex >= endPoints.length)
				endPointIndex = 0;
		}

		this.selectedPointType = -1;
		updateSprites(currTileType, tileX, tileY);

		unsavedChanges = true;

	}

	public void setSelectedTileType(int tileType) {

		this.selectedTileType = tileType;
		this.selectedPointType = -1;

	}

	public void setSelectedPointType(int pathPointType) {

		this.selectedTileType = -1;
		this.selectedPointType = pathPointType;

	}

	public void mousePressed(int x, int y) {

		super.mousePressed(x, y);

		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (editorBar.getBounds().contains(x, y))
				editorBar.mousePressed(x, y);

	}

	public void mouseReleased(int x, int y) {

		super.mouseReleased(x, y);

		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (editorBar.getBounds().contains(x, y))
				editorBar.mouseReleased(x, y);
			else if (selectedTileType != -1) {
				changeTile(x, y);
			} else if (selectedPointType != -1) {
				if (isSpotValid(selectedPointType, x, y))
					setPathPoint(selectedPointType, x, y);
			} else if (getPathPointAt(x, y) != null) {
				selectedPointType = getPathPointAt(x, y).getPointType();
				getPathPointAt(x, y).setPoint(null);
				int tileX = x / Game.TILE_SIZE;
				int tileY = y / Game.TILE_SIZE;
				int tileType = tileData[tileY][tileX].getTileType();
				updateSprites(tileType, tileX, tileY);
			}

	}

	public void mouseDragged(int x, int y) {

		if (!unsavedOverlayActive && unsavedChangesOverlay == null) {
			super.mouseDragged(x, y);
			if (selectedTileType != -1 && inGameArea)
				changeTile(x, y);

		}

	}

	public void mouseMoved(int x, int y) {

		super.mouseMoved(x, y);

		if (!unsavedOverlayActive && unsavedChangesOverlay == null)
			if (editorBar.getBounds().contains(x, y))
				editorBar.mouseMoved(x, y);

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_DELETE) {
			selectedTileType = -1;
			selectedPointType = -1;
		}
	}

	public boolean isInvalidMap() {
		return showInvalidMapWarning;
	}

}
