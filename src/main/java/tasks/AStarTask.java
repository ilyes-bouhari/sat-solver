package tasks;

import common.ClausesSet;
import gui.ClausesPanel;
import gui.LaunchPanel;
import solvers.HeuristicSearch.HeuristicSearch;

import javax.swing.*;

public class AStarTask extends SwingWorker<Object, Void> {

    private ClausesSet clausesSet;
    private ClausesPanel clausesPanel;
    private int executionTimeInSeconds;
    private LaunchPanel launchPanel;

    public AStarTask(ClausesSet clausesSet, ClausesPanel clausesPanel, int executionTimeInSeconds, LaunchPanel launchPanel) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.launchPanel = launchPanel;
    }

    @Override
    protected Object doInBackground() throws Exception {

        HeuristicSearch.AStar(clausesSet, clausesPanel, executionTimeInSeconds, launchPanel, this);

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
