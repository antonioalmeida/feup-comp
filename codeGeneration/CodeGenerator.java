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

	private int number_of_loops = 1;
	private boolean otimizationR;
	private boolean otimizationO;

	public CodeGenerator(SimpleNode root) throws IOException {
		this.root = (SimpleNode) root.getChildren()[0];

		String filename = this.root.getValue() + ".j";

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
		out.println(".class public " + root.getValue());
		out.println(".super java/lang/Object" + "\n");
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

		out.println(".field static " + varName + varType + varValue);
	}

	private void generateGlobals() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

			if (childRoot.getId() == YalTreeConstants.JJTDECLARATION)
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

			if (childRoot.getId() == YalTreeConstants.JJTDECLARATION)

				if(((ASTDeclaration)childRoot).isVarArrayInitialized())
					generateArrayInitilization(childRoot, TAB);
			//	generateArray

		}

		out.println(TAB + "return");
		out.println(".end method");

	}

    private void generateArrayInitilization(SimpleNode declaration, String prefix) {
      SimpleNode scalarElement = ((SimpleNode) declaration.jjtGetChild(0));
      SimpleNode arraySize = (SimpleNode) declaration.jjtGetChild(1).jjtGetChild(0);

      String nameModule = this.root.getValue();
      String nameVariable = scalarElement.getValue();

      String sizeArray;
      // = arraySize.value;

      if (arraySize.jjtGetNumChildren() != 0) {
        SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);
        sizeArray = scalarAccess.getValue();
        loadGlobalVar(sizeArray, prefix);
      } else {
        sizeArray = arraySize.getValue();
        out.println(TAB + "bipush " + sizeArray);
      }

      out.println(TAB + "newarray int");
      out.println(TAB + "putstatic " + nameModule + "/" + nameVariable + " [I");

		out.println();
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
			loadLocalVar(functionNode, varToReturn, TAB);
			break;
		case ARRAY:
			returnType = "a";
			varToReturn = functionNode.getVarNameToReturn();
			loadLocalVar(functionNode,  varToReturn, TAB);
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

	private void generateBody(SimpleNode node) {
		generateBody(node, "");
	}

	private void generateBody(SimpleNode functionNode, String prefix) {
		for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
			SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);

			switch (functionChild.getId()) {
			case YalTreeConstants.JJTCALL:
				generateCall(functionChild, prefix + TAB);
				out.println();
				break;
			case YalTreeConstants.JJTASSIGN:
				generateAssign(functionChild, prefix + TAB);
				break;
			case YalTreeConstants.JJTWHILE:
				generateWhile(functionChild, prefix + TAB);
				break;
			case YalTreeConstants.JJTIFSTATEMENT:
				generateIfStatement(functionChild, prefix + TAB);
				break;
			default:
				break;
			}
		}
	}

	private String generateExprtest(SimpleNode exprTest, String prefix) {

		SimpleNode lhs = (SimpleNode) exprTest.jjtGetChild(0);
		ASTRhs rhs = (ASTRhs) exprTest.jjtGetChild(1);

		generateLHSCompare(lhs, prefix);
		generateRHS(rhs, prefix);

		return generate_relation_op(exprTest.getValue());

	}

	private void generateWhile(SimpleNode functionChild, String prefix) {
		int loop_number = number_of_loops;
		out.println("loop" + loop_number + ":");

		SimpleNode exprTest = (SimpleNode) functionChild.jjtGetChild(0);

		String relation = generateExprtest(exprTest, prefix);

		out.println(relation + " loop" + loop_number + "_end");

		// generate body while
		generateBody(functionChild, prefix);

		out.println("goto loop" + loop_number);
		out.println("loop" + loop_number + "_end:");

		number_of_loops++;
	}

	private void generateIfStatement(SimpleNode node, String prefix) {
		SimpleNode ifNode = (SimpleNode) node.jjtGetChild(0);
		boolean hasElse = node.jjtGetNumChildren() > 1;

		int loopId = number_of_loops++;

		generateIf(ifNode, loopId, hasElse, prefix);

		if (node.jjtGetNumChildren() > 1) {
			SimpleNode elseNode = (SimpleNode) node.jjtGetChild(1);
			generateElse(elseNode, loopId, prefix);
		}
	}

	private void generateIf(SimpleNode node, int loopId, boolean hasElse, String prefix) {
		SimpleNode exprTest = (SimpleNode) node.jjtGetChild(0);

		String relation = generateExprtest(exprTest, prefix);
		out.println(prefix + relation + " loop" + loopId + "_end");

		// generate If body
		generateBody(node, prefix);

		if(hasElse)
			out.println(prefix + TAB + "goto loop" + loopId + "_next");
		out.println(prefix + "loop" + loopId + "_end:");
	}

	private void generateElse(SimpleNode node, int loopId, String prefix) {
		generateBody(node, prefix);

		out.println(prefix + "loop" + loopId + "_next:");
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


	private void loadString(String string, String prefix) {
		out.println(prefix + "ldc " + string);
	}

	private void loadInt(String valueToLoad, String prefix) {
		int value = Integer.parseInt(valueToLoad);
		if ((value >= 0) && (value <= 5)) {
			out.println(prefix + "iconst_" + value);
		} else if (value == -1)
			out.println(prefix + "iconst_m1");
		else
			out.println(prefix + "bipush " + value);
	}

	private void loadGlobalVar(String varName, String prefix) {
		String varType;

		if (root.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println(prefix + "getstatic " + root.getValue() + "/" + varName + varType);
	}

	private void loadLocalVar(SimpleNode node, String varName, String prefix) {
		int varIndex = node.getSymbolIndex(varName);
		String varType;
		String load;

		if (node.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = "i";
		else
			varType = "a";

		if (varIndex <= 3)
			load = "load_";
		else
			load = "load ";

		out.println(prefix + varType + load + varIndex);
	}

	private void generateCallArgs(SimpleNode callNode, String prefix) {
		for (int i = 0; i < callNode.jjtGetNumChildren(); i++) {
			ASTArgument argument = (ASTArgument) callNode.jjtGetChild(i);

			switch (argument.getArgumentType()) {
			case YalTreeConstants.JJTSTRING:
				loadString(argument.getValue(), prefix);
				break;
			case YalTreeConstants.JJTINTEGER:
				loadInt(argument.getValue(), prefix);
				break;
			case YalTreeConstants.JJTID:
				String varName = argument.getValue();
				if (root.getSymbolTable().containsSymbolName(varName)) {
					loadGlobalVar(varName, prefix);
				} else
					this.loadLocalVar(callNode, varName, prefix);
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

	private void generateCallInvoke(SimpleNode callNode, String prefix) {
		String funcName, funcRetType, funcArgs = "";

		funcName = callNode.getValue();
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

//		if (((SimpleNode) callNode.parent).id == YalTreeConstants.JJTTERM)
//			funcRetType = "I";
//		else
//			funcRetType = "V";
		


//		System.out.println(callNode.value);
//		System.out.println(typesArgs.size());
//		System.out.println(typesArgs.get(0));
//		root.getFunctionTable().printFunctions("    ");
		
		
		
		
		
//		
		Vector<Type> typeReturnVector = root.getFunctionTable().getFunctionReturnType(callNode.getValue(), typesArgs);

		// How to know the return type of external library
		if (typeReturnVector.size() == 0)
			funcRetType = "V";
		else {
			Symbol.Type returnType = root.getFunctionTable().getFunctionReturnType(callNode.getValue(), typesArgs).get(0);

			if (returnType == Symbol.Type.SCALAR)
				funcRetType = "I";
			else if (returnType == Symbol.Type.ARRAY)
				funcRetType = "[I";
			else
				funcRetType = "V";

		}
		

		funcName = funcName.replace('.', '/');

		out.println(prefix + "invokestatic " + funcName + "(" + funcArgs + ")" + funcRetType);
	}

	private void generateCall(SimpleNode callNode, String prefix) {
		generateCallArgs(callNode, prefix);
		generateCallInvoke(callNode, prefix);
	}

	private void generateOperation(SimpleNode rhs, String prefix) {
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
		out.println(prefix + operation);

	}
	
	private void generateTerm(ASTRhs rhs, String prefix) {
		for (int i = 0; i < rhs.jjtGetNumChildren(); i++) {

			SimpleNode term = (SimpleNode) rhs.jjtGetChild(i);
			SimpleNode termChild = (SimpleNode) term.jjtGetChild(0);

			// TODO Special Case when a = 1 + - 1;
			// boolean isPositive = true;
			// if (term.value != null)
			// isPositive = false;

			switch (termChild.getId()) {
			case (YalTreeConstants.JJTINTEGER):
				loadInt(termChild.getValue(), prefix);
				break;
			case (YalTreeConstants.JJTSCALARACCESS):
				String varName = termChild.getValue();
				if (root.getSymbolTable().containsSymbolName(varName)) {
					loadGlobalVar(varName, prefix);
				} else
					this.loadLocalVar(rhs, varName, prefix);

				if (((ASTScalarAccess) termChild).getSizeArray())
					out.println(TAB + "arraylength");
				break;
			case (YalTreeConstants.JJTCALL):
				generateCall(termChild, prefix);
				break;

			case (YalTreeConstants.JJTARRAYACCESS):
				generateArrayAcess(termChild, prefix);
				out.println(TAB + "iaload");
				break;
			default:
				break;
			}
		}

		if (rhs.hasOperation()) {
			generateOperation(rhs,prefix);
		}

	}


	private void generateArrayAcess(SimpleNode arrayAcess, String prefix) {
		String varName = arrayAcess.getValue();
		SimpleNode indexNode = (SimpleNode) arrayAcess.jjtGetChild(0);
		String indexValue = indexNode.getValue();

		// ai = a[i]

		// Load array a
		if (root.getSymbolTable().containsSymbolName(varName)) {
			loadGlobalVar(varName, prefix);
		} else
			this.loadLocalVar(arrayAcess, varName, prefix);

		// Load i value
		if (Utils.isInteger(indexValue)) {
			loadInt(indexValue, prefix);
		} else {

			if (root.getSymbolTable().containsSymbolName(indexValue)) {
				loadGlobalVar(indexValue, prefix);
			} else
				this.loadLocalVar(indexNode, indexValue, prefix);

		}

	}

	private void generateRHS(ASTRhs rhs, String prefix) {
		SimpleNode rhsChild = (SimpleNode) rhs.jjtGetChild(0);

		if (rhsChild.getId() == YalTreeConstants.JJTTERM)
			generateTerm(rhs, prefix);
		else {
			generateArrayAssigned(rhsChild, prefix);
		}

	}

	private void generateArrayAssigned(SimpleNode arrayAssigned, String prefix) {
		SimpleNode arraySize = (SimpleNode) arrayAssigned.jjtGetChild(0);
		
		if(arraySize.jjtGetNumChildren()!=0){
		
		SimpleNode scalarAccess = (SimpleNode) arraySize.jjtGetChild(0);

		String varName = scalarAccess.getValue();
		if (root.getSymbolTable().containsSymbolName(varName)) {
			loadGlobalVar(varName, prefix);
		} else
			this.loadLocalVar(scalarAccess, varName, prefix);}
		else {
			loadInt(arraySize.getValue(), prefix);
		}

		out.println(TAB + "newarray int");

	}

	private void generateAssign(SimpleNode node, String prefix) {
		// Assign -> Lhs = Rhs
		/// Rhs -> Term OP Term | [ ArraySize ]
		// Term -> OP? ( INT | Call | ArrayAccess | ScalarAccess )
		// if (((ASTAssign) node).isArrayAssigned()) {
		//
		// } else {
		ASTRhs rhs = (ASTRhs) node.jjtGetChild(1);
		SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);

        if (((ASTAssign) node).isArrayAcess()) {
            generateArrayAcess(lhs, prefix);
            generateRHS(rhs,prefix);
            out.println(TAB + "iastore");
            out.println();
        //i = i + 1 || i = 1 + i (or similar cases)
        } else if(!root.getSymbolTable().containsSymbolName(lhs.getValue())&&// has to be local variable
                  rhs.getValue().equals("+") &&//has to be an increment
                 ( (lhs.getValue().equals(((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getValue())
                    && ((SimpleNode)rhs.jjtGetChild(1).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)//Term in lhs is equal to first term in rhs and second term in rhs is an integer
                    ||
                   (lhs.getValue().equals(((SimpleNode)rhs.jjtGetChild(1).jjtGetChild(0)).getValue())
                    && ((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)//Term in lhs is equal to second term in rhs and first term in rhs is an integer
                 ) ){
       
            int varIndex = node.getSymbolIndex(lhs.getValue());
               
            String constValue1 = ((SimpleNode)rhs.jjtGetChild(1).jjtGetChild(0)).getValue();
           
            String constValue2 = ((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getValue();
           
            if (((SimpleNode)rhs.jjtGetChild(1).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)//case integer is second term in rhs
                out.println(TAB + "iinc " + varIndex + " " + constValue1);
            else if (((SimpleNode)rhs.jjtGetChild(0).jjtGetChild(0)).getId() == YalTreeConstants.JJTINTEGER)//case integer is first term in rhs
                out.println(TAB + "iinc " + varIndex + " " + constValue2);
       
        } else {
            generateRHS(rhs,prefix);
            generateLHSAssign(lhs, prefix);

		}
	}

	private void generateLHSAssign(SimpleNode lhs, String prefix) {
		String varName = lhs.getValue();

		if (root.getSymbolTable().containsSymbolName(varName)) {
			storeGlobalVar(varName, prefix);
		} else
			storeLocalVar(lhs, varName, prefix);

		out.println();

	}

	private void generateLHSCompare(SimpleNode lhs, String prefix) {

		String varName = lhs.getValue();

		if (root.getSymbolTable().containsSymbolName(varName)) {
			loadGlobalVar(varName, prefix);
		} else
			loadLocalVar(lhs, varName, prefix);

		out.println();

	}

	private void storeGlobalVar(String varName, String prefix) {
		String varType;

		if (root.getSymbolTable().getSymbolType(varName) == Symbol.Type.SCALAR)
			varType = " I";
		else
			varType = " [I";

		out.println(prefix + "putstatic " + root.getValue() + "/" + varName + varType);
	}

	private void storeLocalVar(SimpleNode node, String varName, String prefix) {
		// TODO for arrays
		int varIndex = node.getSymbolIndex(varName);

		String varType;
		if (node.getSymbolTable().getSymbolType(varName) == Symbol.Type.ARRAY)
			varType = "a";
		else
			varType = "i";

		String store = "store";

		if (varIndex <= 3)
			store = "store_";
		else
			store = "store ";

		out.println(prefix + varType + store + varIndex);
	}
}