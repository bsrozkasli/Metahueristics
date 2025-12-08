package problem.mdks;

import problem.ObjectiveFunction;
import problem.ObjectiveType;
import representation.BinaryPermutation;

public class MDKSOF implements ObjectiveFunction<BinaryPermutation, MDKSModel> {

    private final double suitablityWeight;
    private final double instructivenessWeight;
    private final double attractivenessWeight;

    public MDKSOF(double attractivenessWeight, double instructivenessWeight, double suitablityWeight) {
        if (attractivenessWeight < 0 || instructivenessWeight < 0 || suitablityWeight < 0) {
            throw new IllegalArgumentException("Weights must be non-negative");
        }
        if (attractivenessWeight + instructivenessWeight + suitablityWeight != 1) {
            throw new IllegalArgumentException("Weights must sum to 1");
        }

        this.attractivenessWeight = attractivenessWeight;
        this.instructivenessWeight = instructivenessWeight;
        this.suitablityWeight = suitablityWeight;
    }

    @Override
    public ObjectiveType type() {
        return ObjectiveType.Maximization;
    }

    @Override
    public double value(MDKSModel pm, BinaryPermutation r) {
        if (!pm.isFeasable(r)) {
            return Double.NEGATIVE_INFINITY;
        }
        double totalSuitablity = 0.0;
        double totalInstructiveness = 0.0;
        double totalAttractiveness = 0.0;

        for (int i = 0; i < r.size(); i++) {
            if (r.get(i)) {
                Item item = pm.getItems()[i];
                totalSuitablity += item.suitablity;
                totalInstructiveness += item.instructiveness;
                totalAttractiveness += item.attractiveness;
            }
        }
        return suitablityWeight * totalSuitablity + instructivenessWeight * totalInstructiveness
                + attractivenessWeight * totalAttractiveness;
    }

    public double getSuitablityWeight() {
        return suitablityWeight;
    }

    public double getInstructivenessWeight() {
        return instructivenessWeight;
    }

    public double getAttractivenessWeight() {
        return attractivenessWeight;
    }
}
