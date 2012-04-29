package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;

public class ChunkStore implements Serializable {
	
	private ConcurrentHashMap<Integer, ArrayList<CoordPair>> chunkStore = new ConcurrentHashMap<Integer, ArrayList<CoordPair>>();
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
	
	public boolean isEmpty() {
		return chunkStore.isEmpty();
	}

	public boolean hasChanged() {

		if (hasChanged) {
			hasChanged = false;
			return true;
		}
		
		return false;
	}

}
