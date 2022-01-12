package common;

import java.util.ArrayList;

public class Clause {
    private ArrayList<Integer> literals = new ArrayList<>();

    public int getLiteral(int position){
        return this.literals.get(position);
    }

    public void addLiteral(int value) {
        this.literals.add(value);
    }

    public int getNumberOfLiterals() {
        return this.literals.size();
    }

    @Override
    public String toString() {
        return "Clause [literals=" + literals + "]";
    }
}
