package alg.greedy.gcp;

import problem.gcp.GCPModel;
import representation.GCPRepresentation;

public class Main {
	public static void main(String[] args) {
		int[][] graph = new int[][] {
				{ 0, 1, 1, 0, 0},
				{ 1, 0, 0, 1, 0},
				{ 1, 0, 0, 1, 1},
				{ 0, 1, 1, 0, 0},
				{ 0, 0, 1, 0, 0}
		};
		
		GCPModel model = new GCPModel(graph);
		
		GreedyGCP greedysolver = new GreedyGCP();
		GCPRepresentation rep = greedysolver.solve(model);
		
		System.out.println(rep);
	}
}
