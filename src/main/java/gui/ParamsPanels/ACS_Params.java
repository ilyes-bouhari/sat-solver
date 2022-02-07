package gui.ParamsPanels;

import lombok.Getter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@Getter
public class ACS_Params extends JPanel {

    private JSpinner alphaSpinner;
    private JSpinner betaSpinner;
    private JSpinner maxIterationsSpinner;
    private JSpinner numberOfAntsSpinner;
    private JSpinner pheromoneInitSpinner;
    private JSpinner evaporationRateSpinner;
    private JSpinner q0Spinner;
    private JSpinner maxStepSpinner;

    public ACS_Params() {
        setupUI();
    }

    void setupUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Params");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JLabel alphaLabel = new JLabel("Alpha : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(alphaLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        alphaSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 1, 0.1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(alphaSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel betaLabel = new JLabel("Beta : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(betaLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        betaSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 1, 0.1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(betaSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel maxIterationsLabel = new JLabel("Iterations : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(maxIterationsLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        maxIterationsSpinner = new JSpinner(new SpinnerNumberModel(200, 1, null, 1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(maxIterationsSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel numberOfAntsLabel = new JLabel("Ants : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(numberOfAntsLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        numberOfAntsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, null, 1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(numberOfAntsSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel pheromoneInitLabel = new JLabel("Pheromone initial : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(pheromoneInitLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        pheromoneInitSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.01, 1, 0.01));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(pheromoneInitSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel evaporationRateLabel = new JLabel("Evaporation rate : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(evaporationRateLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        evaporationRateSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0.1, 1, 0.1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(evaporationRateSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel q0Label = new JLabel("q0 : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(q0Label, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        q0Spinner = new JSpinner(new SpinnerNumberModel(0.7, 0, 1, 0.1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(q0Spinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel maxStepLabel = new JLabel("Max step : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(maxStepLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        maxStepSpinner = new JSpinner(new SpinnerNumberModel(60, 0, 100, 1));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(maxStepSpinner, gridBagConstraints);
    }
}
