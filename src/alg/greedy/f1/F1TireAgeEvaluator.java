package alg.greedy.f1;

import problem.f1.F1Model;
import representation.f1.Compound;

/**
 * {@link F1Evaluator} implementation that switches compounds based on tire age.
 * Starts with SOFT compound and switches to MEDIUM when tire age reaches a threshold.
 */
public class F1TireAgeEvaluator implements F1Evaluator {
    
    /** Maximum tire age before switching compounds */
    private static final int MAX_TIRE_AGE = 20;

    /**
     * Decides on compound and pit stop based on tire age.
     * 
     * Strategy:
     * - Start with SOFT compound
     * - Switch to MEDIUM when tire age reaches MAX_TIRE_AGE
     * - Make a pit stop when switching compounds
     *
     * @param model problem instance providing race parameters
     * @param currentLap current lap index (0-based)
     * @param currentTireAge age of the current tire set
     * @param currentCompound currently used tire compound
     * @param pitStopMade whether a pit stop has already been made
     * @return decision containing compound and pit stop flag
     */
    @Override
    public PitStopDecision decide(F1Model model, int currentLap, int currentTireAge,
                                  Compound currentCompound, boolean pitStopMade) {
        boolean pitStop = false;
        Compound nextCompound = currentCompound;

        // Single pit-stop policy with compound-based control:
        // - Only consider a pit stop if we are currently on SOFT and haven't pitted yet.
        // - When tire age reaches the threshold, switch to MEDIUM exactly once.
        if (!pitStopMade && currentCompound == Compound.SOFT && currentTireAge >= MAX_TIRE_AGE) {
            pitStop = true;
            nextCompound = Compound.MEDIUM; // switch compound exactly once
        }

        return new PitStopDecision(nextCompound, pitStop);
    }
}

