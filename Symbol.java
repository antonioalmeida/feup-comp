
public class Symbol {
	public static enum Type {
		INT, ARRAY 
	}
	
	
	private Type type;
	
	
	public Symbol(Type type) {
		this.type = type;
		
	}
	
	public Type getType() {
		return type;
	}
	
	
}
