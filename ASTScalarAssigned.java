import semantic.Symbol;

/* Generated By:JJTree: Do not edit this line. ASTScalarAssigned.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTScalarAssigned extends SimpleNode {
	public ASTScalarAssigned(int id) {
		super(id);
	}

	public ASTScalarAssigned(Yal p, int id) {
		super(p, id);
	}

	public Symbol.Type getReturnType() {
		return Symbol.Type.SCALAR;
	}

}
/* JavaCC - OriginalChecksum=81a620c329c556098bea424f1a128dee (do not edit this line) */
