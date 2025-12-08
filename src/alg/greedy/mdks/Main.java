package alg.greedy.mdks;

import java.time.Instant;
import problem.mdks.Item;
import problem.mdks.MDKSModel;
import problem.mdks.MDKSOF;

public class Main {

    public static void main(String[] args) {
        Item[] items = new Item[]{
            new Item(7, 8, 9, 5, 50000, 5000),
            new Item(8, 9, 7, 8, 80000, 8000),
            new Item(6, 7, 8, 6, 60000, 6000),
            new Item(9, 8, 8, 15, 150000, 15000),
            new Item(7, 9, 6, 12, 120000, 10000),
            new Item(8, 7, 9, 18, 180000, 20000),
            new Item(9, 9, 7, 20, 200000, 18000),
            new Item(9, 9, 9, 40, 400000, 45000),
            new Item(8, 8, 9, 35, 350000, 40000),
            new Item(7, 9, 8, 30, 300000, 35000),
            new Item(6, 6, 9, 10, 100000, 12000),
            new Item(9, 7, 6, 25, 250000, 22000),
            new Item(5, 8, 7, 12, 110000, 11000),
            new Item(8, 6, 8, 14, 140000, 14000),
            new Item(7, 7, 7, 16, 160000, 16000),
            new Item(6, 8, 8, 9, 90000, 9000),
            new Item(8, 7, 7, 11, 105000, 10500),
            new Item(7, 6, 9, 13, 125000, 13000),
            new Item(9, 8, 7, 22, 220000, 21000),
            new Item(5, 9, 6, 10, 95000, 9500),
            new Item(8, 8, 8, 17, 170000, 17000),
            new Item(6, 7, 7, 8, 75000, 7500),
            new Item(9, 6, 8, 28, 280000, 28000),
            new Item(7, 8, 6, 14, 135000, 13500),
            new Item(8, 9, 8, 19, 190000, 19000),
            new Item(5, 7, 9, 11, 100000, 10000),
            new Item(9, 7, 7, 24, 240000, 24000),
            new Item(6, 9, 7, 13, 130000, 13000),
            new Item(7, 7, 8, 15, 145000, 14500),
            new Item(8, 6, 9, 16, 155000, 15500),
            new Item(9, 9, 8, 32, 320000, 32000),
            new Item(6, 8, 6, 9, 85000, 8500),
            new Item(7, 6, 8, 12, 115000, 11500),
            new Item(8, 8, 7, 18, 175000, 17500),
            new Item(5, 6, 8, 7, 70000, 7000),
            new Item(9, 8, 9, 26, 260000, 26000),
            new Item(7, 7, 6, 13, 128000, 12800),
            new Item(6, 9, 8, 14, 138000, 13800),
            new Item(8, 7, 8, 17, 165000, 16500),
            new Item(9, 6, 7, 23, 230000, 23000),
            new Item(7, 8, 8, 16, 158000, 15800),
            new Item(6, 7, 9, 11, 108000, 10800),
            new Item(8, 9, 6, 19, 188000, 18800),
            new Item(5, 8, 8, 10, 98000, 9800),
            new Item(9, 7, 8, 27, 270000, 27000),
            new Item(7, 9, 7, 15, 148000, 14800),
            new Item(6, 6, 7, 8, 78000, 7800),
            new Item(8, 8, 9, 20, 195000, 19500),
            new Item(9, 9, 6, 29, 290000, 29000),
            new Item(7, 6, 7, 12, 118000, 11800)
        };

        var model = new MDKSModel(800000, 2000000, 200);
        model.setItems(items);
        var objectiveFunction = new MDKSOF(0.1, 0.4, 0.5);

        /*
        var ns = new NaiveSolver();
        var bestPermutation = ns.solve(model, objectiveFunction);
        System.out.print("Naive Solver Best permutation: ");
        var start = Instant.now();
        for (int i = 0; i < bestPermutation.size(); i++) {
            if (bestPermutation.get(i)) {
                System.out.println("1");
            } else {
                System.out.println("0");
            }
        }
        var end = Instant.now();
        System.out.println("\nTime taken: " + java.time.Duration.between(start, end).toMillis() + " ms");
        System.out.println("\nValue: " + objectiveFunction.value(model, bestPermutation));
         */
        var gs = new GreedySolver();
        var bestPermutationGreedy = gs.solve(model, objectiveFunction);
        System.out.println();
        System.out.print("Greedy Solver Best permutation: ");
        var start = Instant.now();
        for (int i = 0; i < bestPermutationGreedy.size(); i++) {
            if (bestPermutationGreedy.get(i)) {
                System.out.print("1");
            } else {
                System.out.print("0");
            }
        }
        var end = Instant.now();
        System.out.println("\nTime taken: " + java.time.Duration.between(start, end).toMillis() + " ms");
        System.out.println("\nValue: " + objectiveFunction.value(model, bestPermutationGreedy));
    }
}
