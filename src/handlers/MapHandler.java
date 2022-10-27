package handlers;

import java.io.File;
import java.util.ArrayList;

import helps.LoadSave;
import objects.Map;

public class MapHandler {

	private ArrayList<Map> maps = new ArrayList<>();
	private ArrayList<File> mapFiles = new ArrayList<>();

	private int animationIndex, tick;
	private int animationSpeed = 18;

	public MapHandler() {

		loadMaps();

	}

	public void loadMaps() {

		maps.clear();
		mapFiles.clear();

		File mapFolder = new File(LoadSave.mapPath);
		File[] files = mapFolder.listFiles();

		if (files != null)
			for (File file : files)
				if (file.getPath().endsWith(LoadSave.mapFileExtension)) {
					mapFiles.add(file);
					maps.add(LoadSave.loadMap(file));
				}

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

	public ArrayList<File> getMapFiles() {
		return mapFiles;
	}

}
