package algs4.part2.program3;

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;


public class BaseballElimination {
	private final int num_teams;
	private final String[] name_teams;
	private final int[] w;
	private final int[] l;
	private final int[] r;
	private final int[][] g;
	private final boolean[] isEliminated;
	private final ArrayList<String>[] R;
	
	public BaseballElimination(String filename) { // create a baseball division from given filename in format specified below
		In in = new In(filename);
		num_teams = in.readInt();
		name_teams = new String[num_teams];
		w = new int[num_teams];
		l = new int[num_teams];
		r = new int[num_teams];
		g = new int[num_teams][num_teams];
		isEliminated = new boolean[num_teams];
		R = (ArrayList<String>[])new ArrayList[num_teams];
		for (int i = 0; i < num_teams; i++) {
			name_teams[i] = in.readString();
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();
			for (int j = 0; j < num_teams; j++)
				g[i][j] = in.readInt();
		}
		checkIsEliminated();
	}
	
	private void checkIsEliminated() {
		int U = num_teams * (num_teams - 1) / 2;
		int V = U + num_teams + 2;
		FlowNetwork[] fn = new FlowNetwork[num_teams];
		FordFulkerson[] ff = new FordFulkerson[num_teams];
		
		FlowEdge e;
		for (int i = 0; i < num_teams; i++) {
			R[i] = new ArrayList<String>();
			isEliminated[i] = false;
			for (int j = 0; j < num_teams; j++) {
				if (i == j) continue;
				if (w[i] + r[i] < w[j]) {
					isEliminated[i] = true;
					R[i].add(name_teams[j]);
					break;
				}
			}
			if (isEliminated[i] == true) continue;
			fn[i] = new FlowNetwork(V);
			for (int j = 0; j < num_teams; j++) {
				e = new FlowEdge(U + j, V - 1, w[i] + r[i] - w[j]);
				fn[i].addEdge(e);
			}
			int index = -1;
			for (int j = 0; j < num_teams - 1; j++) {
				if (j == i) continue;
				for (int k = j + 1; k < num_teams; k++) {
					index++;
					if (k == i) continue;
					e = new FlowEdge(V - 2, index, g[j][k]);
					fn[i].addEdge(e);
					e = new FlowEdge(index, U + j, Double.POSITIVE_INFINITY);
					fn[i].addEdge(e);
					e = new FlowEdge(index, U + k, Double.POSITIVE_INFINITY);
					fn[i].addEdge(e);
				}
			}
			ff[i] = new FordFulkerson(fn[i], V - 2, V - 1);
			for (int j = 0; j < num_teams; j++) {
				if (ff[i].inCut(U + j)) {
					isEliminated[i] = true;
					R[i].add(name_teams[j]);
				}
			}
			if (R[i].isEmpty()) R[i] = null;
		}
	}
	
	public int numberOfTeams() { // number of teams
		return num_teams;
	}
	
	public Iterable<String> teams() { // all teams
		return Arrays.asList(name_teams);
	}
	
	private void validate(int i) {
		if (i < 0 || i >= num_teams)
			throw new java.lang.IllegalArgumentException();
	}
	
	private int getIndexOfTeam(String team) {
		int index = -1;
		for (int i = 0; i < num_teams; i++) {
			if (team.equals(name_teams[i])) {
				index = i;
				break;
			}
		}
		validate(index);
		return index;
	}
	
	public int wins(String team) { // number of wins for given team
		int i = getIndexOfTeam(team);
		return w[i];
	}
	
	public int losses(String team) { // number of losses for given team
		int i = getIndexOfTeam(team);
		return l[i];
	}
	
	public int remaining(String team) { // number of remaining games for given team
		int i = getIndexOfTeam(team);
		return r[i];
	}
	
	public int against(String team1, String team2) { // number of remaining games between team1 and team2
		int i = getIndexOfTeam(team1);
		int j = getIndexOfTeam(team2);
		return g[i][j];
	}
	
	public boolean isEliminated(String team) { // is given team eliminated?
		int i = getIndexOfTeam(team);
		return isEliminated[i];
	}
	public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
		int i = getIndexOfTeam(team);
		return R[i];
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
