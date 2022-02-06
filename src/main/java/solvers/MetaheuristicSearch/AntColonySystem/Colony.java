package solvers.MetaheuristicSearch.AntColonySystem;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import common.Clause;
import gui.ClausesPanel;
import common.ClausesSet;
import command.GenerateResults;
import solvers.HeuristicSearch.HeuristicSearch;

public class Colony {

    private final ClausesSet clausesSet;
    private final ClausesPanel clausesPanel;
    private final common.Solution baseSolution;

    private final double alpha;
    private final double beta;
    private final double pheromoneInit;
    private final double evaporationRate;
    private final int maxIterations;
    private final int numberOfAnts;
    private final double q0;
    private final int maxStep;

    private ArrayList<Literal> tempLiterals = new ArrayList<>();
    private HashMap<Integer, Literal> literals = new HashMap<>();
    private HashMap<Integer, Double> pheromone = new HashMap<>();

    Colony(
        ClausesSet clausesSet,
        ClausesPanel clausesPanel,
        common.Solution baseSolution,
        double alpha,
        double beta,
        int maxIterations,
        int numberOfAnts,
        double pheromoneInit,
        double evaporationRate,
        double q0,
        int maxStep
    ) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.baseSolution = baseSolution;
        this.alpha = alpha;
        this.beta = beta;
        this.maxIterations = maxIterations;
        this.numberOfAnts = numberOfAnts;
        this.pheromoneInit = pheromoneInit;
        this.evaporationRate = evaporationRate;
        this.q0 = q0;
        this.maxStep = maxStep;
    }

    public Solution run() {

        Solution solution;
        Solution bestSolution = new Solution(clausesSet.getNumberOfVariables());

        for (int i = 0; i < maxIterations; i++) {

            ArrayList<Ant> ants = new ArrayList<>();
            for (int j = 0; j < numberOfAnts; j++) {

                solution = constructSolution();
                solution.countSatisfiedClauses(clausesSet, null);
                solution = new Solution(improveSearch(solution));
                int evaluateSolution = solution.countSatisfiedClauses(clausesSet, null);

                onlineStepByStepPheromoneUpdate(solution);
                ants.add(new Ant(solution, evaluateSolution));
            }

            ants.sort(Collections.reverseOrder());
            offlineStepByStepPheromoneUpdate(ants.get(0).getSolution(), ants.get(0).getSatisfiedClauses());

            if(ants.get(0).getSatisfiedClauses() > bestSolution.countSatisfiedClauses(clausesSet, null))
                bestSolution = new Solution(ants.get(0).getSolution());


            boolean response = bestSolution.isTargetReached(clausesSet, clausesPanel != null ? clausesPanel.getTableModel() : null);
            if (response) break;
        }

        return bestSolution;
    }

    Solution constructSolution() {

        Literal literal;

        generateSetOfLiterals();
        Solution solution = new Solution(baseSolution);

        while (literals.size() > 0) {
            literal = nextLiteral();
            literals.remove(literal.getValue());
            literals.remove(-literal.getValue());
            solution.changeLiteral(Math.abs(literal.getValue())-1, literal.getValue());
        }

        return solution;
    }

    Solution improveSearch(Solution solution) {

        Solution tempSolution = new Solution(solution);
        for (int i = 0; i < maxStep; i++) {

            if (solution.getUnsatisfiedClauses().size() == 0) break;

            int randomClause = ThreadLocalRandom.current().nextInt(0, tempSolution.getUnsatisfiedClauses().size());
            int randomLiteral = ThreadLocalRandom.current().nextInt(0, tempSolution.getUnsatisfiedClauses().get(randomClause).getNumberOfLiterals());
            randomLiteral = solution.getUnsatisfiedClauses().get(randomClause).getLiteral(randomLiteral);
            tempSolution.invertLiteral(Math.abs(randomLiteral)-1);
            if (solution.countSatisfiedClauses(clausesSet, null) < tempSolution.countSatisfiedClauses(clausesSet, null)) {
                solution = new Solution(tempSolution);
                tempSolution = new Solution(solution);
            }
        }

        return solution;
    }

    void onlineStepByStepPheromoneUpdate(Solution solution) {
        updatePheromone(solution, evaporationRate * pheromoneInit);
    }

    void offlineStepByStepPheromoneUpdate(Solution solution, int gain) {
        updatePheromone(solution, evaporationRate * gain);
    }

    void updatePheromone(Solution solution, double v) {
        for (int i = 0; i < solution.getSize(); i++) {
            int literal = solution.getLiteral(i);
            pheromone.put(
                literal,
                ((1 - evaporationRate) * pheromone.get(literal)) + (v)
            );
        }
    }

    void generateSetOfLiterals() {

        int literal;
        ArrayList<Integer> variables = new ArrayList<>();

        for (int i = 0; i < baseSolution.getSize(); i++) {
            if (baseSolution.getLiteral(i) != 0) {
                variables.add(i+1);
            }
        }

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {
                literal = Math.abs(clause.getLiteral(j));

                if (!variables.contains(literal)) {
                    variables.add(literal);

                    literals.put(literal, new Literal(literal, calculateHeuristic(literal)));
                    literals.put(-literal, new Literal(-literal, calculateHeuristic(-literal)));
                }

                pheromone.put(literal, pheromoneInit);
                pheromone.put(-literal, pheromoneInit);
            }
        }
    }

    public int calculateHeuristic(int l) {

        int counter = 0;
        int literal;

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {
                literal = clause.getLiteral(j);

                if (literal == l) {
                    counter++;
                    break;
                }
            }
        }

        return counter;
    }

    Literal nextLiteral() {

        Literal literal;
        double sum = 0;
        double q = Math.random();

        loopThroughLiterals(sum);
        if (q < q0) {

            tempLiterals.sort(Collections.reverseOrder());
            literal = tempLiterals.get(0);

        } else {

            sum = tempLiterals.stream().mapToDouble(Literal::getProbability).sum();
            loopThroughLiterals(sum);
            literal = rouletteWheel();
        }

        return literal;
    }

    Literal rouletteWheel() {

        Literal literal = null;
        double sum = tempLiterals.stream().mapToDouble(Literal::getProbability).sum();
        double pick = ThreadLocalRandom.current().nextDouble(0, sum);

        double current = 0;
        for (Literal l : tempLiterals) {
            current += l.getProbability();
            if (current > pick) {
                literal = l;
                break;
            }
        }

        return literal;
    }

    void loopThroughLiterals(double sum) {
        Literal literal;

        tempLiterals.clear();
        for (Map.Entry<Integer, Literal> entry : literals.entrySet()) {
            literal = entry.getValue();

            if (sum == 0) {
                literal.calculateProbability(
                    Math.pow(getPheromone(literal.getValue()), alpha) *
                    Math.pow(literal.getHeuristic(), beta)
                );
            } else {
                literal.calculateProbability(literal.getProbability() / sum);
            }

            tempLiterals.add(literal);
        }
    }

    double getPheromone(int literal) {
        return pheromone.get(literal);
    }

    public static void main(String[] args) {

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream("/uf75-325/uf75-05.cnf"));

        common.Solution solution = (new HeuristicSearch(
            clausesPanel.getClausesSet(),
            null,
            null,
            1,
            null,
            null
        )).AStar();

        System.out.println("A* : " + solution.countSatisfiedClauses(clausesPanel.getClausesSet(), null));
        System.out.println("A* : " + solution);
        Colony colony = new Colony(clausesPanel.getClausesSet(), null, solution, .1, .1 , 2000, 10, .1,.3, .7, 60);

        solution = colony.run();
        System.out.println("ACS : " + solution);
        System.out.println("ACS : " + solution.countSatisfiedClauses(clausesPanel.getClausesSet(), null));
    }
}
