package representation.f1;

import representation.Representation;

import java.util.Arrays;

/**
 * Representation of a Formula 1 race strategy as a chromosome.
 * The chromosome is an array of LapState objects, one for each lap in the race.
 */
public class F1StrategyRepresentation<P extends LapPerformance> implements Representation {
    /** Array of lap states representing the complete race strategy */
    private LapState<P>[] chromosome;

    /**
     * Creates a new F1StrategyRepresentation with the given chromosome.
     *
     * @param chromosome array of lap states
     */
    public F1StrategyRepresentation(LapState<P>[] chromosome) {
        this.chromosome = chromosome;
    }

    /**
     * Gets the chromosome (array of lap states).
     *
     * @return the chromosome array
     */
    public LapState<P>[] getChromosome() {
        return chromosome;
    }

    /**
     * Gets the number of laps in the race strategy.
     *
     * @return number of laps
     */
    public int getLapCount() {
        return chromosome.length;
    }

    /**
     * Gets the LapState for a specific lap.
     *
     * @param lapIndex lap index (0-based)
     * @return LapState for the specified lap
     */
    public LapState<P> getLapState(int lapIndex) {
        return chromosome[lapIndex];
    }

    /**
     * Sets the LapState for a specific lap.
     *
     * @param lapIndex lap index (0-based)
     * @param lapState LapState to set
     */
    public void setLapState(int lapIndex, LapState<P> lapState) {
        chromosome[lapIndex] = lapState;
    }

    /**
     * Creates a deep copy of this representation.
     *
     * @return a new F1StrategyRepresentation with copied chromosome
     */
    @Override
    public Representation copy() {
        @SuppressWarnings("unchecked")
        LapState<P>[] copiedChromosome = new LapState[chromosome.length];
        for (int i = 0; i < chromosome.length; i++) {
            copiedChromosome[i] = chromosome[i] != null ? chromosome[i].copy() : null;
        }
        return new F1StrategyRepresentation<>(copiedChromosome);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        F1StrategyRepresentation<?> that = (F1StrategyRepresentation<?>) obj;
        return Arrays.equals(chromosome, that.chromosome);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chromosome);
    }

    @Override
    public String toString() {
        return "F1StrategyRepresentation{lapCount=" + chromosome.length + "}";
    }
}

