package alg.ls.tsp;

import alg.ls.NeighborGenerator;
import problem.OptimizationProblem;
import problem.tsp.TSPModel;
import representation.IntegerPermutation;
import representation.SimpleSolution;
import representation.Solution;

public class TSPSwapNG implements NeighborGenerator<TSPModel, IntegerPermutation> {

    IntegerPermutation current;
    int left;
    int right;
    TSPModel tsp;

    @Override
    public void init(OptimizationProblem<TSPModel, IntegerPermutation> problem, Solution currentSolution) {
        current = (IntegerPermutation) currentSolution.getRepresentation();
        left= 0;
        right=1;
        tsp = problem.model();
    }

    @Override
    public boolean hasNext() {
        return left < tsp.cityCount() ;
    }

    @Override
    public Solution next(OptimizationProblem<TSPModel, IntegerPermutation> problem) {

        IntegerPermutation neighbor = (IntegerPermutation) current.copy();
        neighbor.swap(left,right);
        right++;
        if (right >= tsp.cityCount() )
        {
            left++;
            right = left+1;
        }

        return new SimpleSolution(neighbor, problem.getObjective().value(tsp,neighbor));
    }

    @Override
    public String toString() {
        return "TSP-Swap-NG";
    }
}
