package algs4.part2.program2;

import java.awt.Color;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.IndexMinPQ;

public class SeamCarver {
	private int width;
	private int height;
	private int rowLen;
	private int colLen;
	private boolean isVertical;
	private int[][] buffer;
	private double[][] energyMatrix;
	
	public SeamCarver(Picture picture) { // create a seam carver object based on the given picture
		if (picture == null || picture.width() == 0 || picture.height() == 0)
			throw new IllegalArgumentException();
		
		width = picture.width();
		height = picture.height();
		rowLen = width;
		colLen = height;
		isVertical = true;
		
		buffer = new int[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				buffer[i][j] = picture.getRGB(j, i);
		
		energyMatrix = new double[height][width];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				energyMatrix[i][j] = energy(j, i);
	}
	
	public Picture picture() { // current picture
		Picture pic = new Picture(width, height);
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
            	pic.setRGB(j, i, buffer[i][j]);
		return pic;
	}
	
	public int width() { // width of current picture
		return width;
	}
	
	public int height() { // height of current picture
		return height;
	}
	
	private int delt(Color c1, Color c2) {
		int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();
		int r2 = c2.getRed();
		int g2 = c2.getGreen();
		int b2 = c2.getBlue();
		return (r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2);
	}
	
	private void checkRange(int number, boolean isX) {
		if (number < 0 || number >= (isX ? width : height))
			throw new IllegalArgumentException();
	}
	
	private void checkSeam(int[] seam) { 
		if (rowLen <= 1 || seam == null || seam.length != colLen)
			throw new IllegalArgumentException();
		for (int i = 0; i < seam.length; i++)
			checkRange(seam[i], isVertical);
		for (int i = 1; i < seam.length; i++)
			if (Math.abs(seam[i]-seam[i-1]) > 1)
				throw new IllegalArgumentException();
	}
	
	public double energy(int x, int y) { // energy of pixel at column x and row y
		checkRange(x, true);
		checkRange(y, false);
		if (x == 0 || x == width-1 || y == 0 || y == height-1)
			return 1000;
		Color rgbl = new Color(buffer[y][x-1]);
		Color rgbr = new Color(buffer[y][x+1]);
		Color rgbu = new Color(buffer[y-1][x]);
		Color rgbd = new Color(buffer[y+1][x]);
		int delt1 = delt(rgbl, rgbr);
		int delt2 = delt(rgbu, rgbd);
		return Math.sqrt(delt1+delt2);
	}
	
	private void relax(int v, int w, int[] vecTo, double[] distTo, IndexMinPQ<Double> pq) {
		double weight = 0.0;
		if (w != width * height + 1) {
			if (isVertical)
				weight = energyMatrix[(w-1)/rowLen][(w-1)%rowLen];
			else
				weight = energyMatrix[(w-1)%rowLen][(w-1)/rowLen];
		}
		if (distTo[w] > distTo[v] + weight) {
			vecTo[w] = v;
			distTo[w] = distTo[v] + weight;
			if (pq.contains(w)) pq.changeKey(w, distTo[w]);
			else pq.insert(w, distTo[w]);
		}
	}
	
	private void transpose() {
		isVertical = !isVertical;
		int tmp = rowLen;
		rowLen = colLen;
		colLen = tmp;
	}
	
	public int[] findHorizontalSeam() { // sequence of indices for horizontal seam
		transpose();
		int[] seam = findVerticalSeam();
		transpose();
		return seam;
	}
	
	public int[] findVerticalSeam() { // sequence of indices for vertical seam
		int V = width * height + 2;
		int[] vecTo = new int[V];
		double[] distTo = new double[V];
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(V);
		
		distTo[0] = 0.0;
		pq.insert(0, distTo[0]);
		for (int i = 1; i < V; i++)
			distTo[i] = Double.POSITIVE_INFINITY;
		
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			if (v == V - 1) {
				continue;
			} else if (v == 0) {
				for (int i = 1; i <= rowLen; i++)
					relax(v, i, vecTo, distTo, pq);
			} else if (v >= V - 1 - rowLen) {
				relax(v, V-1, vecTo, distTo, pq);
			} else {
				relax(v, v+rowLen, vecTo, distTo, pq);
				if (v % rowLen != 1)
					relax(v, v+rowLen-1, vecTo, distTo, pq);
				if (v % rowLen != 0)
					relax(v, v+rowLen+1, vecTo, distTo, pq);
			}
		}
		 
		int currVec = V - 1;
		int[] seam = new int[colLen];
		for (int i = colLen-1; i >= 0 ; i--) {
			seam[i] = (vecTo[currVec]-1) % rowLen;
			currVec = vecTo[currVec];
		}
		return seam;
	}
	
	public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from current picture
		transpose();
		removeVerticalSeam(seam);
		transpose();
	}
	
	public void removeVerticalSeam(int[] seam) { // remove vertical seam from current picture
		checkSeam(seam);
		for (int i = 0; i < colLen; i++) {
			if (isVertical) {
				for (int j = seam[i]; j < rowLen-1; j++) {
					buffer[i][j] = buffer[i][j+1];
					energyMatrix[i][j] = energyMatrix[i][j+1];
				}
			}
			else {
				for (int j = seam[i]; j < rowLen-1; j++) {
					buffer[j][i] = buffer[j+1][i];
					energyMatrix[j][i] = energyMatrix[j+1][i];
				}
			}
		}
		
		rowLen--;
		if (isVertical) width = rowLen;
		else height = rowLen;
		
		for (int i = 0; i < colLen; i++) {
			if (isVertical) {
				if (seam[i] > 0)
					energyMatrix[i][seam[i]-1] = energy(seam[i]-1, i);
				if (seam[i] < rowLen)
					energyMatrix[i][seam[i]] = energy(seam[i], i);
			}
			else {
				if (seam[i] > 0)
					energyMatrix[seam[i]-1][i] = energy(i, seam[i]-1);
				if (seam[i] < rowLen)
					energyMatrix[seam[i]][i] = energy(i, seam[i]);
			}
		}
	}
	
	public static void main(String[] args) {
		String picName = "src/algs4/part2/program2/HJocean.png";
		Picture pic = new Picture(picName);
		SeamCarver sc = new SeamCarver(pic);
		int[] vseam = sc.findVerticalSeam();
		for (int i = 0; i < vseam.length; i++)
			StdOut.printf("%d ", vseam[i]);
		sc.removeVerticalSeam(vseam);
		StdOut.printf("\n%d %d\n", sc.width(), sc.height());
		int[] hseam = sc.findHorizontalSeam();
		for (int i = 0; i < hseam.length; i++)
			StdOut.printf("%d ", hseam[i]);
		sc.removeHorizontalSeam(hseam);
		StdOut.printf("\n%d %d\n", sc.width(), sc.height());
		vseam = sc.findVerticalSeam();
		for (int i = 0; i < vseam.length; i++)
			StdOut.printf("%d ", vseam[i]);
		sc.removeVerticalSeam(vseam);
		StdOut.printf("\n%d %d\n", sc.width(), sc.height());
		hseam = sc.findHorizontalSeam();
		for (int i = 0; i < hseam.length; i++)
			StdOut.printf("%d ", hseam[i]);
		sc.removeHorizontalSeam(hseam);
		StdOut.printf("\n%d %d\n", sc.width(), sc.height());
	}
}
