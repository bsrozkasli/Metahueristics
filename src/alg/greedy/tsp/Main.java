package alg.greedy.tsp;

import problem.tsp.TSPModel;
import representation.IntegerPermutation;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int size = 10;
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

        // Create the TSPModel instance
        TSPModel model = new TSPModel(distances);

        // Print the distance matrix
        System.out.println("Distance Matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%6.2f ", model.getDistanceMatrix()[i][j]);
            }
            System.out.println();
        }
    }
}