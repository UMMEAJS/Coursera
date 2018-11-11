package algs4.part1.program5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

public class KdTree {
	private Node root;
	private int size;
	
	private class Node {
		Node(Point2D p_) { p = p_; }
		Point2D p;
		Node left, right;
	}

	public KdTree() { // construct an empty set of points 
		//...
	}
	
	public boolean isEmpty() { // is the set empty? 
		return root == null;
	}
	
	public int size() { // number of points in the set 
		return size;
	}
	
	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null) throw new IllegalArgumentException();
		root = insert(p, root, 1);
	}
	
	private Node insert(Point2D p, Node node, int level) {
		if (node == null) {
			size++;
			return new Node(p);
		}
		if (p.equals(node.p))
			return node;
		if (level % 2 == 1) {
			if (p.x() < node.p.x())
				node.left = insert(p, node.left, level + 1);
			else node.right = insert(p, node.right, level + 1);
		}
		else {
			if (p.y() < node.p.y())
				node.left = insert(p, node.left, level + 1);
			else node.right = insert(p, node.right, level + 1);
		}
		return node;
	}
	
	public boolean contains(Point2D p) { // does the set contain point p? 
		if (p == null) throw new IllegalArgumentException();
		return contains(p, root, 1);
	}
	
	private boolean contains(Point2D p, Node node, int level) {
		if (node == null) return false;
		if (p.equals(node.p))
			return true;
		if (level % 2 == 1) {
			if (p.x() < node.p.x())
				return contains(p, node.left, level + 1);
			else return contains(p, node.right, level + 1);
		}
		else {
			if (p.y() < node.p.y())
				return contains(p, node.left, level + 1);
			else return contains(p, node.right, level + 1);
		}
	}
	
	public void draw() { // draw all points to standard draw 
		inorderTravel(root);
	}
	
	private void inorderTravel(Node node) {
		if (node == null) return;
		inorderTravel(node.left);
		node.p.draw();
		inorderTravel(node.right);
	}
	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary) 
		if (rect == null) throw new IllegalArgumentException();
		SET<Point2D> points = new SET<Point2D>();
		range(rect, root, points, 1);
		return points;
	}
	
	private void range(RectHV rect, Node node, SET<Point2D> points, int level) {
		if (node == null) return;
		if (rect.contains(node.p))
			points.add(node.p);
		if (level % 2 == 1) {
			if (rect.xmin() < node.p.x())
				range(rect, node.left, points, level + 1);
			if (rect.xmax() >= node.p.x())
				range(rect, node.right, points, level + 1);
		}
		else {
			if (rect.ymin() < node.p.y())
				range(rect, node.left, points, level + 1);
			if (rect.ymax() >= node.p.y())
				range(rect, node.right, points, level + 1);
		}
	}
	
	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null) throw new IllegalArgumentException();
		RectHV rect = new RectHV(0, 0, 1, 1);
		return nearest(rect, p, root, 1, null);
	}
	
	private RectHV getLeftRect(RectHV rect, Node node, int level) {
		RectHV left;
		if (level % 2 == 1)
			left = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
		else
			left = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
		return left;
	}
	
	private RectHV getRightRect(RectHV rect, Node node, int level) {
		RectHV right;
		if (level % 2 == 1)
			right = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
		else
			right = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
		return right;
	}
	
	private boolean cmp(Point2D p, Point2D p1, Point2D p2) {
		if (p1 == null) return true;
		return p.distanceSquaredTo(p1) > p.distanceSquaredTo(p2);
	}
	
	private Point2D nearest(RectHV rect, Point2D p, Node node, int level, Point2D min) {
		if (node == null) return min;
		if (p.equals(node.p)) return node.p;
		RectHV left = getLeftRect(rect, node, level);
		RectHV right = getRightRect(rect, node, level);
		if (cmp(p, min, node.p))
			min = node.p;
		boolean f1 = level % 2 == 1;
		boolean f2 = p.x() < node.p.x();
		boolean f3 = p.y() < node.p.y();
		if (f1 && f2 || !f1 && f3) {
			min = nearest(left, p, node.left, level + 1, min);
			if (right.distanceSquaredTo(p) < p.distanceSquaredTo(min))
				min = nearest(right, p, node.right, level + 1, min);
		}
		else {
			min = nearest(right, p, node.right, level + 1, min);
			if (left.distanceSquaredTo(p) < p.distanceSquaredTo(min))
				min = nearest(left, p, node.left, level + 1, min);
		}
		return min;
	}
	
	public static void main(String[] args) { // unit testing of the methods (optional) 
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kd = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kd.insert(p);
        }
        
        double x = 0.692;
        double y = 0.749;
        Point2D query = new Point2D(x, y);

        StdOut.println(kd.nearest(query));
	}
}
