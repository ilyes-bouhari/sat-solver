package tasks;

import enums.StoppingCriteria;
import gui.LaunchPanel;
import solvers.MetaheuristicSearch.MetaheuristicSearch;

import javax.swing.*;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneticAlgorithmTask extends SwingWorker<Object, Void> {

    private final LaunchPanel launchPanel;
    private final int populationSize;
    private final int maxIterations;
    private final int crossoverRate;
    private final int mutationRate;
    private final StoppingCriteria stoppingCriteria;
    private final int executionTimeInSeconds;

    @Override
    protected Object doInBackground() throws Exception {

        (new MetaheuristicSearch()).GeneticAlgorithm(
            launchPanel,
            populationSize,
            maxIterations,
            crossoverRate,
            mutationRate,
            stoppingCriteria,
            executionTimeInSeconds,
            this
        ).process();

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
