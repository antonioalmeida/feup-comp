package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;
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
	
	public void assignOut(Integer n, ArrayList<Integer> successors) {
		instructions.get(n).out.clear();
		for(int i = 0; i < successors.size(); i++) {
			instructions.get(n).out.or(instructions.get(successors.get(i)).in);
		}
		
	}
	
	public void assignIn(Integer n) {
		instructions.get(n).in.clear();
		BitSet auxiliar = new BitSet();
		auxiliar.or(instructions.get(n).out);
		auxiliar.andNot(instructions.get(n).getDefs());
		instructions.get(n).in.or(instructions.get(n).getUses());
		instructions.get(n).in.or(auxiliar);
		
		
	}
	
	public void livenessAnalysis() {
		for(int i = 0; i < instructions.size(); i++)
			instructions.get(i).defineBitSets(nameToIndex);
		
		boolean changedIteration = false;
		do {
			changedIteration = false;
		for(int i = instructions.size() - 1; i >= 0; i--) {
			BitSet auxiliar = new BitSet();
			if(!changedIteration)
				auxiliar = (BitSet) instructions.get(i).out.clone();
			
			assignOut(i, instructions.get(i).getSuccessors());
			if(!changedIteration && ! auxiliar.equals(instructions.get(i).out))
				changedIteration = true;
			
		}
		}while(changedIteration = false);
		
		
	}

}
