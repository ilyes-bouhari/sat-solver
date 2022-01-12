package solvers.BlindSearch;

import common.BaseNode;

public class Node extends BaseNode {
    int left;
    int right;

    public Node(int state) {
        super(state);
    }

    public void setLeft() {
        this.left = 1;
        this.setValue(this.getState());
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getLeft() {
        return this.left;
    }

    public void setRight() {
        this.right = -1;
        this.setValue(-this.getState());
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getRight() {
        return this.right;
    }

    public void reset() {
        this.setLeft(0);
        this.setRight(0);
        this.setValue(this.getState());
    }
}
