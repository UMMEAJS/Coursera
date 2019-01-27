package algs4.part2.program4;

import java.util.HashSet;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private RWayTrie dict;
    private static int dnum = 8;
    private static int[][] direction = { {-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1} };
	
	// Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	dict = new RWayTrie();
    	for (int i = 0; i < dictionary.length; i++)
    		dict.put(dictionary[i]);
    }

    private void getValidWords(int row, int col, StringBuilder word, RWayTrie.Node node, BoggleBoard board, HashSet<String> words, boolean[][] isVisit) {
    	if (row < 0 || row >= board.rows() || col < 0 || col >= board.cols())
    		return;
    	if (isVisit[row][col] == true)
    		return;
    	char c = board.getLetter(row, col);
    	node = node.getNode(c);
    	if (node == null)
    		return;
    	if (c == 'Q') {
    		node = node.getNode('U');
    		if (node == null)
    			return;
    	}
    	isVisit[row][col] = true;
    	word.append(c);
    	if (c == 'Q') word.append('U');
    	String str = word.toString();
    	if (node.getFlag() && scoreOf(str) > 0) words.add(str);
    	for (int i = 0; i < dnum; i++)
    		getValidWords(row + direction[i][0], col + direction[i][1], word, node, board, words, isVisit);
    	isVisit[row][col] = false;
    	int length = word.length();
    	if (length > 0) {
    		if (length > 1 && word.charAt(length - 1) == 'U' && word.charAt(length - 2) == 'Q')
    			word.delete(length - 2, length);
    		else word.deleteCharAt(word.length() - 1);
    	}
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	HashSet<String> words = new HashSet<String>();
    	RWayTrie.Node root = dict.getRoot();
    	StringBuilder word = new StringBuilder("");
    	boolean[][] isVisit= new boolean[board.rows()][board.cols()];
    	for (int i = 0; i < board.rows(); i++)
    		for (int j = 0; j < board.cols(); j++)
    			getValidWords(i, j, word, root, board, words, isVisit);
    	return words;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (!dict.contains(word)) return 0;
    	int length = word.length();
    	if (length <= 2) return 0;
    	else if (length <= 4) return 1;
    	else if (length == 5) return 2;
    	else if (length == 6) return 3;
    	else if (length == 7) return 5;
    	else return 11;
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
