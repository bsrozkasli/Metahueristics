package alg.greedy.mdks;

import problem.ObjectiveType;
import problem.mdks.MDKSModel;
import problem.mdks.MDKSOF;
import representation.BinaryPermutation;

public class NaiveSolver implements Solver<BinaryPermutation, MDKSModel, MDKSOF> {

    @Override
    public BinaryPermutation solve(MDKSModel model, MDKSOF objectiveFunction) throws IllegalArgumentException {
        if (objectiveFunction.type() != ObjectiveType.Maximization) {
            throw new IllegalArgumentException("NaiveSolver only supports maximization problems");
        }

        int n = model.getItems().length;
        long totalCombinations = 1L << n;

        BinaryPermutation bestPermutation = null;
        double maxValue = Double.NEGATIVE_INFINITY;

        for (long i = 0; i < totalCombinations; i++) {
            boolean[] selection = new boolean[n];

            for (int j = 0; j < n; j++) {
                selection[j] = (i & (1L << j)) != 0;
            }

            BinaryPermutation candidate = new BinaryPermutation(selection);
            double value = objectiveFunction.value(model, candidate);

            if (value > maxValue) {
                maxValue = value;
                bestPermutation = candidate;
            }
        }

        return bestPermutation;
    }
}
