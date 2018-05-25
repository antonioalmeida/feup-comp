import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import semantic.Symbol;
import semantic.Symbol.Type;
import utils.Utils;

//invoke call nao distingue return array ou inteiro DOne
// quando faz a = 1 e "a" é array, espera que inicializa todos os elementos do array com 1

public class CodeGenerator {

	private static String TAB = "\t";
	private SimpleNode root;
	private PrintWriter out;

	private int number_of_loops = 1;

	public CodeGenerator(SimpleNode root) throws IOException {
		this.root = (SimpleNode) root.children[0];

		String filename = this.root.value + ".j";

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
		out.println();
		out.println(".method static public <clinit>()V");
		out.println(TAB + ".limit stack 10");
		out.println(TAB + ".limit locals 0");
		out.println();

		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.id == YalTreeConstants.JJTDECLARATION)
				if (((ASTDeclaration) childRoot).isVarArrayInitialized())
					generateArrayInitilization(childRoot);
			// generateArray
		}

		out.println(TAB + "return");
		out.println(".end method");

	}

	private void generateArrayInitilization(SimpleNode declaration) {
		SimpleNode scalarElement = ((SimpleNode) declaration.jjtGetChild(0));
		SimpleNode arraySize = (SimpleNode) declaration.jjtGetChild(1).jjtGetChild(0);

		String nameModule = this.root.value;
		String nameVariable = scalarElement.value;

		String sizeArray;
		// = arraySize.value;

		if (arraySize.jjtGetNumChildren() != 0) {
			SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);
			sizeArray = scalarAccess.value;
			loadGlobalVar(sizeArray);
		} else {
			sizeArray = arraySize.value;
			out.println(TAB + "bipush " + sizeArray);
		}

		out.println(TAB + "newarray int");
		out.println(TAB + "putstatic " + nameModule + "/" + nameVariable + " [I");
		out.println();
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

		funcName = functionNode.getFuncName();

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
		out.println(TAB + ".limit stack 10");
		out.println(TAB + ".limit locals 10");
		out.println();

	}

	private void generateFunction(ASTFunction functionNode) {
		generateFunctionHeader(functionNode);
		generateBody(functionNode);
		generateFunctionFooter(functionNode);
	}

	private void generateFunctionFooter(ASTFunction functionNode) {
		String returnType, varToReturn;

		switch (functionNode.getFuncReturnType()) {
		case SCALAR:
			returnType = "i";
			 varToReturn = functionNode.getVarNameToReturn();
			loadLocalVar(functionNode, varToReturn);
			break;
		case ARRAY:
			returnType = "a";
			varToReturn = functionNode.getVarNameToReturn();
			loadLocalVar(functionNode,  varToReturn);
			break;
		case VOID:
			returnType = "";
			break;
		default:
			returnType = "";
			break;
		}

		out.println(TAB + returnType + "return");
		out.println();
		out.println(".end method");
		out.println();

	}

	private void generateBody(SimpleNode functionNode) {
		for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
			SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);

			switch (functionChild.id) {
			case YalTreeConstants.JJTCALL:
				generateCall(functionChild);
				out.println();
				break;
			case YalTreeConstants.JJTASSIGN:
				generateAssign(functionChild);
				break;
			case YalTreeConstants.JJTWHILE:
				generateWhile(functionChild);
				break;
			case YalTreeConstants.JJTIFSTATEMENT:
				generateIfStatement(functionChild);
				break;
			default:
				break;
			}
		}
	}

	private String generateExprtest(SimpleNode exprTest) {

		SimpleNode lhs = (SimpleNode) exprTest.jjtGetChild(0);
		ASTRhs rhs = (ASTRhs) exprTest.jjtGetChild(1);

		generateLHSCompare(lhs);
		generateRHS(rhs);

		return generate_relation_op(exprTest.value);

	}

	private void generateWhile(SimpleNode functionChild) {
		int loop_number = number_of_loops;
		out.println("loop" + loop_number + ":");

		SimpleNode exprTest = (SimpleNode) functionChild.jjtGetChild(0);

		String relation = generateExprtest(exprTest);

		out.println(relation + " loop" + loop_number + "_end");

		// generate body while
		generateBody(functionChild);

		out.println("goto loop" + loop_number);
		out.println("loop" + loop_number + "_end:");

		number_of_loops++;
	}

	private void generateIfStatement(SimpleNode node) {
		SimpleNode ifNode = (SimpleNode) node.jjtGetChild(0);
		boolean hasElse = node.jjtGetNumChildren() > 1;

		int loopId = number_of_loops++;

		generateIf(ifNode, loopId, hasElse);

		if (node.jjtGetNumChildren() > 1) {
			SimpleNode elseNode = (SimpleNode) node.jjtGetChild(1);
			generateElse(elseNode, loopId);
		}
	}

	private void generateIf(SimpleNode node, int loopId, boolean hasElse) {
		SimpleNode exprTest = (SimpleNode) node.jjtGetChild(0);

		String relation = generateExprtest(exprTest);
		out.println(relation + " loop" + loopId + "_end");

		// generate If body
		generateBody(node);

		if (hasElse)
			out.println("goto loop" + loopId + "_next");
		out.println("loop" + loopId + "_end:");
	}

	private void generateElse(SimpleNode node, int loopId) {
		generateBody(node);

		out.println("loop" + loopId + "_next:");
	}

	private String generate_relation_op(String rela_op) {
		switch (rela_op) {
		case ">":
			return "if_icmple";
		case "<":
			return "if_icmpge";
		case "<=":
			return "if_icmpgt";
		case ">=":
			return "if_icmplt";
		case "==":
			return "if_icmpne";
		case "!=":
			return "if_icmpeq";
		default:
			break;
		}
		return "";

	}

	private void loadString(String string) {
		out.println(TAB + "ldc " + string);
	}

	private void loadInt(String valueToLoad) {
		int value = Integer.parseInt(valueToLoad);
		if ((value >= 0) && (value <= 5)) {
			out.println(TAB + "iconst_" + value);
		} else if (value == -1)
			out.println(TAB + "iconst_m1");
		else
			out.println(TAB + "bipush " + value);
	}

	private void loadGlobalVar(String varName) {
		String varType;

		if (root.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println(TAB + "getstatic " + root.value + "/" + varName + varType);
	}

	private void loadLocalVar(SimpleNode node, String varName) {
		int varIndex = node.getSymbolIndex(varName);
		String varType;
		String load;

		if (node.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = "i";
		else
			varType = "a";

		if (varIndex <= 3)
			load = "load_";
		else
			load = "load ";

		out.println(TAB + varType + load + varIndex);
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
				String varName = argument.value;
				if (root.symbolTable.containsSymbolName(varName)) {
					loadGlobalVar(varName);
				} else
					this.loadLocalVar(callNode, varName);
				break;
			default:
				break;
			}
		}
	}

	// TODO Refactor this
	private String addModuleToFunction(String funcName) {
		if (funcName.contains("."))
			return funcName;
		else
			return root.value + "." + funcName;

	}

	private void generateCallInvoke(SimpleNode callNode) {
		String funcName, funcRetType, funcArgs = "";

		funcName = callNode.value;
		funcName = addModuleToFunction(funcName);
		Vector<Symbol.Type> typesArgs = new Vector<Symbol.Type>();

		for (int i = 0; i < callNode.jjtGetNumChildren(); i++) {
			ASTArgument argument = (ASTArgument) callNode.jjtGetChild(i);

			switch (argument.getArgumentType()) {
			case YalTreeConstants.JJTSTRING:
				funcArgs += "Ljava/lang/String;";
				break;
			case YalTreeConstants.JJTINTEGER:
				funcArgs += "I";
				typesArgs.add(Symbol.Type.SCALAR);
				break;
			case YalTreeConstants.JJTID:
				// if()
				if (argument.getSymbolTable().getSymbolType(argument.value) == Symbol.Type.SCALAR) {
					funcArgs += "I";
					typesArgs.add(Symbol.Type.SCALAR);
				} else {
					funcArgs += "[I";
					typesArgs.add(Symbol.Type.ARRAY);
				}

				// TODO Vars can be arrays
				break;
			default:
				break;
			}
		}

//		if (((SimpleNode) callNode.parent).id == YalTreeConstants.JJTTERM)
//			funcRetType = "I";
//		else
//			funcRetType = "V";
		


//		System.out.println(callNode.value);
//		System.out.println(typesArgs.size());
//		System.out.println(typesArgs.get(0));
//		root.getFunctionTable().printFunctions("    ");
		
		
		
		
		
//		
		Vector<Type> typeReturnVector = root.getFunctionTable().getFunctionReturnType(callNode.value, typesArgs);

		// How to know the return type of external library
		if (typeReturnVector.size() == 0)
			funcRetType = "V";
		else {
			Symbol.Type returnType = root.getFunctionTable().getFunctionReturnType(callNode.value, typesArgs).get(0);

			if (returnType == Symbol.Type.SCALAR)
				funcRetType = "I";
			else if (returnType == Symbol.Type.ARRAY)
				funcRetType = "[I";
			else
				funcRetType = "V";

		}
		

		funcName = funcName.replace('.', '/');

		out.println(TAB + "invokestatic " + funcName + "(" + funcArgs + ")" + funcRetType);
	}

	private void generateCall(SimpleNode callNode) {
		generateCallArgs(callNode);
		generateCallInvoke(callNode);
	}

	private void generateOperation(SimpleNode rhs) {
		String operation = rhs.value;

		switch (operation) {
		case "+":
			operation = "iadd";
			break;
		case "-":
			operation = "isub";
			break;
		case "*":
			operation = "imul";
			break;
		case "/":
			operation = "idiv";
			break;
		case "<<":
			operation = "ishl";
			break;
		case ">>":
			operation = "ishr";
			break;
		case ">>>":
			operation = "iushr";
			break;
		case "&":
			operation = "iand";
			break;
		case "|":
			operation = "ior";
			break;
		case "^":
			operation = "ixor";
			break;
		default:
			break;
		}
		out.println(TAB + operation);

	}

	private void generateTerm(ASTRhs rhs) {
		for (int i = 0; i < rhs.jjtGetNumChildren(); i++) {

			SimpleNode term = (SimpleNode) rhs.jjtGetChild(i);
			SimpleNode termChild = (SimpleNode) term.jjtGetChild(0);

			// TODO Special Case when a = 1 + - 1;
			// boolean isPositive = true;
			// if (term.value != null)
			// isPositive = false;

			switch (termChild.id) {
			case (YalTreeConstants.JJTINTEGER):
				loadInt(termChild.value);
				break;
			case (YalTreeConstants.JJTSCALARACCESS):
				String varName = termChild.value;
				if (root.symbolTable.containsSymbolName(varName)) {
					loadGlobalVar(varName);
				} else
					this.loadLocalVar(rhs, varName);

				if (((ASTScalarAccess) termChild).getSizeArray())
					out.println(TAB + "arraylength");
				break;
			case (YalTreeConstants.JJTCALL):
				generateCall(termChild);
				break;

			case (YalTreeConstants.JJTARRAYACCESS):
				generateArrayAcess(termChild);
				out.println(TAB + "iaload");
				break;
			default:
				break;
			}
		}

		if (rhs.hasOperation()) {
			generateOperation(rhs);
		}

	}

	private void generateArrayAcess(SimpleNode arrayAcess) {
		String varName = arrayAcess.value;
		SimpleNode indexNode = (SimpleNode) arrayAcess.jjtGetChild(0);
		String indexValue = indexNode.value;

		// ai = a[i]

		// Load array a
		if (root.symbolTable.containsSymbolName(varName)) {
			loadGlobalVar(varName);
		} else
			this.loadLocalVar(arrayAcess, varName);

		// Load i value
		if (Utils.isInteger(indexValue)) {
			loadInt(indexValue);
		} else {

			if (root.symbolTable.containsSymbolName(indexValue)) {
				loadGlobalVar(indexValue);
			} else
				this.loadLocalVar(indexNode, indexValue);

		}

	}

	private void generateRHS(ASTRhs rhs) {
		SimpleNode rhsChild = (SimpleNode) rhs.jjtGetChild(0);

		if (rhsChild.id == YalTreeConstants.JJTTERM)
			generateTerm(rhs);
		else {
			generateArrayAssigned(rhsChild);
		}

	}

	private void generateArrayAssigned(SimpleNode arrayAssigned) {
		SimpleNode arraySize = (SimpleNode) arrayAssigned.jjtGetChild(0);
		
		if(arraySize.jjtGetNumChildren()!=0){
		
		SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);

		String varName = scalarAccess.value;
		if (root.symbolTable.containsSymbolName(varName)) {
			loadGlobalVar(varName);
		} else
			this.loadLocalVar(scalarAccess, varName);}
		else {
			loadInt(arraySize.value);
		}

		out.println(TAB + "newarray int");

	}

	private void generateAssign(SimpleNode node) {
		// Assign -> Lhs = Rhs
		/// Rhs -> Term OP Term | [ ArraySize ]
		// Term -> OP? ( INT | Call | ArrayAccess | ScalarAccess )

		// if (((ASTAssign) node).isArrayAssigned()) {
		//
		// } else {
		ASTRhs rhs = (ASTRhs) node.jjtGetChild(1);
		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);

		if (((ASTAssign) node).isArrayAcess()) {
			generateArrayAcess(lhs);
			generateRHS(rhs);
			out.println(TAB + "iastore");
			out.println();
		} else {
			generateRHS(rhs);
			generateLHSAssign(lhs);

		}

	}

	private void generateLHSAssign(SimpleNode lhs) {
		String varName = lhs.value;

		if (root.symbolTable.containsSymbolName(varName)) {
			storeGlobalVar(varName);
		} else
			storeLocalVar(lhs, varName);

		out.println();

	}

	private void generateLHSCompare(SimpleNode lhs) {
		String varName = lhs.value;

		if (root.symbolTable.containsSymbolName(varName)) {
			loadGlobalVar(varName);
		} else
			loadLocalVar(lhs, varName);

		out.println();

	}

	private void storeGlobalVar(String varName) {
		String varType;

		if (root.symbolTable.getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println(TAB + "putstatic " + root.value + "/" + varName + varType);
	}

	private void storeLocalVar(SimpleNode node, String varName) {
		// TODO for arrays
		int varIndex = node.getSymbolIndex(varName);

		String varType;
		if (node.symbolTable.getSymbolType(varName) == Symbol.Type.ARRAY)
			varType = "a";
		else
			varType = "i";

		String store = "store";

		if (varIndex <= 3)
			store = "store_";
		else
			store = "store ";

		out.println(TAB + varType + store + varIndex);
	}
}
