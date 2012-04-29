package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;

public class TileChunkLoader extends TileEntity {

	public TileChunkLoader() {}

	private synchronized void addToChunkStore() {
		
		if (!ChunkLoadingHandler.getChunkStore().contains(worldObj, getChunkCoords())) {
			ChunkLoadingHandler.getChunkStore().addChunk(worldObj, getChunkCoords());
		}
	}

	private synchronized void removeFromChunkStore() {
		
		ChunkLoadingHandler.getChunkStore().removeChunk(worldObj, getChunkCoords());
	}
	
	protected CoordPair getChunkCoords() {
		return new CoordPair(xCoord >> 4, zCoord >> 4);
	}

	@Override
	public void updateEntity() {

		super.updateEntity();
		addToChunkStore();
	}

	@Override
	public void invalidate() {

		super.invalidate();
		removeFromChunkStore();
	}
}
