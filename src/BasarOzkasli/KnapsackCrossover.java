package BasarOzkasli;

import alg.ga.CrossOverOperator;
import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.SimpleSolution;
import representation.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class KnapsackCrossover implements CrossOverOperator {

    @Override
    public <R extends Representation, PM extends ProblemModel<R>> List<Solution> apply(
            OptimizationProblem<PM, R> problem, Solution parent1, Solution parent2) {
        if (!(parent1.getRepresentation() instanceof KnapsackRepresentation)
                || !(parent2.getRepresentation() instanceof KnapsackRepresentation)) {
            return new ArrayList<>();
        }

        KnapsackRepresentation rep1 = (KnapsackRepresentation) parent1.getRepresentation();
        KnapsackRepresentation rep2 = (KnapsackRepresentation) parent2.getRepresentation();
        KnapsackModel model = (KnapsackModel) problem.model();

        int size = rep1.size();
        int cutPoint = new Random().nextInt(size); // Single Point Crossover

        boolean[] child1Items = Arrays.copyOf(rep1.getItems(), size);
        boolean[] child2Items = Arrays.copyOf(rep2.getItems(), size);

        // Swap after cutPoint
        for (int i = cutPoint; i < size; i++) {
            boolean temp = child1Items[i];
            child1Items[i] = child2Items[i];
            child2Items[i] = temp;
        }

        KnapsackRepresentation childRep1 = new KnapsackRepresentation(child1Items);
        KnapsackRepresentation childRep2 = new KnapsackRepresentation(child2Items);

        repair(model, childRep1);
        repair(model, childRep2);

        double fitness1 = problem.getObjective().value((PM) model, (R) childRep1);
        double fitness2 = problem.getObjective().value((PM) model, (R) childRep2);

        List<Solution> offspring = new ArrayList<>();
        offspring.add(new SimpleSolution(childRep1, fitness1));
        offspring.add(new SimpleSolution(childRep2, fitness2));

        return offspring;
    }

    private void repair(KnapsackModel model, KnapsackRepresentation representation) {
        if (model.isFeasable(representation)) {
            return;
        }

        boolean[] items = representation.getItems();
        double currentWeight = 0;
        List<Integer> selectedIndices = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            if (items[i]) {
                currentWeight += model.getWeight(i);
                selectedIndices.add(i);
            }
        }

        // Greedy removal of lowest V/W ratio
        selectedIndices.sort(Comparator.comparingDouble(i -> model.getValue(i) / model.getWeight(i)));

        int removeIndex = 0;
        while (currentWeight > model.getCapacity() && removeIndex < selectedIndices.size()) {
            int indexToRemove = selectedIndices.get(removeIndex);
            items[indexToRemove] = false;
            currentWeight -= model.getWeight(indexToRemove);
            removeIndex++;
        }
    }
}
