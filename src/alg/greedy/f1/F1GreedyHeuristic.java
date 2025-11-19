package alg.greedy.f1;

import alg.greedy.GreedyHeuristic;
import problem.f1.F1Model;
import representation.f1.F1StrategyRepresentation;
import representation.f1.LapState;
import representation.f1.LapPerformance;
import representation.f1.Compound;

/**
 * Greedy constructive heuristic for Formula 1 race strategy.
 * 
 * The heuristic delegates decision-making to an {@link F1Evaluator} strategy
 * to determine tire compound selection and pit stop timing for each lap.
 * This ensures at least two different compounds are used (satisfying Second Tire Constraint).
 */
public class F1GreedyHeuristic implements GreedyHeuristic<F1Model, F1StrategyRepresentation<LapPerformance>> {
    
    /** Strategy object that decides compound selection and pit stop timing */
    private final F1Evaluator evaluator;

    /**
     * Creates a heuristic that delegates decision-making to the given
     * {@link F1Evaluator} implementation.
     *
     * @param evaluator strategy used for making compound and pit stop decisions
     */
    public F1GreedyHeuristic(F1Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * Constructs a complete race strategy by iteratively making decisions
     * for each lap using the evaluator strategy.
     *
     * @param pm problem model containing race parameters
     * @return a feasible race strategy representation
     */
    @Override
    public F1StrategyRepresentation<LapPerformance> solve(F1Model pm) {
        int totalLaps = pm.getTotalLaps();
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[totalLaps];
        
        Compound currentCompound = Compound.SOFT;
        int tireAge = 0;
        boolean pitStopMade = false;
        
        for (int lap = 0; lap < totalLaps; lap++) {
            // Delegate decision-making to the evaluator
            F1Evaluator.PitStopDecision decision = evaluator.decide(
                pm, lap, tireAge, currentCompound, pitStopMade
            );
            
            boolean pitStop = decision.isPitStop();
            currentCompound = decision.getCompound();

            // Track whether we have made at least one pit stop overall (single switch enforcement)
            pitStopMade = pitStopMade || pitStop;
            
            // Create performance data for this lap
            double weather = 0.0; // Dry conditions
            double tireWear = tireAge * 0.01; // Increasing wear with age
            double fuelLoad = Math.max(0.0, 100.0 - (lap * 1.5)); // Decreasing fuel load, never negative
            
            LapPerformance performance = new LapPerformance(weather, tireWear, tireAge, fuelLoad);
            
            // Create lap state
            LapState<LapPerformance> lapState = new LapState<>(currentCompound, pitStop, performance);
            chromosome[lap] = lapState;
            
            // Update tire age once per lap: reset on pit stop, otherwise increment
            if (pitStop) {
                tireAge = 0;
            } else {
                tireAge++;
            }
        }
        
        return new F1StrategyRepresentation<>(chromosome);
    }
}
