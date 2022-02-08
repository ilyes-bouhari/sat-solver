package solvers.BlindSearch;

import common.Solution;
import gui.LaunchPanel;
import common.ClausesSet;
import solvers.BaseSolver;
import tasks.DepthFirstSearchTask;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlindSearch extends BaseSolver {

    private Solution solution = null;
    private Solution bestSolution = null;
    private Node currentNode = null;

    public BlindSearch(LaunchPanel launchPanel, int executionTimeInSeconds, DepthFirstSearchTask task) {
        super(launchPanel, executionTimeInSeconds, task);
    }

    public Solution DepthFirstSearch() {

        int numberOfVariables = getClausesSet().getNumberOfVariables();
        solution = new Solution(numberOfVariables);
        bestSolution = new Solution(numberOfVariables);
        ArrayList<Node> treeBranch = IntStream.rangeClosed(1, numberOfVariables).mapToObj(Node::new).collect(Collectors.toCollection(ArrayList::new));

        startTimer();

        int level = 1;
        do {

            if (maxProcessingTimeIsReached()) break;

            int temp = level;

            currentNode = treeBranch.get(level-1);
            if ((level <= getClausesSet().getNumberOfVariables()) && currentNode.left == 0) {
                currentNode.setLeft();
                updateThenVerifySolution(level-1);
                ++temp;
            } else if ((level <= getClausesSet().getNumberOfVariables()) && currentNode.right == 0) {
                currentNode.setRight();
                updateThenVerifySolution(level-1);
                ++temp;
            } else {
                solution.changeLiteral(level-1, 0);
                --temp;
                currentNode.reset();
            }

            if (leafReached(getClausesSet(), temp)) {
                --temp;
            }

            updateUI(bestSolution);

            if (targetIsReached(bestSolution)) break;

            level = temp;
        } while (level > 0);

        return bestSolution;
    }

    public void updateThenVerifySolution(int index) {
        solution.changeLiteral(index, currentNode.getValue());
        if(solution.countSatisfiedClauses(getClausesSet(), null) > bestSolution.countSatisfiedClauses(getClausesSet(), null))
            bestSolution = new Solution(solution);
    }

    public boolean leafReached(ClausesSet clausesSet, int level) {
        return clausesSet.getNumberOfVariables() == (level-1);
    }
}
