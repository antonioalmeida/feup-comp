package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import parser.Yal;
import utils.Pair;

public class CodeLine {
	//private static int countIndex = -1;
	private Integer index;
	ArrayList<Pair> usesAndDefs;
	private ArrayList<Integer> successors;
	private ArrayList<Integer> antecessors;
	private BitSet bitSuccessors;
	private BitSet bitAntecessors;
	private BitSet uses;
	private BitSet defs;
	BitSet in;
	BitSet out;
	
	/*public CodeLine() {
		this.index = ++countIndex;
		usesAndDefs = new ArrayList<Pair>();
	}*/
	
	public CodeLine(int index) {
		this.index = index;
		usesAndDefs = new ArrayList<Pair>();
		successors = new ArrayList<Integer>();
		antecessors = new ArrayList<Integer>();
		bitSuccessors = new BitSet();
		bitAntecessors = new BitSet();
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addUse(int symbolIndex) {
		uses.set(symbolIndex);
	}
	
	public void addDef(int symbolIndex) {
		defs.set(symbolIndex);
	}
	
	public BitSet getUses() {
		return uses;
	}
	
	public BitSet getDefs() {
		return defs;
	}
	
	public ArrayList<Integer> getSuccessors() {
		return successors;
	}
	
	public void addToUsesAndDefs(String symbolName, boolean store) {
		Pair pair = new Pair(symbolName, store);
		usesAndDefs.add(pair);
	}
	
	public void addSuccessor(Integer successor) {
		if(!bitSuccessors.get(successor) && successor != index) {
			successors.add(successor);
			bitSuccessors.set(successor);
		}
		
	}
	
	public void addAntecessor(Integer antecessor) {
		if(!bitAntecessors.get(antecessor) && antecessor != index) {
			antecessors.add(antecessor);
			bitAntecessors.set(antecessor);
		}
		/*else {
			System.out.println("b2: "+index+ " "+antecessor);
		}*/
	}
	
	public void defineUsesDefs(HashMap<String, Integer> nameToIndex, int maxIndex) {
		uses = new BitSet(maxIndex + 1);
		defs = new BitSet(maxIndex + 1);
		for(int i=0; i < usesAndDefs.size(); i++) {
			Integer newIndex = nameToIndex.get(usesAndDefs.get(i).getKey());
			if(Yal.getDebug())
				System.out.println("UsesAndDefs: "+usesAndDefs.get(i).getKey()+" => "+newIndex);
			if(newIndex != null) {
					
				if((boolean) usesAndDefs.get(i).getValue()) {
					defs.set(newIndex);
				}
				else
					uses.set(newIndex);
			}
		}
		in = new BitSet(maxIndex + 1);
		out = new BitSet(maxIndex + 1);
	}
	
	/*public void addSuccessors(ArrayList<Integer> successors) {
		this.successors.addAll(successors);
	}
	
	public void addAntecessors(ArrayList<Integer> antecessors) {
		this.antecessors.addAll(antecessors);
	}*/
	
	public void print() {
		System.out.print("("+index+") [ ");
		for(int i=0; i < usesAndDefs.size(); i++) {
			System.out.print(usesAndDefs.get(i).getKey()+"=> "+usesAndDefs.get(i).getValue()+", ");
		}
		System.out.print("]");
	}
	
	public void printSuccessorsAntecessors() {
		System.out.println(index);
		System.out.print("              antecessors: [");
		for(int i= 0; i < antecessors.size(); i++) {
			System.out.print(antecessors.get(i));
			if(i < antecessors.size() - 1 )
				System.out.print(", ");
		}
		System.out.println("]");
		
		System.out.print("              successors: [");
		for(int i= 0; i < successors.size(); i++) {
			System.out.print(successors.get(i));
			if(i < successors.size() - 1 )
				System.out.print(", ");
		}
		System.out.println("]");
	}
	
	
}
