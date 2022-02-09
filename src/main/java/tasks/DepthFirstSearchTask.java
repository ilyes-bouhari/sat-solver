package tasks;

import gui.LaunchPanel;
import solvers.BlindSearch.BlindSearch;

import javax.swing.*;

public class DepthFirstSearchTask extends SwingWorker<Object, Void> {

    private LaunchPanel launchPanel;

    public DepthFirstSearchTask(LaunchPanel launchPanel) {
        this.launchPanel = launchPanel;
    }

    @Override
    protected Object doInBackground() {

        (new BlindSearch(
            launchPanel,
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
