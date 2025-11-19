package problem;


import representation.Representation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Minimal {@link OptimizationProblem} implementation that stores a single
 * model and a mutable list of objectives.
 */
public class SimpleOptimizationProblem <R extends Representation,PM extends ProblemModel<R>> implements OptimizationProblem<R,PM> {
    PM model;
    List<ObjectiveFunction<R,PM>> objectives;

    /**
     * Creates a problem description with the provided model and a single
     * initial objective.
     *
     * @param pm concrete problem model
     * @param of initial objective function
     */
    public SimpleOptimizationProblem(PM pm, ObjectiveFunction<R,PM> of) {
        model = pm;
        objectives = new ArrayList<>();
        objectives.add(of);
    }

    /** {@inheritDoc} */
    @Override
    public PM model() {
        return model;
    }

    /** {@inheritDoc} */
    @Override
    public List<ObjectiveFunction<R,PM>> objectives() {
        return objectives;
    }

    /** {@inheritDoc} */
    @Override
    public void addObjective(ObjectiveFunction<R, PM> objectiveFunction) {
        objectives.add(objectiveFunction);
    }



}