package problem;


import representation.Representation;

/**
 * Describes how a solution representation is evaluated for a specific problem
 * model.
 *
 * @param <R>  type of the solution representation
 * @param <PM> type of the supported problem model
 */
public interface ObjectiveFunction<R extends Representation, PM extends ProblemModel<R>> {

    /**
     * Returns the optimization sense of the objective (minimization or
     * maximization).
     *
     * @return objective type
     */
    ObjectiveType type();

    /**
     * Computes the objective value for the given representation within the
     * specified problem model.
     *
     * @param pm concrete problem model providing the instance data
     * @param r  candidate solution representation
     * @return objective value
     */
    double value(PM pm , R r);
}
