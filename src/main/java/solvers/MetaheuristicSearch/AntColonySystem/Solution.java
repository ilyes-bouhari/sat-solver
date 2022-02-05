package solvers.MetaheuristicSearch.AntColonySystem;

import common.Clause;
import common.ClausesSet;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Solution extends common.Solution {

    private ArrayList<Clause> unsatisfiedClauses = new ArrayList<>();

    public Solution(int size) {
        super(size);
    }

    public Solution(Solution solution) {
        super(solution);
        this.unsatisfiedClauses = solution.getUnsatisfiedClauses();
    }

    public ArrayList<Clause> getUnsatisfiedClauses() {
        return unsatisfiedClauses;
    }

    @Override
    public int satisfiedClauses(ClausesSet clausesSet, DefaultTableModel tableModel) {

        int count = 0;
        int literal = 0;

        unsatisfiedClauses.clear();

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {
                literal = clause.getLiteral(j);

                if (literal == getLiteral(Math.abs(literal) - 1)) {
                    if (tableModel != null) tableModel.setValueAt(1, i, 0);
                    count++;
                    break;
                }

                if ((j == clause.getNumberOfLiterals()-1) && tableModel != null) {
                    tableModel.setValueAt(0, i, 0);
                }

                if ((j == clause.getNumberOfLiterals()-1)) unsatisfiedClauses.add(clause);
            }
        }

        return count;
    }
}
