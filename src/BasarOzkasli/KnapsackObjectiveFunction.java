package BasarOzkasli;

import problem.ObjectiveFunction;
import problem.ObjectiveType;

public class KnapsackObjectiveFunction implements ObjectiveFunction<KnapsackRepresentation, KnapsackModel> {

    @Override
    public ObjectiveType type() {
        return ObjectiveType.Maximization;
    }

    @Override
    public double value(KnapsackModel model, KnapsackRepresentation r) {
        double totalValue = 0;
        boolean[] items = r.getItems();

        for (int i = 0; i < model.getItemCount(); i++) {
            if (items[i]) {
                totalValue += model.getValue(i);
            }
        }
        return totalValue;
    }
}