package representation;

import java.util.Arrays;

/**
 * Basic {@link Solution} implementation that stores an arbitrary representation
 * and an array of objective values.
 */
public class SimpleSolution implements Solution {
    Representation r;
    double[] values;

    /**
     * Convenience constructor for single-objective solutions.
     *
     * @param r     representation to store
     * @param value objective value
     */
    public SimpleSolution(Representation r, double value) {
        this.r =r;
        values = new double[1];
        values[0]= value;
    }

    /**
     * Copy constructor performing a deep copy of the stored representation and
     * objective values.
     */
    public SimpleSolution(SimpleSolution other) {
        r = other.r.copy();
        values = new double[other.values.length];
        System.arraycopy(other.values,0,values,0,other.values.length);
    }

    /** {@inheritDoc} */
    @Override
    public Representation getRepresentation() {
        return r;
    }

    /** {@inheritDoc} */
    @Override
    public double value() {
        return values[0];
    }

    /** {@inheritDoc} */
    @Override
    public double value(int index) {
        return values[index];
    }

    /** {@inheritDoc} */
    @Override
    public double[] values() {
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public Solution copy() {
        return new SimpleSolution(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "R:"+ r + "  V:"+ Arrays.toString(values);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return r.hashCode() + Arrays.hashCode(values);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof SimpleSolution))
            return false;

        SimpleSolution ss = (SimpleSolution) obj;

        return r.equals(ss.r) && Arrays.equals(values,ss.values);
    }
}