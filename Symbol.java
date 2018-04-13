
public class Symbol {
	public static enum Type {
		INT, ARRAY 
	}
	
	
	private Type type;
	private boolean initialized;
	
	public Symbol(Type type, boolean initialized) {
		this.type = type;
		this.initialized = initialized;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
