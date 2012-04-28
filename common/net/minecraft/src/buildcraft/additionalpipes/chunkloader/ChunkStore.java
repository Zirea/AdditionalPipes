package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;

public class ChunkStore implements Serializable {
	
	private HashMap<Integer, ArrayList<CoordPair>> chunkStore = new HashMap<Integer, ArrayList<CoordPair>>();
	private boolean hasChanged = false;
	
	public void addChunk(int dimension, CoordPair coords) {
		
		if (!chunkStore.containsKey(dimension)) {
			chunkStore.put(dimension, new ArrayList<CoordPair>());
		}
		
		chunkStore.get(dimension).add(coords);
		hasChanged = true;
	}
	
	public void removeChunk(int dimension, CoordPair coords) {
		
		if (!chunkStore.containsKey(dimension)) {
			return;
		}
		
		chunkStore.get(dimension).remove(coords);
		hasChanged = true;
	}
	
	public ArrayList<CoordPair> getChunks(int dimension) {
		
		if (chunkStore.get(dimension) == null) {
			return new ArrayList<CoordPair>();
		}
		
		return chunkStore.get(dimension);
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
