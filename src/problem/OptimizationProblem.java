package problem;

import representation.Representation;

import java.util.Arrays;
import java.util.List;

/**
 * Describes the components of an optimization problem: the underlying model
 * (instance data) and one or more objective functions.
 *
 * @param <R>  type of the solution representation
 * @param <PM> type of the problem model
 */
public interface OptimizationProblem<R extends Representation, PM extends ProblemModel<R>> {
    /**
     * @return the model instance that defines the problem data
     */
    PM model();

    /**
     * @return ordered list of objective functions applied to the model
     */
    List<ObjectiveFunction<R,PM>> objectives();

    /**
     * Adds a single objective to the problem definition.
     *
     * @param objectiveFunction objective to be appended
     */
    void addObjective(ObjectiveFunction<R,PM> objectiveFunction);

    /**
     * Convenience helper for appending multiple objectives at once.
     *
     * @param objectiveFunctions objectives to add in order
     */
    default void addObjective(ObjectiveFunction<R,PM>...objectiveFunctions)
    {
        for (int i = 0; i < objectiveFunctions.length; i++) {
            addObjective(objectiveFunctions[i]);
        }
    }

    /**
     * Retrieves a specific objective by index.
     *
     * @param index position of the objective in the list
     * @return objective at the given index
     */
    default ObjectiveFunction<R,PM> getObjective(int index)
    {
        return objectives().get(index);
    }

    /**
     * @return the primary (first) objective of the problem
     */
    default ObjectiveFunction<R,PM> getObjective()
    {
        return getObjective(0);
    }


}
