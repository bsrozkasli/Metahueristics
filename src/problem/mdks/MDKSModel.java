package problem.mdks;

import problem.ProblemModel;
import representation.BinaryPermutation;

public class MDKSModel implements ProblemModel<BinaryPermutation> {

    private final double maxOperatingCost;
    private final double maxBuyCost;
    private final double maxArea;

    private Item[] items;

    public MDKSModel(double maxOperatingCost, double maxBuyCost, double maxArea) {
        this.maxOperatingCost = maxOperatingCost;
        this.maxBuyCost = maxBuyCost;
        this.maxArea = maxArea;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public Item[] getItems() {
        return items;
    }

    @Override
    public boolean isFeasable(BinaryPermutation r) {
        double totalOperatingCost = 0.0;
        double totalBuyCost = 0.0;
        double totalArea = 0.0;
        for (int i = 0; i < r.size(); i++) {
            if (r.get(i)) {
                Item item = items[i];
                totalOperatingCost += item.operatingCost;
                totalBuyCost += item.buyCost;
                totalArea += item.area;
            }
        }

        return totalOperatingCost <= maxOperatingCost && totalBuyCost <= maxBuyCost && totalArea <= maxArea;
    }

    public double getMaxOperatingCost() {
        return maxOperatingCost;
    }

    public double getMaxBuyCost() {
        return maxBuyCost;
    }

    public double getMaxArea() {
        return maxArea;
    }
}
