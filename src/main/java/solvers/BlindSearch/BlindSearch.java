package solvers.BlindSearch;

import common.Solution;
import gui.LaunchPanel;
import gui.ClausesPanel;
import common.ClausesSet;
import gui.SolutionPanel;
import tasks.DepthFirstSearchTask;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlindSearch {

    private final ClausesSet clausesSet;
    private final ClausesPanel clausesPanel;
    private final SolutionPanel solutionPanel;
    private final int executionTimeInSeconds;
    private final LaunchPanel launchPanel;
    private final DepthFirstSearchTask task;
    private Solution solution = null;
    private Solution bestSolution = null;
    private Node currentNode = null;
    private ArrayList<Node> treeBranch;

    public BlindSearch(ClausesSet clausesSet, ClausesPanel clausesPanel, SolutionPanel solutionPanel, int executionTimeInSeconds, LaunchPanel launchPanel, DepthFirstSearchTask task) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.solutionPanel = solutionPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.launchPanel = launchPanel;
        this.task = task;
    }

    public Solution DepthFirstSearch() {

        int numberOfVariables = clausesSet.getNumberOfVariables();
        solution = new Solution(numberOfVariables);
        bestSolution = new Solution(numberOfVariables);
        treeBranch = new ArrayList<>(IntStream.rangeClosed(1, numberOfVariables).mapToObj(Node::new).collect(Collectors.toList()));

        long startTime = System.currentTimeMillis();
        int level = 1;
        do {

            if (((System.currentTimeMillis() - startTime)/1000) >= executionTimeInSeconds || (task != null && (task.isCancelled() || task.isDone())))
                break;
            
            int temp = level;

            currentNode = treeBranch.get(level-1);
            if ((level <= clausesSet.getNumberOfVariables()) && currentNode.left == 0) {
                currentNode.setLeft();
                updateThenVerifySolution(level-1);
                ++temp;
            } else if ((level <= clausesSet.getNumberOfVariables()) && currentNode.right == 0) {
                currentNode.setRight();
                updateThenVerifySolution(level-1);
                ++temp;
            } else {
                solution.changeLiteral(level-1, 0);
                --temp;
                currentNode.reset();
            }

            if (leafReached(clausesSet, temp)) {
                --temp;
            }

            if (launchPanel != null) launchPanel.getSummaryPanel().updateSummary(clausesSet, bestSolution);
            if (solutionPanel != null) solutionPanel.setSolution(bestSolution);

            boolean response = bestSolution.isSolution(clausesSet, clausesPanel != null ? clausesPanel.getTableModel() : null);
            if (response) break;

            level = temp;
        } while (level > 0 && level <= clausesSet.getNumberOfVariables());

        return bestSolution;
    }

    public void updateThenVerifySolution(int index) {
        solution.changeLiteral(index, currentNode.getValue());
        if(solution.satisfiedClauses(clausesSet, null) > bestSolution.satisfiedClauses(clausesSet, null))
            bestSolution = new Solution(solution);
    }

    public boolean leafReached(ClausesSet clausesSet, int level) {
        return clausesSet.getNumberOfVariables() == (level-1);
    }
}
