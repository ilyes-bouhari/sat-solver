package solvers.MetaheuristicSearch;

import common.Clause;
import common.ClausesSet;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Solution {
    public ArrayList<Integer> solution = new ArrayList<>();

    public Solution(int size) {
        for(int i = 1; i <= size; i++) {
            this.solution.add(0);
        }
    }

    public Solution(Solution solution) {
        for(int i = 0; i < solution.getSolutionSize(); i++)
            this.solution.add(solution.getLiteral(i));
    }

    public int getLiteral(int position) {
        return this.solution.get(position);
    }

    public int getSolutionSize() {
        return this.solution.size();
    }

    public void randomSolution() {
        int random;

        for(int i = 0; i < this.getSolutionSize(); i++) {
            random = (int) (Math.random() * 10) % 2;

            this.solution.set(i, ((i+1) * (random == 0 ? -1 : 1)));
        }
    }

    public void changeLiteral(int position, int value) {
        this.solution.set(position, value);
    }

    public void invertLiteral(int position) {
        this.solution.set(position, -this.solution.get(position));
    }

    public int satisfiedClauses(ClausesSet clausesSet, DefaultTableModel tableModel) {

        int count = 0;
        int literal = 0;

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {
                literal = clause.getLiteral(j);

                if (literal == getLiteral(Math.abs(literal) - 1)) {
                    if (tableModel != null) tableModel.setValueAt(1, i, 0);
                    count++;
                    break;
                }

                if ((j == clause.getNumberOfLiterals()-1) && tableModel != null) tableModel.setValueAt(0, i, 0);
            }
        }

        return count;
    }

    public boolean isSolution(ClausesSet clausesSet, DefaultTableModel tableModel) {
        return clausesSet.getNumberOfClause() == satisfiedClauses(clausesSet, tableModel);
    }

    @Override
    public boolean equals(Object o) {

        Solution otherSolution = (Solution) o;

        if (getSolutionSize() != otherSolution.getSolutionSize()) return false;

        for (int i = 0; i < getSolutionSize(); i++) {
            if (getLiteral(i) != otherSolution.getLiteral(i)) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return this.solution.toString();
    }
}
