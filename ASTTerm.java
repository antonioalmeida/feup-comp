import java.util.Vector;

import semantic.Symbol;

/* Generated By:JJTree: Do not edit this line. ASTTerm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
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
        if(children == null) 
            return Symbol.Type.SCALAR;

        return ((SimpleNode) children[0]).getReturnType();
    }
    
    public Vector<Symbol.Type> getReturnTypes(){
    	
    	if(children != null && children.length > 0)
    		if(((SimpleNode) children[0]).toString().equals("Call"))
    			return ((ASTCall) children[0]).getReturnTypes(); 
    	
    	Vector<Symbol.Type> returnTypes =new Vector<Symbol.Type>();
    	returnTypes.add(getReturnType());
    	return returnTypes;
    }
    
    public boolean analyseSymbolTable() {
    	
    	if(children != null && children.length > 0) {
    		SimpleNode child = (SimpleNode) children[0];
    		System.out.println("TERM: "+child.toString());
    		if(child.toString().equals("ScalarAccess")) {
    			if(!verifySymbolTypes(child.value, true, Symbol.Type.SCALAR, Symbol.Type.ARRAY)) {
    				System.out.println("Semantic Error: " + child.value + " should have been initialized");
    				return false;
    			}
    		}
    		else if(child.toString().equals("Call")) {
    			System.out.println("ReturnTypes: "+((ASTCall) child).getReturnTypes());
    			Vector<Symbol.Type> returnTypes = getReturnTypes();
    			if(!returnTypes.contains(Symbol.Type.SCALAR) && !returnTypes.contains(Symbol.Type.ARRAY)) {
    				System.out.println("Semantic Error: In order to be used here, function "+child.value+" should return a SCALAR or an ARRAY.");
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
        if(children == null)
            generatedCode = "iload " + this.value + "\n";
        else
        // when Term -> ScalarAccess | Call | ArrayAccess
            generatedCode = ((SimpleNode) children[0]).generateCode();

        return generatedCode;
    }

}
/* JavaCC - OriginalChecksum=694cdb272d61fc5ab19824a2f80bedc9 (do not edit this line) */
