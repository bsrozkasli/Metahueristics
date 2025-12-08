package alg.greedy.gcp;

import java.util.List;

import alg.greedy.GreedyHeuristic;
import problem.gcp.GCPModel;
import representation.GCPRepresentation;

public class GreedyGCP implements GreedyHeuristic<GCPModel, GCPRepresentation>{

	@Override
	public GCPRepresentation solve(GCPModel pm) {
		GCPRepresentation rep = new GCPRepresentation(pm.graphMatrix.length);
		
		for (int vertex = 0; vertex < pm.graphMatrix.length; vertex++) {
			
			List<Integer> adjacentColors = pm.getAdjacentVertexColors(vertex, rep);
			
			int vertcolor = 1;
			while (adjacentColors.contains(vertcolor))
				vertcolor++;
			
			rep.colors[vertex] = vertcolor;
			
		}
		
		return rep;
	}
	
}
