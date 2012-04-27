package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMap<K, V> implements Serializable {

	private Map<K, V> keyToValueMap = new ConcurrentHashMap<K, V>();
	private Map<V, K> valueToKeyMap = new ConcurrentHashMap<V, K>();
	
	public Set<K> keys() {
		return keyToValueMap.keySet();
	}
	
	public Collection<V> values() {
		return keyToValueMap.values();
	}
	
	public synchronized void put(K key, V value) {
		keyToValueMap.put(key, value);
		valueToKeyMap.put(value, key);
	}
	
	public synchronized V removeByKey(K key) {
		V removedValue = keyToValueMap.remove(key);
		valueToKeyMap.remove(removedValue);
		return removedValue;
	}
	
	public synchronized K removeByValue(V value) {
		K removedKey = valueToKeyMap.remove(value);
		keyToValueMap.remove(removedKey);
		return removedKey;
	}
	
	public boolean containsKey(K key) {
		return keyToValueMap.containsKey(key);
	}
	
	public boolean containsValue(V value) {
		return valueToKeyMap.containsKey(value);
	}
	
	public V get(K key) {
		return keyToValueMap.get(key);
	}
	
	public K getKey(V value) {
		return valueToKeyMap.get(value);
	}
	
}
