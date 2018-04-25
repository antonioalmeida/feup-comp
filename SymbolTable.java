import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;



public class SymbolTable {
	
	/*public static enum Type {
		INT, ARRAY 
	}*/
	
	private HashMap<String, Symbol> symbols;
	private SymbolTable parent;

	

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}
	
	public SymbolTable getParent() {
		return parent;
	}
	
	
	/*public HashMap<String, Type> getSymbols() {
		return symbols;
	}*/
	
	public boolean containsSymbolName(String symbolName) {
		return symbols.containsKey(symbolName);
	}
	
	public boolean containsSymbol(String symbolName, boolean checkInitialized, Symbol.Type... types) {
		//if(symbols.containsKey(symbolName))
		Symbol symbol = symbols.get(symbolName);
		
		
		if(symbol != null) {
			Symbol.Type symbolType = symbol.getType();
			if(Arrays.asList(types).contains(symbolType))
				if(checkInitialized == true)
					if(symbols.get(symbolName).getInitialized() == false)
						return false;
			
				return true;
		}
		
		return false;
		
	}
	
	public boolean addSymbol(String symbolName, Symbol.Type type, boolean initialized) {
		Symbol symbol = new Symbol(type, initialized);
		if(! symbols.containsKey(symbolName)) {			
			symbols.put(symbolName, symbol);
			return true;
		}
		else if(symbols.get(symbolName).getType().equals(type)) {
			symbols.put(symbolName, symbol);
			return true;
		}
		else
			return false;
		
	}
	
	 /**
	   * If checkInitialized equals true, this function checks if a variable symbolName has been initialized to one of types
	   * If checkInitialized equals false, this function checks if a variable symbolName has not been initialized to any type different from types
	   */
	  public boolean verifySymbolTypes(String symbolName, boolean checkInitialized, Symbol.Type... types ) {
			if(this.containsSymbolName(symbolName))
				return this.containsSymbol(symbolName, checkInitialized, types);
			
			else {
				if(parent != null)
					return parent.verifySymbolTypes(symbolName, checkInitialized, types);
				else
					return !checkInitialized;
			}
	  }
	  

	  public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized) {
		   if(! verifySymbolTypes(symbolName, false, type))
				   return false;
			
		    /*if(hasScope) {
		    	symbolTable.addSymbol(symbolName, type, initialized);
		    	return true;
		    }	
		    else if(parent != null)
					return parent.initializeSymbol(symbolName, type, initialized, false);
		    else
		    	return false;*/
		   this.addSymbol(symbolName, type, initialized);
		   return true;
	    }
	
	public void printSymbols(String prefix) {
		/*Iterator<Entry<String, Type>> symbolsIt = symbols.entrySet().iterator();
		while(symbolsIt.hasNext()) {
			Map.Entry<String, Type> pair = (Entry<String, Type>) symbolsIt.next();
			String symbolName = pair.getKey();
			Type type = pair.getValue();
			System.out.print(prefix+symbolName+": "+type);
		}*/
	}
	
	
	
}
