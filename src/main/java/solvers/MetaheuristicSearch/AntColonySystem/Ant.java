package solvers.MetaheuristicSearch.AntColonySystem;

class Ant implements Comparable<Ant> {

    private final Solution solution;
    private final int satisfiedClauses;

    Ant(Solution solution, int satisfiedClauses) {
        this.solution = solution;
        this.satisfiedClauses = satisfiedClauses;
    }

    public Solution getSolution() {
        return solution;
    }

    public int getSatisfiedClauses() {
        return satisfiedClauses;
    }

    @Override
    public int compareTo(Ant anotherAnt) {
        if (this.satisfiedClauses > anotherAnt.satisfiedClauses) return 1;
        if (this.satisfiedClauses < anotherAnt.satisfiedClauses) return -1;

        return 0;
    }

    @Override
    public String toString() {
        return "Ant{" +  "satisfiedClauses=" + satisfiedClauses + '}';
    }
}
