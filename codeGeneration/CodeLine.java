package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;

import utils.Pair;

public class CodeLine {
	private static int countIndex = -1;
	private Integer index;
	ArrayList<Pair> usesAndDefs;
	private ArrayList<Integer> successors;
	private ArrayList<Integer> antecessors;
	private BitSet uses;
	private BitSet defs;
	
	public CodeLine() {
		this.index = ++countIndex;
	}
	
	public CodeLine(int index) {
		this.index = index;
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
	
	
}
