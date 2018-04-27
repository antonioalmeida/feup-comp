package semantic;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import semantic.Symbol.Type;

import java.util.Vector;

import utils.Pair;

public class SymbolTable extends Table {
	
	//private HashMap<String, Symbol> symbols;
	//private Vector<Pair > symbolsVector;
	private SymbolTable parent;

	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
		symbols = new HashMap<String, Symbol>();
		//symbolsVector = new Vector<Pair>();
	}
	
	public SymbolTable getParent() {
		return parent;
	}
	
	
	
	/**
	* If checkInitialized equals true, this function checks if a variable symbolName has been initialized to one of types
	* If checkInitialized equals false, this function checks if a variable symbolName has not been declared to any type different from types
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
		System.out.println("Print Symbols");

		Iterator<Entry<String, Symbol>> symbolsIt = symbols.entrySet().iterator();
		while(symbolsIt.hasNext()) {
			Map.Entry<String, Symbol> pair = (Entry<String, Symbol>) symbolsIt.next();

			String symbolName = pair.getKey();
			Symbol symbol = pair.getValue();
			
			if(symbol.getPrint())
				System.out.println(prefix+symbolName+": "+symbol.getType());
		}
		/*for(Pair pair : symbolsVector) {
			String symbolName = (String) pair.getKey();
			Symbol.Type type = (Symbol.Type) pair.getValue();
			
			System.out.println(prefix+symbolName+": "+type);
		}*/
	}
	
}
