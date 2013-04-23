package net.mms_projects.utils;

public class ManagerWithCurrent<T> extends Manager<T> {

	private String key;

	@Override
	public T put(String key, T value) {
		T returnValue = super.put(key, value);
		if (this.size() == 1) {
			try {
				this.setCurrentItem(key);
			} catch (Exception e) {
			}
		}
		return returnValue;
	}

	public T getCurrentItem() {
		return this.get(this.key);
	}
	
	public void setCurrentItem(String key) throws Exception {
		if (this.containsKey(key)) {
			this.key = key;
		} else {
			throw new Exception("Unknown key " + key + ".");
		}
	}

	public T get() {
		return this.getCurrentItem();
	}



}
