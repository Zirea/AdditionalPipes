package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Chunk;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.util.CoordPair;
import net.minecraft.src.forge.DimensionManager;
import net.minecraft.src.forge.ITextureProvider;

// Referenced classes of package net.minecraft.src:
//            Block, Material

public class BlockChunkLoader extends BlockContainer implements ITextureProvider {

	public BlockChunkLoader(int BlockID, int i) {

		super(BlockID, i, Material.cloth);
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		
		System.out.println("Block added.");
		
		Chunk chunk = world.getChunkFromBlockCoords(i, k);
		ChunkLoadingHandler.getChunkStore().addChunk(0, new CoordPair(chunk.xPosition, chunk.zPosition));
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {

		System.out.println("Block removed.");
		
		Chunk chunk = world.getChunkFromBlockCoords(i, k);
		ChunkLoadingHandler.getChunkStore().removeChunk(0, new CoordPair(chunk.xPosition, chunk.zPosition));
	}

	@Override
	public TileEntity getBlockEntity() {

		return new TileChunkLoader();
	}

	@Override
	public String getTextureFile() {

		return "/net/minecraft/src/buildcraft/additionalpipes/resources/chunkloader.png";
	}

}