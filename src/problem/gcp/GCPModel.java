package problem.gcp;

import java.util.ArrayList;
import java.util.List;

import problem.ProblemModel;
import representation.GCPRepresentation;

public class GCPModel implements ProblemModel<GCPRepresentation>{
	public int[][] graphMatrix;
	
	public GCPModel(int[][] graph) {
		this.graphMatrix = graph;
	}
	
	@Override
	public boolean isFeasable(GCPRepresentation r) {
		if (r.colors.length != graphMatrix.length) return false;
		
		for (int i=0;i<r.colors.length;i++) {
			List<Integer> colors = getAdjacentVertexColors(i, r);
			for (int j=0;j<colors.size();j++) {
				if (colors.get(j)==r.colors[i])
					return false;
			}
		}
		return true;
	}
	
	public List<Integer> getAdjacentVertices(int vertex) {
		List<Integer> neighbors = new ArrayList<Integer>();
		
		for (int i=0;i<graphMatrix[vertex].length;i++) {
			if (graphMatrix[vertex][i]==1 || graphMatrix[i][vertex]==1)
				neighbors.add(i);
		}
		
		return neighbors;
	}
	
	public List<Integer> getAdjacentVertexColors(int vertex, GCPRepresentation r) {
		List<Integer> colors = new ArrayList<Integer>();
		List<Integer> neighbors = getAdjacentVertices(vertex);
		for (int i=0;i<neighbors.size();i++) {
			int color = r.colors[neighbors.get(i)];
			if (color!=0)
				colors.add(color);
		}
		
		
		return colors;
	}

}
