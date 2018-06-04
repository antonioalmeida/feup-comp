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
	private BitSet uses;
	private BitSet defs;
	
	/*public CodeLine() {
		this.index = ++countIndex;
		usesAndDefs = new ArrayList<Pair>();
	}*/
	
	public CodeLine(int index) {
		this.index = index;
		usesAndDefs = new ArrayList<Pair>();
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
	
	public void print() {
		System.out.print("("+index+") [ ");
		for(int i=0; i < usesAndDefs.size(); i++) {
			System.out.print(usesAndDefs.get(i).getKey()+"=> "+usesAndDefs.get(i).getValue()+", ");
		}
		System.out.print("]");
	}
	
	
}
