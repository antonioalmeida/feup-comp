package codeGeneration;

import java.util.ArrayList;
import java.util.BitSet;

import utils.Pair;

public class CodeLine {
	private Integer index;
	//ArrayList<Pair> loadsStores;
	private ArrayList<Integer> successors;
	private ArrayList<Integer> antecessors;
	private BitSet uses;
	private BitSet defs;
	
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
	
	
}
