package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.buildcraft.additionalpipes.chunkloader.TileChunkLoader;
import net.minecraft.src.forge.IChunkLoadHandler;
import net.minecraft.src.forge.ISaveEventHandler;

public class ChunkLoadingHandler implements IChunkLoadHandler, ISaveEventHandler {

    private Minecraft mc = ModLoader.getMinecraftInstance();
    
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
                    //log("Adding chunk: " + chunkCoords, LOG_INFO);
                }
                else {
                    //log(chunkCoords + " already there.", LOG_INFO);
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
                    //log("Keeping chunk: " + chunk.getChunkCoordIntPair(), LOG_INFO);
                    return false;
                }
            }
        }

        //log("Unloading chunk: " + chunk.getChunkCoordIntPair(), LOG_INFO);
        return true;
    }

    @Override
    public boolean canUpdateEntity(Entity entity) {
        return true;
    }

	@Override
	public void onWorldLoad(World world) { 
		
	}

	@Override
	public void onWorldSave(World world) {
	}

	@Override
	public void onChunkLoad(World world, Chunk chunk) { }

	@Override
	public void onChunkUnload(World world, Chunk chunk) { }

	@Override
	public void onChunkSaveData(World world, Chunk chunk, NBTTagCompound data) { }

	@Override
	public void onChunkLoadData(World world, Chunk chunk, NBTTagCompound data) { }
}