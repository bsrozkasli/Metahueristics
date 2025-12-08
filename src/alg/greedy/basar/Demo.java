package alg.greedy.basar;

import BasarOzkasli.KnapsackGreedyHeuristic;
import BasarOzkasli.KnapsackModel;
import BasarOzkasli.KnapsackObjectiveFunction;
import BasarOzkasli.KnapsackRepresentation;

import java.util.Random;


public class Demo {

    public static void main(String[] args) {
        System.out.println("=== Simple Knapsack Demo ===");


        int itemCount = 30;//2^30
        double capacity = 50.0;
        double[] weights = new double[itemCount];
        double[] values = new double[itemCount];

        Random rand = new Random();
        for (int i = 0; i < itemCount; i++) {
            weights[i] = 1 + rand.nextInt(15);
            values[i] = 5 + rand.nextInt(20);
        }


        KnapsackModel model = new KnapsackModel(weights, values, capacity);
        KnapsackObjectiveFunction objFunc = new KnapsackObjectiveFunction();


        KnapsackGreedyHeuristic solver = new KnapsackGreedyHeuristic();
        KnapsackRepresentation solution = solver.solve(model);


        double totalValue = objFunc.value(model, solution);
        double totalWeight = calculateTotalWeight(model, solution);

        System.out.println("Knapsack Capacity : " + capacity);
        System.out.println("Total Value       : " + totalValue);
        System.out.println("Total Weight      : " + totalWeight);
        System.out.println("Is Solution Feasible?: " + model.isFeasable(solution));
        System.out.println("Selected Items    : " + solution.toString());
    }


    private static double calculateTotalWeight(KnapsackModel model, KnapsackRepresentation rep) {
        double sum = 0;
        for (int i = 0; i < rep.size(); i++) {
            if (rep.isSelected(i)) {
                sum += model.getWeight(i);
            }
        }
        return sum;
    }
}