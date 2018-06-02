/* Generated By:JJTree: Do not edit this line. ASTCall.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import java.util.Vector;

import semantic.Symbol;
import semantic.Symbol.Type;
public
class ASTCall extends SimpleNode {
	protected String functionName = null; //changed type to functionName
	private Vector<Symbol.Type> types;

	public ASTCall(int id) {
		super(id);
		types = new Vector<Symbol.Type>();
	}

	public ASTCall(Yal p, int id) {
		super(p, id);
		types = new Vector<Symbol.Type>();
	}
	
	public Vector<Symbol.Type> getReturnTypes() {
		return types;
	}
	/*public void setFunctionName(String functionName){
		this.functionName = functionName;
	}*/
	
	public boolean analyseSymbolTable() {
		Vector<Symbol.Type> argumentTypes = new Vector<Symbol.Type>();
		boolean result = true;
		if(getChildren() != null && getChildren().length > 0) {
			for(Node child : getChildren()) {
				Symbol.Type type = ((SimpleNode) child).getReturnType();
				if(type.equals(Symbol.Type.SCALAR) || type.equals(Symbol.Type.ARRAY) ) {
					argumentTypes.add(type);
				}
				else
					result = false;
			}
		}
		
		if(result == true && ! getValue().contains(".")) {
			Vector<Type> functionsTypes = functionTable.getFunctionReturnType(getValue(), argumentTypes);
			this.types = functionsTypes;
			if(functionsTypes.size() == 0) {
				System.out.println("Semantic Error: Function "+getValue()+" with parameter types "+argumentTypes +"  has not been defined");
				printSemanticError("Function "+getValue()+" with parameter types "+argumentTypes +"  has not been defined");
				return false;
			}
		}
		
		return true;
	}
	
	public String toString(String prefix) { 
		String node = prefix + toString();

		if (this.functionName != null)
			node += " [" + this.getValue() + "." + this.functionName + "]";
		else 
			node += " [" + this.getValue() + "]";
					
		return node; 
	}

}
/* JavaCC - OriginalChecksum=98507d8d70d83f4b72fdf45214a73416 (do not edit this line) */
