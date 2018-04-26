import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {
	
	private SimpleNode root;
	
	//private String filename;
	
	private PrintWriter out;


	public CodeGenerator(SimpleNode root, String filename) throws IOException{
		this.root=(SimpleNode) root.children[0];
		//this.filename=filename;
		FileWriter fw = new FileWriter(filename, false);
	    BufferedWriter bw = new BufferedWriter(fw);
	    this.out = new PrintWriter(bw);
		
	}
	
	public void generateCode(){
		
		generateHeader();
		generateGlobals(root);
		generateStatic();
		generateFunctions();
		out.close();
		
	}

	private void generateHeader() {
		
		out.println(".class public " + root.value);
		out.println( ".super java/lang/Object"  + "\n");
	
		
	}
	
	private void generateGlobals(SimpleNode node) {
		
		for(int i=0; i< node.jjtGetNumChildren(); i++)
		{
			generateGlobals((SimpleNode) node.jjtGetChild(i));
		}
		
		if(node.id == YalTreeConstants.JJTDECLARATION){
			out.println(".field static " + node.value + " I" );
		}

		
	

		
	}
	
	private void generateStatic() {
		// TODO Auto-generated method stub

		
	}
	
	private void generateFunctions() {
		// TODO Auto-generated method stub
		
	}
}
