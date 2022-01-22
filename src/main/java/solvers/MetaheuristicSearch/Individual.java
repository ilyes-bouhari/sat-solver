package solvers.MetaheuristicSearch;

import common.ClausesSet;

public class Individual implements Comparable<Individual> {

    private Solution solution;
    private int fitness;

    public Individual(ClausesSet clausesSet) {
        this.solution = new Solution(clausesSet.getNumberOfVariables());
        this.solution.randomSolution();
        this.fitness = this.solution.satisfiedClauses(clausesSet, null);
    }

    public Individual(ClausesSet clausesSet, Solution solution) {
        this.solution = new Solution(solution);
        this.fitness = this.solution.satisfiedClauses(clausesSet, null);
    }

    public void mutate(ClausesSet clausesSet, int position) {
        this.solution.invertLiteral(position);
        this.fitness = this.solution.satisfiedClauses(clausesSet, null);
    }

    public Solution getSolution() {
        return solution;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Individual{" + "solution=" + solution + ", evaluation=" + fitness + '}';
    }

    @Override
    public int compareTo(Individual otherIndividual) {
        if (this.getFitness() > otherIndividual.getFitness()) return 1;
        if (this.getFitness() < otherIndividual.getFitness()) return -1;

        return 0;
    }
}