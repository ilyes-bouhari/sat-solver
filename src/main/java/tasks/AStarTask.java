package tasks;

import gui.LaunchPanel;
import solvers.HeuristicSearch.HeuristicSearch;

import javax.swing.*;

public class AStarTask extends SwingWorker<Object, Void> {

    private final LaunchPanel launchPanel;

    public AStarTask(LaunchPanel launchPanel) {
        this.launchPanel = launchPanel;
    }

    @Override
    protected Object doInBackground() {

        (new HeuristicSearch(
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
