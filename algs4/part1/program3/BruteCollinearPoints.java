package algs4.part1.program3;

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
	private ArrayList<LineSegment> segments = new ArrayList<>();
	
	public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
		if (points == null || points.length == 0 || hasNull(points))
			throw new IllegalArgumentException();
		Point[] copy = points.clone();
		Arrays.sort(copy);
		if (hasDuplicate(copy)) throw new IllegalArgumentException();
		int length = copy.length;
		for (int first = 0; first < length - 3; first++)
			for (int second = first + 1; second < length - 2; second++)
				for (int third = second + 1; third < length - 1; third++)
					for (int forth = third + 1; forth < length; forth++)
						if (copy[first].slopeTo(copy[second]) == copy[first].slopeTo(copy[third])
							&& copy[first].slopeTo(copy[third]) == copy[first].slopeTo(copy[forth]))
								segments.add(new LineSegment(copy[first], copy[forth]));			
	}
	
	public int numberOfSegments() { // the number of line segments
		return segments.size();
	}
	
	public LineSegment[] segments() { // the line segments
		return segments.toArray(new LineSegment[segments.size()]);
	}
	
	private boolean hasNull(Point[] points) {
		for (int i = 0; i < points.length; i++)
			if (points[i] == null)
				return true;
		return false;
	}
	
	private boolean hasDuplicate(Point[] points) {
		for (int i = 0; i < points.length - 1; i++)
			if (points[i].compareTo(points[i + 1]) == 0)
				return true;
		return false;
	}
	
	public static void main(String[] args) {
		// read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}
