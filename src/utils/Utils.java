package utils;

import java.io.*;
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

	public static String getFileContent(FileInputStream fis, String encoding)
	{
	    StringBuilder sb = null;
		try( BufferedReader br =
					 new BufferedReader(new InputStreamReader(fis, encoding )))
		{
			sb = new StringBuilder();
			String line;
			while(( line = br.readLine()) != null ) {
				sb.append( line );
				sb.append( '\n' );
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return normalize(sb.toString());
	}

	public static String normalize(String str) {
		return str.trim().replaceAll(" +", " ");
	}
}
