package alg.greedy.f1;

import problem.f1.F1Model;
import representation.f1.Compound;

/**
 * Strategy interface for making decisions during the construction of a Formula 1 race strategy.
 * Determines tire compound selection and pit stop timing for each lap.
 */
public interface F1Evaluator {

    /**
     * Determines whether a pit stop should be made and which compound to use for the next lap.
     *
     * @param model problem instance providing race parameters
     * @param currentLap current lap index (0-based)
     * @param currentTireAge age of the current tire set (number of laps used)
     * @param currentCompound currently used tire compound
     * @param pitStopMade whether a pit stop has already been made in this race
     * @return decision containing the compound to use and whether to make a pit stop
     */
    PitStopDecision decide(F1Model model, int currentLap, int currentTireAge, 
                          Compound currentCompound, boolean pitStopMade);

    /**
     * Data class representing a pit stop decision.
     */
    class PitStopDecision {
        private final Compound compound;
        private final boolean pitStop;

        public PitStopDecision(Compound compound, boolean pitStop) {
            this.compound = compound;
            this.pitStop = pitStop;
        }

        public Compound getCompound() {
            return compound;
        }

        public boolean isPitStop() {
            return pitStop;
        }
    }
}

