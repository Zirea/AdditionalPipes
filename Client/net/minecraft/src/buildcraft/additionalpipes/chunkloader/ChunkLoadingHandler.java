package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;
import net.minecraft.src.buildcraft.additionalpipes.util.SaveManager;
import net.minecraft.src.forge.IChunkLoadHandler;
import net.minecraft.src.forge.ISaveEventHandler;

public class ChunkLoadingHandler implements IChunkLoadHandler, ISaveEventHandler {

	private final Minecraft mc = ModLoader.getMinecraftInstance();
	private static ChunkStore chunkStore;

	private void log(String s) {
		System.out.println(s);
	}
	
	@Override
	public void addActiveChunks(World world, Set<ChunkCoordIntPair> chunkList) {

		if (mc.theWorld != null && mc.theWorld.isRemote) {
			return;
		}
		
		for (CoordPair coords : chunkStore.getChunks(world)) {
			
			List<CoordPair> loadArea = chunkStore.getLoadArea(coords);
			for (CoordPair chunkCoordPair : loadArea) {

				ChunkCoordIntPair chunkCoords = chunkCoordPair.getChunkCoordIntPair();
				
				if (!chunkList.contains(chunkCoords)) {
					chunkList.add(chunkCoords);
					//log("Adding chunk: " + chunkCoords);
				}
				else {
					//log(chunkCoords + " already there.");
				}
			}
		}
	}

	@Override
	public boolean canUnloadChunk(Chunk chunk) {

		if (mc.theWorld != null && mc.theWorld.isRemote) {
			return true;
		}

		for (CoordPair coords : chunkStore.getChunks(chunk.worldObj)) {
			
			List<CoordPair> loadArea = chunkStore.getLoadArea(coords);
			for (CoordPair chunkCoordPair : loadArea) {

				if (chunk.getChunkCoordIntPair().equals(chunkCoordPair.getChunkCoordIntPair())) {
					//log("Keeping chunk: " + chunk.getChunkCoordIntPair());
					return false;
				}
			}
		}

		//log("Unloading chunk: " + chunk.getChunkCoordIntPair());
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
	}

	@Override
	public void onWorldSave(World world) {
		
		if (!chunkStore.isEmpty() && chunkStore.hasChanged()) {
			SaveManager.getManager().save("chunkstore", chunkStore);
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