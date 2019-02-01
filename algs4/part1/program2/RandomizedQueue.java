package algs4.part1.program2;

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size;
	private static final int THRESHOLD = 1;
	
	@SuppressWarnings("unchecked")
	private Item[] items = (Item[]) new Object[1];
	
	public RandomizedQueue() { // construct an empty randomized queue
		size = 0;
	}
	
	public boolean isEmpty() { // is the randomized queue empty?
		return size == 0;
	}
	
	public int size() { // return the number of items on the randomized queue
		return size;
	}
	
	public void enqueue(Item item) { // add the item
		if (item == null) throw new IllegalArgumentException();
		if (size == items.length)
			resize(2 * size);
		items[size++] = item;
	}
	
	public Item dequeue() { // remove and return a random item
		if (isEmpty()) throw new NoSuchElementException();
		int randomIndex = StdRandom.uniform(size);
		Item returnItem = items[randomIndex];
		size--;
		if (randomIndex == size)
			items[randomIndex] = null;
		else {
			items[randomIndex] = items[size];
			items[size] = null;
		}
		if (size == items.length / 4)
			resize(items.length / 2);
		return returnItem;
	}
	
	public Item sample() { // return a random item (but do not remove it)
		if (isEmpty()) throw new NoSuchElementException();
		int randomIndex = StdRandom.uniform(size);
		return items[randomIndex];
	}
	
	public Iterator<Item> iterator() { // return an independent iterator over items in random order
		return new RandomQueueIterator();
	}
	
	private void resize(int capacity) {
		if (capacity <= THRESHOLD) return;
		@SuppressWarnings("unchecked")
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < size; i++)
			copy[i] = items[i];
		items = copy;
	}
	
	private class RandomQueueIterator implements Iterator<Item> {
		private int index;
		private final int[] indices;
		
		public RandomQueueIterator() {
			index = 0;
			indices = new int[size];
			for (int i = 0; i < indices.length; i++)
				indices[i] = i;
			StdRandom.shuffle(indices);
		}
		
		public boolean hasNext() { return index < indices.length; }
		
		public Item next() {
			if (!hasNext()) throw new NoSuchElementException();
			return items[indices[index++]];
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
	}
	
	public static void main(String[] args) {  // unit testing (optional)
		RandomizedQueue<String> items = new RandomizedQueue<String>();
		items.enqueue("1");
		items.enqueue("3");
		Iterator<String> iter = items.iterator();
		StdOut.println(iter.next());
		StdOut.println(iter.next());
	}
}
