/* Generated By:JJTree: Do not edit this line. ASTIf.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import semantic.SymbolTable;

public
class ASTIf extends SimpleNode {
	public ASTIf(int id) {
		super(id, true, false, false);
	}

	public ASTIf(Yal p, int id) {
		super(p, id, true, false, false);
	}

	/*public SymbolTable getAssignedSymbolTable() {
		return new SymbolTable(((SimpleNode) parent).getSymbolTable());
	}*/
}
/* JavaCC - OriginalChecksum=890cda73491138fb844da26b5d6b8c0b (do not edit this line) */
