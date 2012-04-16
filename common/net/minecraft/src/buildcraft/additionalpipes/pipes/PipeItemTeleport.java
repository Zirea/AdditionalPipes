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
import java.util.Random;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.additionalpipes.MutiPlayerProxy;
import net.minecraft.src.buildcraft.additionalpipes.gui.GuiHandler;
import net.minecraft.src.buildcraft.additionalpipes.logic.PipeLogicTeleport;
import net.minecraft.src.buildcraft.additionalpipes.network.NetworkID;
import net.minecraft.src.buildcraft.api.*;
import net.minecraft.src.buildcraft.core.StackUtil;
import net.minecraft.src.buildcraft.core.Utils;
/*import net.minecraft.src.buildcraft.core.network.PacketPayload;
import net.minecraft.src.buildcraft.core.network.PacketUpdate;
import net.minecraft.src.buildcraft.core.network.TilePacketWrapper;*/
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.mod_AdditionalPipes;

public class PipeItemTeleport extends PipeTeleport implements IPipeTransportItemsHook {
    
    LinkedList <Integer> idsToRemove = new LinkedList <Integer> ();
    
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
    
    /*
    @Override
    public void setPosition (int xCoord, int yCoord, int zCoord) {
    	
        LinkedList <PipeItemTeleport> toRemove = new LinkedList <PipeItemTeleport> ();

        for (int i = 0; i < ItemTeleportPipes.size(); i++) {
        	
            if (ItemTeleportPipes.get(i).xCoord == xCoord &&  ItemTeleportPipes.get(i).yCoord == yCoord && ItemTeleportPipes.get(i).zCoord == zCoord) {

                toRemove.add(ItemTeleportPipes.get(i));
            }
        }

        ItemTeleportPipes.removeAll(toRemove);
        ItemTeleportPipes.add(this);
        
        super.setPosition(xCoord, yCoord, zCoord);
    }*/

    @Override
    public void updateEntity() {

    	super.updateEntity();
    	
        for (int theID : idsToRemove) {
            ((PipeTransportItems)transport).travelingEntities.remove(theID);
        }

        idsToRemove.clear();
    }


    @Override
    public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations, Position pos, EntityPassiveItem item) {
    	
    	List<PipeTeleport> connectedPipes = getConnectedPipes(false);
        LinkedList<Orientations> result = new LinkedList<Orientations>();

        //If no connected pipes, bounce
        if (connectedPipes.size() <= 0) {
            result.add(pos.orientation.reverse());
            return result;
        }

        Random rand = new Random();
        PipeTeleport destPipe = connectedPipes.get(rand.nextInt(connectedPipes.size()));
        
        if (!addToRandomPipeEntry(destPipe.container, Orientations.Unknown, item.item, item.speed)) {
        	result.add(pos.orientation.reverse());
            return result;
        }
        
        return new LinkedList<Orientations>();
    }
    
    public static boolean addToRandomPipeEntry (TileEntity tile, Orientations from, ItemStack items, float speed) {
		World w = tile.worldObj;
		
		LinkedList <Orientations> possiblePipes = new LinkedList <Orientations> ();
		
		for (int j = 0; j < 6; ++j) {
			if (from.reverse().ordinal() == j) {
				continue;
			}
			
			Position pos = new Position(tile.xCoord, tile.yCoord, tile.zCoord,
					Orientations.values()[j]);
			
			pos.moveForwards(1.0);
			
			TileEntity pipeEntry = w.getBlockTileEntity((int) pos.x,
					(int) pos.y, (int) pos.z);
			
			if (pipeEntry instanceof IPipeEntry && ((IPipeEntry) pipeEntry).acceptItems()) {
				possiblePipes.add(Orientations.values()[j]);
			}
		}
		
		if (possiblePipes.size() > 0) {
			int choice = w.rand.nextInt(possiblePipes.size());
			
			Position entityPos = new Position(tile.xCoord, tile.yCoord, tile.zCoord,
					possiblePipes.get(choice));
			Position pipePos = new Position(tile.xCoord, tile.yCoord, tile.zCoord,
					possiblePipes.get(choice));
			
			entityPos.x += 0.5;
			entityPos.y += Utils.getPipeFloorOf(items);
			entityPos.z += 0.5;
			
			entityPos.moveForwards(0.5);
			
			pipePos.moveForwards(1.0);
			
			IPipeEntry pipeEntry = (IPipeEntry) w.getBlockTileEntity(
					(int) pipePos.x, (int) pipePos.y, (int) pipePos.z);
			
			EntityPassiveItem entity = new EntityPassiveItem(w, entityPos.x,
					entityPos.y, entityPos.z, items);
			
			entity.speed = speed;
			
			pipeEntry.entityEntering(entity, entityPos.orientation);
			items.stackSize = 0;
			return true;
		}
		
		return false;
	}
    
    public Position getNewItemPos(Position Old, Orientations newPos, float f) {
        //Utils.getPipeFloorOf(data.item.item)
        double x = Old.x;
        double y = Old.y;
        double z = Old.z;

        if (newPos == Orientations.XNeg) {
            x += 1;
            y += .5;
            z += .5;
        }
        else if (newPos == Orientations.XPos) {
            //x += .6;
            y += f;
            z += .5;
        }
        else if (newPos == Orientations.YNeg) {
            x += .5;
            y += 1;
            z += .5;
        }
        else if (newPos == Orientations.YPos) {
            x += .5;
            //y += .6;
            z += .5;
        }
        else if (newPos == Orientations.ZNeg) {
            x += .5;
            y += f;
            z += 1;
        }
        else if (newPos == Orientations.ZPos) {
            x += .5;
            y += f;
            //z += .6;
        }

        return new Position(x, y, z);
    }

    @Override
    public void entityEntered(EntityPassiveItem item, Orientations orientation) {}

}
