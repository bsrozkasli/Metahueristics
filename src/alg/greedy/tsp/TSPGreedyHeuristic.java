package alg.greedy.tsp;



import alg.greedy.GreedyHeuristic;
import problem.SimpleOptimizationProblem;
import problem.tsp.TSPMinimumDistanceOF;
import problem.tsp.TSPModel;
import representation.IntegerPermutation;
import representation.SimpleSolution;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * TODO-2 Implement Greedy Heuristic for TSP
 */
public class TSPGreedyHeuristic implements GreedyHeuristic<TSPModel, IntegerPermutation> {

    Random random = new SecureRandom();
    private TSPEvaluator evaluator;


    public TSPGreedyHeuristic(TSPEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public IntegerPermutation solve(TSPModel model) {

        List<Integer> visited =new ArrayList<>();
        List<Integer> nonVisited = new ArrayList<>();

        // Adds every element to the list
        IntStream.range(0,model.cityCount()).forEach(nonVisited::add);


        int first = random.nextInt(model.cityCount());
        visited.add(first);
        nonVisited.remove(first);

        while ( !complete( model, visited))
        {
            int next = chooseNext(model,visited,nonVisited);
            visited.add(next);
            nonVisited.removeIf(x->x==next); // !Am I removing the ith item, the item i ?
        }
        // todo: implement greeady algorithm
        return new IntegerPermutation(visited);
    }

    private int chooseNext(TSPModel model, List<Integer> visited, List<Integer> nonVisiteds) {
        int next = evaluator.choose(model, nonVisiteds,visited.get(visited.size()-1));
        return next;
    }

    private boolean complete(TSPModel model, List<Integer> visited) {


        return model.cityCount() == visited.size();
    }



    static TSPModel randomInstance()
    {
        int size = 1000;
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
        return model;
    }

    /**
     * !Demo This demonstrates everyhing you implemented.
     * @param args
     */
    public static void main(String[] args) {

        TSPGreedyHeuristic heuristic= new TSPGreedyHeuristic(new TSPNearestNeighborEvaluator());
        TSPModel tsp = randomInstance() ;
        SimpleOptimizationProblem<TSPModel,IntegerPermutation> problem =  new SimpleOptimizationProblem<>(tsp,new TSPMinimumDistanceOF());

        for (int i = 0; i < 10; i++) {
            IntegerPermutation ip = heuristic.solve(tsp);
            SimpleSolution solution = new SimpleSolution(ip,problem.getObjective().value(tsp,ip));
            System.out.println(solution);
        }
    }
}
