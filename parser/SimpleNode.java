/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package parser;
import codeGeneration.CodeLine;
import semantic.FunctionTable;
import semantic.Symbol;
import semantic.SymbolTable;
public
class SimpleNode implements Node {

    protected Node parent;
    private Node[] children;
    protected int id;
    private String value;
    protected Yal parser;
    protected SymbolTable symbolTable;
    protected FunctionTable functionTable;
    protected CodeLine codeLine;
    protected boolean hasScope;
    protected boolean hasFunctionScope;
    protected boolean hasCodeLineScope;
    protected Token firstToken;
    protected Token lastToken;
    
    public Token jjtGetFirstToken() {
        return firstToken;
    }

    public void jjtSetFirstToken(Token token) {
        firstToken = token;
    }

    public Token jjtGetLastToken() {
        return lastToken;
    }

    public void jjtSetLastToken(Token token) {
        lastToken = token;
    }
    
    public SimpleNode(int i) {
            this.setValue("");
            this.hasScope = false;
            this.hasFunctionScope = false;
            this.hasCodeLineScope = false;
            id = i;
    }
     
    public SimpleNode(Yal p, int i) {
            this(i);
            parser = p;
    }
    
    public SimpleNode(int i, boolean hasScope, boolean hasFunctionScope, boolean hasCodeLineScope) {
    this.setValue("");
    this.hasScope = hasScope;
    this.hasFunctionScope = hasFunctionScope;
        id = i;
    }

    public SimpleNode(Yal p, int i, boolean hasScope, boolean hasFunctionScope, boolean hasCodeLineScope) {
        this(i, hasScope, hasFunctionScope, hasCodeLineScope);
        parser = p;
    }

    public SymbolTable getAssignedSymbolTable() {
        if(parent == null)
            return null;
        else if(hasScope)
            return new SymbolTable(((SimpleNode) parent).getSymbolTable());
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
    
    /*public CodeLine getAssignedCodeLine() {
    	if(parent == null)
    		return null;
    	else if(hasCodeLineScope)
    		return new CodeLine();
    	else
    		return ((SimpleNode) parent).getCodeLine();
    }*/
    
    public void jjtOpen() {}

    public void jjtClose() {}

    public void jjtSetParent(Node n) { parent = n; }

    public Node jjtGetParent() { return parent; }

    public void jjtAddChild(Node n, int i) {
        if (getChildren() == null)
            setChildren(new Node[i + 1]); 
        else if (i >= getChildren().length) {
            Node c[] = new Node[i + 1];

            System.arraycopy(getChildren(), 0, c, 0, getChildren().length);
            setChildren(c);
        }

        getChildren()[i] = n;
    }

    public Node jjtGetChild(int i) {
        return getChildren()[i];
    }

    public int jjtGetNumChildren() {
        return (getChildren() == null) ? 0 : getChildren().length;
    }

    public void jjtAddValue(String value) { this.setValue(this.getValue() + value); }
    
    public void jjtSetValue(String value) {this.setValue(value);}

    public Object jjtGetValue() { return getValue(); }

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
    
        if(this.getValue() != "")
            node += " [" + this.getValue() + "]";

        return node; 
    }

    /* Override this method if you want to customize how the node dumps
         out its children. */

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        
        if(getChildren() != null) {
            /*
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode)children[i];
                if (n != null) {
                    n.dump(prefix + "   ");
                }
            }
            */

            for(Node child : getChildren())
                if(child != null)
                    ((SimpleNode ) child).dump(prefix + "   ");
        }
    }

    public String generateCode() {
        String generatedCode = "";

        if (getChildren() != null) {
            for(int i = 0; i < getChildren().length; ++i) {
                SimpleNode n = (SimpleNode) getChildren()[i];

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

    public boolean initializeSymbol(String symbolName, Symbol.Type type, boolean initialized, boolean print, int index) {
        return symbolTable.initializeSymbol(symbolName, type, initialized, print, true, index);
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
    
        if(getChildren() != null) {
            for(Node child : getChildren()) {
                if(!((SimpleNode) child).analyse())
                    result = false;
            }
        }
        
        if(!analyseSymbolTable())
            result = false;

        return result;
    }

    public int attributeIndexes(int lastIndex) {
        int currentIndex = lastIndex;
        if(hasScope)
            currentIndex = symbolTable.attributeIndexes(currentIndex);

        if(getChildren() != null) {
            for(Node child : getChildren()) {
                currentIndex = ((SimpleNode) child).attributeIndexes(currentIndex);
            }
        }

        return currentIndex;
    }

    //public void printSymbolTableScope(String prefix)
    
    public void printSymbolTable(String prefix) {
        if(hasScope) {
        	 String printMessage = prefix+"@" + toString();
        	 if(toString().equals("Function"))   
        		 printMessage += " [" + this.getRealValue() + "]";
        	 else if(! this.getValue().equals(""))
        		 printMessage += " [" + this.getValue() + "]";
        	
            System.out.println(printMessage);
            symbolTable.printSymbols(prefix+"   ");
        }

        if (getChildren() != null) {
            for (int i = 0; i < getChildren().length; ++i) {
                SimpleNode n = (SimpleNode)getChildren()[i];
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

        if (getChildren() != null) {
            for (int i = 0; i < getChildren().length; ++i) {
                SimpleNode n = (SimpleNode)getChildren()[i];
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
        if(getValue().equals(""))
            if(getChildren() != null && getChildren().length > 0)
                return ((SimpleNode) getChildren()[0]).getRealValue();
                
        return getValue();
    }
    

    public void printSemanticError(String errorMsg) {
    	String functionName = getFunction();
    	String functionModuleMsg = ", module "+getModule();
    	if(! functionName.equals(""))
    		functionModuleMsg += ", function "+functionName;
    	System.out.println("Semantic Error at line "+firstToken.beginLine +  ", column "+firstToken.beginColumn  +functionModuleMsg+": "+errorMsg + ".");
    }
    
    public String getFunction() {
    	return ((SimpleNode) parent).getFunction();
    }
    
    public String getModule() {
    	return ((SimpleNode) parent).getModule();
    }

    

    public int getLastIndex() {
        return symbolTable.getIndexCount();
    }

	public Node[] getChildren() {
		return children;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

/* JavaCC - OriginalChecksum=ddfb1251cda21b9eb5aadc721edc8350 (do not edit this line) */
