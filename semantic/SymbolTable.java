package semantic;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import semantic.Symbol.Type;

import java.util.Vector;

import utils.Pair;

public class SymbolTable {
	
	//private HashMap<String, Symbol> symbols;
	//private Vector<Pair > symbolsVector;
	private SymbolTable parent;
	protected HashMap<String, Symbol> symbols;

	private int indexCount;

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
		symbols = new HashMap<String, Symbol>();
		indexCount = 0;
		//symbolsVector = new Vector<Pair>();
	}
	
	public SymbolTable getParent() {
		return parent;
	}
	

	
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

	public boolean addSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print, int index) {
		Symbol symbol = new Symbol(type, initialized, print, index);

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
	
	/**
	* If checkInitialized equals true, this function checks if a variable symbolName has been initialized to one of types
	* If checkInitialized equals false, this function checks if a variable symbolName has not been declared to any type different from types
	*/
	public boolean verifySymbolTypes(String symbolName, boolean checkInitialized, boolean checkDeclared,  Symbol.Type... types ) {

		if(this.containsSymbolName(symbolName))
			return this.containsSymbol(symbolName, checkInitialized, types);

		else {
			if(parent != null)
				return parent.verifySymbolTypes(symbolName, checkInitialized, checkDeclared, types);
			else
				return !checkDeclared;
		}
	}

	public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print, boolean verify, boolean useIndex) {
		if(verify && ! verifySymbolTypes(symbolName, false, false, type))
			return false;
		else if(!verifySymbolTypes(symbolName, false, true, type) || containsSymbolName(symbolName)) {
			this.addSymbol(symbolName, type, initialized, print, this.indexCount);
			if(useIndex) {
				
				this.indexCount++;
			}
			else
				this.addSymbol(symbolName, type, initialized, print, -1);
			return true;
		}
		else
			return parent.initializeSymbol(symbolName, type, initialized, false);
	}
	  
	public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print) {
		return initializeSymbol(symbolName, type, initialized, print, true, false);
	}

	public Symbol.Type getSymbolType(String symbolName) {
		if(containsSymbolName(symbolName))
			return symbols.get(symbolName).getType();

		else {
			if(parent != null)
				return parent.getSymbolType(symbolName);
			else
				return Symbol.Type.VOID;
		}
	}
	
	/*public Vector<Pair> getVectorSymbols(){
		return symbolsVector;
	}*/
	
	public void printSymbols(String prefix) {		

		Iterator<Entry<String, Symbol>> symbolsIt = symbols.entrySet().iterator();
		while(symbolsIt.hasNext()) {
			Map.Entry<String, Symbol> pair = (Entry<String, Symbol>) symbolsIt.next();

			String symbolName = pair.getKey();
			Symbol symbol = pair.getValue();
			
			if(symbol.getPrint())
				System.out.println(prefix+symbolName+": "+symbol.getType() + " " + symbol.getIndex());
		}
		/*for(Pair pair : symbolsVector) {
			String symbolName = (String) pair.getKey();
			Symbol.Type type = (Symbol.Type) pair.getValue();
			
			System.out.println(prefix+symbolName+": "+type);
		}*/
	}
	
}
