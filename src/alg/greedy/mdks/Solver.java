package alg.greedy.mdks;

import problem.ObjectiveFunction;
import problem.ProblemModel;
import representation.Representation;

public interface Solver<R extends Representation, Model extends ProblemModel<R>, OF extends ObjectiveFunction<R, Model>> {

    R solve(Model model, OF objectiveFunction);

}
