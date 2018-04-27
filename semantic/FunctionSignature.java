package semantic;

import java.util.Vector;

public class FunctionSignature {
	
	private String name;
	private Vector<Symbol.Type> argumentTypes;
	
	public FunctionSignature(String name){
		this.name = name;
	}

}
