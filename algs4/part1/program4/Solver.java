package algs4.part1.program4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private BoardNode target;
    private Stack<Board> solution;
	
    public Solver(Board initial) { // find a solution to the initial board (using the A* algorithm)
    	if (initial == null) throw new IllegalArgumentException();
    	target = null; solution = null;
    	MinPQ<BoardNode> minpq = new MinPQ<BoardNode>();
    	BoardNode root = new BoardNode(initial, null, false);
    	BoardNode twin = new BoardNode(initial.twin(), null, true);
    	minpq.insert(root);
    	minpq.insert(twin);
    	while (!minpq.isEmpty()) {
    		BoardNode curr = minpq.delMin();
    		if (curr.board.isGoal()) {
    			if (!curr.isTwin) target = curr;
    			break;
    		}
    		for (Board next : curr.board.neighbors()) {
    			if (curr.prev == null || !next.equals(curr.prev.board))
    				minpq.insert(new BoardNode(next, curr, curr.isTwin));
    		}
    	}
    }
    
    public boolean isSolvable() { // is the initial board solvable?
    	return target != null;
    }
    
    public int moves() { // min number of moves to solve initial board; -1 if unsolvable
    	if (!isSolvable()) return -1;
    	return target.moves;
    }
    
    public Iterable<Board> solution() { // sequence of boards in a shortest solution; null if unsolvable
    	if (!isSolvable()) return null;
    	if (solution != null) return solution;
    	solution = new Stack<Board>();
    	BoardNode aux = target;
    	while (aux != null) {
    		solution.push(aux.board);
    		aux = aux.prev;
    	}
    	return solution;
    }
    
    private class BoardNode implements Comparable<BoardNode> {
    	private final Board board;
    	private final BoardNode prev;
    	private final int moves;
    	private final int priority;
    	private final boolean isTwin;
    	
    	public BoardNode(Board board_, BoardNode prev_, boolean isTwin_) {
    		board = board_;
    		prev = prev_;
    		if (prev != null)
    			moves = prev.moves + 1;
    		else
    			moves = 0;
    		isTwin = isTwin_;
    		priority = board.manhattan() + moves;
    	}
    	
    	public int compareTo(BoardNode that) {
    		return Integer.compare(priority, that.priority);
    	}
    }
    
    public static void main(String[] args) { // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
