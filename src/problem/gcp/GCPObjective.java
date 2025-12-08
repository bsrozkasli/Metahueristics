package problem.gcp;

import java.util.ArrayList;
import java.util.List;

import problem.ObjectiveFunction;
import problem.ObjectiveType;
import representation.GCPRepresentation;

public class GCPObjective implements ObjectiveFunction<GCPRepresentation, GCPModel>{

	@Override
	public ObjectiveType type() {
		// TODO Auto-generated method stub
		return ObjectiveType.Minimization;
	}

	@Override
	public double value(GCPModel pm, GCPRepresentation r) {
		List<Integer> colors = new ArrayList<Integer>();
		for (int i=0;i<r.colors.length;i++) {
			if (!colors.contains(r.colors[i]))
				colors.add(r.colors[i]);
		}
		
		return colors.size();
	}
	
}
