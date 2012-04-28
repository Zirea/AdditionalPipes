package net.minecraft.src.buildcraft.additionalpipes.gui;

import net.minecraft.src.*;
import net.minecraft.src.buildcraft.additionalpipes.gui.components.GuiBetterTextField;
import net.minecraft.src.buildcraft.additionalpipes.gui.components.GuiButtonShorter;
import net.minecraft.src.buildcraft.additionalpipes.gui.components.GuiNameSlot;
import net.minecraft.src.buildcraft.additionalpipes.pipes.PipeTeleport;
import net.minecraft.src.buildcraft.additionalpipes.util.FrequencyMap;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;

import org.lwjgl.opengl.GL11;

import codechicken.core.IGuiIndirectButtons;

public class GuiTeleportPipeNew extends GuiScreen implements IGuiIndirectButtons {

	public int guiWidth = 244;
	public int guiHeight = 116;

	private GuiBetterTextField textboxName;
	private GuiBetterTextField textboxFreq;
	private GuiNameSlot slotNames;

	private GuiButton setFreqButton;
	private GuiButton setNameButton;
	private GuiButton removeNameButton;
	private GuiButton selectNameButton;
	private GuiButton canReceiveButton;

	private PipeTeleport pipe;
	private EntityPlayer player;
	private int selectedFreq;
	private FrequencyMap frequencyMap;

	public GuiTeleportPipeNew(TileGenericPipe tile, EntityPlayer player) {

		super();
		this.player = player;
		pipe = (PipeTeleport) tile.pipe;
	}

	@Override
	public void initGui() {

		super.initGui();

		frequencyMap = pipe.getFrequencyMap();

		controlList.clear();

		setFreqButton = new GuiButtonShorter(0, posX(50), posY(7), 22, 16, "Set");
		controlList.add(setFreqButton);

		textboxFreq = new GuiBetterTextField(this, fontRenderer, posX(7), posY(7), 40, 16, "");
		textboxFreq.setMaxStringLength(5);
		textboxFreq.setReturnButton(setFreqButton);
		textboxFreq.setAllowedCharacters("0123456789");

		controlList.add(new GuiButton(1, posX(6), posY(28), 30, 20, "-"));
		controlList.add(new GuiButton(2, posX(42), posY(28), 30, 20, "+"));
		controlList.add(new GuiButton(3, posX(6), posY(52), 30, 20, "-10"));
		controlList.add(new GuiButton(4, posX(42), posY(52), 30, 20, "+10"));
		controlList.add(new GuiButton(5, posX(6), posY(76), 30, 20, "-100"));
		controlList.add(new GuiButton(6, posX(42), posY(76), 30, 20, "+100"));

		setNameButton = new GuiButtonShorter(7, posX(196), posY(29), 42, 16, "Add");
		controlList.add(setNameButton);
		removeNameButton = new GuiButtonShorter(8, posX(196), posY(29), 42, 16, "Remove");
		controlList.add(removeNameButton);

		textboxName = new GuiBetterTextField(this, fontRenderer, posX(80), posY(29), 114, 16, "Test Name");
		textboxName.setMaxStringLength(20);
		textboxName.setReturnButton(setNameButton);
		textboxName.isFocused = false;

		selectNameButton = new GuiButton(9, posX(0), posY(0), 0, 0, "");
		controlList.add(selectNameButton);

		slotNames = new GuiNameSlot(this, fontRenderer, posX(80), posY(49), 156, 46);
		slotNames.registerButtons(null, null, selectNameButton);

		canReceiveButton = new GuiButtonShorter(10, posX(204), posY(8), 34, 16, "");
		controlList.add(canReceiveButton);

		refresh();
	}

	private int posX(int offset) {

		return (width - guiWidth) / 2 + offset;
	}

	private int posY(int offset) {

		return (height - guiHeight) / 2 + offset;
	}

	@Override
	public void updateScreen() {

		/*
		 * if(pipe != null && pipe.isValid()) //tile changed { mc.currentScreen
		 * = null; mc.setIngameFocus(); }
		 */
		super.updateScreen();

		textboxFreq.updateCursorCounter();
		textboxName.updateCursorCounter();

		updateNames();
	}

	@Override
	public void drawScreen(int i, int j, float f) {

		drawDefaultBackground();
		drawContainerBackground();

		GL11.glDisable(2896 /* GL_LIGHTING */);
		GL11.glDisable(2929 /* GL_DEPTH_TEST */);

		fontRenderer.drawString("Connected: " + pipe.getConnectedPipes(true).size(), posX(80), posY(12), 0x404040);
		fontRenderer.drawString("Receive:", posX(160), posY(12), 0x404040);
		fontRenderer.drawString("Owner: " + pipe.logic.owner, posX(6), posY(102), 0x404040);

		slotNames.drawScreen(i, j, f);

		textboxFreq.drawTextBox();
		textboxName.drawTextBox();

		super.drawScreen(i, j, f);// buttons

		GL11.glEnable(2896 /* GL_LIGHTING */);
		GL11.glEnable(2929 /* GL_DEPTH_TEST */);
	}

