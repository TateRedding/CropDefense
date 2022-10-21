package handlers;

import java.io.File;
import java.util.ArrayList;

import helps.LoadSave;
import objects.Map;

public class MapHandler {

	private ArrayList<Map> maps = new ArrayList<>();
	private File[] mapFiles;

	private int animationIndex, tick;
	private int animationSpeed = 18;

	public MapHandler() {

		loadMaps();

	}

	public void loadMaps() {

		maps.clear();

		File mapFolder = new File(LoadSave.mapPath);
		mapFiles = mapFolder.listFiles();

		if (mapFiles != null)
			for (File mapFile : mapFiles)
				if (mapFile.getPath().endsWith(LoadSave.mapFileExtension))
					maps.add(LoadSave.loadMap(mapFile));

	}

	public void update() {

		tick++;
		if (tick >= animationSpeed) {
			tick = 0;
			animationIndex++;
			if (animationIndex >= 4)
				animationIndex = 0;
		}

	}

	public int getAnimationIndex() {
		return animationIndex;
	}

	public ArrayList<Map> getMaps() {
		return maps;
	}

	public File[] getMapFiles() {
		return mapFiles;
	}

}
