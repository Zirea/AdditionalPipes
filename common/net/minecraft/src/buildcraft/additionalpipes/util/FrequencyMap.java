package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.src.NBTTagCompound;

public class FrequencyMap implements Serializable {

	public BidiMap<Integer, String> map = new BidiMap<Integer, String>();

	public Set<Integer> keys() {
		return map.keys();
	}
	
	public Set<String> values() {
		return map.values();
	}
	
	public String getFreqName(int freq) {
		return map.get(freq);
	}
	
	public int getFreq(String name) {
		
		if (map.containsValue(name)) {
			return map.getKey(name);
		}
		
		return -1;
	}
	
	public void setFreqName(int selectedFreq, String name) {
		
		if (map.containsKey(selectedFreq)) {
			map.removeByValue(name);
		}
		
		map.put(selectedFreq, name);
	}

	public void removeFreqName(int selectedFreq) {
		map.removeByKey(selectedFreq);
	}

	public ArrayList<String> getNames() {
		return new ArrayList(map.values());
	}
	
}
