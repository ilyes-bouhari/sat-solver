package tasks;

import common.ClausesSet;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import solvers.BlindSearch.BlindSearch;

import javax.swing.*;

public class DepthFirstSearchTask extends SwingWorker<Object, Void> {

    private ClausesSet clausesSet;
    private ClausesPanel clausesPanel;
    private int executionTimeInSeconds;
    private LaunchPanel launchPanel;
    private SolutionPanel solutionPanel;

    public DepthFirstSearchTask(ClausesSet clausesSet, ClausesPanel clausesPanel, SolutionPanel solutionPanel, int executionTimeInSeconds, LaunchPanel launchPanel) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.launchPanel = launchPanel;
        this.solutionPanel = solutionPanel;
    }

    @Override
    protected Object doInBackground() throws Exception {

        BlindSearch.DepthFirstSearch(clausesSet, clausesPanel, solutionPanel, executionTimeInSeconds, launchPanel, this);

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
