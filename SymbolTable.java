import java.util.HashMap;

public class SymbolTable {
	
	public static enum Type {
		INT, ARRAY 
	}
	
	private HashMap<String, Type> symbols;

	

	public SymbolTable() {
	}
	
	
	public HashMap<String, Type> getSymbols() {
		return symbols;
	}
	
	public boolean containsSymbolName(String symbolName) {
		return symbols.containsKey(symbolName);
	}
	
	public boolean containsSymbol(String symbolName, SymbolTable.Type type) {
		//if(symbols.containsKey(symbolName))
			if(symbols.get(symbolName).equals(type))
				return true;
		
		return false;
	}
	
	public boolean addSymbol(String symbolName, SymbolTable.Type type) {
		if(! symbols.containsKey(symbolName)) {
			symbols.put(symbolName, type);
			return true;
		}
		else if(symbols.get(symbolName).equals(type)) {
			symbols.put(symbolName, type);
			return true;
		}
		else
			return false;
		
	}
	
	
	
	
	/*public boolean verifyInitialization(String symbolName) {
		if(! symbols.containsKey(symbolName))
			if(parent != null)
				return parent.verifyInitialization(symbolName);
			else
				return false;
		else
			return symbols.get(symbolName).getInitialized();
	}
	
	public void initializeSymbol(String symbolName) {
		if(! symbols.containsKey(symbolName))
			if(parent != null)
				parent.initializeSymbol(symbolName);
			
		else
			symbols.get(symbolName).setInitialized(true);
	}*/
}
