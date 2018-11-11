package algs4.part2.program1;

import java.util.TreeMap;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {
	
	private SAP sap;
	private ArrayList<String> words;
	private TreeMap<String, Bag<Integer>> rwords;
	
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
    	checkNull(synsets, hypernyms);
    	
    	In synsetsIn = new In(synsets);
    	words = new ArrayList<String>();
    	rwords = new TreeMap<String, Bag<Integer>>();
    	while (!synsetsIn.isEmpty()) {
    		String line = synsetsIn.readLine();
    		String[] map = line.split(",");
    		String[] nouns = map[1].split(" ");
    		words.add(map[1]);
    		for (String noun : nouns) {
    			int id = Integer.valueOf(map[0]);
    			if (rwords.containsKey(noun))
    				rwords.get(noun).add(id);
    			else {
    				Bag<Integer> ids = new Bag<Integer>();
    				ids.add(id);
        			rwords.put(noun, ids);
    			}
    		}
    	}
    	
    	In hypernymsIn = new In(hypernyms);
    	Digraph graph = new Digraph(words.size());
    	while (!hypernymsIn.isEmpty()) {
    		String line = hypernymsIn.readLine();
    		String[] numbers = line.split(",");
    		int v = Integer.valueOf(numbers[0]);
    		for (String str : numbers) {
    			int w = Integer.valueOf(str);
    			if (w == v) continue;
    			graph.addEdge(v, w);
    		}
    	}
    	
    	checkRootedDAG(graph);
    	sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
    	return rwords.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
    	checkNull(word, "aux");
    	return rwords.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
    	checkNull(nounA, nounB);
    	checkValid(nounA, nounB);
    	Iterable<Integer> v = rwords.get(nounA);
    	Iterable<Integer> w = rwords.get(nounB);
    	return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
 	   	checkNull(nounA, nounB);
 	   	checkValid(nounA, nounB);
 	   	Iterable<Integer> v = rwords.get(nounA);
 	   	Iterable<Integer> w = rwords.get(nounB);
 	   	return words.get(sap.ancestor(v, w));
    }
    
    private void checkNull(Object obj1, Object obj2) {
    	if (obj1 == null || obj2 == null)
    		throw new IllegalArgumentException();
    }
    
    private void checkValid(String nounA, String nounB) {
    	if (!isNoun(nounA) || !isNoun(nounB))
    		throw new IllegalArgumentException();
    }
    
    private void checkRootedDAG(Digraph graph) {
    	int[] visit = new int[graph.V()];
    	for (int v = 0; v < graph.V(); v++) {
    		if (visit[v] == 1) continue;
    		checkDAGHelper(graph, 0, visit);
    	}
    	int rootCount = 0;
    	for (int v = 0; v < graph.V(); v++) {
    		if (!graph.adj(v).iterator().hasNext())
    			++rootCount;
    		if (rootCount > 1)
    			throw new IllegalArgumentException();
    	}
    }
    
    private void checkDAGHelper(Digraph graph, int v, int[] visit) {
    	visit[v] = -1;
    	for (int w : graph.adj(v)) {
    		if (visit[w] == 0)
    			checkDAGHelper(graph, w, visit);
    		if (visit[w] == -1)
    			throw new IllegalArgumentException();
    	}
    	visit[v] = 1;
    }

    // do unit testing of this class
    public static void main(String[] args) {
 	   	String synsets = args[0];
 	   	String hypernyms = args[1];
 	   	WordNet wordnet = new WordNet(synsets, hypernyms);
 	   	StdOut.println(wordnet.distance("change", "thing"));
 	   	StdOut.println(wordnet.sap("change", "thing"));
    }
}
