/* Generated By:JJTree: Do not edit this line. ASTAssign.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTAssign extends SimpleNode {
    public ASTAssign(int id) {
        super(id);
    }

    public ASTAssign(Yal p, int id) {
        super(p, id);
    }

    /*public boolean analyse() {
        System.out.println("Analysing " + toString(""));
        symbolTable = getAssignedSymbolTable();

        return analyseSymbolTable();
    }*/

    public boolean analyseSymbolTable() {
        if(children == null)
            return false;

        // lhs
        SimpleNode lhsChild = (SimpleNode) children[0];
      
        Symbol.Type lhsType = lhsChild.getReturnType();
        

        // rhs
        SimpleNode rhsChild = (SimpleNode) children[1];
        
        Symbol.Type rhsType = rhsChild.getReturnType();

        
        String symbolName = (String) lhsChild.jjtGetValue();       

        return true;
    }

      public Symbol.Type getReturnType() {
        return Symbol.Type.VOID;  
    }



}
/* JavaCC - OriginalChecksum=8a81cbd863c4645c551a38479e4343a7 (do not edit this line) */
