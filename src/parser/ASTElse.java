/* Generated By:JJTree: Do not edit this line. ASTElse.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;

import semantic.SymbolTable;

public
class ASTElse extends SimpleNode {
	public ASTElse(int id) {
		super(id, true, false, false);
	}

	public ASTElse(Yal p, int id) {
		super(p, id, true, false, false);
	}

	/*public SymbolTable getAssignedSymbolTable() {
		return new SymbolTable(((SimpleNode) parent).getSymbolTable(), true);
	}*/
}
/* JavaCC - OriginalChecksum=aebfcf0534193235d176a119735431c9 (do not edit this line) */