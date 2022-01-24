package gui;

import java.awt.*;
import javax.swing.*;
import common.ClausesSet;
import enums.Solvers;
import enums.StoppingCriteria;
import tasks.AStarTask;
import tasks.DepthFirstSearchTask;
import tasks.GeneticAlgorithmTask;
import utils.ComboBoxItem;
import utils.ItemComboBoxRenderer;

import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.util.stream.IntStream;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

public class LaunchPanel extends JPanel {

    private ClausesPanel clausesPanel;
    private SolversPanel solversPanel;
    private SummaryPanel summaryPanel;
    private SolutionPanel solutionPanel;

    private JButton launchButton;
    private JComboBox executionTimeComboBox;
    private SwingWorker task;

    private ClausesSet clausesSet;
    private int executionTimeInSeconds;

    private boolean isRunning = false;
    private boolean clausesLoaded = false;
    private boolean solverChosen = false;
    private boolean executionTimeIsSet = false;

    public LaunchPanel() {

        setupUI();
        setupListeners();
    }

    public void setupUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Launch");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // Execution Time Label
        JLabel executionTimeLabel = new JLabel("Execution Time :");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1;
        add(executionTimeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        ComboBoxItem[] range = IntStream.iterate(0, i -> i + 60).limit(61).boxed()
            .filter(time -> time != 0).map(time -> {
                return new ComboBoxItem(String.valueOf(time), String.valueOf(time/60) + " min");
            }).toArray(ComboBoxItem[]::new);

        executionTimeComboBox = new JComboBox(range);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        add(executionTimeComboBox, gridBagConstraints);

        // Launch button
        gridBagConstraints = new GridBagConstraints();
        launchButton = new JButton("Launch");
        launchButton.setEnabled(false);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(launchButton, gridBagConstraints);
    }

    public void setupListeners() {

        executionTimeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExecutionTimeIsSet(true);
                enableLaunchButton();
            }
        });

        LaunchPanel launchPanel = this;
        launchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!isRunning) {

                    toggleOnRunning(true);

                    Solvers solver = Solvers.valueOf(((ComboBoxItem) solversPanel.getSolversComboBox().getSelectedItem()).getId());
                    executionTimeInSeconds = Integer.parseInt(((ComboBoxItem) executionTimeComboBox.getSelectedItem()).getId());
                    clausesSet = clausesPanel.getClausesSet();

                    switch (solver) {
                        case DFS -> {
                            task = new DepthFirstSearchTask(clausesSet, clausesPanel, solutionPanel, executionTimeInSeconds, launchPanel);
                            task.execute();
                        }
                        case AStar -> {
                            task = new AStarTask(clausesSet, clausesPanel, solutionPanel, executionTimeInSeconds, launchPanel);
                            task.execute();
                        }
                        case GA -> {

                            int populationSize = (int) solversPanel.getGaParamsPanel().getPopulationSizeSpinner().getValue();
                            int maxIteration = (int) solversPanel.getGaParamsPanel().getMaxIterationSpinner().getValue();

                            int crossoverRate = Integer.parseInt(((ComboBoxItem) solversPanel.getGaParamsPanel().getCrossoverRateComboBox().getSelectedItem()).getId());
                            int mutationRate = Integer.parseInt(((ComboBoxItem) solversPanel.getGaParamsPanel().getMutationRateComboBox().getSelectedItem()).getId());

                            StoppingCriteria stoppingCriteria = StoppingCriteria.valueOf(solversPanel.getGaParamsPanel().getStoppingCriteriaButtonGroup().getSelection().getActionCommand());

                            task = new GeneticAlgorithmTask(
                                clausesSet,
                                clausesPanel,
                                solutionPanel,
                                launchPanel,

                                populationSize,
                                maxIteration,
                                crossoverRate,
                                mutationRate,
                                stoppingCriteria,
                                executionTimeInSeconds
                            );
                            task.execute();
                        }
                    }
                } else {

                    launchButton.setEnabled(false);
                    if (task != null && !task.isDone())
                        while(!task.isCancelled())
                            task.cancel(true);
                    launchButton.setEnabled(true);

                    toggleOnRunning(false);
                }
            }
        });
    }

    public void enableLaunchButton() {
        if (clausesLoaded) launchButton.setEnabled(true);
        else launchButton.setEnabled(false);
    }

    public void toggleOnRunning(boolean isRunning) {
        this.isRunning = isRunning;
        clausesPanel.getBenchmarkTypeComboBox().setEnabled(!isRunning);
        clausesPanel.getBenchmarkInstanceComboBox().setEnabled(!isRunning);
        clausesPanel.getLoadClausesButton().setEnabled(!isRunning);
        executionTimeComboBox.setEnabled(!isRunning);
        solversPanel.getSolversComboBox().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getPopulationSizeSpinner().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getMaxIterationSpinner().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getCrossoverRateComboBox().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getMutationRateComboBox().setEnabled(!isRunning);
        launchButton.setText(isRunning ? "STOP ( Running... )" : "Launch");
    }

    public void setClausesLoaded(boolean clausesLoaded) {
        this.clausesLoaded = clausesLoaded;
    }

    public void setSolverChosen(boolean solverChosen) {
        this.solverChosen = solverChosen;
    }

    public void setExecutionTimeIsSet(boolean executionTimeIsSet) {
        this.executionTimeIsSet = executionTimeIsSet;
    }

    public void setClausesPanel(ClausesPanel clausesPanel) {
        this.clausesPanel = clausesPanel;
    }

    public void setSolversPanel(SolversPanel solversPanel) {
        this.solversPanel = solversPanel;
    }

    public SummaryPanel getSummaryPanel() {
        return summaryPanel;
    }

    public void setSummaryPanel(SummaryPanel summaryPanel) {
        this.summaryPanel = summaryPanel;
    }

    public void setSolutionPanel(SolutionPanel solutionPanel) {
        this.solutionPanel = solutionPanel;
    }
}
