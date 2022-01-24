package tasks;

import common.ClausesSet;
import enums.StoppingCriteria;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import solvers.MetaheuristicSearch.MetaheuristicSearch;

import javax.swing.*;

public class GeneticAlgorithmTask extends SwingWorker<Object, Void> {

    private ClausesSet clausesSet;
    private ClausesPanel clausesPanel;
    private SolutionPanel solutionPanel;
    private LaunchPanel launchPanel;

    private int populationSize;
    private int maxIterations;
    private int crossoverRate;
    private int mutationRate;
    private StoppingCriteria stoppingCriteria;
    private int executionTimeInSeconds;

    public GeneticAlgorithmTask(
        ClausesSet clausesSet,
        ClausesPanel clausesPanel,
        SolutionPanel solutionPanel,
        LaunchPanel launchPanel,

        int populationSize,
        int maxIterations,
        int crossoverRate,
        int mutationRate,
        StoppingCriteria stoppingCriteria,
        int executionTimeInSeconds
    ) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.solutionPanel = solutionPanel;
        this.launchPanel = launchPanel;

        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.stoppingCriteria = stoppingCriteria;
        this.executionTimeInSeconds = executionTimeInSeconds;
    }

    @Override
    protected Object doInBackground() throws Exception {

        (new MetaheuristicSearch()).GeneticAlgorithm(
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
