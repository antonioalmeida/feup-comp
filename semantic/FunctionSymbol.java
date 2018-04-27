package semantic;

import java.util.Vector;
import utils.Pair;

public class FunctionSymbol extends Symbol{
	private Vector<Pair> parameters;
	private String returnVariable;
	
	public FunctionSymbol(Type type, boolean initialized, boolean print) {
		super(type, initialized, print);
		parameters = new Vector<Pair>();
		returnVariable = "";
	}
	
	public FunctionSymbol(Type type, boolean initialized, boolean print, String returnVariable) {
		super(type, initialized, print);
		parameters = new Vector<Pair>();
		this.returnVariable = returnVariable;
	}
	
	public void addParameter(String name, Symbol.Type type) {
		parameters.add(new Pair(name, type));
	}
}
