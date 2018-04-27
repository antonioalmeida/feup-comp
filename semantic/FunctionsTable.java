package semantic;

import java.util.HashMap;

public class FunctionsTable extends Table {
	
	public boolean initializeFunction(String functionName, Symbol.Type type, String returnVariable) {
		if(containsSymbolName(functionName))
			return false;
		else {
			symbols.put(functionName, new FunctionSymbol(type, false, true, returnVariable));
			return true;
		}
			
	}
	
	public boolean addParameter(String functionName, String parameter, Symbol.Type parameterType) {
		if(! containsSymbolName(functionName))
			return false;
		return true;
	}
	
	
	
}
