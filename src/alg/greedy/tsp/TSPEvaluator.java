package alg.greedy.tsp;

import problem.tsp.TSPModel;

import java.util.List;

public interface TSPEvaluator {

    int choose(TSPModel model, List<Integer> cities, Integer current);
}
