package solvers.HeuristicSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import common.BaseNode;

@Getter
@Setter
public class Node extends BaseNode implements Comparable<Node> {

    private final int heuristic;
    private int cost;

    private final Node parent;
    private ArrayList<Node> children;

    public Node(Node parent, int state, int heuristic, int cost) {
        super(state);
        this.parent = parent;
        this.heuristic = heuristic;
        this.cost = cost;
    }

    public int getFitness() {
        return this.heuristic + this.cost;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children.stream()
            .filter(node -> Math.abs(node.getValue()) != Math.abs(this.getValue()))
            .collect(Collectors.toCollection(ArrayList<Node>::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Math.abs(this.getValue()) == Math.abs(node.getValue());
    }

    @Override
    public int compareTo(Node anotherNode) {
        return Integer.compare(this.getFitness(), anotherNode.getFitness());
    }

    @Override
    public String toString() {
        return "Node ("+ getValue() +"){" + "heuristic=" + heuristic + ", cost=" + cost + ", fitness="+ getFitness() +"}";
    }
}
