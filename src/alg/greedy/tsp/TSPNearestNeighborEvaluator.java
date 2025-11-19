package alg.greedy.tsp;

import problem.tsp.TSPModel;

import java.util.List;

/**
 * {@link TSPEvaluator} implementation that always selects the closest
 * unvisited city (in terms of Euclidean distance matrix values) to extend the
 * tour.
 */
public class TSPNearestNeighborEvaluator implements TSPEvaluator{
    /**
     * Iterates over all candidate cities and picks the one with the smallest
     * distance from the current city.
     */
    @Override
    public int choose(TSPModel model, List<Integer> cities, Integer current) {

        double[][] distances = model.getDistanceMatrix();

        int closest = cities.get(0);

        for (int i = 1; i < cities.size(); i++) {
             if (distances[current][closest]> distances[current][cities.get(i)])
                closest = cities.get(i);
        }
        return closest;
    }
}
