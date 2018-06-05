package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;

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
	
	public void addToUsesAndDefs(String symbolName, boolean store) {
		Pair pair = new Pair(symbolName, store);
		usesAndDefs.add(pair);
	}
	
	public void addSuccessor(Integer successor) {
		if(!bitSuccessors.get(successor) && successor != index) {
			successors.add(successor);
			bitSuccessors.set(successor);
		}
		else {
			System.out.println("b2: "+index+ " "+successor);
		}
	}
	
	public void addAntecessor(Integer antecessor) {
		if(!bitAntecessors.get(antecessor) && antecessor != index) {
			antecessors.add(antecessor);
			bitAntecessors.set(antecessor);
		}
		else {
			System.out.println("b2: "+index+ " "+antecessor);
		}
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
