package problem.tsp;

import problem.ProblemModel;
import representation.IntegerPermutation;

import java.util.Random;


public class TSPModel implements ProblemModel<IntegerPermutation> {
    double[][] distanceMatrix;

    public TSPModel(double[][] distances) {
        distanceMatrix = distances;
    }

    public int cityCount()
    {
        return distanceMatrix.length;
    }

    @Override
    public boolean isFeasable(IntegerPermutation p) {
        return p.size()==cityCount();
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public static TSPModel randomInstance(int size)
    {
        double[][] distances = new double[size][size];
        Random rand = new Random();

        // Fill the distance matrix with random values, symmetric and 0 on the diagonal
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    double distance = 10 + rand.nextDouble() * 90; // distances between 10 and 100
                    distances[i][j] = distance;
                    distances[j][i] = distance;
                }
            }
        }
        return new TSPModel(distances);
    }

    @Override
    public String toString() {
        return "TSP";
    }
}
