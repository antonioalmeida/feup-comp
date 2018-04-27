package utils;

public class Pair {
	
	protected Object key;
	protected Object value;
	
	public Pair(Object key, Object value) {
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) return false;
		Pair obj = (Pair) o;
		return this.key.equals(obj.getKey()) && this.value.equals(obj.getValue());
	}

}
