package net.minecraft.src.buildcraft.additionalpipes.core;

import java.util.HashMap;

public class FrequencyMap {

	private HashMap<Integer, String> map = new HashMap<Integer, String>();

	public void setFreqName(int selectedFreq, String name) {
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
			if (map.get(freq) == name) {
				return freq;
			}
		}
		
		return -1;
	}
	
}
