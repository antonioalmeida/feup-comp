/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected Object value;
  protected Yal parser;
  protected SymbolTable symbolTable;
  protected boolean hasScope;
  
  public SimpleNode(int i) {
	    this.hasScope = false;
	    symbolTable = assignSymbolTable();
	    id = i;
  }
  
   
  public SimpleNode(Yal p, int i) {
	    this(i);
	    parser = p;
  }
  
  public SimpleNode(int i, boolean hasScope) {
	this.hasScope = hasScope;
    symbolTable = assignSymbolTable();
    id = i;
  }

  public SimpleNode(Yal p, int i, boolean hasScope) {
    this(i, hasScope);
    parser = p;
  }

  public SymbolTable assignSymbolTable() {
	  if(parent == null) {
		  System.out.println("b1null");
		  return null;
	  }
	  else if(hasScope) {
		  System.out.println("b1hasScope");
		  return new SymbolTable(parent.getSymbolTable());
	  }
	  else {
		  System.out.println("b1NoScope");
		  return ((SimpleNode) parent).getSymbolTable();
	  }
  }
  
  public void jjtOpen() {

  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    return YalTreeConstants.jjtNodeName[id]; 
  }
  
  public String toString(String prefix) {
	
  	String node = prefix + toString();
	
	if (this.value != null)
		node += " [" + this.value + "]";
    	 	
  	return node; 
  }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.println(toString(prefix));
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "   ");
        }
      }
    }
  }
  
  
  
  /**
   * If checkInitialized equals true, this function checks if a variable symbolName has been initialized to one of types
   * If checkInitialized equals false, this function checks if a variable symbolName has not been initialized to any type different from types
   */
  public boolean verifySymbolTypes(String symbolName, boolean checkInitialized, Symbol.Type... types ) {
		return symbolTable.verifySymbolTypes(symbolName, checkInitialized, types);
  }
  

  public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized) {
	   	   return symbolTable.initializeSymbol(symbolName, type, initialized);
    }
  
  public int getId() {
    return id;
  }
  
 
  
  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public boolean analyse() {
    boolean result = true;
    //System.out.println("Analysing " + toString(""));

    if(children == null)
     return false; 

    for(Node child : children) {
      if(!child.analyse())
        result = false;
    }

    return result;
  }

  //public void printSymbolTableScope(String prefix)
  
  public void printSymbolTable(String prefix) {
	  if(hasScope) {
		  System.out.println(toString(prefix));
		  symbolTable.printSymbols(prefix);
  	  }
	    if (children != null) {
	      for (int i = 0; i < children.length; ++i) {
	        SimpleNode n = (SimpleNode)children[i];
	        if (n != null) {
	          n.printSymbolTable(prefix + "   ");
	        }
	      }
	    }
	  
  }
	 

}

/* JavaCC - OriginalChecksum=a536ad506ca058676615e1a3304534ab (do not edit this line) */
