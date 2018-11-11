package algs4.part1.program4;

import java.util.Iterator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
	private final int dim;
	private final int[][] board;
	private final int posX;
	private final int posY;
	private int hamming;
	private int manhattan;
	private Queue<Board> neighbors;
	
	// construct a board from an n-by-n array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		if (blocks == null || blocks.length <= 1)
			throw new IllegalArgumentException();
		dim = blocks.length;
		board = new int[dim][dim];
		int x = 0, y = 0;
		for (int i = 0; i < dim; i++) {
    		for (int j = 0; j < dim; j++) {
    			board[i][j] = blocks[i][j];
    			if (board[i][j] == 0) {
    				x = i; y = j;
    			}
    		}
    	}
		posX = x; posY = y;
		hamming = manhattan = -1;
		neighbors = null;
	}
    
    public int dimension() { // board dimension n
    	return dim;
    }
    
    public int hamming() { // number of blocks out of place
    	if (hamming >= 0) return hamming;
    	hamming = 0;
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++)
				if (board[i][j] != 0 && board[i][j] != (i * dim + j + 1) % (dim * dim))
					hamming++;
    	return hamming;
    }
    
    public int manhattan() { // sum of Manhattan distances between blocks and goal
    	if (manhattan >= 0) return manhattan;
    	manhattan = 0;
    	for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (board[i][j] != 0) {
					int r = (board[i][j] - 1) / dim;
					int c = (board[i][j] - 1) % dim;
					manhattan += Math.abs(r - i) + Math.abs(c - j);
				}
			}
    	}
    	return manhattan;
    }
    
    public boolean isGoal() { // is this board the goal board?
    	for (int i = 0; i < dim; i++)
    		for (int j = 0; j < dim; j++)
    			if (board[i][j] != (i * dim + j + 1) % (dim * dim))
    				return false;
    	return true;
    }
    
    public Board twin() { // a board that is obtained by exchanging any pair of blocks
    	int[][] newBoard;
    	if (posX == 0)
    		newBoard = exchange(1, 0, 1, 1);
    	else
    		newBoard = exchange(0, 0, 0, 1);
    	return new Board(newBoard);
    }
    
    public boolean equals(Object y) { // does this board equal y?
    	if (y == null) return false;
    	if (y == this) return true;
    	if (y.getClass() != this.getClass())
    		return false;
    	Board aux = (Board)y;
    	if (aux.dim != dim)
    		return false;
    	if (aux.board.length != aux.board[0].length)
    		return false;
    	for (int i = 0; i < dim; i++)
    		for (int j = 0; j < dim; j++)
    			if (board[i][j] != aux.board[i][j])
    				return false;
    	return true;
    }
    
    public Iterable<Board> neighbors() { // all neighboring boards
    	if (neighbors != null)
    		return neighbors;
    	neighbors = new Queue<Board>();
    	int[] dx = {-1, 0, 1, 0};
    	int[] dy = {0, -1, 0, 1};
    	for (int i = 0; i < 4; i++) {
    		int x = posX + dx[i];
    		int y = posY + dy[i];
    		if (x >= 0 && x < dim && y >= 0 && y < dim) {
    			int[][] newBoard = exchange(x, y, posX, posY);
    			neighbors.enqueue(new Board(newBoard));
    		}
    	}
    	return neighbors;
    }
    
    public String toString() { // string representation of this board (in the output format specified below)
    	String str = "";
    	str += String.valueOf(dim) + '\n';
    	for (int i = 0; i < dim; i++) {
    		for (int j = 0; j < dim; j++)
    			str += ' ' + String.valueOf(board[i][j]) + ' ';
    		str += '\n';
    	}
    	return str;
    }
    
    private int[][] exchange(int x1, int y1, int x2, int y2) {
    	int[][] newBoard = new int[dim][dim];
    	for (int i = 0; i < dim; i++)
    		for (int j = 0; j < dim; j++)
    			newBoard[i][j] = board[i][j];
    	int temp = newBoard[x1][y1];
    	newBoard[x1][y1] = newBoard[x2][y2];
    	newBoard[x2][y2] = temp;
    	return newBoard;
    }
    
	public static void main(String[] args) { // unit tests (not graded)
    	// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println(initial.twin());
        Iterator<Board> iter = initial.neighbors().iterator();
        while (iter.hasNext())
        	StdOut.println(iter.next());
        StdOut.println(initial.hamming()); // 5
        StdOut.println(initial.manhattan()); // 10
        StdOut.println(initial.equals(initial.twin())); // false
    }
}
