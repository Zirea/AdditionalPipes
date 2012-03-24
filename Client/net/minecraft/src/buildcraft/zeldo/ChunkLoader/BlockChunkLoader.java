package net.minecraft.src.buildcraft.zeldo.ChunkLoader;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockChunkLoader extends BlockContainer
{

	public BlockChunkLoader(int BlockID)
	{
		super(BlockID, 12 * 16 + 1, Material.cloth);
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
            System.out.println("Placing block.");
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k)
	{
	}

	@Override
	public TileEntity getBlockEntity()
	{
            return new TileChunkLoader();
	}

}