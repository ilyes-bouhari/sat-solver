package gui;

import common.Solution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SolutionPanel extends JPanel {

    JTextArea solutionLabel;

    public SolutionPanel() {

        setUI();
    }

    public void setUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Solution");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        solutionLabel = new JTextArea("N/A");
        solutionLabel.setLineWrap(true);
        solutionLabel.setBackground(new Color(238, 238, 238));
        solutionLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(solutionLabel, gridBagConstraints);
    }

    public void setSolution(Solution solution) {
        solutionLabel.setText(solution.toString());
    }
}
