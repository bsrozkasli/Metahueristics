package representation;

import java.util.Arrays;
import java.util.List;

/**
 * {@link Permutation} backed by an {@code int[]} for efficiency.
 */
public class IntegerPermutation implements Permutation<Integer>{

    int[] nodes;


    /**
     * Builds a permutation from a list of integers.
     *
     * @param nodeList ordered list of node indices
     */
    public IntegerPermutation(List<Integer> nodeList) {
        nodes = nodeList.stream().mapToInt(x->x).toArray();
    }


    /**
     * @param newNodes backing array (not copied) defining the permutation order
     */
    public IntegerPermutation(int[] newNodes) {
        nodes = newNodes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer get(int index) {
        return nodes[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return nodes.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void swap(int i1, int i2) {
        int tmp = nodes[i1];
        nodes[i1]= nodes[i2];
        nodes[i2]= tmp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Permutation copy() {
        int[] newNodes = new int[nodes.length];


        for (int i = 0; i < nodes.length; i++) {
            newNodes[i]= nodes[i];
        }
        return new IntegerPermutation(newNodes);
    }

    /**
     * @return string representation of the underlying array
     */
    @Override
    public String toString() {
        return Arrays.toString(nodes);
    }
}