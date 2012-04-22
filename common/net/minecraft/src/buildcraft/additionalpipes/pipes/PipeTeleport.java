package net.minecraft.src.buildcraft.additionalpipes.pipes;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.src.buildcraft.additionalpipes.MutiPlayerProxy;
import net.minecraft.src.buildcraft.additionalpipes.logic.PipeLogicTeleport;
import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.core.PersistentWorld;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeTransport;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

public abstract class PipeTeleport extends Pipe {

	public final PipeLogicTeleport logic;
	
	public static List<PipeTeleport> teleportPipes = new LinkedList<PipeTeleport>();
	
	public PipeTeleport(PipeTransport transport, PipeLogicTeleport logic, int itemID) {
		super(transport, logic, itemID);
		this.logic = logic;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!teleportPipes.contains(this)) {
			teleportPipes.add(this);
		}
	}
	
	public List<PipeTeleport> getConnectedPipes(boolean ignoreReceive) {
		
		List<PipeTeleport> temp = new LinkedList<PipeTeleport>();
        removeOldPipes();
        
        PipeLogicTeleport logic = this.logic;

        for (PipeTeleport pipe : teleportPipes) {
        	
        	if (!pipe.isValid() || !this.getClass().equals(pipe.getClass())) {
        		continue;
        	}
        	
        	PipeLogicTeleport pipeLogic = (PipeLogicTeleport) pipe.logic;
        	
    		if (pipeLogic.owner.equalsIgnoreCase(logic.owner) || MutiPlayerProxy.isOnServer() == false) {
            	
                if (pipeLogic.canReceive || ignoreReceive) {
                	
                    if (pipeLogic.freq == logic.freq) {
                    	
                        if (xCoord != pipe.xCoord || yCoord != pipe.yCoord || zCoord != pipe.zCoord ) {
                        	
                             temp.add(pipe);
                        }
                    }
                }
            }
        }

        return temp;
	}
	
	public void removeOldPipes() {
		
        LinkedList <PipeTeleport> toRemove = new LinkedList <PipeTeleport> ();

        for (PipeTeleport pipe : teleportPipes) {
        	
        	if (!BlockGenericPipe.isValid(pipe)) {
        		toRemove.add(pipe);
        		worldObj.removeBlockTileEntity(pipe.xCoord, pipe.yCoord, pipe.zCoord);
        	}
        }

        teleportPipes.removeAll(toRemove);
    }
	
	public Position getPosition() {
		return new Position(xCoord, yCoord, zCoord);
	}
}
