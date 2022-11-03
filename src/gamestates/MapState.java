package gamestates;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

import main.Game;
import objects.Map;
import objects.Tile;
import pathfinding.PathPoint;
import ui.UnsavedChangesOverlay;

public abstract class MapState extends State implements StateMethods, Serializable {

	private static final long serialVersionUID = 999007224533853839L;

	protected Map map;
	protected PathPoint[] startPoints;
	protected PathPoint[] endPoints;
	protected Rectangle gameBounds;
	protected Tile[][] tileData;
	protected UnsavedChangesOverlay unsavedChangesOverlay;

	// mouseX & mouseY are the top right coordinates of the tile the cursor is on
	// relative to the game screen
	protected int mouseX, mouseY;
	protected int justSavedTick, justSavedTickLimit = 300;

	protected boolean inGameArea, unsavedChanges, unsavedOverlayActive, justSaved;

	public MapState(Game game, Map map) {

		super(game);
		this.map = map;
		startPoints = map.getStartPoints();
		endPoints = map.getEndPoints();
		tileData = map.getTileData();
		gameBounds = new Rectangle(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);

	}

	@Override
	public void update() {

		if (unsavedOverlayActive && unsavedChangesOverlay != null)
			unsavedChangesOverlay.update();

	}

	@Override
	public void render(Graphics g) {

		if (unsavedOverlayActive && unsavedChangesOverlay != null)
			unsavedChangesOverlay.draw(g);

	}

	protected void updateCoords(int x, int y) {

		mouseX = (x / Game.TILE_SIZE) * Game.TILE_SIZE;
		mouseY = (y / Game.TILE_SIZE) * Game.TILE_SIZE;

	}

	@Override
	public void mouseClicked(int x, int y) {

	}

	@Override
	public void mousePressed(int x, int y) {

		if (unsavedOverlayActive && unsavedChangesOverlay != null)
			unsavedChangesOverlay.mousePressed(x, y);

	}

	@Override
	public void mouseReleased(int x, int y) {

		if (unsavedOverlayActive && unsavedChangesOverlay != null)
			unsavedChangesOverlay.mouseReleased(x, y);

	}

	@Override
	public void mouseDragged(int x, int y) {

		inGameArea = gameBounds.contains(x, y);
		if (inGameArea)
			updateCoords(x, y);

	}

	@Override
	public void mouseMoved(int x, int y) {

		if (unsavedOverlayActive && unsavedChangesOverlay != null)
			unsavedChangesOverlay.mouseMoved(x, y);

		inGameArea = gameBounds.contains(x, y);
		if (inGameArea)
			updateCoords(x, y);

	}

	public void mouseEntered(int x, int y) {

		if (y < Game.SCREEN_HEIGHT)
			inGameArea = true;

	}

	public void mouseExited(int x, int y) {
		inGameArea = false;
	}

	public Map getMap() {
		return map;
	}

	public boolean isUnsavedChanges() {
		return unsavedChanges;
	}

	public void setUnsavedChangesOverlay(UnsavedChangesOverlay unsavedChangesOverlay) {
		this.unsavedChangesOverlay = unsavedChangesOverlay;
	}

	public void setJustSavedTick(int justSavedTick) {
		this.justSavedTick = justSavedTick;
	}

	public void setUnsavedOverlayActive(boolean unsavedOverlayActive) {
		this.unsavedOverlayActive = unsavedOverlayActive;
	}

	public void setJustSaved(boolean justSaved) {
		this.justSaved = justSaved;
	}

}
