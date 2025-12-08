package problem.tsp;

import alg.InitialSolutionGenerator;
import problem.OptimizationProblem;
import representation.IntegerPermutation;
import representation.SimpleSolution;
import representation.Solution;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * todo: Implement Greedy ISG for TSP
 */
public class TSPRandomISG implements InitialSolutionGenerator<TSPModel, IntegerPermutation> {
    @Override
    public Solution generate(OptimizationProblem<TSPModel, IntegerPermutation> problem) {
       TSPModel tsp=  problem.model();
       List<Integer> cityList= IntStream.range(0, tsp.cityCount()).boxed().collect(Collectors.toList());
       Collections.shuffle(cityList);
       IntegerPermutation ip = new IntegerPermutation(cityList);
       return new SimpleSolution(ip, problem.getObjective().value(tsp,ip));
    }

    @Override
    public String toString() {
        return "TSP-Random-ISG";
    }
}
