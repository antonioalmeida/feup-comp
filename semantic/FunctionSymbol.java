package semantic;

import java.util.Vector;
import utils.Pair;

public class FunctionSymbol extends Symbol{
	private Vector<Pair> parameters;
	private String returnVariable;
	
	
	
	/*public FunctionSymbol(Type type, boolean initialized, boolean print, 
			String returnVariable) {
		super(type, initialized, print);
		parameters = new Vector<Pair>();
		this.returnVariable = returnVariable;
	}*/
	
	public FunctionSymbol(Type type, String returnVariable, Vector<Pair> parameters) {
		super(type, true, true);
		if(type.equals(Symbol.Type.VOID) || type.equals(Symbol.Type.UNDEFINED))
			initialized = false;
		
		this.parameters = parameters;
		this.returnVariable = returnVariable;
	}
	
	/*public void addParameter(String name, Symbol.Type type) {
		parameters.add(new Pair(name, type));
	}*/
	
	@Override
	public boolean equals(Object symbol) {
		FunctionSymbol symbolCast = (FunctionSymbol) symbol;
		
		if(this.type.equals(symbolCast.getType()) && this.initialized == symbolCast.getInitialized()
				&& this.print == symbolCast.getPrint() && this.parameters.equals(symbolCast.parameters)
				&& this.returnVariable.equals(symbolCast.returnVariable))
			return true;
		else
			return false;
	}
	
	public void printSymbol(String prefix) {
		System.out.println(prefix+"@params");
		
	}
}
