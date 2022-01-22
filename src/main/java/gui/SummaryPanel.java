package gui;

import common.ClausesSet;
import common.BaseSolution;
import solvers.MetaheuristicSearch.Solution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SummaryPanel extends JPanel {

    JLabel satisfiedClauses;
    JLabel nonSatisfiedClauses;

    public SummaryPanel() {

        setupUI();
    }

    void setupUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Summary");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        satisfiedClauses = new JLabel("N/A");
        border = BorderFactory.createTitledBorder("Satisfied");
        satisfiedClauses.setBorder(new CompoundBorder(border, margin));
        satisfiedClauses.setForeground(Color.GREEN.darker());
        satisfiedClauses.setFont(new Font("Sans Serif", Font.BOLD, 16));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(satisfiedClauses, gridBagConstraints);

        nonSatisfiedClauses = new JLabel("N/A");
        border = BorderFactory.createTitledBorder("Non-Satisfied");
        nonSatisfiedClauses.setBorder(new CompoundBorder(border, margin));
        nonSatisfiedClauses.setForeground(Color.RED.darker());
        nonSatisfiedClauses.setFont(new Font("Sans Serif", Font.BOLD, 16));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(nonSatisfiedClauses, gridBagConstraints);
    }

    public void updateSummary(ClausesSet clausesSet, Solution solution) {
        float count = solution.satisfiedClauses(clausesSet, null);
        satisfiedClauses.setText((int) count + " clauses = " + Math.round(count*100/clausesSet.getNumberOfClause()) + "%");

        count = clausesSet.getNumberOfClause() - count;
        nonSatisfiedClauses.setText((int) count + " clauses = " + Math.round(count*100/clausesSet.getNumberOfClause()) + "%");
    }

    public JLabel getSatisfiedClauses() {
        return satisfiedClauses;
    }

    public void setSatisfiedClauses(JLabel satisfiedClauses) {
        this.satisfiedClauses = satisfiedClauses;
    }

    public JLabel getNonSatisfiedClauses() {
        return nonSatisfiedClauses;
    }

    public void setNonSatisfiedClauses(JLabel nonSatisfiedClauses) {
        this.nonSatisfiedClauses = nonSatisfiedClauses;
    }
}
