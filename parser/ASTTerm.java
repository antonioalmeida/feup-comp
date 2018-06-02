/* Generated By:JJTree: Do not edit this line. ASTTerm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import java.util.Vector;
import semantic.Symbol;
public
class ASTTerm extends SimpleNode {
    protected String sign = null;

    public ASTTerm(int id) {
        super(id);
    }

    public ASTTerm(Yal p, int id) {
        super(p, id);
    }

    public Symbol.Type getReturnType() {
        // Term -> <INTEGER> | Call | ArrayAccess | ScalarAccess

        // children is null when Term -> <INTEGER>
        if(getChildren() == null) 
            return Symbol.Type.SCALAR;

        return ((SimpleNode) getChildren()[0]).getReturnType();
    }
    
    public Vector<Symbol.Type> getReturnTypes(){
    	
    	if(getChildren() != null && getChildren().length > 0)
    		if(((SimpleNode) getChildren()[0]).toString().equals("Call"))
    			return ((ASTCall) getChildren()[0]).getReturnTypes(); 
    	
    	Vector<Symbol.Type> returnTypes =new Vector<Symbol.Type>();
    	returnTypes.add(getReturnType());
    	return returnTypes;
    }
    
    public boolean analyseSymbolTable() {
    	
    	if(getChildren() != null && getChildren().length > 0) {
    		SimpleNode child = (SimpleNode) getChildren()[0];
    		
    		if(child.toString().equals("ScalarAccess")) {
    			if(!verifySymbolTypes(child.getValue(), true, Symbol.Type.SCALAR, Symbol.Type.ARRAY)) {
    				//System.out.println("Semantic Error: " + child.value + " should have been initialized.");
    				printSemanticError(child.getValue() + " should have been initialized");
    				return false;
    			}
    		}
    		else if(child.toString().equals("Call")) {
    			System.out.println("ReturnTypes: "+((ASTCall) child).getReturnTypes());
    			Vector<Symbol.Type> returnTypes = getReturnTypes();
    			if(!returnTypes.contains(Symbol.Type.SCALAR) && !returnTypes.contains(Symbol.Type.ARRAY)) {
    				//System.out.println("Semantic Error: In order to be used here, function "+child.value+" should return a SCALAR or an ARRAY.");
    				printSemanticError("In order to be used here, function "+child.getValue()+" should return a SCALAR or an ARRAY");
    				return false;
    			}
    			//TODO Check that call returns a scalar
    		}
    	}
    		
    	return true;
    }

    public String generateCode() {
        String generatedCode = "";

        // when Term -> <INTEGER>
        if(getChildren() == null)
            generatedCode = "iload " + this.getValue() + "\n";
        else
        // when Term -> ScalarAccess | Call | ArrayAccess
            generatedCode = ((SimpleNode) getChildren()[0]).generateCode();

        return generatedCode;
    }

}
/* JavaCC - OriginalChecksum=e2941c82560cbaf5af794c73c59e74f3 (do not edit this line) */
