/* Generated By:JJTree: Do not edit this line. ASTArrayAssigned.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import semantic.Symbol;
public
class ASTArrayAssigned extends SimpleNode {
    public ASTArrayAssigned(int id) {
        super(id);
    }

    public ASTArrayAssigned(Yal p, int id) {
        super(p, id);
    }

    public Symbol.Type getReturnType() {
        return Symbol.Type.ARRAY;   
    }

}
/* JavaCC - OriginalChecksum=e9560280f21f1812ad2d77035dffb79d (do not edit this line) */
