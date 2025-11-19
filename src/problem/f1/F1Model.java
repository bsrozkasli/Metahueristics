package problem.f1;

import problem.ProblemModel;
import representation.f1.F1StrategyRepresentation;
import representation.f1.LapState;
import representation.f1.LapPerformance;

/**
 * Problem model for Formula 1 race strategy optimization.
 * Validates that the strategy satisfies the Second Tire Constraint:
 * At least two different tire compounds must be used during the race.
 */
public class F1Model implements ProblemModel<F1StrategyRepresentation<LapPerformance>> {
    /** Total number of laps in the race */
    private final int totalLaps;

    /**
     * Creates a new F1Model for a race with the specified number of laps.
     *
     * @param totalLaps total number of laps in the race
     */
    public F1Model(int totalLaps) {
        this.totalLaps = totalLaps;
    }

    /**
     * Gets the total number of laps in the race.
     *
     * @return total number of laps
     */
    public int getTotalLaps() {
        return totalLaps;
    }

    /**
     * Validates that the representation satisfies the structural constraints:
     * 1. The chromosome must have the correct number of laps
     * 2. The Second Tire Constraint: At least two different tire compounds must be used
     *
     * @param r representation to validate
     * @return true if the representation is feasible
     */
    /**
     * Note: Method name keeps the historical typo "isFeasable" (instead of "isFeasible")
     * to preserve API/source compatibility across the codebase and compiled classes.
     */
    @Override
    public boolean isFeasable(F1StrategyRepresentation<LapPerformance> r) {
        if (r == null || r.getChromosome() == null) {
            return false;
        }

        // Check if the chromosome has the correct number of laps
        if (r.getLapCount() != totalLaps) {
            return false;
        }

        // Check Second Tire Constraint: At least two different compounds must be used
        boolean hasSoft = false;
        boolean hasMedium = false;
        boolean hasHard = false;

        for (int i = 0; i < r.getLapCount(); i++) {
            LapState<LapPerformance> lapState = r.getLapState(i);
            if (lapState == null || lapState.getCompound() == null) {
                return false;
            }

            switch (lapState.getCompound()) {
                case SOFT:
                    hasSoft = true;
                    break;
                case MEDIUM:
                    hasMedium = true;
                    break;
                case HARD:
                    hasHard = true;
                    break;
            }
        }

        // Count how many different compounds are used
        int compoundCount = 0;
        if (hasSoft) compoundCount++;
        if (hasMedium) compoundCount++;
        if (hasHard) compoundCount++;

        // Must use at least 2 different compounds
        return compoundCount >= 2;
    }
}

