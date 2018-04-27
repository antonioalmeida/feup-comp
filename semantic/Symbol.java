package semantic;

public class Symbol {
	public static enum Type {
		SCALAR, ARRAY, VOID, UNDEFINED 
		// void is not really a symbol type, 
		// but useful to define as the return
		// type of intermediate nodes that don't
		// return anything
	}
	
	protected Type type;
	protected boolean initialized;
	protected boolean print; //if set to true it will print this symbol in the symbolTable
	
	
	public Symbol(Type type, boolean initialized) {
		this.type = type;
		this.initialized = initialized;
		this.print = true;
		
	}
	
	public Symbol(Type type, boolean initialized, boolean print) {
		this.type = type;
		this.initialized = initialized;
		this.print = print;
		
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public boolean getPrint() {
		return print;
	}
	
	@Override
	public boolean equals(Object symbol) {
		Symbol symbolCast = (Symbol) symbol;
		
		if(this.type.equals(symbolCast.getType()) && this.initialized == symbolCast.getInitialized()
				&& this.print == symbolCast.getPrint())
			return true;
		else
			return false;
	}
	
	
}
