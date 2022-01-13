package solvers.BlindSearch;

import common.BaseSolution;
import gui.LaunchPanel;
import gui.ClausesPanel;
import common.ClausesSet;
import gui.SolutionPanel;
import tasks.DepthFirstSearchTask;

public abstract class BlindSearch {

    public static boolean isLeftLeaf(Node node, boolean leafIsReached) {
        return node.getValue() > 0 && leafIsReached;
    }

    public static boolean isRightLeaf(Node node, boolean leafIsReached) {
        return node.getValue() < 0 && leafIsReached;
    }

    public static BaseSolution DepthFirstSearch(ClausesSet clausesSet, ClausesPanel clausesPanel, SolutionPanel solutionPanel, int executionTimeInSeconds, LaunchPanel launchPanel, DepthFirstSearchTask task) {

        BaseSolution solution = new BaseSolution(clausesSet.getNumberOfVariables());
        BaseSolution bestSolution = new BaseSolution(solution);

        long startTime = System.currentTimeMillis();

        int counter = 1;
        do {

            if (((System.currentTimeMillis() - startTime)/1000) >= executionTimeInSeconds || (task != null && (task.isCancelled() || task.isDone())))
                break;

            int temp = counter;

            if (isLeftLeaf((Node) solution.getLiteral(counter-1), counter == clausesSet.getNumberOfVariables())) {
                temp--;
            } else if (isRightLeaf((Node) solution.getLiteral(counter-1), counter == clausesSet.getNumberOfVariables())) {
                temp--;
            }

            Node node = (Node) solution.getLiteral(counter-1);
            if ((counter < clausesSet.getNumberOfVariables()) && node.getLeft() == 0) {
                node.setLeft();
                temp++;
            } else if ((counter <= clausesSet.getNumberOfVariables()) && node.getRight() == 0) {
                node.setRight();
                temp++;
            } else {
                temp = counter - 1;
                node.reset();
            }

            if(solution.satisfiedClausesCount(clausesSet, null) > bestSolution.satisfiedClausesCount(clausesSet, null))
                bestSolution = new BaseSolution(solution);

            if (launchPanel != null) launchPanel.getSummaryPanel().updateSummary(clausesSet, bestSolution);
            // if (solutionPanel != null) solutionPanel.setSolution(bestSolution);

            boolean response = bestSolution.isSolution(clausesSet, clausesPanel != null ? clausesPanel.getTableModel() : null);
            if (response) break;

            counter = temp;

        } while (counter > 0 && counter <= clausesSet.getNumberOfVariables());

        return bestSolution;
    }
}
