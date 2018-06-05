package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Vector;

public class FunctionInstructions {
	private ArrayList<CodeLine> instructions;
	private HashMap<String, Integer> nameToIndex;
	private int maxIndex;
	private Vector<Vector<Boolean>> graphMatrix;
	
	public FunctionInstructions(){
		instructions = new ArrayList<CodeLine>();
	}
	
	public void addInstruction(CodeLine line) {
		instructions.add(line);
	}
	
	public void setNameToIndex(HashMap<String, Integer> nameToIndex) {
		this.nameToIndex = nameToIndex;
	}
	
	public void setMaxIndex(int maxIndex) {
		this.maxIndex = maxIndex;
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
	
	
	public void buildGraphMatrix() {
		graphMatrix = new Vector<Vector<Boolean>>(maxIndex + 1);
		for(int i = 0; i <= maxIndex; i++) {
			Vector<Boolean> newVector = new Vector<Boolean>(maxIndex + 1);
			for(int j=0; i < newVector.size(); j++)
				newVector.set(j, false);
			graphMatrix.add(newVector);
		}
		
		for(int i=0; i <= maxIndex; i++) {
			ArrayList<Integer> elementsIn = new ArrayList<Integer>();
			for(int j=0; j <= maxIndex; j++) {
				if(instructions.get(i).in.get(j)) {
					for(int k = 0; k < elementsIn.size(); i++) {
						graphMatrix.get(j).set(elementsIn.get(k), true);
						graphMatrix.get(elementsIn.get(k)).set(j,  true);
					}
					elementsIn.add(j);
					
				}
				
			}
			
			ArrayList<Integer> elementsOut = new ArrayList<Integer>();
			for(int j=0; j <= maxIndex; j++) {
				if(instructions.get(i).in.get(j)) {
					for(int k = 0; k < elementsOut.size(); i++) {
						graphMatrix.get(j).set(elementsOut.get(k), true);
						graphMatrix.get(elementsOut.get(k)).set(j,  true);
					}
					elementsOut.add(j);
					
				}
				
			}
		}
	}
	
	public ArrayList<Integer>  registerAssignement(int nVariables){
		ArrayList<Integer> newIndexes = new ArrayList<Integer>();
		livenessAnalysis();
		buildGraphMatrix();
		
		return newIndexes;
	}

}
