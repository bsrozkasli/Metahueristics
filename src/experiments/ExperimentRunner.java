package experiments;

import alg.IterationBasedTC;
import alg.Metaheuristic;
import alg.ls.LocalSearch;
import alg.ls.tsp.TSPSwapNG;
import problem.OptimizationProblem;
import problem.SimpleOptimizationProblem;
import problem.tsp.TSPMinimumDistanceOF;
import problem.tsp.TSPModel;
import problem.tsp.TSPRandomISG;
import representation.IntegerPermutation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExperimentRunner implements Runnable{

    int repeatCount;
    ResultProcessor resultProcessor;
    Iterable<OptimizationProblem> problemProvider;
    Iterable<Metaheuristic> algorithmProvider;
    List<DataCollector> dataCollectors;

    public ExperimentRunner(int repeatCount, ResultProcessor resultProcessor, Iterable<OptimizationProblem> problemProvider, Iterable<Metaheuristic> algorithmProvider, List<DataCollector> dataCollectors)
    {
        this.repeatCount = repeatCount;
        this.resultProcessor = resultProcessor;
        this.problemProvider = problemProvider;
        this.algorithmProvider = algorithmProvider;
        this.dataCollectors = dataCollectors;
    }

    @Override
    public void run() {
        System.out.println("<<START OF EXPERIMENTS>>");
        for(Metaheuristic alg: algorithmProvider) {
            for (OptimizationProblem problem : problemProvider) {
                performRepeatedRun(alg,problem);
            }
        }
    }

    private void performRepeatedRun(Metaheuristic alg, OptimizationProblem problem) {
        for (int repeat = 0; repeat < repeatCount; repeat++) {
            alg.solve(problem);
            dataCollectors.forEach((dc)->dc.collect(alg));
        }
        StringBuilder resultBuilder= new StringBuilder();
        resultBuilder.append("[" + alg + "] ("+ problem + ") " );
        dataCollectors.forEach(dc->resultBuilder.append(dc.resultString()).append(" "));
        resultProcessor.process(resultBuilder.toString());
    }

    public static void main(String[] args) {
        Metaheuristic alg = new LocalSearch<TSPModel, IntegerPermutation>(
                                new IterationBasedTC(100),
                                new TSPRandomISG(),
                                new TSPSwapNG());

        OptimizationProblem problem = new SimpleOptimizationProblem(TSPModel.randomInstance(100),new TSPMinimumDistanceOF());

        Iterable<Metaheuristic> algP = Collections.singletonList(alg);
        Iterable<OptimizationProblem> problemP = Collections.singletonList(problem);
        ResultProcessor rp = new ToTextFileRP("./data/output/exp1.txt");
        List<DataCollector> dataCollectors = Arrays.asList(
                                                    new AvgBestDC(),
                                                    new AvgIterationDC(),
                                                    new AvgBestAchieveTimeDC(),
                                                    new AvgRunTimeDC());

        ExperimentRunner runner = new ExperimentRunner(
                                        10,
                                        rp,
                                        problemP,
                                        algP,
                                        dataCollectors );

        runner.run();

    }
}
