/* Generated By:JJTree: Do not edit this line. ASTArraySize.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import semantic.Symbol;
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
        if(getChildren() != null && getChildren().length > 0) {
            String symbolName = ((SimpleNode) getChildren()[0]).getValue();

            if(!verifySymbolTypes(symbolName, true, Symbol.Type.SCALAR)) {
                //System.out.println("Semantic Error: " + symbolName + " should have been initialized as a scalar");
                printSemanticError(symbolName + " should have been initialized as a scalar");
                return false;
            }
        }
        return true;
    }

}
/* JavaCC - OriginalChecksum=a220acdd6952405f3788969f4ca1f358 (do not edit this line) */
