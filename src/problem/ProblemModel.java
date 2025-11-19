package problem;

import representation.Representation;

/**
 * Defines the data of a problem instance and the feasibility checks for a
 * candidate representation.
 *
 * @param <R> representation type compatible with the model
 */
public interface ProblemModel<R extends Representation> {
    /**
     * Checks whether the provided representation satisfies the structural
     * constraints imposed by the model instance.
     *
     * @param r representation to validate
     * @return {@code true} if the representation is feasible
     */
    boolean isFeasable( R r);
}