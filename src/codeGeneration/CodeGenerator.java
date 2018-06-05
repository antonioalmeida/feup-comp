package codeGeneration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import parser.ASTArgument;
import parser.ASTAssign;
import parser.ASTDeclaration;
import parser.ASTFunction;
import parser.ASTRhs;
import parser.ASTScalarAccess;
import parser.SimpleNode;
import parser.YalTreeConstants;
import semantic.Symbol;
import semantic.Symbol.Type;
import utils.Utils;

public class CodeGenerator {

	private static String TAB = "\t";
	private SimpleNode root;
	private PrintWriter out;

	private StringBuilder builder;

	private int number_of_loops = 1;
	private boolean otimizationR;
	private boolean otimizationO;
	
	private boolean hasUsedAnExtraReg = false;

	public CodeGenerator(SimpleNode root) throws IOException {
		this.root = (SimpleNode) root.getChildren()[0];

		String filename = this.root.getValue() + ".j";

		FileWriter fw = new FileWriter(filename, false);
		BufferedWriter bw = new BufferedWriter(fw);

		this.builder = new StringBuilder();
		this.out = new PrintWriter(bw);
	}

	public void generateCode() {
		generateHeader();
		generateGlobals();
		generateStatic();
		generateFunctions();

		out.println(builder);
		out.close();
	}

	private void appendln(String content) {
		builder.append(content);
		builder.append("\n");
	}

	private void appendln() {
		builder.append("\n");
	}

	private void generateHeader() {
		appendln(".class public " + root.getValue());
		appendln(".super java/lang/Object" + "\n");
	}

	private void generateDeclaration(ASTDeclaration declaration) {
		String varName, varType, varValue = "";
		SimpleNode element = (SimpleNode) declaration.getChildren()[0];
		varName = element.getValue();

		if (declaration.isVarArray())
			varType = " [I ";
		else {
			varType = " I ";
			if (declaration.isVarScalarAssigned()) {
				SimpleNode assignedScalar = (SimpleNode) declaration.getChildren()[1];
				varValue = "= " + assignedScalar.getValue();
			}
		}

		appendln(".field static " + varName + varType + varValue);
	}

