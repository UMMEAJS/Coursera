package algs4.part1.program1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
	private final int length;
	private int openSites;
	private static final int numberOfDirection = 4;
	private static final int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
	private boolean[] openOrNot;
	private final WeightedQuickUnionUF isFull;
	private final WeightedQuickUnionUF isPercolated;
	
	private int dimensionCompress(int row, int col) { // compress 2-dimension coordinate to 1-dimension index
		return (row - 1) * length + (col - 1);
	}
	
	private void validate(int row, int col) { // check any argument is out of range or not
		if (row <= 0 || row > length || col <= 0 || col > length)
			throw new IllegalArgumentException();
	}
	
	public Percolation(int n) { // create n-by-n grid, with all sites blocked
		if (n <= 0) throw new IllegalArgumentException();
		length = n;
		openSites = 0;
		openOrNot = new boolean[n * n];
		for (int i = 0; i < n * n; i++)
			openOrNot[i] = false;
		isFull = new WeightedQuickUnionUF(n * n + 1);
		isPercolated = new WeightedQuickUnionUF(n * n + 2);
	}
   
	public void open(int row, int col) { // open site (row, col) if it is not open already
		if (isOpen(row, col)) return;
		int index = dimensionCompress(row, col);
		openOrNot[index] = true;
		openSites++;
		for (int i = 0; i < numberOfDirection; i++) {
			int r = row + direction[i][0];
			int c = col + direction[i][1];
			try {
				if (!isOpen(r, c)) continue;
			}
			catch(IllegalArgumentException e) {
				continue;
			}
			int index2 = dimensionCompress(r, c);
			isFull.union(index, index2);
			isPercolated.union(index, index2);
		}
		if (row == 1) {
			isFull.union(index, length * length);
			isPercolated.union(index, length * length);
		}
		if (row == length) isPercolated.union(index, length * length + 1);
	}
   
	public boolean isOpen(int row, int col) { // is site (row, col) open?
		validate(row, col);
		int index = dimensionCompress(row, col);
		return openOrNot[index];
	}
   
	public boolean isFull(int row, int col) { // is site (row, col) full?
		validate(row, col);
		int index = dimensionCompress(row, col);
		return isFull.connected(index, length * length);
	}
   
	public int numberOfOpenSites() { // number of open sites
		return openSites;
	}
   
	public boolean percolates() { // does the system percolate?
		return isPercolated.connected(length * length, length * length + 1);
	}

	public static void main(String[] args) { // test client (optional)
		int n = StdIn.readInt();
		Percolation p = new Percolation(n);
		p.open(1, 1);
		p.open(2, 2);
		p.open(1, 2);
		StdOut.println(p.isFull(2, 2));
		StdOut.println(p.percolates());
	}
}
