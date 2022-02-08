package gui.ParamsPanels;

import utils.ComboBoxItem;
import enums.StoppingCriteria;
import utils.ItemComboBoxRenderer;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import javax.swing.border.Border;
import java.util.stream.IntStream;
import javax.swing.event.ChangeEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeListener;

@Getter
public class GA_Params extends JPanel {

    private JSpinner populationSizeSpinner;
    private JSpinner maxIterationSpinner;
    private JComboBox crossoverRateComboBox;
    private JComboBox mutationRateComboBox;
    private ButtonGroup stoppingCriteriaButtonGroup;
    private JRadioButton executionTimeRadioButton;
    private JRadioButton maxFitnessRadioButton;
    private JRadioButton maxGenerationsRadioButton;

    public GA_Params() {
        setupUI();
        setupListeners();
    }

    public void setupUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Params");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JLabel populationSizeLabel = new JLabel("Population size : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(populationSizeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        populationSizeSpinner = new JSpinner();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(populationSizeSpinner, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel maxIterationLabel = new JLabel("Max iteration : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(maxIterationLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        maxIterationSpinner = new JSpinner();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        add(maxIterationSpinner, gridBagConstraints);

        ComboBoxItem[] percentages = IntStream.rangeClosed(1, 100).mapToObj(percentage -> {
            return new ComboBoxItem(String.valueOf(percentage), String.valueOf(percentage) + " %");
        }).toArray(ComboBoxItem[]::new);

        gridBagConstraints = new GridBagConstraints();
        JLabel crossoverRateLabel = new JLabel("Crossover rate : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(crossoverRateLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        crossoverRateComboBox = new JComboBox(percentages);
        crossoverRateComboBox.setRenderer(new ItemComboBoxRenderer(""));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        crossoverRateComboBox.setSelectedIndex(-1);
        add(crossoverRateComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel mutationRateLabel = new JLabel("Mutation rate : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        add(mutationRateLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        mutationRateComboBox = new JComboBox(percentages);
        mutationRateComboBox.setRenderer(new ItemComboBoxRenderer(""));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        mutationRateComboBox.setSelectedIndex(-1);
        add(mutationRateComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JPanel stoppingCriteriaPanel = new JPanel();
        stoppingCriteriaPanel.setLayout(new GridBagLayout());
        border = BorderFactory.createTitledBorder("Stopping Criteria");
        margin = new EmptyBorder(8, 8, 8, 8);
        stoppingCriteriaPanel.setBorder(new CompoundBorder(border, margin));

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(10, 0, 0, 0);
        add(stoppingCriteriaPanel, gridBagConstraints);

        stoppingCriteriaButtonGroup = new ButtonGroup();

        gridBagConstraints = new GridBagConstraints();
        executionTimeRadioButton  = new JRadioButton("Execution Time");
        executionTimeRadioButton.setActionCommand(String.valueOf(StoppingCriteria.EXECUTION_TIME));
        stoppingCriteriaButtonGroup.add(executionTimeRadioButton);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        stoppingCriteriaPanel.add(executionTimeRadioButton, gridBagConstraints);
        executionTimeRadioButton.setSelected(true);

        gridBagConstraints = new GridBagConstraints();
        maxFitnessRadioButton  = new JRadioButton("Max fitness");
        maxFitnessRadioButton.setActionCommand(String.valueOf(StoppingCriteria.MAX_FITNESS));
        stoppingCriteriaButtonGroup.add(maxFitnessRadioButton);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        stoppingCriteriaPanel.add(maxFitnessRadioButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        maxGenerationsRadioButton  = new JRadioButton("Max generations (iterations)");
        maxGenerationsRadioButton.setActionCommand(String.valueOf(StoppingCriteria.MAX_GENERATION));
        stoppingCriteriaButtonGroup.add(maxGenerationsRadioButton);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        stoppingCriteriaPanel.add(maxGenerationsRadioButton, gridBagConstraints);
    }

    public void setupListeners() {
        populationSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                spinner.setValue((Integer) spinner.getValue() > 1 ? spinner.getValue() : 2);
            }
        });

        maxIterationSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner spinner = (JSpinner) e.getSource();
                spinner.setValue((Integer) spinner.getValue() > 0 ? spinner.getValue() : 1);
            }
        });
    }

    public void resetParams() {
        populationSizeSpinner.setValue(50);
        maxIterationSpinner.setValue(100);
        crossoverRateComboBox.setSelectedIndex(49);
        mutationRateComboBox.setSelectedIndex(4);
    }
}
