package common;

import solvers.BlindSearch.Node;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseSolution {
    public ArrayList<BaseNode> solution = new ArrayList<>();

    public BaseSolution(int size) {
        for(int i = 1; i <= size; i++) {
            this.solution.add(new Node(i));
        }
    }

    public BaseSolution(BaseSolution solution) {
        for(int i = 0; i < solution.getSolutionSize(); i++)
            this.solution.add(new Node(solution.getLiteral(i).getValue()));
    }

    public BaseNode getLiteral(int position) {
        return this.solution.get(position);
    }
    public BaseNode setLiteral(int position, Node value) {
        return this.solution.set(position, value);
    }

    public int getSolutionSize() {
        return this.solution.size();
    }

    public int satisfiedClausesCount(ClausesSet clausesSet, DefaultTableModel tableModel) {
        return (int) Arrays.stream(satisfiedClauses(clausesSet, tableModel)).filter(clauses -> {
            if (clauses != true) return false;
            return true;
        }).count();
    }

    public Boolean[] satisfiedClauses(ClausesSet clausesSet, DefaultTableModel tableModel) {

        int literal;
        Boolean[] satisfiedClauses = new Boolean[clausesSet.getNumberOfClause()];

        for (int i = 0; i < clausesSet.getNumberOfClause(); i++) {

            Clause clause = clausesSet.getClause(i);
            for (int j = 0; j < clause.getNumberOfLiterals(); j++) {

                literal = clause.getLiteral(j);
                satisfiedClauses[i] = false;

                if(literal == this.getLiteral(Math.abs(literal) - 1).getValue()) {
                    if (tableModel != null) tableModel.setValueAt(1, i, 0);
                    satisfiedClauses[i] = true;
                    break;
                }

                if ((j == clause.getNumberOfLiterals()-1) && tableModel != null) tableModel.setValueAt(0, i, 0);
            }
        }

        // TODO: to be removed used for testing
        /*if (tableModel != null) {
            for (int i = 0; i < satisfiedClauses.length; i++) {
                if (satisfiedClauses[i] == false)
                    System.out.println("Clause i = " + (i+1) + " : " + satisfiedClauses[i]);
            }

            System.out.println("Non Satisfied Clauses : " + Arrays.stream(satisfiedClauses).filter(c -> c == false).count());
        }*/

        return satisfiedClauses;
    }

    public boolean isSolution(ClausesSet clausesSet, DefaultTableModel tableModel) {
        return clausesSet.getNumberOfClause() == satisfiedClausesCount(clausesSet, tableModel);
    }

    @Override
    public String toString() {
        return this.solution.toString();
    }
}
