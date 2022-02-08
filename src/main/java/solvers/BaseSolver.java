package solvers;

import common.ClausesSet;
import common.Solution;
import gui.LaunchPanel;
import lombok.Getter;

import javax.swing.*;
import java.time.Duration;
import java.time.Instant;

@Getter
public class BaseSolver {

    private final LaunchPanel launchPanel;
    private final ClausesSet clausesSet;
    private final SwingWorker<Object, Void> task;

    private Instant start;

    public BaseSolver(LaunchPanel launchPanel, int executionTimeInSeconds, SwingWorker<Object, Void> task) {
        this.launchPanel = launchPanel;
        this.task = task;

        this.clausesSet = launchPanel.getClausesPanel().getClausesSet();
        launchPanel.setExecutionTimeInSeconds(executionTimeInSeconds);
    }

    public void startTimer() {
        this.start = Instant.now();
    }

    public boolean maxProcessingTimeIsReached() {
        return getExecutedTimeUntilNow() >= getLaunchPanel().getExecutionTimeInSeconds() ||
            (getTask() != null && (getTask().isCancelled() || getTask().isDone()));
    }

    public boolean targetIsReached(Solution solution) {
        return solution.isTargetReached(getClausesSet(), getTask() != null ? getLaunchPanel().getClausesPanel().getTableModel() : null);
    }

    public void updateUI(Solution bestSolution) {
        if (task != null) launchPanel.getSummaryPanel().updateSummary(launchPanel.getClausesPanel().getClausesSet(), bestSolution, getExecutedTimeUntilNowAsString());
        if (task != null) launchPanel.getSolutionPanel().setSolution(bestSolution);
    }

    public float getExecutedTimeUntilNow() {
        return (float) Duration.between(start, Instant.now()).toMillis() / 1000;
    }

    public String getExecutedTimeUntilNowAsString() {
        return String.valueOf(getExecutedTimeUntilNow());
    }
}
