package alg.greedy.tsp;

import problem.tsp.TSPModel;

import java.util.List;

/**
 * Strategy interface for selecting the next city during the construction of a
 * TSP tour.
 */
public interface TSPEvaluator {

    /**
     * Picks the next city to be appended to the partial tour.
     *
     * @param model   problem instance providing the distance matrix
     * @param cities  candidate cities that have not been visited yet
     * @param current currently visited city index
     * @return index of the city that should be visited next
     */
    int choose(TSPModel model, List<Integer> cities, Integer current);
}
