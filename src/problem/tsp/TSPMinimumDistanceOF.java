package problem.tsp;

import problem.ObjectiveFunction;
import problem.ObjectiveType;
import representation.IntegerPermutation;

/**
 * Objective function that evaluates a TSP tour by summing the distances between
 * consecutive cities (including the return edge to the first city). Lower
 * values correspond to better tours.
 */
public class TSPMinimumDistanceOF implements ObjectiveFunction<IntegerPermutation,TSPModel> {
    @Override
    /**
     * @return minimization objective type
     */
    public ObjectiveType type() {

        // todo: return objective type;
        return ObjectiveType.Minimization;
    }

    @Override
    /**
     * Computes the total tour length including the closing edge.
     *
     * @param tspModel model providing the distance matrix
     * @param ip       permutation that defines the visit order
     * @return total path length
     */
    public double value(TSPModel tspModel, IntegerPermutation ip) {
        double value= 0.0;
        double[][] distances = tspModel.getDistanceMatrix();

        for (int i = 0; i < ip.size(); i++) {
            value += distances[ ip.get(i)] [ ip.get((i+1)%ip.size())];
        }
        // todo: calculate value here!
        return value;
    }

    /**
     * Quick self-test showcasing the objective calculation on a small instance.
     */
    public static void main(String[] args) {
        double[][] distances= {  {0,3,2},
                                 {3,0,4},
                                 {2,4,0}};
        TSPModel tspModel = new TSPModel(distances);

        IntegerPermutation ip = new IntegerPermutation(new int[]{0,2,1});

        TSPMinimumDistanceOF of = new TSPMinimumDistanceOF();

        System.out.println("length:"+ of.value(tspModel,ip));
    }
}