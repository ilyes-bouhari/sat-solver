package gui;

import common.ClausesSet;
import common.Solution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SummaryPanel extends JPanel {

    JLabel satisfiedClauses;
    JLabel nonSatisfiedClauses;
    JLabel executedIn;

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
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(satisfiedClauses, gridBagConstraints);

        nonSatisfiedClauses = new JLabel("N/A");
        border = BorderFactory.createTitledBorder("Non-Satisfied");
        nonSatisfiedClauses.setBorder(new CompoundBorder(border, margin));
        nonSatisfiedClauses.setForeground(Color.RED.darker());
        nonSatisfiedClauses.setFont(new Font("Sans Serif", Font.BOLD, 16));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(nonSatisfiedClauses, gridBagConstraints);

        executedIn = new JLabel("N/A");
        border = BorderFactory.createTitledBorder("Executed in");
        executedIn.setBorder(new CompoundBorder(border, margin));
        executedIn.setFont(new Font("Sans Serif", Font.BOLD, 16));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(executedIn, gridBagConstraints);
    }

    public void updateSummary(ClausesSet clausesSet, Solution solution, String executedTimeUntilNowAsString) {
        float count = solution.countSatisfiedClauses(clausesSet, null);
        satisfiedClauses.setText(getText(clausesSet, count));

        count = clausesSet.getNumberOfClause() - count;
        nonSatisfiedClauses.setText(getText(clausesSet, count));

        executedIn.setText(executedTimeUntilNowAsString + " seconds");
    }

    public String getText(ClausesSet clausesSet, float count) {
        return ((int) count + " clauses = " + String.format("%.2f", (float) (count*100/clausesSet.getNumberOfClause())) + "%");
    }
}
