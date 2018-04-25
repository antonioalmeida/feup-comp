/* Generated By:JJTree: Do not edit this line. ASTDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTDeclaration extends SimpleNode {
    public ASTDeclaration(int id) {
        super(id);
    }

    public ASTDeclaration(Yal p, int id) {
        super(p, id);
    }

    public boolean analyseSymbolTable() {
        System.out.println("Analysing " + toString(""));
        if(children == null)
            return false;

        // Declaration -> Element
        SimpleNode leftChild = (SimpleNode) children[0];
        Symbol.Type typeLeftChild = leftChild.getType();
        String symbolName = (String) leftChild.jjtGetValue();
        
        Symbol.Type type = typeLeftChild;
        //boolean noErrorsFound = true;
        boolean initialized = false;
        if(children.length > 1) {
        	initialized = true;
        	SimpleNode rightChild = (SimpleNode) children[1];
        	Symbol.Type typeRightChild = rightChild.getType();
        	
        	if(! typeLeftChild.equals(typeRightChild)) {
        		type = Symbol.Type.ARRAY;
        	}
        	/*if(! typeLeftChild.equals(typeRightChild)) {
        		noErrorsFound = false;
        		if(typeLeftChild.equals(Symbol.Type.ARRAY))
        			System.out.println("Semantic Error: An array can't be assigned to a scalar");
        		else if(verifySymbolTypes(symbolName, false, SymbolType.ARR))
        			System.out.println("Semantic Error: A Scalar can't be assigned to an array");
        	}*/
        	
        }
        	
        
        	
        //if(noErrorsFound) {
        	if(initializeSymbol( symbolName, type, initialized) == false){
        		System.out.println("Semantic Error: Can't initialize "+symbolName);
        		return false;
        	}
       // }
        else return true;

    }
    
    
   	public String generateCode() {
   		String generatedCode = "";
   		generatedCode += ".field static " + this.value + " I" + "\n";
   		

   		if (children != null) {
   			for (int i = 0; i < children.length; ++i) {
   				SimpleNode n = (SimpleNode) children[i];
   				if (n != null) {
   					generatedCode += n.generateCode();
   				}
   			}
   		}
   		

   		return generatedCode;

   	}


}
/* JavaCC - OriginalChecksum=f0ec2df2bb99c0df69ade4aa7071b5f6 (do not edit this line) */
