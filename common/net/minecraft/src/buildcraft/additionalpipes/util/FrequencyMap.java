package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrequencyMap implements Serializable {

	private static final long serialVersionUID = -820035699061337927L;
	
	private final Map<String, BidiMap> map = new HashMap<String, BidiMap>();
	private boolean hasChanged = false;

	public Set<Integer> keys(String username) {

		if (map.get(username) == null) {
			return null;
		}

		return map.get(username).keySet();
	}

	public Collection<String> values(String username) {

		if (map.get(username) == null) {
			return null;
		}

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
			map.get(username).remove(selectedFreq);
		}

		map.get(username).put(selectedFreq, name);
		hasChanged = true;
	}

	public void removeFreqName(String username, int selectedFreq) {

		if (map.get(username) == null) {
			return;
		}

		map.get(username).remove(selectedFreq);
		hasChanged = true;
	}

	public ArrayList<String> getNames(String username) {

		ArrayList<String> names = new ArrayList<String>();

		if (map.get(username) != null) {
			names.addAll(map.get(username).values());
		}

		return names;
	}
	
	public boolean hasChanged() {
		
		if (hasChanged) {
			hasChanged = false;
			return true;
		}
		
		return false;
	}
	
	public Map getMap() {
		return map;
	}

}
