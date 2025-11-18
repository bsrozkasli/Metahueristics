package problem;

import com.sun.deploy.util.ArrayUtil;
import representation.Representation;

import java.util.Arrays;
import java.util.List;

public interface OptimizationProblem<R extends Representation, PM extends ProblemModel<R>> {
    PM model();  // The data that defines the problem
    List<ObjectiveFunction<R,PM>> objectives(); // The objectives of the problem

    void addObjective(ObjectiveFunction<R,PM> objectiveFunction);

    default void addObjective(ObjectiveFunction<R,PM>...objectiveFunctions)
    {
        for (int i = 0; i < objectiveFunctions.length; i++) {
            addObjective(objectiveFunctions[i]);
        }
    }

    default ObjectiveFunction<R,PM> getObjective(int index)
    {
        return objectives().get(index);
    }

    default ObjectiveFunction<R,PM> getObjective()
    {
        return getObjective(0);
    }


}
