package BasarOzkasli;

import alg.ga.ParentSelector;
import problem.OptimizationProblem;
import problem.ProblemModel;
import representation.Representation;
import representation.Solution;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class KnapsackParentSelector implements ParentSelector {

    // Select exactly 2 parents using Tournament Selection (or Roulette)
    // Tournament is often better for preserving diversity and simple to control
    // pressure.
    // User mentioned "Rulet veya Turnuva". I'll implement Tournament size 3 for
    // robustness.

    @Override
    public <R extends Representation, PM extends ProblemModel<R>> List<Solution> select(
            OptimizationProblem<PM, R> problem, List<Solution> population) {
        List<Solution> parents = new ArrayList<>();
        parents.add(tournamentSelect(population, 5));
        parents.add(tournamentSelect(population, 5));
        return parents;
    }

    private Solution tournamentSelect(List<Solution> population, int tournamentSize) {
        Solution best = null;
        Random rand = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            Solution candidate = population.get(rand.nextInt(population.size()));
            if (best == null || candidate.value() > best.value()) {
                best = candidate;
            }
        }
        return best;
    }
}
