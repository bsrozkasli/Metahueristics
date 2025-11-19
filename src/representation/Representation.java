package representation;

/**
 * Marker interface for solution encodings used by optimization algorithms.
 * Implementations must provide copy, equality and hashing semantics.
 */
public interface Representation {
    /**
     * @return deep copy of the representation
     */
    Representation copy();

    /**
     * @return hash code consistent with {@link #equals(Object)}
     */
    int hashCode();

    /**
     * Compares two representations for structural equality.
     */
    boolean equals(Object obj);
}