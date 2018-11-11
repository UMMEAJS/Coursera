package algs4.part1.program2;

import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> randomizeQueue = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			String string = StdIn.readString();
			randomizeQueue.enqueue(string);
		}
		Iterator<String> iter = randomizeQueue.iterator();
		for (int i = 0; i < k; i++)
			StdOut.println(iter.next());
	}
}
