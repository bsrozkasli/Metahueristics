package problem.tsp;

import problem.ObjectiveFunction;
import problem.ObjectiveType;
import representation.IntegerPermutation;

/**
 * TODO-1: Implement Minimum total path length objective
 */
public class TSPMinimumDistanceOF implements ObjectiveFunction<IntegerPermutation,TSPModel> {
    @Override
    public ObjectiveType type() {

        return ObjectiveType.Minimization;
    }

    @Override
    public double value(TSPModel tspModel, IntegerPermutation ip) {
        double value= 0.0;
        double[][] distances = tspModel.getDistanceMatrix();

        for (int i = 0; i < ip.size(); i++) {
            value += distances[ ip.get(i)] [ ip.get((i+1)%ip.size())];
        }

        return value;
    }


    @Override
    public String toString() {
        return "Minimum Distance OF";
    }

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