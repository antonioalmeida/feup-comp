import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, Symbol> symbolsInitialization;
	private SymbolTable parent;
	
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}
	
	
	public HashMap<String, Symbol> getSymbolsInitialization() {
		return symbolsInitialization;
	}
	
	public SymbolTable getParent() {
		return parent;
	}
	
	public boolean verifyInitialization(String symbolName) {
		if(! symbolsInitialization.containsKey(symbolName))
			if(parent != null)
				return parent.verifyInitialization(symbolName);
			else
				return false;
		else
			return symbolsInitialization.get(symbolName).getInitialized();
	}
	
	public void initializeSymbol(String symbolName) {
		if(! symbolsInitialization.containsKey(symbolName))
			if(parent != null)
				parent.initializeSymbol(symbolName);
			
		else
			symbolsInitialization.get(symbolName).setInitialized(true);
	}
}
