package BasarOzkasli;

import alg.IterationBasedTC;
import alg.ga.GA;
import problem.OptimizationProblem;
import problem.SimpleOptimizationProblem;
import representation.SimpleSolution;
import representation.Solution;

import java.util.List;

public class KnapsackGADemo {

    public static void main(String[] args) {
        System.out.println("=== Knapsack GA Verification Demo ===");

        // 1. Create a small dummy problem
        // 5 Items. Capacity 10.
        // Item 0: W=2, V=10
        // Item 1: W=5, V=20
        // Item 2: W=8, V=30 (High value but heavy)
        // Item 3: W=3, V=15
        // Item 4: W=1, V=5
        double[] weights = { 2, 5, 8, 3, 1 };
        double[] values = { 10, 20, 30, 15, 5 };
        double capacity = 10.0;

        KnapsackModel model = new KnapsackModel(weights, values, capacity);
        KnapsackObjectiveFunction objFunc = new KnapsackObjectiveFunction();
        SimpleOptimizationProblem<KnapsackModel, KnapsackRepresentation> problem = new SimpleOptimizationProblem<>(
                model, objFunc);

        System.out.println("Problem Created: 5 items, Capacity 10.0");

        // 2. Test Initial Solution Generator
        System.out.println("\n--- Testing ISG & Repair ---");
        KnapsackRandomISG isg = new KnapsackRandomISG();
        Solution s1 = isg.generate(problem);
        System.out.println("Generated S1: " + s1.getRepresentation());
        System.out.println("S1 Feasible: " + model.isFeasable((KnapsackRepresentation) s1.getRepresentation()));
        System.out.println("S1 Value: " + s1.value());

        // 3. Test Mutation
        System.out.println("\n--- Testing Mutation ---");
        KnapsackMutation mutation = new KnapsackMutation();
        // Force mutate by creating a loop or just apply once and see if it changes (it
        // might not due to prob).
        // Let's force a change conceptually or just run it.
        // For demo, we just call apply.
        Solution mutant = new SimpleSolution((SimpleSolution) s1); // Copy
        mutation.apply(problem, mutant);
        System.out.println("Original S1: " + s1.getRepresentation());
        System.out.println("Mutant  S1: " + mutant.getRepresentation());
        System.out.println("Mutant Feasible: " + model.isFeasable((KnapsackRepresentation) mutant.getRepresentation()));

        // 4. Test Crossover
        System.out.println("\n--- Testing Crossover ---");
        Solution s2 = isg.generate(problem);
        System.out.println("Parent 1: " + s1.getRepresentation());
        System.out.println("Parent 2: " + s2.getRepresentation());

        KnapsackCrossover crossover = new KnapsackCrossover();
        List<Solution> children = crossover.apply(problem, s1, s2);
        if (!children.isEmpty()) {
            System.out.println("Child 1 : " + children.get(0).getRepresentation());
            System.out.println("Child 1 Feasible: "
                    + model.isFeasable((KnapsackRepresentation) children.get(0).getRepresentation()));
            System.out.println("Child 2 : " + children.get(1).getRepresentation());
            System.out.println("Child 2 Feasible: "
                    + model.isFeasable((KnapsackRepresentation) children.get(1).getRepresentation()));
        } else {
            System.out.println("Crossover returned empty (types mismatch?)");
        }

        // 5. Test Full GA Run
        System.out.println("\n--- Testing Full GA Execution ---");
        GA<KnapsackModel, KnapsackRepresentation> ga = new GA<>(new IterationBasedTC<>(50)); // 50 iterations
        ga.setPopulationSize(10);
        ga.setCrossOverRate(0.9);
        ga.setMutationRate(1.0); // Let operator handle per-bit prob

        ga.setInitialSolutionGenerator(isg);
        ga.setCrossOverOperator(crossover);
        ga.setMutationOperator(mutation);
        ga.setParentSelector(new KnapsackParentSelector());
        ga.setVictimSelector(new KnapsackVictimSelector());

        Solution best = ga.solve(problem);
        System.out.println("Best Solution found: " + best.getRepresentation());
        System.out.println("Best Value: " + best.value());
        System.out.println("Is Feasible: " + model.isFeasable((KnapsackRepresentation) best.getRepresentation()));
    }
}
