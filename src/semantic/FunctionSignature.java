package semantic;

import java.util.Vector;

public class FunctionSignature {
	
	private String name;
	private Vector<Symbol.Type> argumentTypes;
	private Symbol.Type returnType;
	
	public FunctionSignature(String name){
		this.name = name;
	}
	
	public FunctionSignature(String name, Vector<Symbol.Type> argumentTypes, Symbol.Type returnType) {
		this.name = name;
		this.argumentTypes = argumentTypes;
		this.returnType = returnType;
	}
	
	public void addArgumentType(Symbol.Type type) {
		argumentTypes.add(type);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector<Symbol.Type> getArgumentTypes(){
		return argumentTypes;
	}
	
	@Override
	public boolean equals(Object fs) {
		FunctionSignature fsCast = (FunctionSignature) fs;
		return name.equals(fsCast.name) && argumentTypes.equals(fsCast.argumentTypes) && returnType.equals(fsCast.returnType);
	}
	
	@Override
	public int hashCode() {
		return 31 * name.hashCode() + 31*31*argumentTypes.hashCode() + returnType.hashCode();
	}

}
