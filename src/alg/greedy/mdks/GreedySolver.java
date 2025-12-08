package alg.greedy.mdks;

import java.util.ArrayList;
import java.util.List;
import problem.ObjectiveType;
import problem.mdks.Item;
import problem.mdks.MDKSModel;
import problem.mdks.MDKSOF;
import representation.BinaryPermutation;

public class GreedySolver implements Solver<BinaryPermutation, MDKSModel, MDKSOF> {

    @Override
    public BinaryPermutation solve(MDKSModel model, MDKSOF objectiveFunction) {
        if (objectiveFunction.type() != ObjectiveType.Maximization) {
            throw new IllegalArgumentException("GreedySolver only supports maximization problems");
        }

        Item[] items = model.getItems();
        int n = items.length;

        List<Integer> itemIndices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            itemIndices.add(i);
        }

        itemIndices.sort((Integer i1, Integer i2) -> {
            double efficiency1 = calculateEfficiency(items[i1], model, objectiveFunction);
            double efficiency2 = calculateEfficiency(items[i2], model, objectiveFunction);

            return Double.compare(efficiency2, efficiency1);
        });

        boolean[] selected = new boolean[n];

        for (Integer idx : itemIndices) {

            selected[idx] = true;
            BinaryPermutation candidate = new BinaryPermutation(selected);

            if (!model.isFeasable(candidate)) {
                selected[idx] = false;
            }
        }

        return new BinaryPermutation(selected);
    }

    private double calculateEfficiency(Item item, MDKSModel model, MDKSOF objectiveFunction) {

        double value = objectiveFunction.getSuitablityWeight() * item.suitablity
                + objectiveFunction.getInstructivenessWeight() * item.instructiveness
                + objectiveFunction.getAttractivenessWeight() * item.attractiveness;

        double normalizedCost = (item.area / model.getMaxArea())
                + (item.buyCost / model.getMaxBuyCost())
                + (item.operatingCost / model.getMaxOperatingCost());

        if (normalizedCost == 0) {
            return Double.MAX_VALUE;
        }

        return value / normalizedCost;
    }
}
