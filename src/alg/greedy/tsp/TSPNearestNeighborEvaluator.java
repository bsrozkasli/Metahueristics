package alg.greedy.tsp;

import problem.tsp.TSPModel;

import java.util.List;

public class TSPNearestNeighborEvaluator implements TSPEvaluator{
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
