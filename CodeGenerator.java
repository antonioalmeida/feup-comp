import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import semantic.Symbol;

public class CodeGenerator {

	private SimpleNode root;

	private PrintWriter out;

	public CodeGenerator(SimpleNode root, String filename) throws IOException {
		this.root = (SimpleNode) root.children[0];

		FileWriter fw = new FileWriter(filename, false);
		BufferedWriter bw = new BufferedWriter(fw);

		this.out = new PrintWriter(bw);
	}

	public void generateCode() {
		generateHeader();
		generateGlobals(root);
		generateStatic();
		generateFunctions();
		out.close();
	}

	private void generateHeader() {
		out.println(".class public " + root.value);
		out.println(".super java/lang/Object" + "\n");
	}

	private void generateGlobals(SimpleNode node) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++)
			generateGlobals((SimpleNode) node.jjtGetChild(i));

		if (node.id == YalTreeConstants.JJTDECLARATION)
			generateDeclaration(node);
	}

	private void generateStatic() {
		// TODO Auto-generated method stub

	}

	private void generateFunctions() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.id == YalTreeConstants.JJTFUNCTION)
				generateFunction(childRoot);
		}
	}

	private void generateFunctionHeader(SimpleNode functionNode) {
		out.print(".method public static " + functionNode.value);

		if (functionNode.jjtGetNumChildren() == 0)
			out.println("()V");
		else {
			SimpleNode childFunction = (SimpleNode) functionNode.jjtGetChild(0);
			if (childFunction.id == YalTreeConstants.JJTVARLIST) {
				out.print("(");

				for (int i = 0; i < childFunction.jjtGetNumChildren(); i++)
					out.print("I");

				out.println(")V");
			} 
			else
				out.println("()V");
		}
	}

	private void generateFunctionMainHeader(SimpleNode functionNode) {
		out.println(".method public static main([Ljava/lang/String;)V");
	}

	private void generateAssignFunction(SimpleNode functionNode) {
		SimpleNode element = (SimpleNode) functionNode.jjtGetChild(0);
		SimpleNode functionAssign = (SimpleNode) functionNode.jjtGetChild(1);

		out.print(".method public static " + functionAssign.value);

		if (functionNode.jjtGetNumChildren() <= 2)
			out.println("()I");
		else {
			SimpleNode childFunction = (SimpleNode) functionNode.jjtGetChild(2);
			if (childFunction.id == YalTreeConstants.JJTVARLIST) {
				out.print("(");

				for (int i = 0; i < childFunction.jjtGetNumChildren(); i++)
					out.print("I");
				out.println(")I");
			} 
			else
				out.println("()I");
		}

	}

	private void generateFunction(SimpleNode functionNode) {
		out.println();

		if (functionNode.value.equals("main"))
			generateFunctionMainHeader(functionNode);
		else if (functionNode.jjtGetNumChildren() >= 2
				&& ((SimpleNode) functionNode.jjtGetChild(1)).id == YalTreeConstants.JJTFUNCTIONASSIGN)
			generateAssignFunction(functionNode);
		else
			generateFunctionHeader(functionNode);

		// body

		// TODO: limit
		out.println(".limit locals 10");
		out.println(".limit stack 10");
		out.println();

		generateBody(functionNode);

		if (functionNode.jjtGetNumChildren() >= 2
				&& ((SimpleNode) functionNode.jjtGetChild(1)).id == YalTreeConstants.JJTFUNCTIONASSIGN) {
			if (((SimpleNode) functionNode.jjtGetChild(0)).id == YalTreeConstants.JJTSCALARELEMENT)
				out.println("ireturn");
			else
				out.println("areturn");
		} 
		else
			out.println("return");

		out.println(".end method");
		out.println();
	}

	private void generateBody(SimpleNode functionNode) {
		for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
			SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);

			switch (functionChild.id) {
				case YalTreeConstants.JJTCALL:
					generateCall(functionChild);
					break;
				case YalTreeConstants.JJTASSIGN:
					generateAssign(functionChild);
					break;
				case YalTreeConstants.JJTWHILE:
					//generateWhile();
					break;
				case YalTreeConstants.JJTIF:
					//generateIf();
					break;
				default:
					break;
			}
		}
	}
	
	private void loadString(String string){
		out.println("ldc " + string);
	}
	
	private void loadInt(String valueToLoad){
		int value = Integer.parseInt(valueToLoad);
		if ((value >= 0) && (value < 6)) {
			out.println("iconst_" + value);
		} else if (value == -1)
			out.println("iconst_m1");
		else
			out.println("bipush " + value);
	}
	
	private void loadGlobalVar(String varName) {
		out.print("getstatic " + root.value + "/" + varName);

		if (root.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			out.println(" I");
		else
			out.println(" [I");
	}
	
	private void generateCallArgs(SimpleNode functionChild){
		
		for (int i = 0; i < functionChild.jjtGetNumChildren(); i++) {
			SimpleNode argument = (SimpleNode) functionChild.jjtGetChild(i);
			SimpleNode typeArgument = (SimpleNode) argument.jjtGetChild(0);

			if (typeArgument.id == YalTreeConstants.JJTSTRING)
				loadString(argument.value);
			else if (typeArgument.id == YalTreeConstants.JJTINTEGER) {
				loadInt(argument.value);
			} else {
				if(root.symbolTable.containsSymbolName(argument.value)){
					loadGlobalVar(argument.value);
				}
				//TODO else if for local variables and parameters
				
			}

			if (i + 1 != functionChild.jjtGetNumChildren())
				out.print("");

		}
		
	}
	
	
	private void generateCall(SimpleNode functionChild) {

		generateCallArgs(functionChild);
		
		out.print("invokestatic " + functionChild.value + "(");

		for (int i = 0; i < functionChild.jjtGetNumChildren(); i++) {
			SimpleNode argument = (SimpleNode) functionChild.jjtGetChild(i);
			SimpleNode typeArgument = (SimpleNode) argument.jjtGetChild(0);

			if (typeArgument.id == YalTreeConstants.JJTSTRING)
				out.print("Ljava/lang/String");
			else
				out.print("I");

			if ((i + 1 != functionChild.jjtGetNumChildren()) || (typeArgument.id == YalTreeConstants.JJTSTRING))
				out.print(";");

		}

		out.print(")");

		if (((SimpleNode) functionChild.parent).id == YalTreeConstants.JJTTERM)
			out.print("I");
		else
			out.print("V");

		out.println();
		out.println();
	}
	
	private void generateOperation(SimpleNode rhs) {
		String operation = rhs.value;

		switch (operation) {
		case "+":
			out.println("iadd");
			break;
		case "-":
			out.println("isub");
			break;
		case "*":
			out.println("imul");
			break;
		case "/":
			out.println("idiv");
			break;
		case "<<":
			out.println("ishl");
			break;
		case ">>":
			out.println("ishr");
			break;
		case ">>>":
			out.println("iushr");
			break;
		case "&":
			out.println("iand");
			break;
		case "|":
			out.println("ior");
			break;
		case "^":
			out.println("ixor");
			break;
		default:
			break;
		}

	}
	
	
	private void generateRHS(SimpleNode rhs) {

		for (int i = 0; i < rhs.jjtGetNumChildren(); i++) {

			SimpleNode term = (SimpleNode) rhs.jjtGetChild(i);
			SimpleNode termChild = (SimpleNode) term.jjtGetChild(0);

			boolean isPositive = true;
			if (term.value != null)
				isPositive = false;

			switch (termChild.id) {
			case (YalTreeConstants.JJTINTEGER):
				loadInt(termChild.value);
				break;
			case (YalTreeConstants.JJTSCALARACCESS):
				if(root.symbolTable.containsSymbolName(termChild.value)){
					loadGlobalVar(termChild.value);
				}
			
			
				//TODO else if for local variables and parameters
			
				//TODO .size
				break;
			case (YalTreeConstants.JJTCALL):
				generateCall(termChild);
				break;
			default:
				break;

			}
			
		}
		
		if (rhs.jjtGetNumChildren() == 2){
			generateOperation(rhs);
		}
		
		
	}

	private void generateAssign(SimpleNode node) {
		// Assign -> Lhs = Rhs
		/// Rhs -> Term OP Term | [ ArraySize ]
		// Term -> OP? ( INT | Call | ArrayAccess | ScalarAccess )

		SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);
		generateRHS(rhs);
		
		
		
		
		
		
		
		
		//out.print(rhs.generateCode());

		
		
		
		
		
		
		//TODO: right now always assuming 
		//ArrayAccess and ScalarAccess are 
		// from static fields, need to cover
		// local variables aswell
		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
		out.println("putstatic " + lhs.value + " I");

		out.println("");
		
		
		
		if(root.symbolTable.containsSymbolName(lhs.value)){
			out.print("putstatic " + root.value + "/" + lhs.value);
			
			if (root.symbolTable.getSymbolType(lhs.value) == Symbol.Type.SCALAR)
				out.println(" I");
			else 
				out.println(" [I");
		}
		//TODO else if for local variables and parameters
	}

	private void generateDeclaration(SimpleNode node) {
		SimpleNode scalarElement = (SimpleNode) node.children[0];
		// out.println(".field static "+ scalarElement.value + " I" );

		if (node.children.length == 2) {
			SimpleNode assignedElement = (SimpleNode) node.children[1];
			
			if (assignedElement.id == YalTreeConstants.JJTSCALARASSIGNED)
				out.println(".field static " + scalarElement.value + " I = " + assignedElement.value);
		} 
		else
			out.println(".field static " + scalarElement.value + " I");
	}
}
