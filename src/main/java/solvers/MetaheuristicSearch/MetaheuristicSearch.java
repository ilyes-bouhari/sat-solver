package solvers.MetaheuristicSearch;

import common.Solution;
import enums.StoppingCriteria;
import gui.LaunchPanel;
import solvers.MetaheuristicSearch.AntColonySystem.AntColonySystem;
import solvers.MetaheuristicSearch.GeneticAlgorithm.GeneticAlgorithm;
import tasks.ACSTask;
import tasks.GeneticAlgorithmTask;

public class MetaheuristicSearch {

    public GeneticAlgorithm GeneticAlgorithm(
        LaunchPanel launchPanel,
        int populationSize,
        int maxIterations,
        int crossoverRate,
        int mutationRate,
        StoppingCriteria stoppingCriteria,
        GeneticAlgorithmTask task
    ) {
        return new GeneticAlgorithm(
            launchPanel,
            populationSize,
            maxIterations,
            crossoverRate,
            mutationRate,
            stoppingCriteria,
            task
        );
    }

    public AntColonySystem AntColonySystem(
        LaunchPanel launchPanel,
        Solution baseSolution,
        double alpha,
        double beta,
        int maxIterations,
        int numberOfAnts,
        double pheromoneInit,
        double evaporationRate,
        double q0,
        int maxStep,
        ACSTask task
    ) {
        return new AntColonySystem(
            launchPanel,
            baseSolution,
            alpha,
            beta,
            maxIterations,
            numberOfAnts,
            pheromoneInit,
            evaporationRate,
            q0,
            maxStep,
            task
        );
    }
}
