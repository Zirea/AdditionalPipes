package net.minecraft.src.buildcraft.additionalpipes.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.Tessellator;

public class GuiButtonArrow extends GuiButton
{
	public GuiButtonArrow(int i, int j, int k, int l, int i1, int arrow)
	{
		super(i, j, k, l, i1, "");
		setArrowDirection(arrow);
	}
	
	public void setArrowDirection(int dir)
	{
		arrowdirection = dir;
	}
	
	public void drawButton(Minecraft minecraft, int i, int j)
	{
		if(!drawButton)
        {
            return;
        }
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, minecraft.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        boolean flag = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
        mouseDragged(minecraft, i, j);
        
        int colour = !enabled ? 0xFFA0A0A0 : flag ? 0xFFFFFFA0 : 0xFFFFFFFF;
        
        drawArrow(minecraft, xPosition + width / 2 - 2, yPosition + (height - 8) / 2, colour);
	}
	
	private void drawArrow(Minecraft mc, int x, int y, int colour)
	{
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/codechicken/wirelessredstone/core/arrow.png"));
		
		float r = (float)(colour >> 16 & 0xff) / 255F;
        float g = (float)(colour >> 8 & 0xff) / 255F;
        float b = (float)(colour & 0xff) / 255F;
        float a = (float)(colour >> 24 & 0xff) / 255F;
		
        GL11.glColor4f(r, g, b, a);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + 8, zLevel, arrowdirection * 0.25, 1);
        tessellator.addVertexWithUV(x + 8, y + 8, zLevel, (arrowdirection + 1) * 0.25, 1);
        tessellator.addVertexWithUV(x + 8, y + 0, zLevel, (arrowdirection + 1) * 0.25, 0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, arrowdirection * 0.25, 0);
        tessellator.draw();
	}
	
	int arrowdirection;
}