	private boolean nameIsUsed(String name) {

		return frequencyMap.getFreq(player.username, name) > -1;
	}

	private void updateNames() {

		String frequencyName = frequencyMap.getFreqName(player.username, selectedFreq);
		String typedName = textboxName.getText();
		boolean set = frequencyName == null || frequencyName.equals("") || !frequencyName.equals(typedName);
		boolean canset = !typedName.equals("") && !nameIsUsed(typedName);

		setNameButton.enabled = set && canset;
		setNameButton.drawButton = set;
		removeNameButton.enabled = !set;
		removeNameButton.drawButton = !set;

		if (set) {
			slotNames.updateNameList(frequencyMap.getNames(player.username), textboxName.getText());
		}
		else if (frequencyName.equals(typedName)) {
			slotNames.updateNameList(frequencyMap.getNames(player.username), "");
			slotNames.removeName(typedName);
		}
		else {
			slotNames.clearNameList();
		}
	}

	private void reloadNameText() {

		String name = frequencyMap.getFreqName(player.username, selectedFreq);
		if (name == null) {
			name = "";
		}
		textboxName.setText(name);
	}

	private void refresh() {

		selectedFreq = pipe.logic.freq;
		textboxFreq.setText(String.valueOf(pipe.logic.freq));

		canReceiveButton.displayString = pipe.logic.canReceive ? "True" : "False";

		reloadNameText();
	}

	private void pressIncrementalButton(int id) {

		switch (id) {
		case 1:
			selectedFreq--;
			break;
		case 2:
			selectedFreq++;
			break;
		case 3:
			selectedFreq -= 10;
			break;
		case 4:
			selectedFreq += 10;
			break;
		case 5:
			selectedFreq -= 100;
			break;
		case 6:
			selectedFreq += 100;
			break;
		}

		if (selectedFreq < 0 || selectedFreq > 99999) {
			selectedFreq = 0;
		}

		setNewFreq();
	}

	private void pressSetFreqButton() {

		selectedFreq = Integer.parseInt(textboxFreq.getText());
		setNewFreq();
	}

	private void pressSetNameButton() {

		frequencyMap.setFreqName(player.username, selectedFreq, textboxName.getText());
		// NEED TO SEND NETWORK UPDATE
	}

	private void pressRemNameButton() {

		frequencyMap.removeFreqName(player.username, selectedFreq);
		textboxName.setText("");
		// NEED TO SEND NETWORK UPDATE
	}

	private void selectSlotName() {

		int freq = frequencyMap.getFreq(player.username, slotNames.getSelectedName());
		if (freq > -1) {
			selectedFreq = freq;
			setNewFreq();
		}
	}

	private void pressReceiveButton() {

		pipe.logic.toggleReceive();
		refresh();
	}

	@Override
	public void buttonPressed(GuiButton guibutton) {

		actionPerformed(guibutton);
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		if (button.id == 0) {
			pressSetFreqButton();
		}
		else if (button.id >= 1 && button.id <= 6) {
			pressIncrementalButton(button.id);
		}
		else if (button.id == 7) {
			pressSetNameButton();
		}
		else if (button.id == 8) {
			pressRemNameButton();
		}
		else if (button.id == 9) {
			selectSlotName();
		}
		else if (button.id == 10) {
			pressReceiveButton();
		}
		else {
			slotNames.actionPerformed(button);
		}
	}

	@Override
	protected void keyTyped(char c, int i) {

		super.keyTyped(c, i);

		if (i == mc.gameSettings.keyBindInventory.keyCode && !textboxName.isFocused) {

			mc.thePlayer.closeScreen();
			return;
		}

		textboxFreq.textboxKeyTyped(c, i);
		textboxName.textboxKeyTyped(c, i);
		slotNames.keyTyped(c, i);
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {

		super.mouseClicked(i, j, k);

		textboxFreq.mouseClicked(i, j, k);
		textboxName.mouseClicked(i, j, k);
		slotNames.mouseClicked(i, j, k);
	}

	@Override
	protected void mouseMovedOrUp(int i, int j, int k) {

		super.mouseMovedOrUp(i, j, k);

		slotNames.mouseMovedOrUp(i, j, k);
	}

	public void selectNextField() {

		textboxFreq.setFocused(textboxName.isFocused);
		textboxName.setFocused(!textboxName.isFocused);
	}

	private void setNewFreq() {

		pipe.logic.freq = selectedFreq;
		refresh();
	}

	private void drawContainerBackground() {

		int i = mc.renderEngine.getTexture("/net/minecraft/src/buildcraft/additionalpipes/resources/guiTeleportSmall.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(i);

		int posx = width / 2 - guiWidth / 2;
		int posy = height / 2 - guiHeight / 2;

		drawTexturedModalRect(posx, posy, 0, 0, guiWidth, guiHeight);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		
		if (!frequencyMap.getNames(player.username).isEmpty() && frequencyMap.hasChanged()) {
			System.out.println("Saving frequency map.");
			pipe.saveFrequencyMap(frequencyMap); 
		}
	}
}
