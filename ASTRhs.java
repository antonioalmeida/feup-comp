/* Generated By:JJTree: Do not edit this line. ASTRhs.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTRhs extends SimpleNode {
    

    public ASTRhs(int id) {
        super(id);
    }

    public ASTRhs(Yal p, int id) {
        super(p, id);
    }

    public boolean analyseSymbolTable() {
        System.out.println("Analysing " + toString(""));
        // Rhs -> Term (OP Term)? | [ArraySize]

        // Term OP Term
        if(children.length > 1) {
            //TODO: add analyse on lhs and rhs before getting return type
            Symbol.Type lhsType = ((SimpleNode) children[0]).getReturnType();
            Symbol.Type rhsType = ((SimpleNode) children[1]).getReturnType();

            if(lhsType.equals(Symbol.Type.ARRAY)) {
               System.out.println("Semantic error: " + ((SimpleNode) children[0]).value + " is of type " + lhsType + ", which is not allowed here ");
               return false;
            }
            else if(rhsType.equals(Symbol.Type.ARRAY)) {
                System.out.println("Semantic error: " + ((SimpleNode) children[1]).value + " is of type " + rhsType + ", which is not allowed here ");
                return false;
             }
           

           
        }

        return true;
    }

    public Symbol.Type getReturnType() {
    	Symbol.Type lhsType = ((SimpleNode) children[0]).getReturnType();      
        return lhsType;
    }

    public String generateCode() {
        String generatedCode = "";

        if(children != null)
            for(Node child : children)
                generatedCode += ((SimpleNode) child).generateCode();

        // if Rhs -> Term OP Term
        if(children.length > 1) {
            switch(this.value) {
                case "+":
                    generatedCode += "iadd\n";
                    break;
            }
        }

        return generatedCode;
    }

}
/* JavaCC - OriginalChecksum=f98f0d78c74a90d4cd0f52de8c8b26b9 (do not edit this line) */
