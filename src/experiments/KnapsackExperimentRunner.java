package experiments;

import BasarOzkasli.*;
import alg.IterationBasedTC;
import alg.TimeBasedTC;
import alg.ga.GA;
import problem.OptimizationProblem;
import problem.SimpleOptimizationProblem;
import representation.Solution;

import java.io.*;
import java.util.*;

public class KnapsackExperimentRunner {

    public static void main(String[] args) {
        System.out.println("=== SE 4492 Homework-2: Knapsack Problem Experiments ===\n");

        // Generate or load problem instances
        List<KnapsackModel> exp1Instances = generateInstances(5, 30, 50.0); // 5 instances for Exp 1
        List<KnapsackModel> exp2Instances = generateInstances(10, 30, 50.0); // 10 instances for Exp 2

        KnapsackObjectiveFunction objFunc = new KnapsackObjectiveFunction();

        // ========== EXPERIMENT 1: Parameter Tuning ==========
        System.out.println("=== Experiment 1: Parameter Tuning (Population Size) ===");
        System.out.println("Running GA with 5 instances, 5 population sizes, 10 repetitions each...\n");

        int[] popSizes = { 10, 20, 30, 50, 100 };
        int repetitions = 10;
        int iterations = 1000;

        // Results: [instance][popSize] = average fitness
        double[][] exp1Results = new double[exp1Instances.size()][popSizes.length];
        List<Integer> allPopSizes = new ArrayList<>();
        List<Double> allFitnessValues = new ArrayList<>();

        StringBuilder exp1Report = new StringBuilder();
        exp1Report.append("Experiment 1: Parameter Tuning Results\n");
        exp1Report.append("=".repeat(80)).append("\n");
        exp1Report.append(String.format("%-15s", "Instance"));
        for (int popSize : popSizes) {
            exp1Report.append(String.format("%-15s", "Pop=" + popSize));
        }
        exp1Report.append("\n").append("-".repeat(80)).append("\n");

        for (int instIdx = 0; instIdx < exp1Instances.size(); instIdx++) {
            KnapsackModel model = exp1Instances.get(instIdx);
            SimpleOptimizationProblem<KnapsackModel, KnapsackRepresentation> problem = 
                new SimpleOptimizationProblem<>(model, objFunc);

            System.out.println("Instance " + (instIdx + 1) + ": N=" + model.getItemCount() + 
                             ", Capacity=" + String.format("%.1f", model.getCapacity()));

            exp1Report.append(String.format("%-15s", "Instance " + (instIdx + 1)));

            for (int popIdx = 0; popIdx < popSizes.length; popIdx++) {
                int popSize = popSizes[popIdx];
                double totalBestFitness = 0;

                for (int r = 0; r < repetitions; r++) {
                    GA<KnapsackModel, KnapsackRepresentation> ga = new GA<>(new IterationBasedTC<>(iterations));
                    setupGA(ga, popSize);

                    Solution best = ga.solve(problem);
                    totalBestFitness += best.value();
                    
                    // Collect data for visualization
                    allPopSizes.add(popSize);
                    allFitnessValues.add(best.value());
                }

                double avgFitness = totalBestFitness / repetitions;
                exp1Results[instIdx][popIdx] = avgFitness;
                exp1Report.append(String.format("%-15.2f", avgFitness));
            }
            exp1Report.append("\n");
        }

        // Determine best population size (highest average across all instances)
        int bestPopSize = determineBestPopSize(exp1Results, popSizes);
        System.out.println("\nBest Population Size (from Exp 1): " + bestPopSize);
        exp1Report.append("\nBest Population Size: ").append(bestPopSize).append("\n");
        exp1Report.append("=".repeat(80)).append("\n\n");

        // Save Experiment 1 results
        saveReport("data/output/experiment1_results.txt", exp1Report.toString());
        System.out.println("\nExperiment 1 results saved to: data/output/experiment1_results.txt");

        // Visualization for Experiment 1
        try {
            GraalPythonVisualizer.plotParameterTuning(allPopSizes, allFitnessValues);
            System.out.println("Experiment 1 visualization saved to: data/output/exp1_boxplot.png");
        } catch (Exception e) {
            System.err.println("Visualization failed (Python/GraalVM may not be configured): " + e.getMessage());
        }

        // ========== EXPERIMENT 2: Comparison (Greedy vs GA) ==========
        System.out.println("\n=== Experiment 2: Comparison (Greedy vs GA) ===");
        System.out.println("Using best population size: " + bestPopSize);
        System.out.println("Running on 10 instances with 20 repetitions each...\n");

        int compRepetitions = 20;
        long timeLimitMillis = 60000; // 1 minute

        StringBuilder exp2Report = new StringBuilder();
        exp2Report.append("Experiment 2: Comparison Results (Greedy vs GA)\n");
        exp2Report.append("=".repeat(80)).append("\n");
        exp2Report.append(String.format("%-15s %-20s %-20s %-20s %-20s\n", 
            "Instance", "Greedy Avg Fit", "Greedy Avg Time", "GA Avg Fit", "GA Avg Time"));
        exp2Report.append("-".repeat(80)).append("\n");

        List<String> algorithms = new ArrayList<>();
        List<Double> avgFitnessGreedy = new ArrayList<>();
        List<Double> avgFitnessGA = new ArrayList<>();
        List<Double> avgTimeGreedy = new ArrayList<>();
        List<Double> avgTimeGA = new ArrayList<>();

        double totalGreedyFitness = 0, totalGAFitness = 0;
        double totalGreedyTime = 0, totalGATime = 0;

        for (int instIdx = 0; instIdx < exp2Instances.size(); instIdx++) {
            KnapsackModel model = exp2Instances.get(instIdx);
            SimpleOptimizationProblem<KnapsackModel, KnapsackRepresentation> problem = 
                new SimpleOptimizationProblem<>(model, objFunc);

            System.out.println("Instance " + (instIdx + 1) + ": N=" + model.getItemCount() + 
                             ", Capacity=" + String.format("%.1f", model.getCapacity()));

            // Greedy Heuristic
            KnapsackGreedyHeuristic greedy = new KnapsackGreedyHeuristic();
            double greedyTotalFit = 0, greedyTotalTime = 0;

            for (int r = 0; r < compRepetitions; r++) {
                long start = System.currentTimeMillis();
                KnapsackRepresentation sol = greedy.solve(model);
                long end = System.currentTimeMillis();
                greedyTotalFit += objFunc.value(model, sol);
                greedyTotalTime += (end - start);
            }

            double greedyAvgFit = greedyTotalFit / compRepetitions;
            double greedyAvgTime = greedyTotalTime / compRepetitions;

            // GA
            double gaTotalFit = 0, gaTotalTime = 0;

            for (int r = 0; r < compRepetitions; r++) {
                GA<KnapsackModel, KnapsackRepresentation> ga = new GA<>(new TimeBasedTC<>(timeLimitMillis));
                setupGA(ga, bestPopSize);

                long start = System.currentTimeMillis();
                Solution best = ga.solve(problem);
                long end = System.currentTimeMillis();

                gaTotalFit += best.value();
                gaTotalTime += (end - start);
            }

            double gaAvgFit = gaTotalFit / compRepetitions;
            double gaAvgTime = gaTotalTime / compRepetitions;

            exp2Report.append(String.format("%-15s %-20.2f %-20.2f %-20.2f %-20.2f\n",
                "Instance " + (instIdx + 1), greedyAvgFit, greedyAvgTime, gaAvgFit, gaAvgTime));

            totalGreedyFitness += greedyAvgFit;
            totalGAFitness += gaAvgFit;
            totalGreedyTime += greedyAvgTime;
            totalGATime += gaAvgTime;
        }

        // Overall averages
        double overallGreedyFit = totalGreedyFitness / exp2Instances.size();
        double overallGAFit = totalGAFitness / exp2Instances.size();
        double overallGreedyTime = totalGreedyTime / exp2Instances.size();
        double overallGATime = totalGATime / exp2Instances.size();

        exp2Report.append("-".repeat(80)).append("\n");
        exp2Report.append(String.format("%-15s %-20.2f %-20.2f %-20.2f %-20.2f\n",
            "OVERALL AVG", overallGreedyFit, overallGreedyTime, overallGAFit, overallGATime));
        exp2Report.append("=".repeat(80)).append("\n");

        // Save Experiment 2 results
        saveReport("data/output/experiment2_results.txt", exp2Report.toString());
        System.out.println("\nExperiment 2 results saved to: data/output/experiment2_results.txt");

        // Visualization for Experiment 2
        try {
            GraalPythonVisualizer.plotComparison(
                Arrays.asList("Greedy", "GA"), 
                Arrays.asList(overallGreedyFit, overallGAFit),
                Arrays.asList(overallGreedyTime, overallGATime));
            System.out.println("Experiment 2 visualizations saved to: data/output/exp2_fitness_comparison.png and exp2_time_comparison.png");
        } catch (Exception e) {
            System.err.println("Visualization failed (Python/GraalVM may not be configured): " + e.getMessage());
        }

        // Print summary
        System.out.println("\n=== Summary ===");
        System.out.println("Experiment 1: Parameter tuning completed");
        System.out.println("  - Best Population Size: " + bestPopSize);
        System.out.println("Experiment 2: Comparison completed");
        System.out.println("  - Greedy Average Fitness: " + String.format("%.2f", overallGreedyFit));
        System.out.println("  - GA Average Fitness: " + String.format("%.2f", overallGAFit));
        System.out.println("  - Greedy Average Time: " + String.format("%.2f", overallGreedyTime) + " ms");
        System.out.println("  - GA Average Time: " + String.format("%.2f", overallGATime) + " ms");
    }

