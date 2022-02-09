package solvers.HeuristicSearch;

import java.util.*;

import common.Clause;
import common.Solution;
import gui.LaunchPanel;
import tasks.AStarTask;
import common.ClausesSet;
import solvers.BaseSolver;

public class HeuristicSearch extends BaseSolver {

    public HeuristicSearch(LaunchPanel launchPanel, AStarTask task) {
        super(launchPanel, task);
    }

    public Solution AStar() {

        ArrayList<Node> nodes = generateNodes(getClausesSet());
        nodes.sort(Collections.reverseOrder());

        ArrayList<Node> open = new ArrayList<>(nodes);
        ArrayList<Node> closed = new ArrayList<>();

        Solution solution = new Solution(getClausesSet().getNumberOfVariables());
        Solution bestSolution = new Solution(solution);

        startTimer();

        while (!open.isEmpty()) {

            if (maxProcessingTimeIsReached()) break;

            Node currentNode = open.remove(0);
            closed.add(currentNode);

            solution.update(reconstructPath(currentNode));

            if(solution.countSatisfiedClauses(getClausesSet(), null) > bestSolution.countSatisfiedClauses(getClausesSet(), null))
                bestSolution = new Solution(solution);

            updateUI(bestSolution);

            if (targetIsReached(bestSolution)) break;

            if (currentNode.getChildren().size() > 0) {

                for (Node node : currentNode.getChildren()) {

                    Node newNode = new Node(
                        currentNode,
                        node.getValue(),
                        node.getHeuristic(),
                        0
                    );

                    newNode.setCost(calculateCost(getClausesSet(), reconstructPath(newNode)));
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
