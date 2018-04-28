package semantic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import utils.Pair;

public class FunctionTable {
	
	protected HashMap<FunctionSignature, FunctionSymbol> functions;
	
	public FunctionTable() {
		functions = new HashMap<FunctionSignature, FunctionSymbol>();
	}
	
	public Vector<Symbol.Type> getFunctionReturnType(String name, Vector<Symbol.Type> arguments) {
		Vector<Symbol.Type> returnVector = new Vector<Symbol.Type>();
		if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.SCALAR)))
			returnVector.add(Symbol.Type.SCALAR);
		if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.ARRAY)))
			returnVector.add(Symbol.Type.ARRAY);
		if(functions.containsKey(new FunctionSignature(name, arguments, Symbol.Type.VOID)))
			returnVector.add(Symbol.Type.VOID);
		
		
		return returnVector;
	}
	public boolean initializeFunction(String name, Vector<Symbol.Type> argumentTypes, 
			Vector<Pair> parameters, Symbol.Type returnType, String returnValue) {
		
		if(getFunctionReturnType(name, argumentTypes).contains(returnType))
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
	public void printFunctions(String prefix) {

		Iterator<Entry<FunctionSignature, FunctionSymbol>> functionsIt = functions.entrySet().iterator();
		while(functionsIt.hasNext()) {
			Map.Entry<FunctionSignature, FunctionSymbol> pair = (Entry<FunctionSignature, FunctionSymbol>) functionsIt.next();

			FunctionSignature signature = pair.getKey();
			FunctionSymbol fSymbol = pair.getValue();
			
			System.out.println(prefix+"@function["+signature.getName()+"]");
			fSymbol.printSymbol(prefix+"   ");
		}
	}
	
	
	
}
