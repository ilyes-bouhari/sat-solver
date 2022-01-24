package solvers.MetaheuristicSearch;

import common.ClausesSet;
import common.Solution;
import enums.StoppingCriteria;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import solvers.MetaheuristicSearch.GeneticAlgorithm.GeneticAlgorithm;
import tasks.GeneticAlgorithmTask;

public class MetaheuristicSearch {

    public GeneticAlgorithm GeneticAlgorithm(
        ClausesSet clausesSet,
        ClausesPanel clausesPanel,
        SolutionPanel solutionPanel,
        LaunchPanel launchPanel,

        int populationSize,
        int maxIterations,
        int crossoverRate,
        int mutationRate,
        StoppingCriteria stoppingCriteria,
        int executionTimeInSeconds,

        GeneticAlgorithmTask task
    ) {
        return new GeneticAlgorithm(
            clausesSet,
            clausesPanel,
            solutionPanel,
            launchPanel,

            populationSize,
            maxIterations,
            crossoverRate,
            mutationRate,
            stoppingCriteria,
            executionTimeInSeconds,

            task
        );
    }
}
