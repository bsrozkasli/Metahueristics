package alg;

import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;

public class TimeBasedTC<PM extends ProblemModel<R>, R extends Representation> implements TerminalCondition<PM, R> {

    private final long maxDurationMillis;
    private long startTime;
    private boolean started = false;

    public TimeBasedTC(long maxDurationMillis) {
        this.maxDurationMillis = maxDurationMillis;
    }

    @Override
    public boolean isSatisfied(Metaheuristic<PM, R> alg, OptimizationProblem<PM, R> problem) {
        if (!started) {
            startTime = System.currentTimeMillis();
            started = true;
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) >= maxDurationMillis;
    }
}
