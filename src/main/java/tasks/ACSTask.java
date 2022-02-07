package tasks;

import javax.swing.*;
import lombok.AllArgsConstructor;

import gui.LaunchPanel;
import common.Solution;
import solvers.HeuristicSearch.HeuristicSearch;
import solvers.MetaheuristicSearch.AntColonySystem.ACS;

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

        Solution baseSolution = (new HeuristicSearch(
            launchPanel.getClausesPanel().getClausesSet(),
            null,
            null,
            1,
            null,
            null
        )).AStar();

        return (new ACS(
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
        ).run());
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
