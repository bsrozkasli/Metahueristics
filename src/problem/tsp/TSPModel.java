package problem.tsp;

import problem.ProblemModel;
import representation.IntegerPermutation;


public class TSPModel implements ProblemModel<IntegerPermutation> {
    double[][] distanceMatrix;

    public TSPModel(double[][] distances) {
        distanceMatrix = distances;
    }

    public int cityCount()
    {
        return distanceMatrix.length;
    }

    @Override
    public boolean isFeasable(IntegerPermutation p) {
        return p.size()==cityCount();
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
