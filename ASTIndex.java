import semantic.Symbol;
import utils.Utils;

/* Generated By:JJTree: Do not edit this line. ASTIndex.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTIndex extends SimpleNode {
    public ASTIndex(int id) {
        super(id);
    }

    public ASTIndex(Yal p, int id) {
        super(p, id);
    }
 
    public boolean analyseSymbolTable() {
        System.out.println("Analyzing Index");
        if(!Utils.isInteger(this.value))
            if(!verifySymbolTypes(this.value, true, Symbol.Type.SCALAR)) {
                System.out.println("Semantic Error: Index of an array " + this.value + " should have been initialized to a scalar");
                return false;
            }
            
        return true;
    }

    public String generateCode() {
        String generatedCode = "";

        // when Index -> <INTEGER>
        if(Utils.isInteger(this.value)) {
            generatedCode = "iload " + this.value + "\n";
            return generatedCode;  
        }


        //TODO: check if it's a static field
        // or a local variable, right now we're 
        //assuming it's always a static field

        generatedCode += "getstatic " + this.value;
        if(getReturnType().equals(Symbol.Type.ARRAY))
            generatedCode += " [I";
        else 
            generatedCode += " I";

        generatedCode += "\n";
        return generatedCode;
    }

}
/* JavaCC - OriginalChecksum=c8a78f8c8f68646756fd3e8467b076ca (do not edit this line) */
