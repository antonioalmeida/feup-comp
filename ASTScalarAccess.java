/* Generated By:JJTree: Do not edit this line. ASTScalarAccess.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTScalarAccess extends SimpleNode {
  protected boolean size_array = false;
	
	
  public ASTScalarAccess(int id) {
    super(id);
  }

  public ASTScalarAccess(Yal p, int id) {
    super(p, id);
  }


  public void set_size_access(boolean set){
  	size_array = set;
  }
  
  public String toString(String prefix) { 
  	String node = prefix + toString();
	
	if (this.value != null){
		if (this.size_array == true)
			node += " [" + "sizeof(" + this.value + ")]";
		else node += " [" + this.value + "]";
	}
	
    
    	 	
  	return node; 
  }
}
/* JavaCC - OriginalChecksum=dc46aab5f6a610de55713f684578a33a (do not edit this line) */
