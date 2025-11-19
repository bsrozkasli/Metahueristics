package Test;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import problem.f1.F1Model;
import problem.f1.F1TotalTimeOF;
import alg.greedy.f1.F1GreedyHeuristic;
import alg.greedy.f1.F1TireAgeEvaluator;
import alg.greedy.f1.F1Evaluator;
import representation.f1.*;

/**
 * Comprehensive test suite for F1 Race Strategy Optimization
 * Tests all components: Model, Objective Function, Representation, and Greedy Heuristic
 *
 * @author Basar Ozkaşlı
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class F1StrategyTest {

    private F1Model model58;
    private F1Model model20;
    private F1TotalTimeOF objectiveFunction;

    @BeforeEach
    void setUp() {
        model58 = new F1Model(58);
        model20 = new F1Model(20);
        objectiveFunction = new F1TotalTimeOF();
    }


    @Test
    @Order(1)
    @DisplayName("LapPerformance: Constructor and Getters")
    void testLapPerformanceCreation() {
        LapPerformance perf = new LapPerformance(0.5, 0.3, 15, 75.5);

        assertEquals(0.5, perf.getWeather(), 0.001);
        assertEquals(0.3, perf.getTireWear(), 0.001);
        assertEquals(15, perf.getTireAge());
        assertEquals(75.5, perf.getFuelLoad(), 0.001);
    }

    @Test
    @Order(2)
    @DisplayName("LapPerformance: Copy Method")
    void testLapPerformanceCopy() {
        LapPerformance original = new LapPerformance(0.0, 0.1, 10, 90.0);
        LapPerformance copy = original.copy();

        assertNotSame(original, copy, "Copy should create new instance");
        assertEquals(original.getWeather(), copy.getWeather());
        assertEquals(original.getTireAge(), copy.getTireAge());

        // Modify copy, original should be unchanged
        copy.setTireAge(20);
        assertEquals(10, original.getTireAge());
        assertEquals(20, copy.getTireAge());
    }

    @Test
    @Order(3)
    @DisplayName("LapState: Constructor and Compound Assignment")
    void testLapStateCreation() {
        LapPerformance perf = new LapPerformance(0.0, 0.05, 5, 95.0);
        LapState<LapPerformance> state = new LapState<>(Compound.SOFT, false, perf);

        assertEquals(Compound.SOFT, state.getCompound());
        assertFalse(state.isPitStop());
        assertNotNull(state.getPerformance());
        assertEquals(5, state.getPerformance().getTireAge());
    }

    @Test
    @Order(4)
    @DisplayName("LapState: Deep Copy with Generics")
    void testLapStateCopy() {
        LapPerformance perf = new LapPerformance(0.0, 0.1, 10, 85.0);
        LapState<LapPerformance> original = new LapState<>(Compound.MEDIUM, true, perf);
        LapState<LapPerformance> copy = original.copy();

        assertNotSame(original, copy);
        assertNotSame(original.getPerformance(), copy.getPerformance());
        assertEquals(original.getCompound(), copy.getCompound());
        assertEquals(original.isPitStop(), copy.isPitStop());
    }

    @Test
    @Order(5)
    @DisplayName("F1StrategyRepresentation: Chromosome Length")
    void testStrategyRepresentationLength() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[58];
        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        assertEquals(58, strategy.getLapCount());
    }

    @Test
    @Order(6)
    @DisplayName("F1StrategyRepresentation: Copy Method")
    void testStrategyRepresentationCopy() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[3];
        for (int i = 0; i < 3; i++) {
            chromosome[i] = new LapState<>(Compound.SOFT, false,
                    new LapPerformance(0.0, 0.0, i, 100.0));
        }

        F1StrategyRepresentation<LapPerformance> original =
                new F1StrategyRepresentation<>(chromosome);
        F1StrategyRepresentation<LapPerformance> copy =
                (F1StrategyRepresentation<LapPerformance>) original.copy();

        assertNotSame(original, copy);
        assertEquals(original.getLapCount(), copy.getLapCount());

        // Modify copy's chromosome
        copy.getLapState(0).setCompound(Compound.HARD);
        assertEquals(Compound.SOFT, original.getLapState(0).getCompound());
        assertEquals(Compound.HARD, copy.getLapState(0).getCompound());
    }


    @Test
    @Order(7)
    @DisplayName("F1Model: Correct Lap Count Validation")
    void testModelLapCountValidation() {
        F1StrategyRepresentation<LapPerformance> validStrategy = createValidStrategy(58);
        F1StrategyRepresentation<LapPerformance> invalidStrategy = createValidStrategy(50);

        assertTrue(model58.isFeasable(validStrategy));
        assertFalse(model58.isFeasable(invalidStrategy));
    }

    @Test
    @Order(8)
    @DisplayName("F1Model: Second Tire Constraint - Valid (2 Compounds)")
    void testSecondTireConstraintValid() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[58];

        // 30 laps SOFT, 28 laps MEDIUM
        for (int i = 0; i < 30; i++) {
            chromosome[i] = new LapState<>(Compound.SOFT, i == 0 ? false : false,
                    new LapPerformance(0.0, 0.0, i, 100.0));
        }
        for (int i = 30; i < 58; i++) {
            chromosome[i] = new LapState<>(Compound.MEDIUM, i == 30 ? true : false,
                    new LapPerformance(0.0, 0.0, i - 30, 100.0));
        }

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        assertTrue(model58.isFeasable(strategy),
                "Strategy with 2 compounds should be valid");
    }

    @Test
    @Order(9)
    @DisplayName("F1Model: Second Tire Constraint - Invalid (1 Compound)")
    void testSecondTireConstraintInvalid() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[58];

        // All laps with SOFT only
        for (int i = 0; i < 58; i++) {
            chromosome[i] = new LapState<>(Compound.SOFT, false,
                    new LapPerformance(0.0, 0.0, i, 100.0));
        }

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        assertFalse(model58.isFeasable(strategy),
                "Strategy with only 1 compound should be INVALID");
    }

    @Test
    @Order(10)
    @DisplayName("F1Model: Second Tire Constraint - Valid (3 Compounds)")
    void testSecondTireConstraintThreeCompounds() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[58];

        // 20 SOFT, 20 MEDIUM, 18 HARD
        for (int i = 0; i < 20; i++) {
            chromosome[i] = new LapState<>(Compound.SOFT, false,
                    new LapPerformance(0.0, 0.0, i, 100.0));
        }
        for (int i = 20; i < 40; i++) {
            chromosome[i] = new LapState<>(Compound.MEDIUM, i == 20 ? true : false,
                    new LapPerformance(0.0, 0.0, i - 20, 100.0));
        }
        for (int i = 40; i < 58; i++) {
            chromosome[i] = new LapState<>(Compound.HARD, i == 40 ? true : false,
                    new LapPerformance(0.0, 0.0, i - 40, 100.0));
        }

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        assertTrue(model58.isFeasable(strategy),
                "Strategy with 3 compounds should be valid");
    }

    @Test
    @Order(11)
    @DisplayName("F1Model: Null Safety Checks")
    void testModelNullSafety() {
        assertFalse(model58.isFeasable(null));

        F1StrategyRepresentation<LapPerformance> emptyStrategy =
                new F1StrategyRepresentation<>(null);
        assertFalse(model58.isFeasable(emptyStrategy));
    }

    // ═══════════════════════════════════════════════════════════
    // OBJECTIVE FUNCTION TESTS
    // ═══════════════════════════════════════════════════════════

    @Test
    @Order(12)
    @DisplayName("ObjectiveFunction: Type is Minimization")
    void testObjectiveFunctionType() {
        assertEquals(problem.ObjectiveType.Minimization, objectiveFunction.type());
    }

    @Test
    @Order(13)
    @DisplayName("ObjectiveFunction: Delta Calculation (Compound Time Loss)")
    void testDeltaCalculation() {
        // Create 1-lap strategies with different compounds
        F1StrategyRepresentation<LapPerformance> softStrategy =
                createSingleLapStrategy(Compound.SOFT, 0);
        F1StrategyRepresentation<LapPerformance> mediumStrategy =
                createSingleLapStrategy(Compound.MEDIUM, 0);
        F1StrategyRepresentation<LapPerformance> hardStrategy =
                createSingleLapStrategy(Compound.HARD, 0);

        F1Model model1 = new F1Model(1);

        double softTime = objectiveFunction.value(model1, softStrategy);
        double mediumTime = objectiveFunction.value(model1, mediumStrategy);
        double hardTime = objectiveFunction.value(model1, hardStrategy);

        // SOFT delta = 0.0
        assertEquals(0.0, softTime, 0.001, "SOFT compound should add 0.0 seconds");

        // MEDIUM delta = 0.5
        assertEquals(0.5, mediumTime, 0.001, "MEDIUM compound should add 0.5 seconds");

        // HARD delta = 1.0
        assertEquals(1.0, hardTime, 0.001, "HARD compound should add 1.0 seconds");
    }

    @Test
    @Order(14)
    @DisplayName("ObjectiveFunction: Degradation Calculation")
    void testDegradationCalculation() {
        // Tire age = 10 laps → degradation = 0.05 × 10 = 0.5 seconds
        F1StrategyRepresentation<LapPerformance> strategy =
                createSingleLapStrategy(Compound.SOFT, 10);

        F1Model model1 = new F1Model(1);
        double time = objectiveFunction.value(model1, strategy);

        // delta (SOFT) + degradation (0.05 × 10) = 0.0 + 0.5 = 0.5
        assertEquals(0.5, time, 0.001);
    }

    @Test
    @Order(15)
    @DisplayName("ObjectiveFunction: Pit Stop Time Penalty")
    void testPitStopPenalty() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[1];
        chromosome[0] = new LapState<>(Compound.SOFT, true, // Pit stop = true
                new LapPerformance(0.0, 0.0, 0, 100.0));

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        F1Model model1 = new F1Model(1);
        double time = objectiveFunction.value(model1, strategy);

        // delta (0.0) + degradation (0.0) + pit stop (28.0) = 28.0
        assertEquals(28.0, time, 0.001);
    }

    @Test
    @Order(16)
    @DisplayName("ObjectiveFunction: Complete Formula Test")
    void testCompleteFormulaCalculation() {
        // Lap with: MEDIUM compound, tire age 15, pit stop
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[1];
        chromosome[0] = new LapState<>(Compound.MEDIUM, true,
                new LapPerformance(0.0, 0.0, 15, 100.0));

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        F1Model model1 = new F1Model(1);
        double time = objectiveFunction.value(model1, strategy);

        // delta (0.5) + degradation (0.05 × 15 = 0.75) + pit stop (28.0) = 29.25
        assertEquals(29.25, time, 0.001);
    }

    @Test
    @Order(17)
    @DisplayName("ObjectiveFunction: Multi-Lap Cumulative Calculation")
    void testMultiLapCalculation() {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[3];

        // Lap 1: SOFT, age 0, no pit
        chromosome[0] = new LapState<>(Compound.SOFT, false,
                new LapPerformance(0.0, 0.0, 0, 100.0));
        // Lap 2: SOFT, age 1, no pit
        chromosome[1] = new LapState<>(Compound.SOFT, false,
                new LapPerformance(0.0, 0.0, 1, 98.5));
        // Lap 3: MEDIUM, age 0, pit stop
        chromosome[2] = new LapState<>(Compound.MEDIUM, true,
                new LapPerformance(0.0, 0.0, 0, 97.0));

        F1StrategyRepresentation<LapPerformance> strategy =
                new F1StrategyRepresentation<>(chromosome);

        F1Model model3 = new F1Model(3);
        double totalTime = objectiveFunction.value(model3, strategy);

        // Lap 1: 0.0 + 0.0 + 0.0 = 0.0
        // Lap 2: 0.0 + 0.05 + 0.0 = 0.05
        // Lap 3: 0.5 + 0.0 + 28.0 = 28.5
        // Total: 28.55
        assertEquals(28.55, totalTime, 0.001);
    }

    // ═══════════════════════════════════════════════════════════
    // GREEDY HEURISTIC TESTS
    // ═══════════════════════════════════════════════════════════

    @Test
    @Order(18)
    @DisplayName("GreedyHeuristic: Generates Valid Strategy")
    void testGreedyGeneratesValidStrategy() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model58);

        assertNotNull(strategy);
        assertEquals(58, strategy.getLapCount());
        assertTrue(model58.isFeasable(strategy),
                "Greedy heuristic must produce valid strategy");
    }

    @Test
    @Order(19)
    @DisplayName("GreedyHeuristic: Satisfies Second Tire Constraint")
    void testGreedySatisfiesSecondTireConstraint() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model58);

        boolean hasSoft = false, hasMedium = false, hasHard = false;

        for (int i = 0; i < strategy.getLapCount(); i++) {
            Compound compound = strategy.getLapState(i).getCompound();
            if (compound == Compound.SOFT) hasSoft = true;
            if (compound == Compound.MEDIUM) hasMedium = true;
            if (compound == Compound.HARD) hasHard = true;
        }

        int compoundCount = (hasSoft ? 1 : 0) + (hasMedium ? 1 : 0) + (hasHard ? 1 : 0);
        assertTrue(compoundCount >= 2,
                "Strategy must use at least 2 different compounds");
    }

    @Test
    @Order(20)
    @DisplayName("GreedyHeuristic: Makes At Least One Pit Stop")
    void testGreedyMakesPitStop() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model58);

        int pitStopCount = 0;
        for (int i = 0; i < strategy.getLapCount(); i++) {
            if (strategy.getLapState(i).isPitStop()) {
                pitStopCount++;
            }
        }

        assertTrue(pitStopCount >= 1,
                "Strategy must have at least 1 pit stop");
    }

    @Test
    @Order(21)
    @DisplayName("GreedyHeuristic: Tire Age Resets After Pit Stop")
    void testTireAgeResetsAfterPitStop() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model58);

        for (int i = 0; i < strategy.getLapCount() - 1; i++) {
            LapState<LapPerformance> currentLap = strategy.getLapState(i);
            LapState<LapPerformance> nextLap = strategy.getLapState(i + 1);

            if (currentLap.isPitStop()) {
                assertEquals(0, nextLap.getPerformance().getTireAge(),
                        "Tire age should reset to 0 after pit stop at lap " + (i + 1));
            }
        }
    }

    @Test
    @Order(22)
    @DisplayName("GreedyHeuristic: Fuel Load Never Negative")
    void testFuelLoadNeverNegative() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model58);

        for (int i = 0; i < strategy.getLapCount(); i++) {
            double fuelLoad = strategy.getLapState(i).getPerformance().getFuelLoad();
            assertTrue(fuelLoad >= 0.0,
                    "Fuel load should never be negative at lap " + (i + 1) + ", got: " + fuelLoad);
        }
    }

    @Test
    @Order(23)
    @DisplayName("GreedyHeuristic: Deterministic Behavior")
    void testGreedyDeterministicBehavior() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());

        F1StrategyRepresentation<LapPerformance> strategy1 = heuristic.solve(model58);
        F1StrategyRepresentation<LapPerformance> strategy2 = heuristic.solve(model58);

        double time1 = objectiveFunction.value(model58, strategy1);
        double time2 = objectiveFunction.value(model58, strategy2);

        assertEquals(time1, time2, 0.001,
                "Greedy heuristic should produce same result for same input");
    }

    @Test
    @Order(24)
    @DisplayName("GreedyHeuristic: Works for Different Race Lengths")
    void testGreedyDifferentRaceLengths() {
        F1Model shortRace = new F1Model(20);
        F1Model normalRace = new F1Model(58);
        F1Model longRace = new F1Model(70);

        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());

        F1StrategyRepresentation<LapPerformance> shortStrategy = heuristic.solve(shortRace);
        F1StrategyRepresentation<LapPerformance> normalStrategy = heuristic.solve(normalRace);
        F1StrategyRepresentation<LapPerformance> longStrategy = heuristic.solve(longRace);

        assertEquals(20, shortStrategy.getLapCount());
        assertEquals(58, normalStrategy.getLapCount());
        assertEquals(70, longStrategy.getLapCount());

        assertTrue(shortRace.isFeasable(shortStrategy));
        assertTrue(normalRace.isFeasable(normalStrategy));
        assertTrue(longRace.isFeasable(longStrategy));
    }

    // ═══════════════════════════════════════════════════════════
    // INTEGRATION TESTS
    // ═══════════════════════════════════════════════════════════

    @Test
    @Order(25)
    @DisplayName("Integration: Complete Workflow")
    void testCompleteWorkflow() {
        // Step 1: Create model
        F1Model model = new F1Model(58);

        // Step 2: Create objective function
        F1TotalTimeOF objective = new F1TotalTimeOF();

        // Step 3: Create greedy heuristic
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());

        // Step 4: Generate solution
        F1StrategyRepresentation<LapPerformance> strategy = heuristic.solve(model);

        // Step 5: Validate solution
        assertTrue(model.isFeasable(strategy), "Solution must be feasible");

        // Step 6: Evaluate solution
        double totalTime = objective.value(model, strategy);

        assertTrue(totalTime > 0, "Total time must be positive");
        assertTrue(totalTime < 10000, "Total time should be reasonable (< 10000s)");
    }

    @Test
    @Order(26)
    @DisplayName("Integration: Copy Independence")
    void testCopyIndependence() {
        F1GreedyHeuristic heuristic = new F1GreedyHeuristic(new F1TireAgeEvaluator());
        F1StrategyRepresentation<LapPerformance> original = heuristic.solve(model20);
        F1StrategyRepresentation<LapPerformance> copy =
                (F1StrategyRepresentation<LapPerformance>) original.copy();

        // Modify copy
        copy.getLapState(0).setCompound(Compound.HARD);
        copy.getLapState(0).getPerformance().setTireAge(999);

        // Original should be unchanged
        assertNotEquals(Compound.HARD, original.getLapState(0).getCompound());
        assertNotEquals(999, original.getLapState(0).getPerformance().getTireAge());
    }

    // ═══════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════

    private F1StrategyRepresentation<LapPerformance> createValidStrategy(int laps) {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[laps];

        for (int i = 0; i < laps / 2; i++) {
            chromosome[i] = new LapState<>(Compound.SOFT, false,
                    new LapPerformance(0.0, 0.0, i, 100.0));
        }
        for (int i = laps / 2; i < laps; i++) {
            chromosome[i] = new LapState<>(Compound.MEDIUM, i == laps / 2 ? true : false,
                    new LapPerformance(0.0, 0.0, i - laps / 2, 100.0));
        }

        return new F1StrategyRepresentation<>(chromosome);
    }

    private F1StrategyRepresentation<LapPerformance> createSingleLapStrategy(
            Compound compound, int tireAge) {
        @SuppressWarnings("unchecked")
        LapState<LapPerformance>[] chromosome = new LapState[1];
        chromosome[0] = new LapState<>(compound, false,
                new LapPerformance(0.0, 0.0, tireAge, 100.0));

        return new F1StrategyRepresentation<>(chromosome);
    }
}