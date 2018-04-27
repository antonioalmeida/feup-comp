package semantic;

import java.util.Arrays;
import java.util.HashMap;

public class Table {
	protected HashMap<String, Symbol> symbols;
	
	public boolean containsSymbolName(String symbolName) {
		return symbols.containsKey(symbolName);
	}
	
	public boolean containsSymbol(String symbolName, boolean checkInitialized, Symbol.Type... types) {
		Symbol symbol = symbols.get(symbolName);
		
		if(symbol != null) {
			Symbol.Type symbolType = symbol.getType();

			if(Arrays.asList(types).contains(symbolType)) {
				if(checkInitialized)
					if(!symbols.get(symbolName).getInitialized())
						return false;
			
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addSymbol(String symbolName, Symbol.Type type, boolean initialized) {
		Symbol symbol = new Symbol(type, initialized);

		if(!symbols.containsKey(symbolName)) {			
			symbols.put(symbolName, symbol);
			//symbolsVector.add(new Pair(symbolName, type));
			return true;
		}
		else if(symbols.get(symbolName).getType().equals(type)) {
			symbols.put(symbolName, symbol);
			return true;
		}
		else
			return false;
	}
}
