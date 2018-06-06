package semantic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import semantic.Symbol.Type;

import java.util.Vector;

import utils.IntegerReference;
import utils.Pair;

public class SymbolTable {
    
   
    private SymbolTable parent;
    protected HashMap<String, Symbol> symbols;

    private int indexCount;
    private HashMap<String, Integer> nameToIndex;
    private IntegerReference maxIndex;
    private HashMap<Integer, Pair> indexToName;

    

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        symbols = new HashMap<String, Symbol>();
        this.indexCount = 0;
        if(parent == null) {
        	nameToIndex = null;
        	maxIndex = null;
        	indexToName = null;
        }
        else {
        	nameToIndex = parent.nameToIndex;
        	maxIndex = parent.maxIndex;
        	indexToName = parent.indexToName;
        }
    }
    
    public SymbolTable(SymbolTable parent, boolean newNameToIndex) {
    	this.parent = parent;
        symbols = new HashMap<String, Symbol>();
        this.indexCount = 0;
    	if(newNameToIndex) {
    		nameToIndex = new HashMap<String, Integer>();
    		maxIndex = new IntegerReference(-1);
    		indexToName = new HashMap<Integer, Pair>();
    	}
    	else
    		if(parent == null) {
            	nameToIndex = null;
            	maxIndex = null;
            	indexToName = null;
            }
            else {
            	nameToIndex = parent.nameToIndex;
            	maxIndex = parent.maxIndex;
            	indexToName = parent.indexToName;
            }
    }
    
    public HashMap<String, Symbol> getSymbols() {
		return symbols;
	}

    public int getIndexCount() {
        return indexCount;
    }

    public void incIndexCount() {
        indexCount++;
    }

	public HashMap<String, Integer> getNameToIndex() {
		return nameToIndex;
	}
	
	public int getMaxIndex() {
		if(maxIndex != null)
			return maxIndex.getValue();
		else
			return -1;
	}
	
	public HashMap<Integer, Pair> getIndexToName(){
		return indexToName;
	}
	
	public Symbol getSymbolFromName(String symbolName) {
		if (containsSymbolName(symbolName))
			return symbols.get(symbolName);
		return null;
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

    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print, boolean verify, int index) {
        if(verify && ! verifySymbolTypes(symbolName, false, false, type))
            return false;
        else if(!verifySymbolTypes(symbolName, false, true, type) || containsSymbolName(symbolName)) {
            this.addSymbol(symbolName, type, initialized, print, index);
            this.nameToIndex.put(symbolName, index);
            this.indexToName.put(index, new Pair(symbolName, symbols.get(symbolName)));
            if(this.maxIndex.getValue() < index)
            	this.maxIndex.setValue(index);
            return true;
        }
        else
            return parent.initializeSymbol(symbolName, type, initialized, print);
    }

    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print, boolean verify) {
        if(verify && ! verifySymbolTypes(symbolName, false, false, type))
            return false;
        else if(!verifySymbolTypes(symbolName, false, true, type) || containsSymbolName(symbolName)) {
            this.addSymbol(symbolName, type, initialized, print, -1);
            return true;
        }
        else
            return parent.initializeSymbol(symbolName, type, initialized, print);
    }
      
    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print) {
        return initializeSymbol(symbolName, type, initialized, print, true);
    }

    public int attributeIndexes(int lastIndex) {
        int currentIndex = lastIndex;

        Iterator<Entry<String, Symbol>> symbolsIt = symbols.entrySet().iterator();
        while(symbolsIt.hasNext()) {
            Map.Entry<String, Symbol> pair = (Entry<String, Symbol>) symbolsIt.next();

            Symbol symbol = pair.getValue();
            String name = pair.getKey();
           
            if(symbol.getIndex() == -1) {
            	Integer index = this.nameToIndex.get(name);
            	if(index == null) {
            		symbol.setIndex(++currentIndex);
            		nameToIndex.put(name, currentIndex);
            		indexToName.put(currentIndex, new Pair(name, symbols.get(name)));
            		 if(this.maxIndex.getValue() < currentIndex)
                     	this.maxIndex.setValue(currentIndex);
            	}
            	else {
            		symbol.setIndex(index);
            	}
            }
        }

        return currentIndex;
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

    public int getSymbolIndex(String symbolName) {
        if(containsSymbolName(symbolName))
            return symbols.get(symbolName).getIndex();
        else {
            if(parent != null)
                return parent.getSymbolIndex(symbolName);
            else
                return -1;
        }
    }
    
    public void printSymbols(String prefix) {       

        Iterator<Entry<String, Symbol>> symbolsIt = symbols.entrySet().iterator();
        while(symbolsIt.hasNext()) {
            Map.Entry<String, Symbol> pair = (Entry<String, Symbol>) symbolsIt.next();

            String symbolName = pair.getKey();
            Symbol symbol = pair.getValue();
            
            if(symbol.getPrint())
                System.out.println(prefix+symbolName+": "+symbol.getType() + " - " + symbol.getIndex());
        }
    }

	
	
    
}
