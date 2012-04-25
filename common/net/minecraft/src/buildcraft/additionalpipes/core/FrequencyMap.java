package net.minecraft.src.buildcraft.additionalpipes.core;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.src.NBTTagCompound;

public class FrequencyMap {

	private HashMap<Integer, String> map = new HashMap<Integer, String>();

	public void setFreqName(int selectedFreq, String name) {
		
		if (map.values().contains(name)) {
			map.remove(getFreq(name));
		}
		
		map.put(selectedFreq, name);
	}

	public void removeFreqName(int selectedFreq) {
		map.remove(selectedFreq);
	}
	
	public String getFreqName(int freq) {
		return map.get(freq);
	}
	
	public int getFreq(String name) {
		
		for (int freq : map.keySet()) {
			if (map.get(freq).equals(name)) {
				return freq;
			}
		}
		
		return -1;
	}

	public ArrayList<String> getNames() {
		return new ArrayList(map.values());
	}

	public ArrayList getFormattedNames() {

		ArrayList<String> names = new ArrayList<String>();
		for (Integer freq : map.keySet()) {
			String name = formatName(freq);
			names.add(name);
		}
		
		return names;
	}
	
	public String formatName(int freq) {
		return new StringBuilder().append(getFreqName(freq)).append(" (").append(freq).append(")").toString();
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		
		int i = 1;
		nbt.setInteger("numFrequencyMappings", i);
		
		for (int freq : map.keySet()) {
			
			nbt.setString("freqMapping" + i, freq + ":" + map.get(freq));
			i++;
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		
		ArrayList<String> mappings = new ArrayList<String>();
		
		int i = nbt.getInteger("numFrequencyMappings");
		while (i > 0) {
			mappings.add(nbt.getString("freqMapping" + i));
			i--;
		}
		
		for (String mapping : mappings) {
			
			String[] parts = mapping.split(":");
			map.put(Integer.parseInt(parts[0]), parts[1]);
		}
	}
	
}
