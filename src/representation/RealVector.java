package representation;



import java.util.Arrays;

/**
 * Immutable view over a vector of real-valued decision variables.
 */
public class RealVector implements Representation {

    double[] nodes;

    /**
     * @param nodes backing array containing the vector values
     */
    public RealVector(double[] nodes) {
        this.nodes = nodes;
    }


    /**
     * @return dimensionality of the vector
     */
    public int len()
    {
        return nodes.length;
    }

    /**
     * @return reference to the underlying array (not copied)
     */
    public double[] getNodes() {
        return nodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Representation copy() {
        double[] newNodes = new double[nodes.length];
        for (int i = 0; i < newNodes.length; i++) {
            newNodes[i] = nodes[i];
        }
        return new RealVector(newNodes);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Arrays.hashCode(nodes);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof RealVector))
            return false;

        RealVector rv = (RealVector) obj;
        return Arrays.equals(nodes,rv.nodes);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Arrays.toString(nodes);
    }
}
