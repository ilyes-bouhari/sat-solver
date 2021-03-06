package gui;

import enums.Solvers;
import gui.ParamsPanels.ACS_Params;
import gui.ParamsPanels.GA_Params;
import lombok.Getter;
import utils.ComboBoxItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

@Getter
public class SolversPanel extends JPanel {

    private LaunchPanel launchPanel;
    private GA_Params gaParamsPanel;
    private ACS_Params acsParamsPanel;
    private JComboBox solversComboBox;

    public SolversPanel() {
        setupUI();
        setupListeners();
    }

    public void setupUI() {
        setLayout(new GridBagLayout());

        Border border = BorderFactory.createTitledBorder("Solver");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        ComboBoxItem[] solvers = new ComboBoxItem[] {
            new ComboBoxItem(String.valueOf(Solvers.DFS), "Depth-First Search (DFS)"),
            new ComboBoxItem(String.valueOf(Solvers.AStar), "A Star (A*)"),
            new ComboBoxItem(String.valueOf(Solvers.GA), "Genetic Algorithm (GA)"),
            new ComboBoxItem(String.valueOf(Solvers.ACS), "Ant Colony System (ACS)"),
        };

        solversComboBox = new JComboBox(solvers);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        gridBagConstraints.ipady = 0;
        add(solversComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gaParamsPanel = new GA_Params();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        add(gaParamsPanel, gridBagConstraints);
        gaParamsPanel.setVisible(false);

        acsParamsPanel = new ACS_Params();
        add(acsParamsPanel, gridBagConstraints);
        acsParamsPanel.setVisible(false);
    }

    public void setupListeners() {
        solversComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Solvers solver = Solvers.valueOf(((ComboBoxItem) Objects.requireNonNull(solversComboBox.getSelectedItem())).getId());

                launchPanel.setSolverChosen(true);
                launchPanel.enableLaunchButton();

                hideAllParamsPanels();
                resetParams();

                switch (solver) {
                    case GA: gaParamsPanel.setVisible(true); break;
                    case ACS: acsParamsPanel.setVisible(true); break;
                }
            }
        });
    }

    public void hideAllParamsPanels() {
        gaParamsPanel.setVisible(false);
        acsParamsPanel.setVisible(false);
    }

    public void resetParams() {
        gaParamsPanel.resetParams();
    }

    public JComboBox getSolversComboBox() { return this.solversComboBox; }

    public GA_Params getGaParamsPanel() {
        return gaParamsPanel;
    }

    public void setLaunchPanel(LaunchPanel launchPanel) {
        this.launchPanel = launchPanel;
    }
}
