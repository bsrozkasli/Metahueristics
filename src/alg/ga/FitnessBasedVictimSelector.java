package alg.ga;

import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.Solution;

import java.util.Collections;
import java.util.List;


/**
 * TODO: Performs victim selection acc.to fitness (disproportional)
 */
public class FitnessBasedVictimSelector implements VictimSelector{
    @Override
    public <R extends Representation, PM extends ProblemModel<R>> List<Solution> select(OptimizationProblem<PM, R> problem, List<Solution> population, int count) {
        return Collections.emptyList();
    }
}
