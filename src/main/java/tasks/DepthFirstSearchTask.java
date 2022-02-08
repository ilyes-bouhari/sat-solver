package tasks;

import gui.LaunchPanel;
import solvers.BlindSearch.BlindSearch;

import javax.swing.*;

public class DepthFirstSearchTask extends SwingWorker<Object, Void> {

    private int executionTimeInSeconds;
    private LaunchPanel launchPanel;

    public DepthFirstSearchTask(LaunchPanel launchPanel, int executionTimeInSeconds) {
        this.launchPanel = launchPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
    }

    @Override
    protected Object doInBackground() {

        (new BlindSearch(
            launchPanel,
            executionTimeInSeconds,
            this
        )).DepthFirstSearch();

        return null;
    }

    @Override
    protected void done() {
        super.done();
        launchPanel.toggleOnRunning(false);
    }
}
