package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.buildcraft.additionalpipes.chunkloader.TileChunkLoader;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;
import net.minecraft.src.buildcraft.additionalpipes.util.SaveManager;
import net.minecraft.src.forge.DimensionManager;
import net.minecraft.src.forge.IChunkLoadHandler;
import net.minecraft.src.forge.ISaveEventHandler;

public class ChunkLoadingHandler implements IChunkLoadHandler, ISaveEventHandler {

	private Minecraft mc = ModLoader.getMinecraftInstance();
	private static ChunkStore chunkStore;

	private void log(String s) {
		System.out.println(s);
	}
	
	@Override
	public void addActiveChunks(World world, Set<ChunkCoordIntPair> chunkList) {

		if (mc.theWorld != null && mc.theWorld.isRemote) {
			return;
		}

		for (TileChunkLoader tile : TileChunkLoader.chunkLoaderList) {

			List<ChunkCoordIntPair> loadArea = tile.getLoadArea();
			for (ChunkCoordIntPair chunkCoords : loadArea) {

				if (!chunkList.contains(chunkCoords)) {
					chunkList.add(chunkCoords);
					log("Adding chunk: " + chunkCoords);
				}
				else {
					log(chunkCoords + " already there.");
				}
			}
		}

	}

	@Override
	public boolean canUnloadChunk(Chunk chunk) {

		if (mc.theWorld != null && mc.theWorld.isRemote) {
			return true;
		}

		for (TileChunkLoader tile : TileChunkLoader.chunkLoaderList) {

			List<ChunkCoordIntPair> loadArea = tile.getLoadArea();
			for (ChunkCoordIntPair chunkCoords : loadArea) {

				if (chunk.worldObj.getChunkFromChunkCoords(chunkCoords.chunkXPos, chunkCoords.chunkZPosition).equals(chunk)) {
					log("Keeping chunk: " + chunk.getChunkCoordIntPair());
					return false;
				}
			}
		}

		log("Unloading chunk: " + chunk.getChunkCoordIntPair());
		return true;
	}

	@Override
	public boolean canUpdateEntity(Entity entity) {
		return true;
	}
	
	public static ChunkStore getChunkStore() {
		return chunkStore;
	}

	@Override
	public void onWorldLoad(World world) {
		
		if (chunkStore == null) {
			chunkStore = (ChunkStore) SaveManager.getManager().load("chunkstore", new ChunkStore());
		}
		
		ArrayList<CoordPair> chunksToLoad = chunkStore.getChunks(world.worldProvider.worldType);
		for (CoordPair coords : chunksToLoad) {
			
			if (!world.getChunkProvider().chunkExists(coords.x, coords.z)) {
				world.getChunkProvider().loadChunk(coords.x, coords.z);
				System.out.println("Forcing chunk load: " + coords );
			}
		}
	}

	@Override
	public void onWorldSave(World world) {
		
		if (!chunkStore.isEmpty() && chunkStore.hasChanged()) {
			SaveManager.getManager().save("chunkstore", chunkStore);
			System.out.println("Saving chunkStore");
		}
		
	}

	@Override
	public void onChunkLoad(World world, Chunk chunk) {}

	@Override
	public void onChunkUnload(World world, Chunk chunk) {}

	@Override
	public void onChunkSaveData(World world, Chunk chunk, NBTTagCompound data) {}

	@Override
	public void onChunkLoadData(World world, Chunk chunk, NBTTagCompound data) {}
	
}