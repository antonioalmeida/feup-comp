/* Generated By:JJTree: Do not edit this line. ASTArgument.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTArgument extends SimpleNode {
  public ASTArgument(int id) {
    super(id);
  }

  public ASTArgument(Yal p, int id) {
    super(p, id);
  }
    
  public int getArgumentType(){
	  SimpleNode typeArgument = (SimpleNode) jjtGetChild(0);
	  return typeArgument.id;
  }

}
/* JavaCC - OriginalChecksum=bcfaffac6c182a1c4c4b38a87de9d6ea (do not edit this line) */
