package problem.tsp;

import problem.ProblemModel;
import representation.IntegerPermutation;
/**
 * Problem model for a symmetric Traveling Salesman Problem. The model keeps the
 * full distance matrix between all cities and validates integer permutations as
 * feasible tours when they contain all cities.
 */
public class TSPModel implements ProblemModel<IntegerPermutation> {
    double[][] distanceMatrix;

    /**
     * @param distances symmetric matrix of pairwise city distances
     */
    public TSPModel(double[][] distances) {
        distanceMatrix = distances;
    }

    /**
     * @return number of cities encoded in the model
     */
    public int cityCount()
    {
        return distanceMatrix.length;
    }

    /**
     * Checks whether the permutation contains the correct number of cities.
     */
    @Override
    public boolean isFeasable(IntegerPermutation p) {
        return p.size()==cityCount();
    }

    /**
     * @return reference to the distance matrix
     */
    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }
}
