package BasarOzkasli;

import alg.InitialSolutionGenerator;
import problem.OptimizationProblem;
import representation.SimpleSolution;
import representation.Solution;
import utils.RandUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnapsackRandomISG implements InitialSolutionGenerator<KnapsackModel, KnapsackRepresentation> {

    @Override
    public Solution generate(OptimizationProblem<KnapsackModel, KnapsackRepresentation> problem) {
        KnapsackModel model = problem.model();
        int itemCount = model.getItemCount();
        boolean[] items = new boolean[itemCount];

        // Randomly select items
        for (int i = 0; i < itemCount; i++) {
            if (RandUtils.rollDice(0.5)) {
                items[i] = true;
            }
        }

        KnapsackRepresentation representation = new KnapsackRepresentation(items);
        repair(model, representation);

        double fitness = problem.getObjective().value(model, representation);
        return new SimpleSolution(representation, fitness);
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
