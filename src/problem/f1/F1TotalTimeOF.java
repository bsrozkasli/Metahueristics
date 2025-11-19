package problem.f1;

import problem.ObjectiveFunction;
import problem.ObjectiveType;
import representation.f1.F1StrategyRepresentation;
import representation.f1.LapState;
import representation.f1.Compound;
import representation.f1.LapPerformance;

/**
 * Objective function that calculates the total race time for a Formula 1 strategy.
 * 
 * Formula: TotalTime = sum(lap=1 to laps)(delta + degradation × lap + t_pit-stop)
 * 
 * Where:
 * - delta: compound time loss (0.0 for SOFT, 0.5 for MEDIUM, 1.0 for HARD)
 * - degradation × lap: tire wear loss (0.05 × tireAge)
 * - t_pit-stop: pit stop time (28.0 seconds if pit stop, 0.0 otherwise)
 */
public class F1TotalTimeOF implements ObjectiveFunction<F1StrategyRepresentation<LapPerformance>, F1Model> {
    
    /** Degradation rate per lap (seconds per lap age) */
    private static final double DEGRADATION_RATE = 0.05;
    
    /** Pit stop time penalty in seconds */
    private static final double PIT_STOP_TIME = 28.0;

    /**
     * Returns the optimization sense (minimization for total time).
     *
     * @return ObjectiveType.Minimization
     */
    @Override
    public ObjectiveType type() {
        return ObjectiveType.Minimization;
    }

    /**
     * Computes the total race time for the given strategy representation.
     * 
     * The calculation follows these steps for each lap:
     * 1. Calculate delta (compound time loss)
     * 2. Calculate degradation × lap (tire wear loss)
     * 3. Calculate t_pit-stop (pit stop time if applicable)
     * 4. Sum all three terms to get lap time
     * 5. Add lap time to total time
     *
     * @param pm problem model providing race parameters
     * @param r  strategy representation to evaluate
     * @return total race time in seconds
     */
    @Override
    public double value(F1Model pm, F1StrategyRepresentation<LapPerformance> r) {
        double totalTime = 0.0;
        
        for (int lap = 0; lap < r.getLapCount(); lap++) {
            LapState<LapPerformance> lapState = r.getLapState(lap);
            
            // Step 1: Calculate delta (compound time loss)
            double delta = getCompoundTimeLoss(lapState.getCompound());
            
            // Step 2: Calculate degradation × lap (tire wear loss)
            int tireAge = lapState.getPerformance().getTireAge();
            double degradationLoss = DEGRADATION_RATE * tireAge;
            
            // Step 3: Calculate t_pit-stop (pit stop time if applicable)
            double pitStopTime = lapState.isPitStop() ? PIT_STOP_TIME : 0.0;
            
            // Step 4: Calculate lap time
            double lapTime = delta + degradationLoss + pitStopTime;
            
            // Step 5: Add to total time
            totalTime += lapTime;
        }
        
        return totalTime;
    }

    /**
     * Gets the compound time loss (delta) for a given tire compound.
     * 
     * @param compound tire compound
     * @return time loss in seconds (0.0 for SOFT, 0.5 for MEDIUM, 1.0 for HARD)
     */
    private double getCompoundTimeLoss(Compound compound) {
        switch (compound) {
            case SOFT:
                return 0.0;
            case MEDIUM:
                return 0.5;
            case HARD:
                return 1.0;
            default:
                return 0.0;
        }
    }
}

