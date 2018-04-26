
public class Symbol {
	public static enum Type {
		SCALAR, ARRAY, VOID 
		// void is not really a symbol type, 
		// but useful to define as the return
		// type of intermediate nodes that don't
		// return anything
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
	
	@Override
	public boolean equals(Object symbol) {
		Symbol symbolCast = (Symbol) symbol;
		
		if(this.type.equals(symbolCast.getType()) && this.initialized == symbolCast.getInitialized())
			return true;
		else
			return false;
	}
	
	
}
