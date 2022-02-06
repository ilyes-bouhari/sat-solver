package solvers.HeuristicSearch;

import java.util.*;

import common.Clause;
import common.ClausesSet;
import common.Solution;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import tasks.AStarTask;

public class HeuristicSearch {

    private final ClausesSet clausesSet;
    private final ClausesPanel clausesPanel;
    private final int executionTimeInSeconds;
    private final LaunchPanel launchPanel;
    private final SolutionPanel solutionPanel;
    private final AStarTask task;

    public HeuristicSearch(ClausesSet clausesSet, ClausesPanel clausesPanel, SolutionPanel solutionPanel, int executionTimeInSeconds, LaunchPanel launchPanel, AStarTask task) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.solutionPanel = solutionPanel;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.launchPanel = launchPanel;
        this.task = task;
    }

    public Solution AStar() {

        ArrayList<Node> nodes = generateNodes(clausesSet);
        nodes.sort(Collections.reverseOrder());

        ArrayList<Node> open = new ArrayList<>(nodes);
        ArrayList<Node> closed = new ArrayList<>();

        Solution solution = new Solution(clausesSet.getNumberOfVariables());
        Solution bestSolution = new Solution(solution);

        long startTime = System.currentTimeMillis();

        while (!open.isEmpty()) {

            if (((System.currentTimeMillis() - startTime)/1000) >= executionTimeInSeconds || (task != null && (task.isCancelled() || task.isDone())))
                break;

            Node currentNode = open.remove(0);
            closed.add(currentNode);

            solution.update(reconstructPath(currentNode));

            if(solution.countSatisfiedClauses(clausesSet, null) > bestSolution.countSatisfiedClauses(clausesSet, null))
                bestSolution = new Solution(solution);

            if (launchPanel != null) launchPanel.getSummaryPanel().updateSummary(clausesSet, bestSolution);
            if (solutionPanel != null) solutionPanel.setSolution(bestSolution);

            boolean response = bestSolution.isTargetReached(clausesSet, clausesPanel != null ? clausesPanel.getTableModel() : null);
            if (response) break;

            if (currentNode.getChildren().size() > 0) {

                for (Node node : currentNode.getChildren()) {

                    Node newNode = new Node(
                        currentNode,
                        node.getValue(),
                        node.getHeuristic(),
                        0
                    );

                    newNode.setCost(calculateCost(clausesSet, reconstructPath(newNode)));
                    newNode.setChildren(currentNode.getChildren());

                    open.add(newNode);
                }

                open.sort(Collections.reverseOrder());
            }
        }

        return bestSolution;
    }

    public ArrayList<Node> generateNodes(ClausesSet clausesSet) {

        int literal;
        ArrayList<Integer> variables = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<Node>();

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {
                literal = Math.abs(clause.getLiteral(j));

                if (!variables.contains(literal)) {
                    variables.add(literal);
                    nodes.add(
                        new Node(
                            null,
                            -literal,
                            calculateHeuristic(clausesSet, -literal),
                            calculateCost(clausesSet, new ArrayList<Integer>(Arrays.asList(-literal)))
                        )
                    );
                    nodes.add(
                        new Node(
                            null,
                            literal,
                            calculateHeuristic(clausesSet, literal),
                            calculateCost(clausesSet, new ArrayList<Integer>(Arrays.asList(literal)))
                        )
                    );
                }
            }
        }

        for (Node node : nodes) {
            node.setChildren(nodes);
        }

        return nodes;
    }

    public int calculateHeuristic(ClausesSet clausesSet, int n) {

        int counter = 0;
        int literal;

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            for (int j = 0; j < clausesSet.getClause(i).getNumberOfLiterals(); j++) {
                literal = clausesSet.getClause(i).getLiteral(j);

                if (Math.abs(literal) == Math.abs(n)) {
                    counter++;
                    break;
                }
            }
        }

        return counter;
    }

    public int calculateCost(ClausesSet clausesSet, ArrayList<Integer> path) {

        int counter = 0;
        int literal;

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {
            for (int j = 0; j < clausesSet.getClause(i).getNumberOfLiterals(); j++) {
                literal = clausesSet.getClause(i).getLiteral(j);

                if (path.contains(literal)) {
                    counter++;
                    break;
                }
            }
        }

        return counter;
    }

    public ArrayList<Integer> reconstructPath(Node current) {

        ArrayList<Integer> path = new ArrayList<>();

        while (current != null) {
            path.add(current.getValue());
            current = current.getParent();
        }

        Collections.reverse(path);

        return path;
    }
}
