package algs4.part1.program5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

public class PointSET {
	private final SET<Point2D> points;
	
	public PointSET() { // construct an empty set of points 
		points = new SET<Point2D>();
	}
	
	public boolean isEmpty() { // is the set empty? 
		return points.isEmpty();
	}
	
	public int size() { // number of points in the set 
		return points.size();
	}
	
	public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
		if (p == null) throw new IllegalArgumentException();
		points.add(p);
	}
	
	public boolean contains(Point2D p) { // does the set contain point p? 
		if (p == null) throw new IllegalArgumentException();
		return points.contains(p);
	}
	
	public void draw() { // draw all points to standard draw 
		for (Point2D point : points) point.draw();
	}
	
	public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary) 
		if (rect == null) throw new IllegalArgumentException();
		SET<Point2D> ret = new SET<Point2D>();
		for (Point2D point : points)
			if (rect.contains(point))
				ret.add(point);
		return ret;
	}
	
	public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty 
		if (p == null) throw new IllegalArgumentException();
		if (isEmpty()) return null;
		double min_distance = Integer.MAX_VALUE;
		Point2D ret = new Point2D(0, 0);
		for (Point2D point : points) {
			double distance = point.distanceTo(p);
			if (distance < min_distance) {
				min_distance = distance;
				ret = point;
			}
		}
		return ret;
	}
	
    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        
        double x = 0.0625;
        double y = 0.71875;
        Point2D query = new Point2D(x, y);

        StdOut.println(brute.nearest(query));
    }
}
