package codeGeneration;

import java.util.ArrayList;
import java.util.HashMap;

public class FunctionInstructions {
	private ArrayList<CodeLine> instructions;
	private HashMap<String, Integer> nameToIndex;
	
	public FunctionInstructions(){
		instructions = new ArrayList<CodeLine>();
	}
	
	public void addInstruction(CodeLine line) {
		instructions.add(line);
	}
	
	public void setNameToIndex(HashMap<String, Integer> nameToIndex) {
		this.nameToIndex = nameToIndex;
	}
	
	public void addSuccessorsAntecessors(ArrayList<Integer> antecessors, Integer successor) {
		
		for(int i = 0; i < antecessors.size(); i++) {
			instructions.get(antecessors.get(i)).addSuccessor(successor);
			instructions.get(successor).addAntecessor(antecessors.get(i));
		}
	}
	public void printSuccessorsAntecessors() {
		for(int i = 0; i < instructions.size(); i++) {
			instructions.get(i).printSuccessorsAntecessors();
		}
	}

}
