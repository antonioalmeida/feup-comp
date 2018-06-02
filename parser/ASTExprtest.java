/* Generated By:JJTree: Do not edit this line. ASTExprtest.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import semantic.Symbol;
public
class ASTExprtest extends SimpleNode {
    public ASTExprtest(int id) {
        super(id);
    }

    public ASTExprtest(Yal p, int id) {
        super(p, id);
    }

    public boolean analyseSymbolTable() {
        if(getChildren() == null)
            return false;

        // lhs
        SimpleNode lhsChild = (SimpleNode) getChildren()[0];
        Symbol.Type lhsType = lhsChild.getReturnType();

        // rhs
        SimpleNode rhsChild = (SimpleNode) getChildren()[1];
        Symbol.Type rhsType = rhsChild.getReturnType();

        SimpleNode rhslhsChild = (SimpleNode) rhsChild.getChildren()[0];

        if(rhslhsChild.toString().equals("ArrayAssigned")) {
            //System.out.println("Semantic Error: The size of an array can't be used in a comparision");
            printSemanticError("The size of an array can't be used in a comparision");
            return false;
        }

        String lhsSymbol = (String) lhsChild.jjtGetValue();
        String rhsSymbol = (String) rhsChild.jjtGetValue();

        if((getValue().equals("==") || getValue().equals("!=")) 
            && lhsType.equals(Symbol.Type.ARRAY) 
            && rhsType.equals(Symbol.Type.ARRAY))
            return true;
        else {
            if(lhsChild.toString().equals("ScalarAccess") && !lhsChild.getSizeArray() && !verifySymbolTypes(lhsSymbol, true, Symbol.Type.SCALAR)) {
                //System.out.println("Semantic error: " + lhsChild.getRealValue() + " should have been initialized to a scalar in order to be used in the comparison");
                printSemanticError(lhsChild.getRealValue() + " should have been initialized to a scalar in order to be used in the comparison");
            	return false;
            }
            else if(!rhsType.equals(Symbol.Type.SCALAR)) {
                //System.out.println("Semantic error: " + rhsChild.getRealValue() + " should have been initialized to a scalar in order to be used in the comparison");
                printSemanticError(rhsChild.getRealValue() + " should have been initialized to a scalar in order to be used in the comparison");
                return false;
            }
        }

        return true;
    } 
}
/* JavaCC - OriginalChecksum=e47ba1733afaa63aff312491c45c83ec (do not edit this line) */
