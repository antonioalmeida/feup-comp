/* Generated By:JJTree: Do not edit this line. ASTId.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

public
class ASTId extends SimpleNode {
  public ASTId(int id) {
    super(id);
  }

  public ASTId(Yal p, int id) {
    super(p, id);
  }
  
  public boolean analyseSymbolTable() {
	  if(this.parent.toString().equals("Argument"))
		  addCodeLine(((SimpleNode) this.parent).getValue(), false);
	  return true;
  }

}
/* JavaCC - OriginalChecksum=1b168110886a2d0968c1d4e535d70f1a (do not edit this line) */