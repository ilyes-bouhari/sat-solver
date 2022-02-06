package solvers.MetaheuristicSearch.AntColonySystem;

public class Literal implements Comparable<Literal> {

    private final int value;
    private final int heuristic;
    private double probability;

    public Literal(int value, int heuristic) {
        this.value = value;
        this.heuristic = heuristic;
    }

    public int getValue() {
        return value;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public double getProbability() {
        return probability;
    }

    public void calculateProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "Literal{" + "value=" + value + ", heuristic=" + heuristic + ", probability=" + probability + '}';
    }

    @Override
    public int compareTo(Literal anotherLiteral) {
        return Double.compare(this.getProbability(), anotherLiteral.getProbability());

    }
}
