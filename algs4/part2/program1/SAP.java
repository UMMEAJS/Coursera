package algs4.part2.program1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    
    private static final int INF = Integer.MAX_VALUE;
    private int recentV = -1;
    private int recentW = -1;
    private int shortestLength;
    private int shortestAncestor;
    private Digraph graph;
    private int[] distToV;
    private int[] distToW;
    private boolean[] marked;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        graph = new Digraph(G);
        distToV = new int[graph.V()];
        distToW = new int[graph.V()];
        marked = new boolean[graph.V()];
        for (int i = 0; i < graph.V(); i++)
            distToV[i] = distToW[i] = INF;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        //This optimization cause a bug on test for a unknown reason
        if (v == recentV && w == recentW || v == recentW && w == recentV)
            return shortestLength == INF ? -1 : shortestLength;
        shortest(v, w);
        return shortestLength == INF ? -1 : shortestLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        //This optimization cause a bug on test for a unknown reason
        if (v == recentV && w == recentW || v == recentW && w == recentV)
            return shortestAncestor;
        shortest(v, w);
        return shortestAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        shortest(v, w);
        return shortestLength == INF ? -1 : shortestLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        shortest(v, w);
        return shortestAncestor;
    }
    
    private void shortestHelper(Queue<Integer> queue, Queue<Integer> track, boolean flag) {
        int[] distTo = distToV;
        if (flag) distTo = distToW;
        if (!queue.isEmpty()) {
            int v = queue.dequeue();
            track.enqueue(v);
            if (marked[v]) {
                int currLength = distToV[v] + distToW[v];
                if (currLength < shortestLength) {
                    shortestLength = currLength;
                    shortestAncestor = v;
                }
            }
            if (!marked[v])
                marked[v] = true;
            if (distTo[v] < shortestLength) {
                for (int vv : graph.adj(v)) {
                    if (distTo[vv] == INF) {
                        distTo[vv] = distTo[v] + 1;
                        queue.enqueue(vv);
                    }
                }
            }
        }
    }
    
    private void shortest(int v, int w) {
        shortestLength = INF;
        shortestAncestor = -1;
        Queue<Integer> vQ = new Queue<Integer>();
        Queue<Integer> wQ = new Queue<Integer>();
        Queue<Integer> track = new Queue<Integer>();
        distToV[v] = 0; vQ.enqueue(v);
        distToW[w] = 0; wQ.enqueue(w);
        
        while (!vQ.isEmpty() || !wQ.isEmpty()) {
            shortestHelper(vQ, track, false);
            shortestHelper(wQ, track, true);
        }
        while (!track.isEmpty()) {
            int id = track.dequeue();
            marked[id] = false;
            distToV[id] = distToW[id] = INF;
        }
        
        recentV = v;
        recentW = w;
    }
    
    private void shortest(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);
        
        int allShortestLength = INF;
        int allShortestAncestor = -1;
        
        for (int vv : v) {
            for (int ww : w) {
                shortest(vv, ww);
                if (shortestLength < allShortestLength) {
                    recentV = vv;
                    recentW = ww;
                    allShortestLength = shortestLength;
                    allShortestAncestor = shortestAncestor;
                }
            }
        }
        
        shortestLength = allShortestLength;
        shortestAncestor = allShortestAncestor;
    }
    
    private void checkVertex(int v) {
        if (v < 0 || v >= graph.V())
            throw new IllegalArgumentException();
    }
    
    private void checkIterable(Iterable<Integer> V) {
        if (V == null) throw new IllegalArgumentException();
        for (Integer v : V) {
            if (v == null)
                throw new IllegalArgumentException();
            checkVertex(v);
        }
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int length   = sap.length(8, 13); // 5
        int ancestor = sap.ancestor(8, 13); // 8
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
