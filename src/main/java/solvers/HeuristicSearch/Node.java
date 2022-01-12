package solvers.HeuristicSearch;

import common.BaseNode;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Node extends BaseNode implements Comparable<Node>{

    private int heuristic;
    private int cost;

    private Node parent;
    private ArrayList<Node> children;

    public Node(Node parent, int state, int heuristic, int cost) {
        super(state);
        this.parent = parent;
        this.heuristic = heuristic;
        this.cost = cost;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
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
    public int compareTo(Node anotherNode) {
        if (this.getFitness() > anotherNode.getFitness()) return 1;
        if (this.getFitness() < anotherNode.getFitness()) return -1;

        return 0;
    }

    @Override
    public String toString() {
        return "Node ("+ getValue() +"){" + "heuristic=" + heuristic + ", cost=" + cost + ", fitness="+ getFitness() +"}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Math.abs(this.getValue()) == Math.abs(node.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
