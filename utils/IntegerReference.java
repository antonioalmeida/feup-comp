package utils;

public class IntegerReference {
	private int value;
	
	public IntegerReference() {
		value = 0;
	}
	
	public IntegerReference(int value) {
		this.value = value;
	}
	
	public void inc() {
		value++;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
