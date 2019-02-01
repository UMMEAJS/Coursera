package algs4.part2.program5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private final static int R = 256;
	
    private static char[] initAlphabet() {
		char[] alphabet = new char[R];
		for (int i = 0; i < R; i++)
			alphabet[i] = (char)i;
		return alphabet;
	}
	
    private static int findIdx(char[] alphabet, char c) {
		for (int i = 0; i < alphabet.length; i++)
			if (alphabet[i] == c)
				return i;
		return -1;
	}
	
    private static void moveToFront(char[] alphabet, int idx) {
		char c = alphabet[idx];
		for (int i = idx-1; i >= 0; i--)
			alphabet[i+1] = alphabet[i];
		alphabet[0] = c;
	}
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	char[] alphabet = initAlphabet();
    	while (!BinaryStdIn.isEmpty()) {
    		char c = BinaryStdIn.readChar();
    		int idx = findIdx(alphabet, c);
    		BinaryStdOut.write((char)idx);
    		moveToFront(alphabet, idx);
    	}
    	BinaryStdOut.close();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	char[] alphabet = initAlphabet();
    	while (!BinaryStdIn.isEmpty()) {
    		int idx = (int)BinaryStdIn.readChar();
    		BinaryStdOut.write(alphabet[idx]);
    		moveToFront(alphabet, idx);
    	}
    	BinaryStdOut.close();
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    	char c = args[0].charAt(0);
    	if (c == '-') encode();
    	if (c == '+') decode();
    }
}
