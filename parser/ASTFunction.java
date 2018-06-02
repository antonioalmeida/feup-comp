/* Generated By:JJTree: Do not edit this line. ASTFunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import java.util.Vector;

import semantic.Symbol;
import semantic.Symbol.Type;
import semantic.SymbolTable;
import utils.Pair;
public
class ASTFunction extends SimpleNode {
	private String returnValue;
	private Symbol.Type returnType;
	public ASTFunction(int id) {
		super(id, true, false);
		returnValue = "";
		returnType = Symbol.Type.VOID;
	}

	public ASTFunction(Yal p, int id) {
		super(p, id, true, false);
		returnValue = "";
		returnType = Symbol.Type.VOID;
	}
	
	//TODO: See what this means
	@SuppressWarnings("unchecked")
	public boolean addFunctionEntry() {
		boolean ret = true;
		symbolTable = getAssignedSymbolTable();
        functionTable = getAssignedFunctionTable();
        String functionName = "";
        String returnValue = "";
        Symbol.Type returnType = Symbol.Type.VOID;
        Vector<Symbol.Type> argumentTypes = new Vector<Symbol.Type>();
        Vector<Pair> parameters = new Vector<Pair>();
       
        if(getChildren() != null && getChildren().length > 1 && 
        		getChildren()[1].toString().equals("FunctionAssign")) {
        	returnValue = ((SimpleNode) getChildren()[0]).getValue();
        	returnType = ((SimpleNode) getChildren()[0]).getReturnType();
        	functionName = ((SimpleNode) getChildren()[1]).getValue();
        	if(getChildren().length > 2 && getChildren()[2].toString().equals("Varlist")) {
        		Pair pair = ((ASTVarlist) getChildren()[2]).getArguments();
        		argumentTypes = (Vector<Type>) pair.getValue();
        		parameters = (Vector<Pair>) pair.getKey();
        	}
        	
        }
        else {
        	functionName = getValue();
        	if(getChildren() != null && getChildren().length > 0
        			&& getChildren()[0].toString().equals("Varlist")) {
        		Pair pair = ((ASTVarlist) getChildren()[0]).getArguments();
        		argumentTypes = (Vector<Type>) pair.getValue();
        		parameters = (Vector<Pair>) pair.getKey(); 
        	}
        }
        
        this.returnValue = returnValue;
        this.returnType = returnType;
        if(! returnValue.equals(""))
        	if(!verifySymbolTypes((String) returnValue, false, returnType)) {
        		//System.out.println("Semantic Error: Return Value "+returnValue+" has already been declared.");
        		printSemanticError("Return Value "+returnValue+" has previously been declared with a type different from "+returnType);
        		ret = false;
        	}
        	else if(! initializeSymbol((String) returnValue, (Symbol.Type) returnType, false, true, false)){
        		//System.out.println("Semantic Error: Could not initialize "+returnValue+" .");
        		printSemanticError("Could not initialize "+returnValue);
        		ret = false;
        	}
        for(Pair pair : parameters) {
        	if(pair.getKey().equals(returnValue)) {
        		//System.out.println("Semantic Error: Argument "+pair.getKey()+" is being has both a return value and a parameter.");
        		printSemanticError("Argument "+pair.getKey()+" is being has both a return value and a parameter");
        		ret = false;
        	}
        	else if(symbolTable.verifySymbolTypes((String) pair.getKey(), false, true, Symbol.Type.ARRAY, Symbol.Type.SCALAR)) {
        		//System.out.println("Semantic Error: Argument "+pair.getKey()+" has already been declared.");
        		printSemanticError("Argument "+pair.getKey()+" has already been declared");
        		ret = false;
        	}
        	else if(! initializeSymbol((String) pair.getKey(), (Symbol.Type) pair.getValue(), true, false)){
        		//System.out.println("Semantic Error: Could not initialize "+pair.getKey()+" .");
        		printSemanticError("Could not initialize "+pair.getKey());
        		ret = false;
        	}
        }
        if(! functionTable.initializeFunction(functionName, argumentTypes, parameters, returnType, returnValue)) {
        	//System.out.println("Semantic Error: Function "+functionName+ " with argument types "+argumentTypes+" and return Type "+returnType +" has already been defined");
        	printSemanticError("Function "+functionName+ " with argument types "+argumentTypes+" and return Type "+returnType +" has already been defined");
        	ret = false;;
        }
		
		
		return ret;

	  	
	}
	
	
	public boolean analyseSymbolTable() {
		
		/*if(!returnValue.equals(""))
			if(!verifySymbolTypes(returnValue, true, returnType)) {
				//System.out.println("Semantic Error: In function "+getRealValue()+", return value "+returnValue+" should have been initialized");
				printSemanticError("In function "+getRealValue()+", return value "+returnValue+" should have been initialized");
			}
		*/
		return true;
	}
	
	public boolean analyse() {
		boolean result = true;
	    
        if(getChildren() != null) {
            for(Node child : getChildren()) {
                if(!((SimpleNode) child).analyse())
                    result = false;
            }
        }
        
        if(!analyseSymbolTable())
            result = false;
       

        return result;
	}
	
	public String getRealValue() {

        if(getChildren() != null && getChildren().length > 1 && 
        		getChildren()[1].toString().equals("FunctionAssign"))
        	return ((SimpleNode) getChildren()[1]).getValue();
        else 
        	return getValue();
	}
	
	public boolean isMainFunction() {
		return getValue().equals("main");
	}
	
	public boolean isAssignFunction() {
		return this.jjtGetNumChildren() >= 2
				&& ((SimpleNode) this.jjtGetChild(1)).id == YalTreeConstants.JJTFUNCTIONASSIGN;
	}
	
	public boolean hasVarList() {
		
			for (int i = 0; i < jjtGetNumChildren(); i++){
				SimpleNode functionChild = (SimpleNode) jjtGetChild(i);
				if (functionChild.id == YalTreeConstants.JJTVARLIST)
					return true;

			}
			return false;
	}
	
	public SimpleNode getVarList() {		
		for (int i = 0; i < jjtGetNumChildren(); i++){
			SimpleNode functionChild = (SimpleNode) jjtGetChild(i);
			if (functionChild.id == YalTreeConstants.JJTVARLIST)
				return functionChild;
		}
		return null;
}

	public Type getFuncReturnType() {		
		if(isAssignFunction()){
			SimpleNode nodeReturn = (SimpleNode) jjtGetChild(0);
			if(nodeReturn.id==YalTreeConstants.JJTSCALARELEMENT)
				return Symbol.Type.SCALAR;
			else return Symbol.Type.ARRAY;
		}
		return  Symbol.Type.VOID;
	}

	public SymbolTable getAssignedSymbolTable() {
		return new SymbolTable(((SimpleNode) parent).getSymbolTable(), true);
	}
	
	public String getVarNameToReturn(){
		SimpleNode element = (SimpleNode) jjtGetChild(0);
		
		return element.getValue();
		
	}
	
	public String getFuncName() {
		String funcName;
		
		if (this.jjtGetNumChildren() >= 2 && ((SimpleNode) jjtGetChild(1)).id == YalTreeConstants.JJTFUNCTIONASSIGN)
			funcName =  ((SimpleNode) jjtGetChild(1)).getValue();

		else
			funcName = this.getValue();
		
		return funcName;

	}
	
	public String getFunction() {
		return getRealValue();
	}

}
/* JavaCC - OriginalChecksum=ad69cb22ee7e36d1a135f50fb354570c (do not edit this line) */
