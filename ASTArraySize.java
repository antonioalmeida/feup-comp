import semantic.Symbol;

/* Generated By:JJTree: Do not edit this line. ASTArraySize.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArraySize extends SimpleNode {
    public ASTArraySize(int id) {
        super(id);
    }

    public ASTArraySize(Yal p, int id) {
        super(p, id);
    }

    public Symbol.Type getReturnType() {
        return Symbol.Type.SCALAR;  
    }

    public boolean analyseSymbolTable() {
        if(children != null && children.length > 0) {
            String symbolName = ((SimpleNode) children[0]).value;

            if(!verifySymbolTypes(symbolName, true, Symbol.Type.SCALAR)) {
                //System.out.println("Semantic Error: " + symbolName + " should have been initialized as a scalar");
                printSemanticError(symbolName + " should have been initialized as a scalar");
                return false;
            }
        }
        return true;
    }

}
/* JavaCC - OriginalChecksum=157ede1329c1b5fc291489b1d41966bb (do not edit this line) */
