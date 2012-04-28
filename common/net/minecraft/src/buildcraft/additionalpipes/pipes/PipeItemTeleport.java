/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src.buildcraft.additionalpipes.pipes;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.logic.PipeLogicTeleport;
import net.minecraft.src.buildcraft.additionalpipes.network.NetworkID;
import net.minecraft.src.buildcraft.additionalpipes.util.FrequencyMap;
import net.minecraft.src.buildcraft.additionalpipes.util.SaveManager;
import net.minecraft.src.buildcraft.api.*;
import net.minecraft.src.buildcraft.core.Utils;
/*import net.minecraft.src.buildcraft.core.network.PacketPayload;
 import net.minecraft.src.buildcraft.core.network.PacketUpdate;
 import net.minecraft.src.buildcraft.core.network.TilePacketWrapper;*/
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.mod_AdditionalPipes;

public class PipeItemTeleport extends PipeTeleport implements IPipeTransportItemsHook {

	public static FrequencyMap frequencyMap;

	public PipeItemTeleport(int itemID) {

		super(new PipeTransportItems(), new PipeLogicTeleport(NetworkID.GUI_PIPE_TP), itemID);
	}

	@Override
	public int getMainBlockTexture() {

		return mod_AdditionalPipes.DEFUALT_ITEM_TELEPORT_TEXTURE;
	}

	@Override
	public void readjustSpeed(EntityPassiveItem item) {

		((PipeTransportItems) transport).defaultReajustSpeed(item);
	}

	@Override
	public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations, Position pos, EntityPassiveItem item) {

		LinkedList<Orientations> result = new LinkedList<Orientations>();

		List<PipeTeleport> connectedPipes = getConnectedPipes(false);
		if (connectedPipes.isEmpty()) {
			result.add(pos.orientation.reverse());
			return result;
		}

		PipeTeleport destPipe;
		while (!connectedPipes.isEmpty()) {

			int i = worldObj.rand.nextInt(connectedPipes.size());
			destPipe = connectedPipes.get(i);
			if (addToRandomPipeEntry(destPipe.container, Orientations.Unknown, item)) {
				break;
			}
			connectedPipes.remove(i);
		}

		return result;
	}

	public static boolean addToRandomPipeEntry(TileEntity tile, Orientations from, EntityPassiveItem item) {

		World w = tile.worldObj;

		LinkedList<Orientations> possiblePipes = new LinkedList<Orientations>();

		for (int j = 0; j < 6; ++j) {

			if (from.reverse().ordinal() == j) {
				continue;
			}

			Position pos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, Orientations.values()[j]);
			pos.moveForwards(1.0);

			TileEntity pipeEntry = Utils.getTile(w, pos, Orientations.Unknown);

			if (pipeEntry instanceof TileGenericPipe && ((TileGenericPipe) pipeEntry).pipe instanceof PipeItemTeleport) {
				continue;
			}

			if (pipeEntry instanceof IPipeEntry && ((IPipeEntry) pipeEntry).acceptItems()) {
				possiblePipes.add(Orientations.values()[j]);
			}
		}

		if (possiblePipes.size() > 0) {

			int choice = w.rand.nextInt(possiblePipes.size());

			Position entityPos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, possiblePipes.get(choice));
			entityPos.x += 0.5;
			entityPos.y += Utils.getPipeFloorOf(item.item);
			entityPos.z += 0.5;
			entityPos.moveForwards(0.5);

			Position pipePos = new Position(tile.xCoord, tile.yCoord, tile.zCoord, possiblePipes.get(choice));
			pipePos.moveForwards(1.0);

			IPipeEntry pipeEntry = (IPipeEntry) w.getBlockTileEntity((int) pipePos.x, (int) pipePos.y, (int) pipePos.z);

			item.setPosition(entityPos.x, entityPos.y, entityPos.z);

			pipeEntry.entityEntering(item, entityPos.orientation);

			return true;
		}

		return false;
	}

	@Override
	public void entityEntered(EntityPassiveItem item, Orientations orientation) {}
	
	@Override
	public FrequencyMap getFrequencyMap() {

		if (frequencyMap == null) {
			
			frequencyMap = new FrequencyMap();
			frequencyMap = (FrequencyMap) SaveManager.getManager().load(getSmallClassName() + "freqmap", frequencyMap);
		}

		return frequencyMap;
	}

}
