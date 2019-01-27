package algs4.part2.program4;

import edu.princeton.cs.algs4.StdOut;

public class RWayTrie
{
	private static final int R = 26;
	private Node root = new Node();
	
	public static class Node {
		private boolean flag;
		private Node[] next = new Node[R];
		
		public boolean getFlag() {
			return flag;
		}
		
		public Node getNode(char c) {
			return next[c - 'A'];
		}
	}

	public Node getRoot() {
		return root;
	}
	
	public void put(String key) {
		root = put(root, key, 0);
	}

	private Node put(Node x, String key, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) { x.flag = true; return x; }
		int c = key.charAt(d) - 'A';
		x.next[c] = put(x.next[c], key, d+1);
		return x;
	}
	
	public boolean contains(String key) {
		Node x = get(root, key, 0);
		if (x == null) return false;
		return x.flag;
	}
	
	private Node get(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) return x;
		int c = key.charAt(d) - 'A';
		return get(x.next[c], key, d+1);
	}
	
	public static void main(String[] args) {
		RWayTrie trie = new RWayTrie();
		trie.put("ABC");
		trie.put("ABCD");
		StdOut.println(trie.contains("ABCD"));
		Node r = trie.getRoot();
		r = r.getNode('A');
		r = r.getNode('B');
		r = r.getNode('C');
		if (r != null) StdOut.println(r.getFlag());
	}
}
