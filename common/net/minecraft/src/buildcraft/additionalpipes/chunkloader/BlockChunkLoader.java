package net.minecraft.src.buildcraft.additionalpipes.chunkloader;

import java.util.ArrayList;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

// Referenced classes of package net.minecraft.src:
//            Block, Material

public class BlockChunkLoader extends BlockContainer implements ITextureProvider {

	public BlockChunkLoader(int BlockID, int i) {
		super(BlockID, i, Material.cloth);
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {};

	@Override
	public TileEntity getBlockEntity() {
		return new TileChunkLoader();
	}

	@Override
	public String getTextureFile() {
		return "/net/minecraft/src/buildcraft/additionalpipes/resources/chunkloader.png";
	}
	
	@Override
	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}

}