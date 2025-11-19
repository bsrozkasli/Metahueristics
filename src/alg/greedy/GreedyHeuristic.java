package alg.greedy;

import problem.ProblemModel;
import representation.Representation;

/**
 * Generic contract for greedy constructive heuristics that operate on a concrete
 * {@link ProblemModel} and produce a {@link Representation} describing a
 * candidate solution.
 *
 * @param <PM> type of the supported {@link ProblemModel}
 * @param <R>  type of the produced {@link Representation}
 */
public interface GreedyHeuristic<PM extends ProblemModel<R>, R extends Representation> {

    /**
     * Builds a single solution for the supplied problem model using the
     * heuristic's selection strategy.
     *
     * @param pm concrete problem model instance containing the input data
     * @return representation of the constructed solution
     */
    R solve(PM pm);
}
