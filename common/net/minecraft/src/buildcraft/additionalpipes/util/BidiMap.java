package net.minecraft.src.buildcraft.additionalpipes.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BidiMap<K, V> implements Map<K, V>, Serializable, Cloneable {

	private static final long serialVersionUID = 7154675557525627071L;
	
	private final Map<K, V> keyToValueMap = new ConcurrentHashMap<K, V>();
	private final Map<V, K> valueToKeyMap = new ConcurrentHashMap<V, K>();

	@Override
	public Collection<V> values() {
		return keyToValueMap.values();
	}

	@Override
	public synchronized V put(K key, V value) {
		
		keyToValueMap.put(key, value);
		valueToKeyMap.put(value, key);
		return value;
	}
	
	@Override
	public synchronized V remove(Object key) {

		V removedValue = keyToValueMap.remove(key);
		valueToKeyMap.remove(removedValue);
		return removedValue;
	}

	public synchronized K removeByValue(V value) {

		K removedKey = valueToKeyMap.remove(value);
		keyToValueMap.remove(removedKey);
		return removedKey;
	}

	@Override
	public boolean containsKey(Object key) {
		return keyToValueMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return valueToKeyMap.containsKey(value);
	}

	@Override
	public V get(Object key) {
		return keyToValueMap.get(key);
	}

	public K getKey(V value) {
		return valueToKeyMap.get(value);
	}

	@Override
	public synchronized void clear() {
		keyToValueMap.clear();
		valueToKeyMap.clear();
	}

	@Override
	public Set entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return keyToValueMap.isEmpty();
	}

	@Override
	public Set keySet() {
		return keyToValueMap.keySet();
	}

	@Override
	public void putAll(Map m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return keyToValueMap.size();
	}

}
