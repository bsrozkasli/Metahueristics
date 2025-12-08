package alg.ga;

import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.Solution;

import java.util.Collections;
import java.util.List;


/**
 * TODO: Implement RoulletteWheel Selection that selects individual acc. to its fitness
 *
 */
public class RouletteWheelSelection implements ParentSelector{
    @Override
    public <R extends Representation, PM extends ProblemModel<R>> List<Solution> select(OptimizationProblem<PM, R> problem, List<Solution> population) {
        return Collections.emptyList();
    }
}