    private static void setupGA(GA<KnapsackModel, KnapsackRepresentation> ga, int popSize) {
        ga.setPopulationSize(popSize);
        ga.setCrossOverRate(0.9);
        ga.setMutationRate(1.0);
        ga.setInitialSolutionGenerator(new KnapsackRandomISG());
        ga.setCrossOverOperator(new KnapsackCrossover());
        ga.setMutationOperator(new KnapsackMutation());
        ga.setParentSelector(new KnapsackParentSelector());
        ga.setVictimSelector(new KnapsackVictimSelector());
    }

    private static int determineBestPopSize(double[][] results, int[] popSizes) {
        double[] avgByPopSize = new double[popSizes.length];
        
        for (int popIdx = 0; popIdx < popSizes.length; popIdx++) {
            double sum = 0;
            for (int instIdx = 0; instIdx < results.length; instIdx++) {
                sum += results[instIdx][popIdx];
            }
            avgByPopSize[popIdx] = sum / results.length;
        }

        int bestIdx = 0;
        for (int i = 1; i < avgByPopSize.length; i++) {
            if (avgByPopSize[i] > avgByPopSize[bestIdx]) {
                bestIdx = i;
            }
        }

        return popSizes[bestIdx];
    }

    private static List<KnapsackModel> generateInstances(int count, int itemCount, double baseCapacity) {
        List<KnapsackModel> instances = new ArrayList<>();
        Random rand = new Random(42); // Fixed seed for reproducibility

        for (int i = 0; i < count; i++) {
            double[] weights = new double[itemCount];
            double[] values = new double[itemCount];
            
            // Generate weights and values
            double totalWeight = 0;
            for (int j = 0; j < itemCount; j++) {
                weights[j] = 1 + rand.nextInt(15); // Weight between 1-15
                values[j] = 5 + rand.nextInt(20);   // Value between 5-24
                totalWeight += weights[j];
            }

            // Capacity is a fraction of total weight (moderate difficulty)
            double capacity = baseCapacity + (totalWeight * 0.3) + rand.nextDouble() * (totalWeight * 0.2);
            
            instances.add(new KnapsackModel(weights, values, capacity));
        }

        return instances;
    }

    private static void saveReport(String filepath, String content) {
        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs(); // Create directories if they don't exist
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.print(content);
            }
        } catch (IOException e) {
            System.err.println("Failed to save report to " + filepath + ": " + e.getMessage());
        }
    }
}
