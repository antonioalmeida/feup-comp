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

	//if set to true it will print this symbol in the symbolTable
	protected boolean print; 

	// symbols' position in it's scope
	// -1 if it's a function or not a 
	// local variable
	protected int index;

	public Symbol(Type type, boolean initialized, boolean print, int index) {
		this.type = type;
		this.initialized = initialized;
		this.print = print;	
		this.index = index;
	}

	public Symbol(Type type, boolean initialized, boolean print) {
		this(type, initialized, print, -1);
	}
	
	public Symbol(Type type, boolean initialized) {
		this(type, initialized, true, -1);
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean getInitialized() {
		return initialized;
	}
	
	public boolean getPrint() {
		return true;
		//return print;
	}

	public int getIndex() {
		return index;
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
