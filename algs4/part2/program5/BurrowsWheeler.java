package algs4.part2.program5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
    	StringBuilder input = new StringBuilder();
    	while (!BinaryStdIn.isEmpty()) {
    		char c = BinaryStdIn.readChar();
    		input.append(c);
    	}
    	String text = input.toString();
    	
    	CircularSuffixArray csa = new CircularSuffixArray(text);
    	int length = text.length();
    	for (int i = 0; i < length; i++) {
    		if (csa.index(i) == 0) {
    			BinaryStdOut.write(i);
    			break;
    		}
    	}
    	for (int i = 0; i < length; i++) {
    		int idx = (csa.index(i) + length - 1) % length;
    		char c = text.charAt(idx);
    		BinaryStdOut.write(c);
    	}
    	BinaryStdOut.close();
    }
    
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
    	int first = BinaryStdIn.readInt();
    	StringBuilder input = new StringBuilder();
    	while (!BinaryStdIn.isEmpty()) {
    		char c = BinaryStdIn.readChar();
    		input.append(c);
    	}
    	String last = input.toString();
    	int length = last.length();
    	
    	int[] next = new int[length];
    	char[] head = new char[length];
    	int R = 256;
    	int count[] = new int[R+1];
    	for (int i = 0; i < length; i++)
    		count[last.charAt(i)+1]++;
    	for (int i = 0; i < R; i++)
    		count[i+1] += count[i];
    	for (int i = 0; i < length; i++) {
    		char c = last.charAt(i);
    		int idx = count[c]++;
    		next[idx] = i;
    		head[idx] = c;
    	}
    	
    	char c = head[first];
    	for (int i = 0; i < length; i++) {
    		BinaryStdOut.write(c);
    		first = next[first];
    		c = head[first];
    	}
    	BinaryStdOut.close();
    }
    
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
    	char c = args[0].charAt(0);
    	if (c == '-') transform();
    	if (c == '+') inverseTransform();
    }
}
