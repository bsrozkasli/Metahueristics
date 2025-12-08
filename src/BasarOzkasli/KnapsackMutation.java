package BasarOzkasli;

import alg.ga.MutationOperation;
import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.Solution;
import utils.RandUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnapsackMutation implements MutationOperation {

    @Override
    public <R extends Representation, PM extends ProblemModel<R>> void apply(OptimizationProblem<PM, R> problem,
            Solution s) {
        if (!(s.getRepresentation() instanceof KnapsackRepresentation)) {
            return;
        }

        KnapsackRepresentation rep = (KnapsackRepresentation) s.getRepresentation();
        KnapsackModel model = (KnapsackModel) problem.model();
        boolean[] items = rep.getItems();
        boolean changed = false;

        // "Bit-Flip Logic: ... for each item with very low prob (e.g. 1% or 1/L) flip"
        double rate = 1.0 / items.length; // 1/L is standard for bit-flip

        for (int i = 0; i < items.length; i++) {
            if (RandUtils.rollDice(rate)) {
                items[i] = !items[i];
                changed = true;
            }
        }

        if (changed) {
            repair(model, rep);
            // Re-evaluate fitness
            double newFitness = problem.getObjective().value((PM) model, (R) rep);
            // Update fitness in SimpleSolution (assuming single objective)
            s.values()[0] = newFitness;
        }
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
