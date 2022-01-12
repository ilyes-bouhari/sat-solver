package common;

public class BaseNode {

    int state;
    int value;

    public BaseNode(int state) {
        this.state = state;
        this.setValue(state);
    }

    public int getState() {
        return state;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
