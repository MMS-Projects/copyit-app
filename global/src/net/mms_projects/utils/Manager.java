package net.mms_projects.utils;

import java.util.HashMap;
import java.util.Iterator;

public class Manager<T> extends HashMap<String, T> implements Iterable<T> {

	public void addItem(String name, T item) {
		this.put(name, item);
	}

	public T get(int index) {
		return get(getKey(index));
	}

	public int getIndex(String itemName) {
		int index = 0;
		for (String key : this.keySet()) {
			if (key.equalsIgnoreCase(itemName)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public String getKey(int index) {
		int searchIndex = 0;
		for (String key : this.keySet()) {
			if (searchIndex == index) {
				return key;
			}
			index++;
		}
		return null;
	}

	public String[] getKeys() {
		String[] keys = new String[this.size()];
		int i = 0;
		for (String key : this.keySet()) {
			keys[i] = key;
			i++;
		}
		return keys;
	}

	@Override
	public Iterator<T> iterator() {
		return this.values().iterator();
	}

}
