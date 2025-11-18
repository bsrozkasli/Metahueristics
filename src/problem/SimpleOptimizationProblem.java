package problem;


import representation.Representation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleOptimizationProblem <R extends Representation,PM extends ProblemModel<R>> implements OptimizationProblem<R,PM> {
    PM model;
    List<ObjectiveFunction<R,PM>> objectives;

    public SimpleOptimizationProblem(PM pm, ObjectiveFunction<R,PM> of) {
        model = pm;
        objectives = new ArrayList<>();
        objectives.add(of);
    }

    @Override
    public PM model() {
        return model;
    }

    @Override
    public List<ObjectiveFunction<R,PM>> objectives() {
        return objectives;
    }

    @Override
    public void addObjective(ObjectiveFunction<R, PM> objectiveFunction) {
        objectives.add(objectiveFunction);
    }



}