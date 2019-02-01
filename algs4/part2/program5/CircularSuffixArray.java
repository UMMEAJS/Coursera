package algs4.part2.program5;

import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
	private final String text;
	private final int length;
	private int[] index;
	
	public CircularSuffixArray(String s) { // circular suffix array of s
		if (s == null)
			throw new IllegalArgumentException();
		text = s;
		length = text.length();
		index = new int[length];
		for (int i = 0; i < length; i++)
			index[i] = i;
		sort(0, length-1, 0);
	}
	
	private void sort(int lo, int hi, int d) {
		if (hi <= lo) return;
		if (d >= length) return;
		int lt = lo, gt = hi;
		char v = getChar(index[lo], d);
		int i = lo + 1;
		while (i <= gt) {
			char t = getChar(index[i], d);
			if (t < v) exch(lt++, i++);
			else if (t > v) exch(i, gt--);
			else i++;
		}
		sort(lo, lt-1, d);
		sort(lt, gt, d+1);
		sort(gt+1, hi, d);
	}
	
	private void exch(int i, int j) {
		int swap = index[i];
		index[i] = index[j];
		index[j] = swap;
	}
	
	private char getChar(int i, int d) {
		int idx = (i + d) % length;
		return text.charAt(idx);
	}
	
	public int length() { // length of s
		return length;
	}
	
	public int index(int i) { // returns index of ith sorted suffix
		if (i < 0 || i >= length)
			throw new IllegalArgumentException();
		return index[i];
	}
	
	public static void main(String[] args) { // unit testing (required)
		String str = "ABRACADABRA!";
		CircularSuffixArray csa = new CircularSuffixArray(str);
		StdOut.println(csa.length());
		for (int i = 0; i < csa.length(); i++)
			StdOut.println(csa.index(i));
	}
}
