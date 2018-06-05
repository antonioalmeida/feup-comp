package utils;

import java.util.BitSet;
import java.util.Vector;

public class Utils {
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	public static void printBitSet(BitSet bitSet) {
		for(int i = 0; i < bitSet.length(); i++) {
			System.out.print(bitSet.get(i) ? "1" : "0" );
		}
	}
	
	public static void printMatrixBoolean(Vector<Vector<Boolean>> v) {
		for(int i = 0; i < v.size(); i++) {
			for(int j=0; j < v.get(i).size(); j++) {
				if(v.get(i).get(j))
					System.out.print("1");
				else
					System.out.print("0");
			}
			System.out.println("");
		}
	}
}
