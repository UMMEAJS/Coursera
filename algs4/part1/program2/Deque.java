package algs4.part1.program2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	private int size;
	private Node head;
	private Node tail;
	
	private class Node {
		private Item var;
		private Node prev;
		private Node next;
		public Node(Item item) {
			var = item;
			prev = next = null;
		}
	}
	
	public Deque() { // construct an empty deque
		size = 0;
		head = tail = null;
	}
	
	public boolean isEmpty() { // is the deque empty?
		return head == null;
	}
	
	public int size() { // return the number of items on the deque
		return size;
	}
	
	public void addFirst(Item item) { // add the item to the front
		if (item == null) throw new IllegalArgumentException();
		if (isEmpty()) head = tail = new Node(item);
		else {
			Node temp = new Node(item);
			temp.next = head;
			head.prev = temp;
			head = temp;
		}
		size++;
	}
	
	public void addLast(Item item) { // add the item to the end
		if (item == null) throw new IllegalArgumentException();
		if (isEmpty()) head = tail = new Node(item);
		else {
			Node temp = new Node(item);
			tail.next = temp;
			temp.prev = tail;
			tail = temp;
		}
		size++;
	}
	
	public Item removeFirst() { // remove and return the item from the front
		if (isEmpty()) throw new NoSuchElementException();
		Item var = head.var;
		head = head.next;
		if (head == null) tail = null;
		else head.prev = null;
		size--;
		return var;
	}
	
	public Item removeLast() { // remove and return the item from the end
		if (isEmpty()) throw new NoSuchElementException();
		Item var = tail.var;
		tail = tail.prev;
		if (tail == null) head = null;
		else tail.next = null;
		size--;
		return var;
	}
	
	public Iterator<Item> iterator() { // return an iterator over items in order from front to end
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<Item> {
		private Node first;
		public DequeIterator() { first = head; }
		public boolean hasNext() { return first != null; }
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			Item temp =  first.var;
			first = first.next;
			return temp;
		}
		public void remove() { throw new UnsupportedOperationException(); }
	}
	
	public static void main(String[] args) { // unit testing (optional)
		Deque<String> d = new Deque<String>();
		StdOut.println(d.isEmpty());
		d.addLast("ss");
		d.addFirst("aa");
		Iterator<String> iter = d.iterator();
		StdOut.println(iter.next());
		StdOut.println(iter.next());
		StdOut.println(d.isEmpty());
		StdOut.println(d.size());
		String s = d.removeFirst();
		String sa = d.removeLast();
		StdOut.println(s);
		StdOut.println(sa);
		StdOut.println(d.isEmpty());
	}
}
