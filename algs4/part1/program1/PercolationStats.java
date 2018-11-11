package algs4.part1.program1;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private final double mean;
	private final double stddev;
	private final double lo;
	private final double hi;
	
	public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
		if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
		final double CONFIDENCE_1_96 = 1.96;
		double[] possibilities = new double[trials];
		for (int i = 0; i < trials; i++) {
			Percolation p = new Percolation(n);
			int[] sequence = new int[n * n];
			for (int j = 0; j < n * n; j++)
				sequence[j] = j;
			StdRandom.shuffle(sequence);
			for (int j = 0; j < n * n; j++) {
				int row = sequence[j] / n + 1;
				int col = sequence[j] % n + 1;
				p.open(row, col);
				if (p.percolates()) {
					double nsq = n * n;
					possibilities[i] = p.numberOfOpenSites() / nsq;
					break;
				}
			}
		}
		mean = StdStats.mean(possibilities);
		stddev = StdStats.stddev(possibilities);
		lo = mean - CONFIDENCE_1_96 * stddev / Math.sqrt(trials);
		hi = mean + CONFIDENCE_1_96 * stddev / Math.sqrt(trials);
	}
	
	public double mean() { // sample mean of percolation threshold
		return mean;
	}
	
	public double stddev() { // sample standard deviation of percolation threshold
		return stddev;
	}
	
	public double confidenceLo() { // low  endpoint of 95% confidence interval
		return lo;
	}
	
	public double confidenceHi() { // high endpoint of 95% confidence interval
		return hi;
	}
	
	public static void main(String[] args) { // test client (described below)
		int a = StdIn.readInt();
		int b = StdIn.readInt();
		PercolationStats p = new PercolationStats(a, b);
		double mean = p.mean();
		double stddev = p.stddev();
		double lo = p.confidenceLo();
		double hi = p.confidenceHi();
		StdOut.print("mean                    = ");
		StdOut.println(mean);
		StdOut.print("stddev                  = ");
		StdOut.println(stddev);
		StdOut.print("95% confidence interval = [");
		StdOut.print(lo);
		StdOut.print(", ");
		StdOut.print(hi);
		StdOut.println(']');
	}
}
