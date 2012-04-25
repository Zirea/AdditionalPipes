package net.minecraft.src.buildcraft.additionalpipes.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import codechicken.core.GuiScrollSlot;
import codechicken.core.IGuiIndirectButtons;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.buildcraft.additionalpipes.core.FrequencyMap;

public class GuiNameSlot extends GuiScrollSlot
{
	public class NameSlotComparator implements Comparator<String>
	{
	    public int compare(String s1, String s2) 
	    {
	    	boolean match1 = doesNameMatch(s1, match);
	    	boolean match2 = doesNameMatch(s2, match);
	    	if(match1 == match2)
	    	{
	    		return s1.compareToIgnoreCase(s2);
	    	}
	    	else
	    	{
	    		return match1 ? -1 : 1;
	    	}
	    }
	}
	
	public GuiNameSlot(IGuiIndirectButtons parent, FontRenderer fontRenderer, int x, int y, int width, int height)
	{
		super(parent);
		setSize(x, y, width, height);
		this.fontRenderer = fontRenderer;
		names = new ArrayList<String>();
	}
	
	public int getSlotHeight()
	{
		return 10;
	}

	protected int getNumSlots()
	{
		return names.size();
	}
	
	public void drawOverlay()
	{
		super.drawOverlay();
		drawRect(x, y - 7, x + width, y - 1, 0xFFC6C6C6);//top box blend
		drawRect(x, y + height + 1, x + width, y + height + 6, 0xFFC6C6C6);//bottom box blend
	}
	
	protected void drawSlot(int slot, int x, int y, boolean selected)
	{
		String name = names.get(slot);
		int colour;
		if(selected)
		{
			colour = 0xF0F0F0;
		}
		else if(!doesNameMatch(name, match))
		{
			colour = 0x707070;
		}
		else
		{
			colour = 0xA0A0A0;
		}
		fontRenderer.drawString(name, x, y, colour);
	}
	
	protected void slotClicked(int slot, boolean doubleclick)
	{
		if(doubleclick)
		{
			parentscreen.buttonPressed(activatebutton);
		}
		else
		{
			selectedslot = slot;
		}
	}
	
	protected boolean isSlotSelected(int slot)
	{
		return slot == selectedslot;
	}
	
	public void selectNext()
	{
		if(selectedslot == -1)
		{
			return;
		}
		
		selectedslot++;
		
		if(selectedslot == getNumSlots())
		{
			selectedslot--;
		}

		showSlot(selectedslot);
	}
	
	public void selectPrev()
	{
		if(selectedslot == -1)
		{
			return;
		}
		
		selectedslot--;
		
		if(selectedslot == -1)
		{
			selectedslot++;
		}
		
		showSlot(selectedslot);
	}
	
	protected void unfocus()
	{
		selectedslot = -1;
	}
	
	private void sortNames()
	{
		Collections.sort(names, new NameSlotComparator());
	}
	
	public void updateNameList(ArrayList names, String match)
	{
		this.names = names;
		this.match = match;
		sortNames();
	}

	public void removeName(String name)
	{
		names.remove(name);
		sortNames();
	}
	
	public void addName(String name)
	{
		names.add(name);
		sortNames();
	}
	
	public void clearNameList()
	{
		names.clear();
	}
	
	public static boolean doesNameMatch(String name, String match)
	{
		return name.length() >= match.length() && name.substring(0, match.length()).equalsIgnoreCase(match);
	}
	
	public String getSelectedName()
	{
		if(selectedslot == -1)
		{
			return "";
		}
		else
		{
			return names.get(selectedslot);
		}
	}
	
	private ArrayList<String> names;
	private String match = "";
	private int selectedslot = -1;
	
	final FontRenderer fontRenderer;
}
