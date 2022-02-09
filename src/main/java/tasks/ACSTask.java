package tasks;

import javax.swing.*;
import lombok.AllArgsConstructor;

import gui.LaunchPanel;
import common.Solution;
import solvers.HeuristicSearch.HeuristicSearch;
import solvers.MetaheuristicSearch.AntColonySystem.AntColonySystem;
import solvers.MetaheuristicSearch.MetaheuristicSearch;

@AllArgsConstructor
public class ACSTask extends SwingWorker<Object, Void> {

    private final LaunchPanel launchPanel;

    private final double alpha;
    private final double beta;
    private final int maxIterations;
    private final int numberOfAnts;
    private final double pheromoneInit;
    private final double evaporationRate;
    private final double q0;
    private final int maxStep;

    @Override
    protected Object doInBackground() {

        double executionTimeInSeconds = launchPanel.getExecutionTimeInSeconds();
        launchPanel.setExecutionTimeInSeconds(1);
        Solution baseSolution = (new HeuristicSearch(
            launchPanel,
            null
        )).AStar();

        launchPanel.setExecutionTimeInSeconds(executionTimeInSeconds);
        (new MetaheuristicSearch()).AntColonySystem(
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
            this
        ).run();

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