	private void generateGlobals() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.getId() == YalTreeConstants.JJTDECLARATION)
				generateDeclaration((ASTDeclaration) childRoot);
		}
	}

	private void generateStatic() {
		appendln(".method static public <clinit>()V");

		int headerIndex = builder.length();
		StackController stack = new StackController();

		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.getId() == YalTreeConstants.JJTDECLARATION)

				if (((ASTDeclaration) childRoot).isVarArrayInitialized())
					generateArrayInitilization(childRoot, TAB, stack);
			// generateArray

		}

		appendln(TAB + "return");
		appendln(".end method");

		int limitStack = stack.getMax();

		StringBuilder localBuilder = new StringBuilder();
		//TODO: confirm that this is always 0
		localBuilder.append(TAB + ".limit locals 0");
		localBuilder.append("\n");
		localBuilder.append(TAB + ".limit stack " + limitStack);
		localBuilder.append("\n");
		localBuilder.append("\n");

		builder.insert(headerIndex, localBuilder);
	}

	private void generateArrayInitilization(SimpleNode declaration, String prefix, StackController stack) {
		SimpleNode scalarElement = ((SimpleNode) declaration.jjtGetChild(0));
		SimpleNode arraySize = (SimpleNode) declaration.jjtGetChild(1).jjtGetChild(0);

		String nameModule = this.root.getValue();
		String nameVariable = scalarElement.getValue();

		String sizeArray;
		// = arraySize.value;

		if (arraySize.jjtGetNumChildren() != 0) {
			SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);
			sizeArray = scalarAccess.getValue();
			loadGlobalVar(sizeArray, prefix, stack);
		} else {
			loadInt(arraySize, TAB, stack);
		}

		appendln(TAB + "newarray int");
		stack.addInstruction(YalInstructions.PUTSTATIC);
		appendln(TAB + "putstatic " + nameModule + "/" + nameVariable + " [I");

		appendln();
	}

	private void generateFunctions() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.getId() == YalTreeConstants.JJTFUNCTION)
				generateFunction((ASTFunction) childRoot);
		}
	}

	private String getVarType(SimpleNode var) {
		if (var.getId() == YalTreeConstants.JJTARRAYELEMENT)
			return "[I";
		else if (var.getId() == YalTreeConstants.JJTSCALARELEMENT)
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

		appendln(".method public static " + funcName + "(" + funcArgs + ")" + funcReturnType);
	}

	private void generateFunctionMainHeader(SimpleNode functionNode) {
		appendln(".method public static main([Ljava/lang/String;)V");
	}

	private void generateFunctionHeader(ASTFunction functionNode) {
		appendln();

		if (functionNode.isMainFunction())
			generateFunctionMainHeader(functionNode);
		else
			generateFuncHeader(functionNode);
	}

	private void generateFunction(ASTFunction functionNode) {
		StackController stack = new StackController();

		generateFunctionHeader(functionNode);
		int headerIndex = builder.length();

		generateBody(functionNode, stack);
		generateFunctionFooter(functionNode, stack);

		generateFunctionLimits(functionNode, headerIndex, stack);
	}

	public void generateFunctionLimits(ASTFunction functionNode, int headerIndex, StackController stack) {
		StringBuilder localBuilder = new StringBuilder();
		localBuilder.append('\n');

		int limitLocals = functionNode.getIndexCounter() + 1;
		int limitStack = stack.getMax();

		if (functionNode.isMainFunction())
			limitLocals++;
		
		if (hasUsedAnExtraReg)
			limitLocals++;

		localBuilder.append(TAB + ".limit locals " + limitLocals);
		localBuilder.append("\n");
		localBuilder.append(TAB + ".limit stack " + limitStack);
		localBuilder.append("\n");
		localBuilder.append("\n");

		builder.insert(headerIndex, localBuilder);
	}

	private void generateFunctionFooter(ASTFunction functionNode, StackController stack) {
		String returnType, varToReturn;

		switch (functionNode.getFuncReturnType()) {
		case SCALAR:
			returnType = "i";
			varToReturn = functionNode.getVarNameToReturn();
			loadLocalVar(functionNode, varToReturn, TAB, stack);
			break;
		case ARRAY:
			returnType = "a";
			varToReturn = functionNode.getVarNameToReturn();
			loadLocalVar(functionNode, varToReturn, TAB, stack);
			break;
		case VOID:
			returnType = "";
			break;
		default:
			returnType = "";
			break;
		}

		appendln(TAB + returnType + "return");
		appendln();
		appendln(".end method");
		appendln();

	}

	private void generateBody(SimpleNode node, StackController stack) {
		generateBody(node, "", stack);
	}

	private void generateBody(SimpleNode functionNode, String prefix, StackController stack) {
		for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
			SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);

			switch (functionChild.getId()) {
			case YalTreeConstants.JJTCALL:
				generateCall(functionChild, prefix + TAB, stack);
				appendln();
				break;
			case YalTreeConstants.JJTASSIGN:
				generateAssign(functionChild, prefix + TAB, stack);
				break;
			case YalTreeConstants.JJTWHILE:
				generateWhile(functionChild, prefix + TAB, stack);
				break;
			case YalTreeConstants.JJTIFSTATEMENT:
				generateIfStatement(functionChild, prefix + TAB, stack);
				break;
			default:
				break;
			}
		}
	}

	private String generateExprtest(SimpleNode exprTest, String prefix, StackController stack) {

		SimpleNode lhs = (SimpleNode) exprTest.jjtGetChild(0);
		ASTRhs rhs = (ASTRhs) exprTest.jjtGetChild(1);

		boolean compareToZero = false;
		if( rhs.jjtGetNumChildren()  == 1){
			SimpleNode termChild = (SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0); 
			if (termChild.getId() == YalTreeConstants.JJTINTEGER)
				if (termChild.getValue().equals("0"))
					compareToZero = true;
			
		}
		
		generateLHSCompare(lhs, prefix, stack);
		if (!compareToZero){
			generateRHS(rhs, prefix, stack);
			stack.addInstruction(YalInstructions.IF);
			return generate_relation_op(exprTest.getValue());
		} else {
			stack.addInstruction(YalInstructions.IF_ZERO);
			return generate_relation_op_zero(exprTest.getValue());
		}
	}

	private void generateWhile(SimpleNode functionChild, String prefix, StackController stack) {
		int loop_number = number_of_loops;
		appendln("loop" + loop_number + ":");

		SimpleNode exprTest = (SimpleNode) functionChild.jjtGetChild(0);

		String relation = generateExprtest(exprTest, prefix, stack);

		appendln(relation + " loop" + loop_number + "_end");

		// generate body while
		generateBody(functionChild, prefix, stack);

		appendln("goto loop" + loop_number);
		appendln("loop" + loop_number + "_end:");

		number_of_loops++;
	}

	private void generateIfStatement(SimpleNode node, String prefix, StackController stack) {
		SimpleNode ifNode = (SimpleNode) node.jjtGetChild(0);
		boolean hasElse = node.jjtGetNumChildren() > 1;

		int loopId = number_of_loops++;

		generateIf(ifNode, loopId, hasElse, prefix, stack);

		if (node.jjtGetNumChildren() > 1) {
			SimpleNode elseNode = (SimpleNode) node.jjtGetChild(1);
			generateElse(elseNode, loopId, prefix, stack);
		}
	}

	private void generateIf(SimpleNode node, int loopId, boolean hasElse, String prefix, StackController stack) {
		SimpleNode exprTest = (SimpleNode) node.jjtGetChild(0);

		String relation = generateExprtest(exprTest, prefix, stack);
		appendln(prefix + relation + " loop" + loopId + "_end");

		// generate If body
		generateBody(node, prefix, stack);

		if (hasElse)
			appendln(prefix + TAB + "goto loop" + loopId + "_next");
		appendln(prefix + "loop" + loopId + "_end:");
	}

	private void generateElse(SimpleNode node, int loopId, String prefix, StackController stack) {
		generateBody(node, prefix, stack);

		appendln(prefix + "loop" + loopId + "_next:");
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
	
	private String generate_relation_op_zero(String rela_op) {
		switch (rela_op) {
		case ">":
			return "ifle";
		case "<":
			return "ifge";
		case "<=":
			return "ifgt";
		case ">=":
			return "iflt";
		case "==":
			return "ifne";
		case "!=":
			return "ifeq";
		default:
			break;
		}
		return "";
	}

	private void loadString(String string, String prefix, StackController stack) {
		stack.addInstruction(YalInstructions.LDC);
		appendln(prefix + "ldc " + string);
	}

	private void loadInt(SimpleNode valueToLoad, String prefix, StackController stack) {
		int value = Integer.parseInt(valueToLoad.getValue());
		
		SimpleNode parentNode = (SimpleNode) valueToLoad.jjtGetParent();
		if(parentNode.getId()==YalTreeConstants.JJTTERM && parentNode.getValue().equals("-"))
			value=-value;

		if ((value >= 0) && (value <= 5)) {
			appendln(prefix + "iconst_" + value);
			stack.addInstruction(YalInstructions.ICONST);
		} else if (value == -1) {
			appendln(prefix + "iconst_m1");
			stack.addInstruction(YalInstructions.ICONST);
		}
		else if (value > -129 && value < 128) {
			stack.addInstruction(YalInstructions.BIPUSH);
			appendln(prefix + "bipush " + value);
		}
		else if (value > -32769 && value < 32768) {
			stack.addInstruction(YalInstructions.SIPUSH);
			appendln(prefix + "sipush " + value);
		}
		else {
			stack.addInstruction(YalInstructions.LDC);
			appendln(prefix + "ldc " + value);
		}
	}

	private void loadGlobalVar(String varName, String prefix, StackController stack) {
		String varType;

		if (root.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		stack.addInstruction(YalInstructions.GETSTATIC);
		appendln(prefix + "getstatic " + root.getValue() + "/" + varName + varType);
	}

	private void loadLocalVar(SimpleNode node, String varName, String prefix, StackController stack) {
		int varIndex = node.getSymbolIndex(varName);
		String varType;
		String load;

		if (node.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR) {
			varType = "i";
			stack.addInstruction(YalInstructions.ILOAD);
		}
		else {
			varType = "a";
			stack.addInstruction(YalInstructions.ALOAD);
		}

		if (varIndex <= 3)
			load = "load_";
		else
			load = "load ";

		appendln(prefix + varType + load + varIndex);
	}

	private void generateCallArgs(SimpleNode callNode, String prefix, StackController stack) {
		for (int i = 0; i < callNode.jjtGetNumChildren(); i++) {
			ASTArgument argument = (ASTArgument) callNode.jjtGetChild(i);

			switch (argument.getArgumentType()) {
			case YalTreeConstants.JJTSTRING:
				loadString(argument.getValue(), prefix, stack);
				break;
			case YalTreeConstants.JJTINTEGER:
				loadInt(argument, prefix, stack);
				break;
			case YalTreeConstants.JJTID:
				String varName = argument.getValue();
				if (root.getSymbolTable().containsSymbolName(varName)) {
					loadGlobalVar(varName, prefix, stack);
				} else
					this.loadLocalVar(callNode, varName, prefix, stack);
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
			return root.getValue() + "." + funcName;

	}

	private void generateCallInvoke(SimpleNode callNode, String prefix, StackController stack) {
		String funcName, funcRetType, funcArgs = "";
		boolean isMain = false;

		funcName = callNode.getValue();
		
		if(funcName.equals("main"))
			isMain=true;		
		
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
				if (argument.getSymbolTable().getSymbolType(argument.getValue()) == Symbol.Type.SCALAR) {
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

		// if (((SimpleNode) callNode.parent).id == YalTreeConstants.JJTTERM)
		// funcRetType = "I";
		// else
		// funcRetType = "V";

		// System.appendln(callNode.value);
		// System.appendln(typesArgs.size());
		// System.appendln(typesArgs.get(0));
		// root.getFunctionTable().printFunctions(" ");

		//
		Vector<Type> typeReturnVector = root.getFunctionTable().getFunctionReturnType(callNode.getValue(), typesArgs);

		// How to know the return type of external library
		if (typeReturnVector.size() == 0) {

			if (((SimpleNode) callNode.jjtGetParent()).getId() == YalTreeConstants.JJTTERM)
				funcRetType = "I";
			else
				funcRetType = "V";

		} else {
			Symbol.Type returnType = root.getFunctionTable().getFunctionReturnType(callNode.getValue(), typesArgs)
					.get(0);

			if (returnType == Symbol.Type.SCALAR)
				funcRetType = "I";
			else if (returnType == Symbol.Type.ARRAY)
				funcRetType = "[I";
			else
				funcRetType = "V";

		}
		

		funcName = funcName.replace('.', '/');
		
		if(isMain){
			appendln(prefix + "aconst_null");
			funcArgs+="[Ljava/lang/String;";					
		}


		int nArgs = callNode.jjtGetNumChildren();
		stack.addInstruction(-nArgs);
		appendln(prefix + "invokestatic " + funcName + "(" + funcArgs + ")" + funcRetType);
	}

	private void generateCall(SimpleNode callNode, String prefix, StackController stack) {
		generateCallArgs(callNode, prefix, stack);
		generateCallInvoke(callNode, prefix, stack);
	}

	private void generateOperation(SimpleNode rhs, String prefix, StackController stack) {
		String operation = rhs.getValue();

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

		stack.addInstruction(YalInstructions.OPERATION);
		appendln(prefix + operation);
	}

	private void generateTerm(ASTRhs rhs, String prefix, StackController stack) {
		for (int i = 0; i < rhs.jjtGetNumChildren(); i++) {

			SimpleNode term = (SimpleNode) rhs.jjtGetChild(i);
			SimpleNode termChild = (SimpleNode) term.jjtGetChild(0);

			// TODO Special Case when a = 1 + - 1;
			// boolean isPositive = true;
			// if (term.value != null)
			// isPositive = false;

			switch (termChild.getId()) {
			case (YalTreeConstants.JJTINTEGER):
				loadInt(termChild, prefix, stack);
				break;
			case (YalTreeConstants.JJTSCALARACCESS):
				String varName = termChild.getValue();
				if (root.getSymbolTable().containsSymbolName(varName)) {
					loadGlobalVar(varName, prefix, stack);
				} else
					this.loadLocalVar(rhs, varName, prefix, stack);

				if (((ASTScalarAccess) termChild).getSizeArray())
					appendln(TAB + "arraylength");
				
				if(term.getValue().equals("-"))
					appendln(TAB + "ineg");
				break;
			case (YalTreeConstants.JJTCALL):
				generateCall(termChild, prefix, stack);
				if(term.getValue().equals("-"))
					appendln(TAB + "ineg");
				break;

			case (YalTreeConstants.JJTARRAYACCESS):
				generateArrayAcess(termChild, prefix, stack);

				stack.addInstruction(YalInstructions.IALOAD);
				appendln(TAB + "iaload");
				if(term.getValue().equals("-"))
					appendln(TAB + "ineg");
				break;
			default:
				break;
			}
		}
		


		if (rhs.hasOperation()) {
			generateOperation(rhs, prefix, stack);
		}

	}

	private void generateArrayAcess(SimpleNode arrayAcess, String prefix, StackController stack) {
		String varName = arrayAcess.getValue();
		SimpleNode indexNode = (SimpleNode) arrayAcess.jjtGetChild(0);
		String indexValue = indexNode.getValue();

		// ai = a[i]

		// Load array a
		if (root.getSymbolTable().containsSymbolName(varName)) {
			loadGlobalVar(varName, prefix, stack);
		} else
			this.loadLocalVar(arrayAcess, varName, prefix, stack);

		// Load i value
		if (Utils.isInteger(indexValue)) {
			loadInt(indexNode, prefix, stack);
		} else {

			if (root.getSymbolTable().containsSymbolName(indexValue)) {
				loadGlobalVar(indexValue, prefix, stack);
			} else
				this.loadLocalVar(indexNode, indexValue, prefix, stack);

		}

	}

	private void generateRHS(ASTRhs rhs, String prefix, StackController stack) {
		SimpleNode rhsChild = (SimpleNode) rhs.jjtGetChild(0);

		if (rhsChild.getId() == YalTreeConstants.JJTTERM)
			generateTerm(rhs, prefix, stack);
		else {
			generateArrayAssigned(rhsChild, prefix, stack);
		}

	}

	private void generateArrayAssigned(SimpleNode arrayAssigned, String prefix, StackController stack) {
		SimpleNode arraySize = (SimpleNode) arrayAssigned.jjtGetChild(0);

		if (arraySize.jjtGetNumChildren() != 0) {

			SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);

			String varName = scalarAccess.getValue();
			if (root.getSymbolTable().containsSymbolName(varName)) {
				loadGlobalVar(varName, prefix, stack);
			} else
				this.loadLocalVar(scalarAccess, varName, prefix, stack);

			if (((ASTScalarAccess) scalarAccess).getSizeArray())
				appendln(TAB + "arraylength");

		} else {
			loadInt(arraySize, prefix, stack);
		}

		appendln(TAB + "newarray int");

	}

	private void generateAssign(SimpleNode node, String prefix, StackController stack) {
		// Assign -> Lhs = Rhs
		/// Rhs -> Term OP Term | [ ArraySize ]
		// Term -> OP? ( INT | Call | ArrayAccess | ScalarAccess )
		// if (((ASTAssign) node).isArrayAssigned()) {
		//
		// } else {
		ASTRhs rhs = (ASTRhs) node.jjtGetChild(1);
		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);

		if (((ASTAssign) node).isArrayAcess()) {
			generateArrayAcess(lhs, prefix, stack);
			generateRHS(rhs, prefix, stack);
			stack.addInstruction(YalInstructions.IASTORE);
			appendln(TAB + "iastore");
			appendln();
			// i = i + 1 || i = 1 + i (or similar cases)
		} else if (!root.getSymbolTable().containsSymbolName(lhs.getValue()) && 
				// has to be local variable
				rhs.getValue().equals("+") && // has to be an increment
				((lhs.getValue().equals(((SimpleNode) rhs.jjtGetChild(0).jjtGetChild(0)).getValue())
						&& ((SimpleNode) rhs.jjtGetChild(1).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)
						// Term in lhs is equal to first term in rhs and second term in rhs is an integer
						|| (lhs.getValue().equals(((SimpleNode) rhs.jjtGetChild(1).jjtGetChild(0)).getValue())
								&& ((SimpleNode) rhs.jjtGetChild(0).jjtGetChild(0))
										.getId() == YalTreeConstants.JJTINTEGER)
				// Term in lhs is equal to second term in rhs and first term in rhs is an integer
						)) {

			int varIndex = node.getSymbolIndex(lhs.getValue());

			String constValue1 = ((SimpleNode) rhs.jjtGetChild(1).jjtGetChild(0)).getValue();

			String constValue2 = ((SimpleNode) rhs.jjtGetChild(0).jjtGetChild(0)).getValue();

			if (((SimpleNode) rhs.jjtGetChild(1).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)
				// case integer is second term in rhs
				appendln(TAB + "iinc " + varIndex + " " + constValue1);
			else if (((SimpleNode) rhs.jjtGetChild(0).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)
				// case integer is first term in rhs
				appendln(TAB + "iinc " + varIndex + " " + constValue2);

		}
		else if((lhs.getSymbolTable().getSymbolType(lhs.getValue())==Symbol.Type.ARRAY ||
				root.getSymbolTable().getSymbolType(lhs.getValue())==Symbol.Type.ARRAY)
				&& (((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getId()==YalTreeConstants.JJTINTEGER ||
				((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getId()==YalTreeConstants.JJTSCALARACCESS)){ //just right integer  TODO more general
			int loop_number = number_of_loops;

			hasUsedAnExtraReg = true;

			int localI = rhs.getSymbolTable().getMaxIndex() + 1;

			SimpleNode rhsparent = (SimpleNode) rhs.jjtGetParent();
			while (rhsparent.getId() != YalTreeConstants.JJTFUNCTION)
				rhsparent = (SimpleNode) rhsparent.jjtGetParent();

			appendln();
			appendln(TAB + "iconst_0");
			stack.addInstruction(YalInstructions.ICONST);
			appendln(TAB + "istore " + localI); // istore para < 5
			stack.addInstruction(YalInstructions.ISTORE);

			appendln("loop" + loop_number + ":");
			appendln(TAB + "iload " + localI); // iload para < 5
			stack.addInstruction(YalInstructions.ILOAD);

			generateLHSCompare(lhs, prefix, stack);

			appendln(TAB + "arraylength");

			appendln("if_icmpge loop" + loop_number + "_end");
			stack.addInstruction(YalInstructions.IF);
			generateLHSCompare(lhs, prefix, stack);
			appendln(TAB + "iload " + localI); // iload para < 5
			stack.addInstruction(YalInstructions.ILOAD);
			generateRHS(rhs, prefix, stack);
			appendln(TAB + "iastore");
			stack.addInstruction(YalInstructions.IASTORE);

			appendln(TAB + "iinc " + localI + " 1");

			appendln("goto loop" + loop_number);
			appendln("loop" + loop_number + "_end:");
			number_of_loops++;

		}

		else {
			generateRHS(rhs, prefix, stack);
			generateLHSAssign(lhs, prefix, stack);
		}
	}

	private void generateLHSAssign(SimpleNode lhs, String prefix, StackController stack) {
		String varName = lhs.getValue();

		if (root.getSymbolTable().containsSymbolName(varName)) {
			storeGlobalVar(varName, prefix, stack);
		} else
			storeLocalVar(lhs, varName, prefix, stack);

		appendln();
	}

	private void generateLHSCompare(SimpleNode lhs, String prefix, StackController stack) {

		String varName = lhs.getValue();

		if (root.getSymbolTable().containsSymbolName(varName)) {
			loadGlobalVar(varName, prefix, stack);
		} else
			loadLocalVar(lhs, varName, prefix, stack);

		appendln();

	}

	private void storeGlobalVar(String varName, String prefix, StackController stack) {
		String varType;

		if (root.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		stack.addInstruction(YalInstructions.PUTSTATIC);
		appendln(prefix + "putstatic " + root.getValue() + "/" + varName + varType);
	}

	private void storeLocalVar(SimpleNode node, String varName, String prefix, StackController stack) {
		// TODO for arrays
		int varIndex = node.getSymbolIndex(varName);

		String varType;
		if (node.getSymbolTable().getSymbolType(varName) == Symbol.Type.ARRAY) {
			varType = "a";
			stack.addInstruction(YalInstructions.ASTORE);
		}
		else {
			varType = "i";
			stack.addInstruction(YalInstructions.ISTORE);
		}

		String store = "store";

		if (varIndex <= 3)
			store = "store_";
		else
			store = "store ";

		appendln(prefix + varType + store + varIndex);
	}
}
