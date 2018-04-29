package semantic;

import java.util.Vector;
import utils.Pair;

public class FunctionSymbol extends Symbol{
	private Vector<Pair> parameters;
	private String returnVariable;
	
	
	
	
	public FunctionSymbol(Type type, String returnVariable, Vector<Pair> parameters) {
		super(type, true, true);
		if(type.equals(Symbol.Type.VOID) || type.equals(Symbol.Type.UNDEFINED))
			initialized = false;
		
		this.parameters = parameters;
		this.returnVariable = returnVariable;
	}
	
	
	
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
		if(parameters.size() > 0)
			System.out.println(prefix+"@params:");
		for(Pair p : parameters)
				System.out.println(prefix+"   "+p.getKey()+": "+p.getValue());
		
		if(returnVariable.equals(""))
			System.out.println(prefix+"@return: "+type);
		else
			System.out.println(prefix+"@return["+returnVariable+"]: "+type);
		
	}
}
