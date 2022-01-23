package tasks;

import common.ClausesSet;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import solvers.HeuristicSearch.HeuristicSearch;

import javax.swing.*;

public class AStarTask extends SwingWorker<Object, Void> {

    private ClausesSet clausesSet;
    private ClausesPanel clausesPanel;
    private int executionTimeInSeconds;
    private LaunchPanel launchPanel;
    private SolutionPanel solutionPanel;

    public AStarTask(ClausesSet clausesSet, ClausesPanel clausesPanel, SolutionPanel solutionPanel, int executionTimeInSeconds, LaunchPanel launchPanel) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.launchPanel = launchPanel;
        this.solutionPanel = solutionPanel;
    }

    @Override
    protected Object doInBackground() throws Exception {

        (new HeuristicSearch(
            clausesSet,
            clausesPanel,
            solutionPanel,
            executionTimeInSeconds,
            launchPanel,
            this
        )).AStar();

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
