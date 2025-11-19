package problem;

interface SolutionEvaluator {
    boolean betterThan(double v1, double v2);
}

/**
 * Enumerates the possible optimization senses and provides helper utilities to
 * compare objective values accordingly.
 */
public enum ObjectiveType {
    /** Objective that prefers smaller values. */
    Minimization( (v1,v2)-> v1<v2),
    /** Objective that prefers larger values. */
    Maximization( (v1,v2)-> v1>v2);

    SolutionEvaluator evaluator;

    /**
     * Determines if the first value is better than the second for this
     * objective type.
     *
     * @param v1 first value
     * @param v2 second value
     * @return {@code true} if {@code v1} is better than {@code v2}
     */
    public boolean betterThan(double v1, double v2)
    {
        return evaluator.betterThan(v1,v2);
    }

    ObjectiveType(SolutionEvaluator evaluator)
    {
        this.evaluator = evaluator;
    }

}
