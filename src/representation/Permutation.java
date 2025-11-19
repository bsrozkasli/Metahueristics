package representation;

/**
 * Represents permutation-based solution encodings such as tours or orderings.
 *
 * @param <T> type of the elements contained in the permutation
 */
public interface Permutation<T>  extends Representation {
    /**
     * @param index zero-based position
     * @return value stored at the given position
     */
    T get(int index);

    /**
     * @return number of elements in the permutation
     */
    int size();

    /**
     * Swaps the elements located at the two specified indices.
     */
    void swap(int i1, int i2);

    /**
     * @return deep copy of the permutation
     */
    Permutation copy();
}
