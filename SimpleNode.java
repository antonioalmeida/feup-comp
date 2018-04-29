import semantic.FunctionTable;
import semantic.Symbol;
import semantic.SymbolTable;

/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

    protected Node parent;
    protected Node[] children;
    protected int id;
    protected String value;
    protected Yal parser;
    protected SymbolTable symbolTable;
    protected FunctionTable functionTable;
    protected boolean hasScope;
    protected boolean hasFunctionScope;
    
    public SimpleNode(int i) {
            this.value = "";
            this.hasScope = false;
            this.hasFunctionScope = false;
            id = i;
    }
     
    public SimpleNode(Yal p, int i) {
            this(i);
            parser = p;
    }
    
    public SimpleNode(int i, boolean hasScope, boolean hasFunctionScope) {
    this.value = "";
    this.hasScope = hasScope;
    this.hasFunctionScope = hasFunctionScope;
        id = i;
    }

    public SimpleNode(Yal p, int i, boolean hasScope, boolean hasFunctionScope) {
        this(i, hasScope, hasFunctionScope);
        parser = p;
    }

    public SymbolTable getAssignedSymbolTable() {
        if(parent == null)
            return null;
        else if(hasScope)
            return new SymbolTable(parent.getSymbolTable());
        else
            return ((SimpleNode) parent).getSymbolTable();
    }
    
    public FunctionTable getAssignedFunctionTable() {
    	if(parent == null)
    		return null;
    	else if(hasFunctionScope)
    		return new FunctionTable();
    	else
    		return ((SimpleNode) parent).getFunctionTable();
    }
    
    public void jjtOpen() {}

    public void jjtClose() {}

    public void jjtSetParent(Node n) { parent = n; }

    public Node jjtGetParent() { return parent; }

    public void jjtAddChild(Node n, int i) {
        if (children == null)
            children = new Node[i + 1]; 
        else if (i >= children.length) {
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

    public void jjtAddValue(String value) { this.value = this.value + value; }
    
    public void jjtSetValue(String value) {this.value = value;}

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
    
        if(this.value != "")
            node += " [" + this.value + "]";

        return node; 
    }

    /* Override this method if you want to customize how the node dumps
         out its children. */

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        
        if(children != null) {
            /*
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode)children[i];
                if (n != null) {
                    n.dump(prefix + "   ");
                }
            }
            */

            for(Node child : children)
                if(child != null)
                    ((SimpleNode ) child).dump(prefix + "   ");
        }
    }

    public String generateCode() {
        String generatedCode = "";

        if (children != null) {
            for(int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];

                if(n != null) {
                    generatedCode += n.generateCode();
                }
            }
        }

        return generatedCode;
    }
    
    /**
     * If checkInitialized equals true, this function checks if a variable symbolName has been initialized to one of types
     * If checkInitialized equals false, this function checks if a variable symbolName has not been initialized to any type different from types
     */
    public boolean verifySymbolTypes(String symbolName, boolean checkInitialized, Symbol.Type... types ) {
        return symbolTable.verifySymbolTypes(symbolName, checkInitialized, checkInitialized, types);
    }

    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized) {
        return symbolTable.initializeSymbol(symbolName, type, initialized, true);
    }
    
    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print) {
        return symbolTable.initializeSymbol(symbolName, type, initialized, print);
    }

    public int getSymbolIndex(String symbolName) {
        return symbolTable.getSymbolIndex(symbolName);
    }
    
    public int getId() {
        return id;
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    public FunctionTable getFunctionTable() {
    	return functionTable;
    }
    
    public boolean analyseSymbolTable() {
        return true;
    }
    
   
    
    public boolean analyse() {
        symbolTable = getAssignedSymbolTable();
        functionTable = getAssignedFunctionTable();
        boolean result = true;
    
        if(children != null) {
            for(Node child : children) {
                if(!child.analyse())
                    result = false;
            }
        }
        
        if(!analyseSymbolTable())
            result = false;
       

        return result;
    }

    //public void printSymbolTableScope(String prefix)
    
    public void printSymbolTable(String prefix) {
        if(hasScope) {
        	 String printMessage = prefix+"@" + toString();
        	 if(toString().equals("Function"))   
        		 printMessage += " [" + this.getRealValue() + "]";
        	 else if(! this.value.equals(""))
        		 printMessage += " [" + this.value + "]";
        	
            System.out.println(printMessage);
            symbolTable.printSymbols(prefix+"   ");
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
    
    public void printFunctionTable(String prefix) {
    	if(hasFunctionScope) {
            System.out.println(toString(prefix+"@"));
            functionTable.printFunctions(prefix+"   ");
        }

        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode)children[i];
                if (n != null) {
                    n.printFunctionTable(prefix + "");
                }
            }
        }
    }
    
    public Symbol.Type getReturnType() {
        return Symbol.Type.VOID;
    }
        
    public boolean getSizeArray() {
        return false;
    }
        
    public String getRealValue() {
        if(value.equals(""))
            if(children != null && children.length > 0)
                return ((SimpleNode) children[0]).getRealValue();
                
        return value;
    }

}

/* JavaCC - OriginalChecksum=a536ad506ca058676615e1a3304534ab (do not edit this line) */
