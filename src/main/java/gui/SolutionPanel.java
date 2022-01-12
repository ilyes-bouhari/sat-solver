package gui;

import common.BaseSolution;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.multi.MultiLabelUI;
import javax.swing.text.View;
import java.awt.*;

public class SolutionPanel extends JPanel {

    JLabel solutionLabel;

    public SolutionPanel() {

        setUI();
    }

    public void setUI() {
        /*setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Solution");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        solutionLabel = new JLabel("N/A");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(solutionLabel, gridBagConstraints);*/
    }

    public void setSolution(BaseSolution solution) {
        /*solutionLabel.setText(solution.toString());*/
    }
}
