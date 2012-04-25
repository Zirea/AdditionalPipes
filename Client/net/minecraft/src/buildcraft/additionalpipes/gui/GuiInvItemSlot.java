package net.minecraft.src.buildcraft.additionalpipes.gui;

import org.lwjgl.input.Keyboard;

import codechicken.core.CoreUtils;
import codechicken.core.IGuiIndirectButtons;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiInvItemSlot extends Gui
{
	public GuiInvItemSlot(int x, int y, InventoryPlayer playerinv, ItemStack[] defaults, int selection, IGuiIndirectButtons parent)
	{
		this.x = x;
		this.y = y;
		
		parentscreen = parent;
		
		defaultitems = defaults;
		invitems = new ItemStack[defaultitems.length];
		invslotnumbers = new int[defaultitems.length];
		inv = playerinv;
		searchInventoryItems();
		selectItem(selection);
	}
	
	public void drawSlot(Minecraft mc, FontRenderer fontrenderer)
	{        
        drawSlotBox(x, y);
        itemRenderer.renderItemIntoGUI(fontrenderer, mc.renderEngine, invitems[selecteditem], x, y);
	}
	
	public void drawSlotBox(int x, int y)
	{
		drawRect(x - 1, y - 1, x + 17, y + 17, 0xFF8B8B8B);//big grey
		drawRect(x - 1, y - 1, x + 16, y + 16, 0xFF373737);//small black
		drawRect(x + 0, y + 0, x + 17, y + 17, 0xFFFFFFFF);//small white
		drawRect(x + 0, y + 0, x + 16, y + 16, 0xFF8B8B8B);//small grey
	}
	
	public void setFocused(boolean focus)
	{
		focused = focus;
	}
	
	public void registerButtons(GuiButton next, GuiButton prev, GuiButton activate)
	{
		nextbutton = next;
		prevbutton = prev;
		activatebutton = activate;
	}

	public void mouseClicked(int mousex, int mousey, int button)
	{
		if(mousex >= x && mousex < x + 16 && mousey >= y && mousey < y + 16)//clicked on us
		{
			setFocused(true);
		}
		else
		{
			setFocused(false);
		}
	}
	
	public void keyTyped(char c, int keyindex)
	{
		if(!focused)
		{
			return;
		}
		if(keyindex == Keyboard.KEY_LEFT)
		{
			cyclePrevItem();
		}
		if(keyindex == Keyboard.KEY_RIGHT)
		{
			cycleNextItem();
		}
		if(keyindex == Keyboard.KEY_RETURN && activatebutton != null)
		{
			parentscreen.buttonPressed(activatebutton);
		}
	}
	
	public void actionPerformed(GuiButton guibutton)
	{
		if(nextbutton != null && guibutton.id == nextbutton.id)
		{
			cycleNextItem();
		}
		else if(prevbutton != null && guibutton.id == prevbutton.id)
		{
			cyclePrevItem();
		}
	}
	
	public void decrementCurrentStack()
	{
		if(invitems[selecteditem] == null)
		{
			return;
		}
		int slot = invslotnumbers[selecteditem];
		ItemStack item = invitems[selecteditem];
		item.stackSize -= 1;
		
		if(CoreUtils.isClient())
		{
			//CorePacketHandler.sendSetSlot(slot, item);
		}
		
		if(item.stackSize == 0)
		{
			inv.mainInventory[slot] = null;
			searchInventoryItems();
			selectItem(selecteditem);
		}
	}
	
	public boolean currentStackExists()
	{
		return invitems[selecteditem] != null;
	}
	
	public void selectItem(int index)
	{
		selecteditem = index;
		if(invitems[selecteditem] == null)
		{
			cycleNextItem();
		}
	}
	
	public void cycleNextItem()
	{
		int cycleindex = selecteditem;
		while(true)
		{
			cycleindex++;
			if(cycleindex >= invitems.length)
			{
				cycleindex = 0;
			}
			if(cycleindex == selecteditem)
			{
				return;
			}
			if(invitems[cycleindex] != null)
			{
				selecteditem = cycleindex;
				return;
			}
		}
	}
	
	public void cyclePrevItem()
	{
		int cycleindex = selecteditem;
		while(true)
		{
			cycleindex--;
			if(cycleindex < 0)
			{
				cycleindex = invitems.length - 1;
			}
			if(cycleindex == selecteditem)
			{
				return;
			}
			if(invitems[cycleindex] != null)
			{
				selecteditem = cycleindex;
				return;
			}
		}
	}
	
	private void searchInventoryItems()
	{
		for(int i = 0; i < defaultitems.length; i++)
		{
			invitems[i] = null;
			invslotnumbers[i] = -1;
			for(int j = 0; j < inv.mainInventory.length; j++)
			{
				ItemStack invstack = inv.getStackInSlot(j);
				if(invstack == null)
				{
					continue;
				}
				if(defaultitems[i].isItemEqual(invstack))
				{
					invitems[i] = invstack;
					invslotnumbers[i] = j;
					break;
				}
			}
		}
	}

	public int getSelectedIndex()
	{
		return invitems[selecteditem] == null ? -1 : selecteditem;
	}
	
	public int x;
	public int y;
	
	protected GuiButton nextbutton;
	protected GuiButton prevbutton;
	protected GuiButton activatebutton;
	protected IGuiIndirectButtons parentscreen;
	
	public boolean focused;
	
	protected ItemStack[] invitems;
	protected int[] invslotnumbers;
	protected ItemStack[] defaultitems;
	protected InventoryPlayer inv;
			
	protected int selecteditem;
	
	private static RenderItem itemRenderer = new RenderItem();
}
