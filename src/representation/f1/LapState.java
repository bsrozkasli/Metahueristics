package representation.f1;

/**
 * Represents the state of a single lap in a Formula 1 race strategy.
 * Contains decision variables (tire compound, pit stop) and performance data.
 */
public class LapState<P extends LapPerformance> {
    /** Tire compound used for this lap */
    private Compound compound;
    
    /** Whether a pit stop was made during this lap */
    private boolean pitStop;
    
    /** Physical performance data for this lap */
    private P performance;

    /**
     * Creates a new LapState instance.
     *
     * @param compound tire compound used
     * @param pitStop whether a pit stop was made
     * @param performance performance data for this lap
     */
    public LapState(Compound compound, boolean pitStop, P performance) {
        this.compound = compound;
        this.pitStop = pitStop;
        this.performance = performance;
    }

    public Compound getCompound() {
        return compound;
    }

    public void setCompound(Compound compound) {
        this.compound = compound;
    }

    public boolean isPitStop() {
        return pitStop;
    }

    public void setPitStop(boolean pitStop) {
        this.pitStop = pitStop;
    }

    public P getPerformance() {
        return performance;
    }

    public void setPerformance(P performance) {
        this.performance = performance;
    }

    /**
     * Creates a deep copy of this LapState object.
     *
     * @return a new LapState instance with copied values
     */
    @SuppressWarnings("unchecked")
    public LapState<P> copy() {
        P perfCopy = null;
        if (performance != null) {
            perfCopy = (P) performance.copy();
        }
        return new LapState<>(compound, pitStop, perfCopy);
    }

    @Override
    public String toString() {
        return String.format("LapState{compound=%s, pitStop=%s, performance=%s}",
                compound, pitStop, performance);
    }
}

