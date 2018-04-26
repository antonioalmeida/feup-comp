/* Generated By:JJTree: Do not edit this line. ASTScalarAccess.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTScalarAccess extends SimpleNode {
  protected boolean size_array = false;
	
	
  public ASTScalarAccess(int id) {
    super(id);
  }

  public ASTScalarAccess(Yal p, int id) {
    super(p, id);
  }

  public void set_size_access(boolean set){
  	size_array = set;
  }
  
  public String toString(String prefix) { 
  	String node = prefix + toString();
	
	if (this.value != null){
		if (this.size_array == true)
			node += " [" + "sizeof(" + this.value + ")]";
		else node += " [" + this.value + "]";
	}
    	 	
  	return node; 
  }

  public boolean analyseSymbolTable() {
    //TODO: update this, this is just for testing
    System.out.println("Analysing " + toString(""));

    if(size_array == true) {    	
    	if(!symbolTable.containsSymbol(this.value, false, Symbol.Type.ARRAY)) {
    		System.out.println("Semantic Error: "+this.value+" has not been previously declared as an array");
    		return false;
    	}
    }
    
    return true;
  }
  
  public boolean getSizeArray() {
	  return size_array;
  }

  public Symbol.Type getReturnType() {
    String symbolName = ((String) jjtGetValue());

    // if is in symbol table, return its type
    //if(symbolTable.verifySymbolTypes(symbolName, false, Symbol.Type.SCALAR, Symbol.Type.ARRAY, Symbol.Type.VOID))
    Symbol.Type type = symbolTable.getSymbolType(symbolName);
    if(size_array == true) {
    	if(!type.equals(Symbol.Type.VOID))
    		return Symbol.Type.SCALAR;
    }
  
    return symbolTable.getSymbolType(symbolName);

    // else, it can be either one
    //return Symbol.Type.VOID;
  }

}
/* JavaCC - OriginalChecksum=dc46aab5f6a610de55713f684578a33a (do not edit this line) */
