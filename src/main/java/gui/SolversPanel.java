package gui;

import common.ClausesSet;
import utils.CustomBasicComboBoxRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

public class SolversPanel extends JPanel {

    private LaunchPanel launchPanel;
    private JComboBox<String> solversComboBox;

    public SolversPanel () {
        setupUI();
        setupListeners();
    }

    public void setupUI() {
        // setLayout(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Solver");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        solversComboBox = new JComboBox<>();
        solversComboBox.setRenderer(new CustomBasicComboBoxRenderer("Select a SAT solver"));
        solversComboBox.setModel(
            new DefaultComboBoxModel<String>(
                new String[] {
                        "Depth-First Search (DFS)",
                        "Heuristic Search (A*)",
                }
            )
        );
        solversComboBox.setSelectedIndex(-1);

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        add(solversComboBox, gridBagConstraints);
    }

    public void setupListeners() {
        solversComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launchPanel.setSolverChosen(true);
                launchPanel.enableLaunchButton();
            }
        });
    }

    public JComboBox<String> getSolversComboBox() { return this.solversComboBox; }

    public void setLaunchPanel(LaunchPanel launchPanel) {
        this.launchPanel = launchPanel;
    }
}
