package tasks;

import gui.LaunchPanel;
import solvers.HeuristicSearch.HeuristicSearch;

import javax.swing.*;

public class AStarTask extends SwingWorker<Object, Void> {

    private final LaunchPanel launchPanel;
    private final int executionTimeInSeconds;

    public AStarTask(LaunchPanel launchPanel, int executionTimeInSeconds) {
        this.launchPanel = launchPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
    }

    @Override
    protected Object doInBackground() {

        (new HeuristicSearch(
            launchPanel,
            executionTimeInSeconds,
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
