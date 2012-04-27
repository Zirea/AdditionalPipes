package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.src.NBTTagCompound;

public class FrequencyMap implements Serializable {

	private Map<String, BidiMap> map = new HashMap<String, BidiMap>();

	public Set<Integer> keys(String username) {
		return map.get(username).keys();
	}
	
	public Collection<String> values(String username) {
		return map.get(username).values();
	}
	
	public String getFreqName(String username, int freq) {
		
		if (map.get(username) == null) {
			return null;
		}
		
		return (String) map.get(username).get(freq);
	}
	
	public int getFreq(String username, String freqName) {
		
		if (map.get(username) != null && map.get(username).containsValue(freqName)) {
			return (Integer) map.get(username).getKey(freqName);
		}
		
		return -1;
	}
	
	public void setFreqName(String username, int selectedFreq, String name) {
		
		if (map.get(username) == null) {
			map.put(username, new BidiMap<Integer, String>());
		}
		
		if (map.get(username).containsKey(selectedFreq)) {
			map.get(username).removeByValue(name);
		}
		
		map.get(username).put(selectedFreq, name);
	}

	public void removeFreqName(String username, int selectedFreq) {
		map.get(username).removeByKey(selectedFreq);
	}

	public ArrayList<String> getNames(String username) {
		
		ArrayList<String> names = new ArrayList<String>();
		
		if (map.get(username) != null) {
			names.addAll(map.get(username).values());
		}
		
		return names;
	}
	
}
