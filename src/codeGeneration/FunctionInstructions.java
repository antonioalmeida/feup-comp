package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Vector;

import parser.Yal;
import utils.Utils;

public class FunctionInstructions {
	private ArrayList<CodeLine> instructions;
	private HashMap<String, Integer> nameToIndex;
	private int maxIndex;
	private int nArgs;
	private Vector<Vector<Boolean>> graphMatrix;
	private ArrayList<Integer> nIntersections;
	private ArrayList<Integer> removedFromGraph;
	private ArrayList<Integer> newIndexes;
	
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
	
	public void setNArgs(int nArgs) {
		this.nArgs = nArgs;
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
	
	
	
	public void livenessAnalysis(boolean debugMode) {
		
		
		boolean changedIteration = false;
		do {
			changedIteration = false;
		for(int i = instructions.size() - 1; i >= 0; i--) {
			BitSet auxiliar = new BitSet();
			if(!changedIteration)
				auxiliar = (BitSet) instructions.get(i).out.clone();
			if(debugMode) {
				System.out.print("Out("+i+"): ");
				Utils.printBitSet(instructions.get(i).out);
				System.out.print("  ");
			}
			assignOut(i, instructions.get(i).getSuccessors());
			if(!changedIteration && ! auxiliar.equals(instructions.get(i).out))
				changedIteration = true;
			
			auxiliar = new BitSet();
			if(!changedIteration)
				auxiliar = (BitSet) instructions.get(i).in.clone();
			
			if(debugMode) {
				System.out.print("In("+i+"): ");
				Utils.printBitSet(instructions.get(i).in);
				System.out.println("");
			}
			assignIn(i);
			if(!changedIteration && !auxiliar.equals(instructions.get(i).in))
				changedIteration = true;
			
		}
		}while(changedIteration == true);		
		
	}
	
	
	public void buildGraphMatrix() {
		graphMatrix = new Vector<Vector<Boolean>>(maxIndex + 1);
		for(int i = 0; i <= maxIndex; i++) {
			Vector<Boolean> newVector = new Vector<Boolean>(maxIndex + 1);
			for(int j=0; j <= maxIndex; j++)
				newVector.add(new Boolean(false));
			graphMatrix.add(newVector);
		}
		
		for(int i=0; i < instructions.size(); i++) {
			ArrayList<Integer> elementsIn = new ArrayList<Integer>();
			for(int j=0; j <= maxIndex; j++) {
				
				if(instructions.get(i).in.get(j)) {
					for(int k = 0; k < elementsIn.size(); k++) {
						
						graphMatrix.get(j).set(elementsIn.get(k), true);
						graphMatrix.get(elementsIn.get(k)).set(j,  true);
					}
					elementsIn.add(j);
					
				}
				
			}
			
			ArrayList<Integer> elementsOut = new ArrayList<Integer>();
			for(int j=0; j <= maxIndex; j++) {
				
				if(instructions.get(i).out.get(j)) {
					for(int k = 0; k < elementsOut.size(); k++) {
						
						graphMatrix.get(j).set(elementsOut.get(k), true);
						graphMatrix.get(elementsOut.get(k)).set(j,  true);
					}
					elementsOut.add(j);
					
				}
				
			}
		}
	}
	
	public void graphColoring(int nVariables) {
		nIntersections  = new ArrayList<Integer>();
		
		for(int i=0; i <= maxIndex; i++) {
			int countIntersections = 0;
			for(int j = 0; j <= maxIndex; j++) {
				if(graphMatrix.get(i).get(j))
					countIntersections ++;
			}
			nIntersections.add(countIntersections);
		}
		
		newIndexes = new ArrayList<Integer>();
		for(int i = 0; i <= maxIndex; i++)
			newIndexes.add(i);
		
		removedFromGraph = new ArrayList<Integer>();
		ArrayList<Boolean> indexesRemoved = new ArrayList<Boolean>();
		for(int i=0; i <= maxIndex; i++)
			indexesRemoved.add(false);
		
		//removes from graph
		while (removedFromGraph.size() < maxIndex + 1 - nArgs) {
			int minIntersections = 99999999;
			int minIndex = -1;
			for(int i =nArgs; i <= maxIndex; i++) {
				if(! indexesRemoved.get(i)) {
					if(minIndex == -1 || nIntersections.get(i) < minIntersections)
						minIntersections = nIntersections.get(i);
						minIndex = i;
				}
			}
			removedFromGraph.add(minIndex);
			indexesRemoved.set(minIndex, true);
			for(int i = 0; i <= maxIndex; i++) {
				if(i != minIndex && graphMatrix.get(i).get(minIndex)) {
					nIntersections.set(i, nIntersections.get(i) - 1);
				}
			}
		}
		
		int variablesUsed = nArgs;
		Vector<Vector<Integer>> registersToVariables = new Vector<Vector<Integer>> ();
		for(int i = 0; i < nArgs; i++) {
			Vector<Integer> newVector = new Vector<Integer>();
			newVector.add(i);
			registersToVariables.add(newVector);
		}
		for(int i = nArgs; i <= maxIndex; i++) {
			newIndexes.set(i, -1);
		}
		
		//Starts coloring the graph with the top of the stack
		while(removedFromGraph.size() > 0) {
			Integer topStack = removedFromGraph.get(removedFromGraph.size() - 1);
			removedFromGraph.remove(removedFromGraph.size() - 1);
			boolean registerAssigned = false;
			for(int i = 0; i < registersToVariables.size() && registerAssigned == false; i++) {
				boolean intersectionFound = false;
				for(int j = 0; j < registersToVariables.get(i).size() && intersectionFound == false; j++) {
					if(graphMatrix.get(i).get(topStack))
						intersectionFound = true;
				}
				if(intersectionFound == false) {
					registersToVariables.get(i).add(topStack);
					registerAssigned = true;
				}
			}
		}
		
		
	}
	
	public ArrayList<Integer>  registerAssignement(int nVariables){
		if(Yal.getDebug())
			System.out.println("maxIndex: "+maxIndex);
		for(int i = 0; i < instructions.size(); i++)
			instructions.get(i).defineUsesDefs(nameToIndex, maxIndex);
		if(Yal.getDebug())
			printUsesDefs();
		ArrayList<Integer> newIndexes = new ArrayList<Integer>();
		livenessAnalysis(true);
		buildGraphMatrix();
		
		if(Yal.getDebug())
			Utils.printMatrixBoolean(graphMatrix);
		
		return newIndexes;
	}
	
	public void printUsesDefs() {
		for(int i = 0; i < instructions.size(); i++) {
			System.out.print("Uses("+i+"): ");
			Utils.printBitSet(instructions.get(i).getUses());
			System.out.println("");
			System.out.print("Defs("+i+"): ");
			Utils.printBitSet(instructions.get(i).getDefs());
			System.out.println("");
		}
	}

}
