package BasarOzkasli;

import alg.greedy.GreedyHeuristic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KnapsackGreedyHeuristic implements GreedyHeuristic<KnapsackModel, KnapsackRepresentation> {

    private static class ItemRatio implements Comparable<ItemRatio> {
        int index;
        double ratio;

        public ItemRatio(int index, double ratio) {
            this.index = index;
            this.ratio = ratio;
        }

        @Override
        public int compareTo(ItemRatio other) {
            return Double.compare(other.ratio, this.ratio);
        }
    }

    @Override
    public KnapsackRepresentation solve(KnapsackModel model) {
        int n = model.getItemCount();
        List<ItemRatio> ratios = new ArrayList<>();


        for (int i = 0; i < n; i++) {
            double r = model.getValue(i) / model.getWeight(i);
            ratios.add(new ItemRatio(i, r));
        }
        Collections.sort(ratios);

        boolean[] selectedItems = new boolean[n];
        double currentWeight = 0;

        for (ItemRatio item : ratios) {
            int idx = item.index;
            double weight = model.getWeight(idx);

            if (currentWeight + weight <= model.getCapacity()) {
                selectedItems[idx] = true;
                currentWeight += weight;
            }
        }

        return new KnapsackRepresentation(selectedItems);
    }
}