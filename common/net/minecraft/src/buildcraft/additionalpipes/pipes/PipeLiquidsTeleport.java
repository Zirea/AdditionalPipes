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
import net.minecraft.src.mod_AdditionalPipes;
import net.minecraft.src.buildcraft.additionalpipes.logic.PipeLogicTeleport;
import net.minecraft.src.buildcraft.additionalpipes.network.NetworkID;
import net.minecraft.src.buildcraft.additionalpipes.util.FrequencyMap;
import net.minecraft.src.buildcraft.additionalpipes.util.SaveManager;
import net.minecraft.src.buildcraft.api.ILiquidContainer;
import net.minecraft.src.buildcraft.api.IPipeEntry;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.IPipeTransportLiquidsHook;
import net.minecraft.src.buildcraft.transport.PipeTransportLiquids;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

public class PipeLiquidsTeleport extends PipeTeleport implements IPipeTransportLiquidsHook {

	class OilReturn {

		public Orientations theOrientation;
		public ILiquidContainer iliquid;

		public OilReturn(Orientations a, ILiquidContainer b) {

			theOrientation = a;
			iliquid = b;
		}
	}

	public static FrequencyMap frequencyMap;

	public PipeLiquidsTeleport(int itemID) {

		super(new PipeTransportLiquids(), new PipeLogicTeleport(NetworkID.GUI_PIPE_TP), itemID);

		((PipeTransportLiquids) transport).flowRate = 80;
		((PipeTransportLiquids) transport).travelDelay = 2;
	}

	@Override
	public int getMainBlockTexture() {

		return mod_AdditionalPipes.DEFUALT_LIQUID_TELEPORT_TEXTURE;
	}

	@Override
	public int fill(Orientations from, int quantity, int id, boolean doFill) {

		List<PipeTeleport> pipeList = getConnectedPipes(false);

		if (pipeList.size() == 0) {
			return 0;
		}

		// System.out.println("PipeList Size: " + pipeList.size());
		int i = worldObj.rand.nextInt(pipeList.size());
		LinkedList<OilReturn> theList = getPossibleLiquidMovements(pipeList.get(i).getPosition());

		if (theList.size() <= 0) {
			return 0;
		}

		// System.out.println("theList Size: " + theList.size());
		int used = 0;
		int a = 0;

		while (theList.size() > 0 && used <= 0) {
			a = worldObj.rand.nextInt(theList.size());
			// System.out.println("A: " + a);
			used = theList.get(a).iliquid.fill(theList.get(a).theOrientation.reverse(), quantity, id, doFill);
			theList.remove(a);
		}

		// System.out.println("Fill " + used);
		return used;

	}

	public LinkedList<OilReturn> getPossibleLiquidMovements(Position pos) {

		LinkedList<OilReturn> result = new LinkedList<OilReturn>();

		for (int o = 0; o <= 5; ++o) {
			
			Position newPos = new Position(pos);
			newPos.orientation = Orientations.values()[o];
			newPos.moveForwards(1.0);

			TileEntity tile = Utils.getTile(worldObj, newPos, Orientations.Unknown);
			
			if (tile instanceof TileGenericPipe && !(((TileGenericPipe)tile).pipe instanceof PipeTeleport) && canReceiveLiquid2(newPos)) {
				result.add(new OilReturn(Orientations.values()[o], (ILiquidContainer) tile));
			}
		}

		return result;
	}

	public boolean canReceiveLiquid2(Position p) {

		TileEntity entity = worldObj.getBlockTileEntity((int) p.x, (int) p.y, (int) p.z);

		if (entity == null) {
			return false;
		}

		if (entity instanceof IPipeEntry || entity instanceof ILiquidContainer) {
			return true;
		}

		return false;
	}

	@Override
	public Position getPosition() {

		return new Position(xCoord, yCoord, zCoord);
	}
	
	@Override
	public FrequencyMap getFrequencyMap() {

		if (frequencyMap == null) {
			
			frequencyMap = new FrequencyMap();
			frequencyMap = (FrequencyMap) SaveManager.getManager().load(getSmallClassName() + "freqmap", frequencyMap);
		}

		return frequencyMap;
	}

}
