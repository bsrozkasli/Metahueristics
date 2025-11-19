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
 * Greedy constructive heuristic for the Traveling Salesman Problem (TSP). The
 * heuristic starts from a random city and repeatedly asks a {@link TSPEvaluator}
 * to choose the next city until all cities have been visited. The final visit
 * order is returned as an {@link IntegerPermutation}.
 */
public class TSPGreedyHeuristic implements GreedyHeuristic<TSPModel, IntegerPermutation> {

    /** Secure random generator used to select the starting city. */
    Random random = new SecureRandom();
    /** Strategy object that decides which city should be visited next. */
    private TSPEvaluator evaluator;


    /**
     * Creates a heuristic that delegates the next-city selection to the given
     * {@link TSPEvaluator} implementation.
     *
     * @param evaluator strategy used when multiple candidate cities are available
     */
    public TSPGreedyHeuristic(TSPEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * Constructs a complete TSP tour by iteratively selecting the next city
     * until all have been visited.
     */
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
        return new IntegerPermutation(visited);
    }

    /**
     * Delegates the selection of the next city to the evaluator using the most
     * recently visited city as the current position.
     *
     * @param model        problem instance that provides distances
     * @param visited      ordered list of already visited city indices
     * @param nonVisiteds  candidate cities that have not been visited yet
     * @return index of the next city to visit
     */
    private int chooseNext(TSPModel model, List<Integer> visited, List<Integer> nonVisiteds) {
        int next = evaluator.choose(model, nonVisiteds,visited.get(visited.size()-1));
        return next;
    }

    /**
     * Checks whether the tour already contains all cities in the model.
     *
     * @param model   problem instance to compare against
     * @param visited current sequence of visited cities
     * @return {@code true} when all cities have been visited
     */
    private boolean complete(TSPModel model, List<Integer> visited) {


        return model.cityCount() == visited.size();
    }



    /**
     * Creates a random symmetric TSP instance with distances between 10 and 100
     * units for demonstration purposes.
     *
     * @return randomly generated {@link TSPModel}
     */
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
     * Demo entry point that constructs a random TSP instance and runs the greedy
     * heuristic multiple times, printing the resulting solutions and their
     * objective values.
     *
     * @param args ignored command-line arguments
     */
    public static void main(String[] args) {

        TSPGreedyHeuristic heuristic= new TSPGreedyHeuristic(new TSPNearestNeighborEvaluator());
        TSPModel tsp = randomInstance() ;
        SimpleOptimizationProblem<IntegerPermutation,TSPModel> problem =  new SimpleOptimizationProblem(tsp,new TSPMinimumDistanceOF());

        for (int i = 0; i < 10; i++) {
            IntegerPermutation ip = heuristic.solve(tsp);
            SimpleSolution solution = new SimpleSolution(ip,problem.getObjective().value(tsp,ip));
            System.out.println(solution);
        }
    }
}
