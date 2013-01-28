package com.gfk.senbot.framework.data;

import java.util.HashMap;

/**
 * A HashMap with case insensitive keys
 * 
 * @author joostschouten
 *
 * @param <V>
 */

public class CaseinsensitiveMap<V> extends HashMap<String, V>{
	
	@Override
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}

	@Override
	public V get(Object key) {
		return super.get(((String)key).toLowerCase());
	}
	
}
