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
		generateGlobals();
		generateStatic();
		generateFunctions();
		out.close();
	}

	private void generateHeader() {
		out.println(".class public " + root.value);
		out.println(".super java/lang/Object" + "\n");
	}

	private void generateDeclaration(ASTDeclaration declaration) {
		String varName, varType, varValue = "";
		SimpleNode element = (SimpleNode) declaration.children[0];

		varName = element.value;

		if (declaration.isVarArray())
			varType = " [I ";
		else {
			varType = " I ";
			if (declaration.isVarScalarAssigned()) {
				SimpleNode assignedScalar = (SimpleNode) declaration.children[1];
				varValue = "= " + assignedScalar.value;
			}

		}

		out.println(".field static " + varName + varType + varValue);

	}

	private void generateGlobals() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.id == YalTreeConstants.JJTDECLARATION)
				generateDeclaration((ASTDeclaration) childRoot);
		}

	}

	private void generateStatic() {
		// TODO Auto-generated method stub

	}

	private void generateFunctions() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.id == YalTreeConstants.JJTFUNCTION)
				generateFunction((ASTFunction) childRoot);
		}
	}

	private String getVarType(SimpleNode var) {
		if (var.id == YalTreeConstants.JJTARRAYELEMENT)
			return "[I";
		else if (var.id == YalTreeConstants.JJTSCALARELEMENT)
			return "I";
		return null;

	}

	private void generateFuncHeader(ASTFunction functionNode) {
		String funcName, funcReturnType, funcArgs = "";

		funcName = functionNode.value;

		if (functionNode.hasVarList()) {
			SimpleNode varList = functionNode.getVarList();
			for (int i = 0; i < varList.jjtGetNumChildren(); i++) {
				SimpleNode var = (SimpleNode) varList.jjtGetChild(i);
				funcArgs += getVarType(var);
			}
		}

		switch (functionNode.getFuncReturnType()) {
		case SCALAR:
			funcReturnType = "I";
			break;
		case ARRAY:
			funcReturnType = "[I";
			break;
		case VOID:
			funcReturnType = "V";
			break;
		default:
			funcReturnType = "";
			break;
		}

		out.println(".method public static " + funcName + "(" + funcArgs + ")" + funcReturnType);

	}

	private void generateFunctionMainHeader(SimpleNode functionNode) {
		out.println(".method public static main([Ljava/lang/String;)V");
	}

	private void generateFunctionHeader(ASTFunction functionNode) {
		out.println();

		if (functionNode.isMainFunction())
			generateFunctionMainHeader(functionNode);
		else
			generateFuncHeader(functionNode);

		// TODO: limit
		out.println(".limit locals 10");
		out.println(".limit stack 10");
		out.println();

	}

	private void generateFunction(ASTFunction functionNode) {

		generateFunctionHeader(functionNode);

		generateBody(functionNode);

		generateFunctionFooter(functionNode);

	}

	private void generateFunctionFooter(ASTFunction functionNode) {
		String returnType;

		switch (functionNode.getFuncReturnType()) {
		case SCALAR:
			returnType = "a";
			break;
		case ARRAY:
			returnType = "i";
			break;
		case VOID:
			returnType = "";
			break;
		default:
			returnType = "";
			break;
		}

		out.println(returnType + "return");
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
				// generateWhile();
				break;
			case YalTreeConstants.JJTIF:
				// generateIf();
				break;
			default:
				break;
			}
		}
	}

	private void loadString(String string) {
		out.println("ldc " + string);
	}

	private void loadInt(String valueToLoad) {
		int value = Integer.parseInt(valueToLoad);
		if ((value >= 0) && (value <= 5)) {
			out.println("iconst_" + value);
		} else if (value == -1)
			out.println("iconst_m1");
		else
			out.println("bipush " + value);
	}

	private void loadGlobalVar(String varName) {
		String varType;

		if (root.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println("getstatic " + root.value + "/" + varName + varType);
	}

	private void generateCallArgs(SimpleNode callNode) {
		for (int i = 0; i < callNode.jjtGetNumChildren(); i++) {
			ASTArgument argument = (ASTArgument) callNode.jjtGetChild(i);

			switch (argument.getArgumentType()) {
			case YalTreeConstants.JJTSTRING:
				loadString(argument.value);
				break;
			case YalTreeConstants.JJTINTEGER:
				loadInt(argument.value);
				break;
			case YalTreeConstants.JJTID:
				if (root.symbolTable.containsSymbolName(argument.value)) {
					loadGlobalVar(argument.value);
				}
				// TODO else if for local variables and parameters
				break;
			default:
				break;
			}
		}
	}

	private void generateCallInvoke(SimpleNode callNode) {
		String funcName, funcRetType, funcArgs = "";

		funcName = callNode.value;

		for (int i = 0; i < callNode.jjtGetNumChildren(); i++) {
			ASTArgument argument = (ASTArgument) callNode.jjtGetChild(i);

			switch (argument.getArgumentType()) {
			case YalTreeConstants.JJTSTRING:
				funcArgs += "Ljava/lang/String";
				break;
			case YalTreeConstants.JJTINTEGER:
				funcArgs += "I";
				break;
			case YalTreeConstants.JJTID:
				funcArgs += "I";
				// TODO Vars can be arrays
				break;
			default:
				break;
			}

			if ((i + 1 != callNode.jjtGetNumChildren()) || (argument.getArgumentType() == YalTreeConstants.JJTSTRING))
				funcArgs += ";";

		}

		if (((SimpleNode) callNode.parent).id == YalTreeConstants.JJTTERM)
			funcRetType = "I";
		else
			funcRetType = "V";

		out.println("invokestatic " + funcName + "(" + funcArgs + ")" + funcRetType);
		out.println();

	}

	private void generateCall(SimpleNode callNode) {

		generateCallArgs(callNode);

		generateCallInvoke(callNode);

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

	private void generateRHS(ASTRhs rhs) {

		for (int i = 0; i < rhs.jjtGetNumChildren(); i++) {

			SimpleNode term = (SimpleNode) rhs.jjtGetChild(i);
			SimpleNode termChild = (SimpleNode) term.jjtGetChild(0);

			//TODO Special Case when a = 1 + - 1;
//			boolean isPositive = true;
//			if (term.value != null)
//				isPositive = false;

			switch (termChild.id) {
			case (YalTreeConstants.JJTINTEGER):
				loadInt(termChild.value);
				break;
			case (YalTreeConstants.JJTSCALARACCESS):
				if (root.symbolTable.containsSymbolName(termChild.value)) {
					loadGlobalVar(termChild.value);
				}

				// TODO else if for local variables and parameters

				// TODO .size
				break;
			case (YalTreeConstants.JJTCALL):
				generateCall(termChild);
				break;
			default:
				break;

			}

		}

		if (rhs.hasOperation()) {
			generateOperation(rhs);
			
		}
		//TODO store result in variable

	}

	private void generateAssign(SimpleNode node) {
		// Assign -> Lhs = Rhs
		/// Rhs -> Term OP Term | [ ArraySize ]
		// Term -> OP? ( INT | Call | ArrayAccess | ScalarAccess )

		ASTRhs rhs = (ASTRhs) node.jjtGetChild(1);
		generateRHS(rhs);

		// out.print(rhs.generateCode());

		// TODO: right now always assuming
		// ArrayAccess and ScalarAccess are
		// from static fields, need to cover
		// local variables aswell

		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
		generateLHS(lhs);

	}

	private void generateLHS(SimpleNode lhs) {
		String varName = lhs.value;

		if (root.symbolTable.containsSymbolName(varName)) {
			storeGlobalVar(varName);
		}
		// TODO else if for local variables and parameters

	}

	private void storeGlobalVar(String varName) {

		String varType;

		if (root.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println("putstatic " + root.value + "/" + varName + varType);

	}

}
