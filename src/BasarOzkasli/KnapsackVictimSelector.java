package BasarOzkasli;

import alg.ga.VictimSelector;
import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.Solution;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KnapsackVictimSelector implements VictimSelector {

    @Override
    public <R extends Representation, PM extends ProblemModel<R>> List<Solution> select(
            OptimizationProblem<PM, R> problem, List<Solution> population, int count) {
        // Select 'count' worst solutions to remove
        return population.stream()
                .sorted(Comparator.comparingDouble(Solution::value)) // Ascending order (lowest fitness first)
                .limit(count)
                .collect(Collectors.toList());
    }
}
