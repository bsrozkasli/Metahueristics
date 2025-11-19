package representation;

/**
 * Represents solutions along with their objective values.
 */
public interface Solution {
    /**
     * @return underlying decision representation
     */
    Representation getRepresentation();

    /**
     * Convenience method for single-objective problems returning the first
     * objective value.
     */
    double value();

    /**
     * @param index objective index
     * @return value at the supplied index
     */
    double value(int index);

    /**
     * @return array of objective values
     */
    double[] values();

    /**
     * @return deep copy of the solution
     */
    Solution copy();

    int hashCode();
    boolean equals(Object obj);

}
