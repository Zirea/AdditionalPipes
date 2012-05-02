package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;

public class ChunkStore implements Serializable {
	
	private static final long serialVersionUID = 5455301483129502046L;
	
	private final ConcurrentHashMap<Integer, ArrayList<CoordPair>> chunkStore = new ConcurrentHashMap<Integer, ArrayList<CoordPair>>();
	private transient boolean hasChanged = false;
	
	public synchronized void addChunk(int dimension, CoordPair coords) {
		
		if (!chunkStore.containsKey(dimension)) {
			chunkStore.put(dimension, new ArrayList<CoordPair>());
		}
		
		chunkStore.get(dimension).add(coords);
		hasChanged = true;
	}
	
	
	public void addChunk(World world, CoordPair coords) {
		addChunk(world.worldProvider.worldType, coords);
	}
	
	public synchronized void removeChunk(int dimension, CoordPair coords) {
		
		if (!chunkStore.containsKey(dimension)) {
			return;
		}
		
		chunkStore.get(dimension).remove(coords);
		hasChanged = true;
	}

	public void removeChunk(World world, CoordPair coords) {
		removeChunk(world.worldProvider.worldType, coords);
	}

	public boolean contains(int dimension, CoordPair coords) {
		
		if (!chunkStore.containsKey(dimension)) {
			return false;
		}
		
		return chunkStore.get(dimension).contains(coords);
	}
	
	public boolean contains(World world, CoordPair coords) {
		return contains(world.worldProvider.worldType, coords);
	}
	
	public ArrayList<CoordPair> getChunks(int dimension) {
		
		if (chunkStore.get(dimension) == null) {
			return new ArrayList<CoordPair>();
		}
		
		return chunkStore.get(dimension);
	}
	
	public ArrayList<CoordPair> getChunks(World world) {
		return getChunks(world.worldProvider.worldType);
	}
	
	public List<CoordPair> getLoadArea(CoordPair coords) {

		List<CoordPair> loadArea = new LinkedList<CoordPair>();

		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				CoordPair chunkCoords = new CoordPair(coords.x + x, coords.z + z);
				loadArea.add(chunkCoords);
			}
		}

		return loadArea;
	}
	
	public boolean isEmpty() {
		
		for (int dimension : chunkStore.keySet()) {
			if (!chunkStore.get(dimension).isEmpty()) {
				return false;
			}
		}
		
		return true;
	}

	public boolean hasChanged() {

		if (hasChanged) {
			hasChanged = false;
			return true;
		}
		
		return false;
	}


	public int size() {

		int size = 0;
		for (ArrayList list : chunkStore.values()) {
			size += list.size();
		}
		
		return size;
	}

}
