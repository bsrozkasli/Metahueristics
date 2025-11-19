package basar;

import problem.f1.F1Model;
import problem.f1.F1TotalTimeOF;
import alg.greedy.f1.F1GreedyHeuristic;
import alg.greedy.f1.F1TireAgeEvaluator;
import representation.f1.F1StrategyRepresentation;
import representation.f1.LapState;
import representation.f1.LapPerformance;
import representation.f1.Compound;

/**
 * Demo program for F1 Race Strategy Optimization using Greedy Heuristic.
 *
 * This demo:
 * 1. Creates a 58-lap race problem
 * 2. Runs a greedy tire-age-based strategy
 * 3. Validates the solution
 * 4. Displays detailed results
 *
 * @author Basar Ozkaşlı
 */
public class Demo {

    private static final int RACE_LAPS = 58;

    public static void main(String[] args) {
        printHeader();

        // Create problem components
        F1Model model = new F1Model(RACE_LAPS);
        F1TotalTimeOF objective = new F1TotalTimeOF();
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());

        // Solve the problem
        System.out.println("Running greedy heuristic...\n");
        F1StrategyRepresentation<LapPerformance> solution = heuristic.solve(model);

        // Evaluate solution
        boolean isValid = model.isFeasable(solution);
        double totalTime = objective.value(model, solution);

        // Display results
        printResults(solution, isValid, totalTime);
        printStrategyDetails(solution);
        printTimeBreakdown(solution, totalTime);

        printFooter();
    }

    /**
     * Prints the program header.
     */
    private static void printHeader() {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("     F1 RACE STRATEGY OPTIMIZATION - GREEDY DEMO       ");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("Race Configuration:");
        System.out.println("  • Total Laps: " + RACE_LAPS);
        System.out.println("  • Algorithm: Greedy Heuristic (Tire Age Strategy)");
        System.out.println("  • Constraint: Second Tire Rule (≥2 compounds)");
        System.out.println();
    }

    /**
     * Prints main results: validity and total time.
     */
    private static void printResults(F1StrategyRepresentation<LapPerformance> solution,
                                     boolean isValid, double totalTime) {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("                        RESULTS                         ");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.printf("  Solution Valid:     %s\n", isValid ? "✓ YES" : "✗ NO");
        System.out.printf("  Total Race Time:    %.2f seconds\n", totalTime);
        System.out.printf("                      %.2f minutes\n", totalTime / 60.0);
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println();
    }

    /**
     * Prints strategy details: compounds used, pit stops, and stints.
     */
    private static void printStrategyDetails(F1StrategyRepresentation<LapPerformance> solution) {
        System.out.println("Strategy Details:");
        System.out.println("─────────────────────────────────────────────────────────");

        // Count compounds and pit stops
        int softLaps = 0, mediumLaps = 0, hardLaps = 0, pitStops = 0;

        for (int i = 0; i < solution.getLapCount(); i++) {
            LapState<LapPerformance> lap = solution.getLapState(i);

            switch (lap.getCompound()) {
                case SOFT:   softLaps++;   break;
                case MEDIUM: mediumLaps++; break;
                case HARD:   hardLaps++;   break;
            }

            if (lap.isPitStop()) pitStops++;
        }

        // Print compound usage
        System.out.println("Tire Compounds Used:");
        if (softLaps > 0) {
            System.out.printf("  • SOFT:   %2d laps (%.1f%%)\n",
                    softLaps, (softLaps * 100.0) / solution.getLapCount());
        }
        if (mediumLaps > 0) {
            System.out.printf("  • MEDIUM: %2d laps (%.1f%%)\n",
                    mediumLaps, (mediumLaps * 100.0) / solution.getLapCount());
        }
        if (hardLaps > 0) {
            System.out.printf("  • HARD:   %2d laps (%.1f%%)\n",
                    hardLaps, (hardLaps * 100.0) / solution.getLapCount());
        }

        System.out.println();
        System.out.println("Pit Stops:");
        System.out.printf("  • Total: %d stop%s\n", pitStops, pitStops == 1 ? "" : "s");

        // Print pit stop locations
        for (int i = 0; i < solution.getLapCount(); i++) {
            if (solution.getLapState(i).isPitStop()) {
                Compound before = i > 0 ? solution.getLapState(i - 1).getCompound() : Compound.SOFT;
                Compound after = solution.getLapState(i).getCompound();
                System.out.printf("  • Lap %d: %s → %s\n", i + 1, before, after);
            }
        }

        System.out.println();
        printStints(solution);
    }

    /**
     * Prints stint information (continuous runs on same tire set).
     */
    private static void printStints(F1StrategyRepresentation<LapPerformance> solution) {
        System.out.println("Race Stints:");

        int stintNum = 1;
        Compound currentCompound = solution.getLapState(0).getCompound();
        int stintStart = 1;

        for (int i = 1; i < solution.getLapCount(); i++) {
            if (solution.getLapState(i).isPitStop()) {
                int stintEnd = i;
                int stintLength = stintEnd - stintStart + 1;
                System.out.printf("  • Stint %d: Laps %2d-%2d (%2d laps on %s)\n",
                        stintNum, stintStart, stintEnd, stintLength, currentCompound);

                stintNum++;
                stintStart = i + 1;
                currentCompound = solution.getLapState(i).getCompound();
            }
        }

        // Final stint
        int stintLength = solution.getLapCount() - stintStart + 1;
        System.out.printf("  • Stint %d: Laps %2d-%2d (%2d laps on %s)\n",
                stintNum, stintStart, solution.getLapCount(), stintLength, currentCompound);

        System.out.println();
    }

    /**
     * Prints time breakdown by component.
     */
    private static void printTimeBreakdown(F1StrategyRepresentation<LapPerformance> solution,
                                           double totalTime) {
        System.out.println("Time Breakdown:");
        System.out.println("─────────────────────────────────────────────────────────");

        double compoundTime = 0.0;
        double degradationTime = 0.0;
        double pitStopTime = 0.0;

        for (int i = 0; i < solution.getLapCount(); i++) {
            LapState<LapPerformance> lap = solution.getLapState(i);

            // Compound delta
            switch (lap.getCompound()) {
                case SOFT:   compoundTime += 0.0; break;
                case MEDIUM: compoundTime += 0.5; break;
                case HARD:   compoundTime += 1.0; break;
            }

            // Degradation
            degradationTime += 0.05 * lap.getPerformance().getTireAge();

            // Pit stop
            if (lap.isPitStop()) pitStopTime += 28.0;
        }

        System.out.printf("  Compound Loss:      %8.2f sec (%5.1f%%)\n",
                compoundTime, (compoundTime / totalTime) * 100);
        System.out.printf("  Degradation Loss:   %8.2f sec (%5.1f%%)\n",
                degradationTime, (degradationTime / totalTime) * 100);
        System.out.printf("  Pit Stop Loss:      %8.2f sec (%5.1f%%)\n",
                pitStopTime, (pitStopTime / totalTime) * 100);
        System.out.println("  ─────────────────────────────────────");
        System.out.printf("  TOTAL TIME:         %8.2f sec (100.0%%)\n", totalTime);
        System.out.println();
    }

    /**
     * Prints the program footer.
     */
    private static void printFooter() {
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println("                    DEMO COMPLETED                      ");
        System.out.println("════════════════════════════════════════════════════════");
    }
}