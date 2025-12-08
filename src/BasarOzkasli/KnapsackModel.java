package BasarOzkasli;

import problem.ProblemModel;

public class KnapsackModel implements ProblemModel<KnapsackRepresentation> {

    private final double[] weights;
    private final double[] values;
    private final double capacity;

    public KnapsackModel(double[] weights, double[] values, double capacity) {
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
    }
    public int getItemCount() {
        return weights.length;
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public double getValue(int index) {
        return values[index];
    }

    public double getCapacity() {
        return capacity;
    }

    @Override
    public boolean isFeasable(KnapsackRepresentation r) {
        double totalWeight = 0;
        boolean[] items = r.getItems();

        for (int i = 0; i < items.length; i++) {
            if (items[i]) {
                totalWeight += weights[i];
            }
        }
        return totalWeight <= capacity;
    }
}
