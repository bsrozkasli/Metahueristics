package problem.mdks;

public class Item {

    public final double suitablity;
    public final double instructiveness;
    public final double attractiveness;
    public final double operatingCost;
    public final double buyCost;
    public final double area;

    public Item(double area, double attractiveness, double buyCost, double instructiveness, double operatingCost, double suitablity) {
        this.area = area;
        this.attractiveness = attractiveness;
        this.buyCost = buyCost;
        this.instructiveness = instructiveness;
        this.operatingCost = operatingCost;
        this.suitablity = suitablity;
    }
}
