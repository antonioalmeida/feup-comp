import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {

	private SimpleNode root;

	// private String filename;

	private PrintWriter out;

	public CodeGenerator(SimpleNode root, String filename) throws IOException {
		this.root = (SimpleNode) root.children[0];
		// this.filename=filename;
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

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			generateGlobals((SimpleNode) node.jjtGetChild(i));
		}

		if (node.id == YalTreeConstants.JJTDECLARATION) {
			generateDeclaration(node);

		}

	}

	private void generateStatic() {
		// TODO Auto-generated method stub

	}

	private void generateFunctions() {
		for (int i = 0; i < root.jjtGetNumChildren(); i++) {
			SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);
			if (childRoot.id == YalTreeConstants.JJTFUNCTION) {
				generateFunction(childRoot);

			}
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
			} else
				out.println("()V");

		}

	}

	private void generateFunctionMainHeader(SimpleNode functionNode) {
		out.print(".method public static main([Ljava/lang/String;)V");

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
			} else
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
		} else
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

				break;
			case YalTreeConstants.JJTIF:

				break;
			default:
				break;
			}
		}

	}
	
	private void generateCall(SimpleNode functionChild) {
		out.print("invokestatic " + functionChild.value + "(");

		for (int i = 0; i < functionChild.jjtGetNumChildren(); i++)
			out.print("I");

		out.print(")");
		
		if(((SimpleNode)functionChild.parent).id==YalTreeConstants.JJTTERM)
			out.print("I");
		else out.print("V");

		out.println();
	}

	private void generateAssign(SimpleNode functionChild) {
		// TODO Auto-generated method stub

	}

	

	private void generateDeclaration(SimpleNode node) {

		SimpleNode scalarElement = (SimpleNode) node.children[0];
		// out.println(".field static "+ scalarElement.value + " I" );

		if (node.children.length == 2) {
			SimpleNode assignedElement = (SimpleNode) node.children[1];
			if (assignedElement.id == YalTreeConstants.JJTSCALARASSIGNED)
				out.println(".field static " + scalarElement.value + " I = " + assignedElement.value);
		} else
			out.println(".field static " + scalarElement.value + " I");

	}
}
