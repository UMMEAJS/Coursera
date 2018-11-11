package algs4.part2.program1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	
	private WordNet wordnet;
	
	public Outcast(WordNet wordnet) { // constructor takes a WordNet object
		if (wordnet == null)
			throw new IllegalArgumentException();
		this.wordnet = wordnet;
	}
	
	public String outcast(String[] nouns) { // given an array of WordNet nouns, return an outcast
		int[][] aux = new int[nouns.length][nouns.length];
		for (int i = 0; i < nouns.length - 1; i++) {
			for (int j = i + 1; j < nouns.length; j++) {
				aux[i][j] = wordnet.distance(nouns[i], nouns[j]);
				aux[j][i] = aux[i][j];
			}
		}
		int sum = -1, index = -1, curr;
		for (int i = 0; i < nouns.length; i++) {
			curr = 0;
			for (int j = 0; j < nouns.length; j++)
				curr += aux[i][j];
			if (sum == -1 || sum < curr) {
				sum = curr;
				index = i;
			}
		}
		return nouns[index];
	}
	
	public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
