/* Generated By:JJTree: Do not edit this line. ASTDeclaration.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import semantic.Symbol;

public
class ASTDeclaration extends SimpleNode {
    public ASTDeclaration(int id) {
        super(id);
    }

    public ASTDeclaration(Yal p, int id) {
        super(p, id);
    }

    public boolean analyseSymbolTable() {
        
        if(getChildren() == null)
            return false;

        SimpleNode leftChild = (SimpleNode) getChildren()[0];
        Symbol.Type typeLeftChild = leftChild.getReturnType();
        String symbolName = (String) leftChild.jjtGetValue();
        
        Symbol.Type type = typeLeftChild;
        boolean initialized = false;

        // This boolean will be set to 
        // true if declaration is of the 
        // form LHS = RHS;
        boolean scalarInitialization = false; 
       	
        boolean alreadyChecked = false;
        if(getChildren().length > 1) {
        	initialized = true;        	
        	SimpleNode rightChild = (SimpleNode) getChildren()[1];
            Symbol.Type typeRightChild = rightChild.getReturnType();
        	if(! typeLeftChild.equals(typeRightChild)) {
        		type = Symbol.Type.ARRAY;
        	}
        	else if(typeLeftChild.equals(Symbol.Type.SCALAR))
        		scalarInitialization = true;
        	
        	 if(typeLeftChild.equals(Symbol.Type.ARRAY) && typeRightChild.equals(Symbol.Type.SCALAR)) {
        		alreadyChecked = true;
        		if(! verifySymbolTypes(symbolName, true, Symbol.Type.ARRAY)){ //scalarInitialization is always valid
            		 	//System.out.println("Semantic Error: Can't initialize array " + symbolName+" as its size should have been declared before.");
            		 	printSemanticError("Can't initialize array " + symbolName+" as its size should have been declared before");
        				return false;
                }
             	/*else {
             		initializeSymbol(symbolName, Symbol.Type.ARRAY, true);
            			return true;
             	}*/
             	
             }
        }
        	
        
        if(alreadyChecked == false && !initializeSymbol(symbolName, type, initialized)) {
            if(!scalarInitialization) {
               //System.out.println("Semantic Error: Can't initialize " + symbolName);
               printSemanticError("Can't initialize " + symbolName);
               return false;
            }
        	else if(! verifySymbolTypes(symbolName, true, Symbol.Type.ARRAY)){ //scalarInitialization is always valid
        		 //System.out.println("Semantic Error: Can't initialize array " + symbolName+" as its size should have been declared before.");
                 printSemanticError("Can't initialize array " + symbolName+" as its size should have been declared before");
        		 return false;
            }
        	
        	/*else {
        		initializeSymbol(symbolName, Symbol.Type.ARRAY, true);
                return true;
        	}*/
        }

       return true;
    }

    public Symbol.Type getReturnType() {
    	return Symbol.Type.VOID;
    }
   	
   	public boolean isVarScalarAssigned(){
   		return jjtGetNumChildren() == 2 && ((SimpleNode) this.jjtGetChild(1)).id ==YalTreeConstants.JJTSCALARASSIGNED;
   	}
   	
   	
	public boolean isVarArray() {
		return ((SimpleNode) this.jjtGetChild(0)).id == YalTreeConstants.JJTARRAYELEMENT || (jjtGetNumChildren() == 2
				&& ((SimpleNode) this.jjtGetChild(1)).id == YalTreeConstants.JJTARRAYASSIGNED);
	}

	public boolean isVarArrayInitialized() {
		return (jjtGetNumChildren() == 2 && ((SimpleNode) this.jjtGetChild(1)).id == YalTreeConstants.JJTARRAYASSIGNED);
	}

}
/* JavaCC - OriginalChecksum=f4f56adaeb8307fa09a008497343528d (do not edit this line) */
