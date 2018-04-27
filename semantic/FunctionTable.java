package semantic;

import java.util.HashMap;
import java.util.Vector;

import utils.Pair;

public class FunctionTable {
	
	protected HashMap<FunctionSignature, FunctionSymbol> functions;
	
	public FunctionTable() {
		functions = new HashMap<FunctionSignature, FunctionSymbol>();
	}
	
	public Symbol.Type getFunctionReturnType(String name, Vector<Symbol.Type> arguments) {
		if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.SCALAR)))
			return Symbol.Type.SCALAR;
		else if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.ARRAY)))
			return Symbol.Type.ARRAY;
		else if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.VOID)))
			return Symbol.Type.VOID;
		else 
			return Symbol.Type.UNDEFINED;
	}
	public boolean initializeFunction(String name, Vector<Symbol.Type> argumentTypes, 
			Vector<Pair> parameters, Symbol.Type returnType, String returnValue) {
		if(getFunctionReturnType(name, argumentTypes).equals(returnType))
			return false;
		else {
			
			
			functions.put(new FunctionSignature(name, argumentTypes, returnType), 
					new FunctionSymbol(returnType, returnValue, parameters));
			return true;
		}
			
	}
	
	/*public boolean addParameter(String functionName, String parameter, Symbol.Type parameterType) {
		if(! containsSymbolName(functionName))
			return false;
		return true;
	}*/
	public void printFunctions() {
		
	}
	
	
	
}
