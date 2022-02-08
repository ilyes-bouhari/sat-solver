package gui;

import java.awt.*;
import javax.swing.*;
import common.ClausesSet;
import enums.Solvers;
import enums.StoppingCriteria;
import gui.ParamsPanels.ACS_Params;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import tasks.ACSTask;
import tasks.AStarTask;
import tasks.DepthFirstSearchTask;
import tasks.GeneticAlgorithmTask;
import utils.ComboBoxItem;

import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.stream.IntStream;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

@Getter
@Setter
public class LaunchPanel extends JPanel {

    private ClausesPanel clausesPanel;
    private SolversPanel solversPanel;
    private SummaryPanel summaryPanel;
    private SolutionPanel solutionPanel;

    @Setter(AccessLevel.NONE)
    private JButton launchButton;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private JComboBox executionTimeComboBox;

    @Setter(AccessLevel.NONE)
    private SwingWorker<Object, Void> task;
    private int executionTimeInSeconds;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean isRunning = false;

    @Getter(AccessLevel.NONE)
    private boolean clausesLoaded = false;

    @Getter(AccessLevel.NONE)
    private boolean solverChosen = false;

    @Getter(AccessLevel.NONE)
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
            .filter(time -> time != 0).map(time -> new ComboBoxItem(String.valueOf(time), time / 60 + " min")).toArray(ComboBoxItem[]::new);

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

                    Solvers solver = getChosenSolver();
                    setExecutionTimeInSeconds(getChosenExecutionTime());
                    ClausesSet clausesSet = clausesPanel.getClausesSet();

                    switch (solver) {
                        case DFS: {

                            (task = new DepthFirstSearchTask(
                                launchPanel,
                                executionTimeInSeconds
                            )).execute();
                            break;
                        }
                        case AStar: {

                            (task = new AStarTask(
                                launchPanel,
                                executionTimeInSeconds
                            )).execute();
                            break;
                        }
                        case GA: {

                            int populationSize = getChosenPopulationSize();
                            int maxIteration = getChosenMaxIterations();
                            int crossoverRate = getChosenCrossoverRate();
                            int mutationRate = getChosenMutationRate();
                            StoppingCriteria stoppingCriteria = getChosenStoppingCriteria();

                            (task = new GeneticAlgorithmTask(
                                launchPanel,
                                populationSize,
                                maxIteration,
                                crossoverRate,
                                mutationRate,
                                stoppingCriteria,
                                executionTimeInSeconds
                            )).execute();
                            break;
                        }
                        case ACS: {

                            ACS_Params acs_params = solversPanel.getAcsParamsPanel();
                            double alpha = (double) acs_params.getAlphaSpinner().getValue();
                            double beta = (double) acs_params.getBetaSpinner().getValue();
                            int maxIterations = (int) acs_params.getMaxIterationsSpinner().getValue();
                            int numberOfAnts = (int) acs_params.getNumberOfAntsSpinner().getValue();
                            double pheromoneInit = (double) acs_params.getPheromoneInitSpinner().getValue();
                            double evaporationRate = (double) acs_params.getEvaporationRateSpinner().getValue();
                            double q0 = (double) acs_params.getQ0Spinner().getValue();
                            int maxStep = (int) acs_params.getMaxStepSpinner().getValue();

                            (task = new ACSTask(
                                launchPanel,
                                alpha,
                                beta,
                                maxIterations,
                                numberOfAnts,
                                pheromoneInit,
                                evaporationRate,
                                q0,
                                maxStep,
                                executionTimeInSeconds
                            )).execute();
                            break;
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
        launchButton.setEnabled(clausesLoaded);
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
        solversPanel.getGaParamsPanel().getExecutionTimeRadioButton().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getMaxFitnessRadioButton().setEnabled(!isRunning);
        solversPanel.getGaParamsPanel().getMaxGenerationsRadioButton().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getAlphaSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getBetaSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getMaxIterationsSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getNumberOfAntsSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getPheromoneInitSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getEvaporationRateSpinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getQ0Spinner().setEnabled(!isRunning);
        solversPanel.getAcsParamsPanel().getMaxStepSpinner().setEnabled(!isRunning);
        launchButton.setText(isRunning ? "STOP ( Running... )" : "Launch");
    }

    private Solvers getChosenSolver() {
        return Solvers.valueOf(((ComboBoxItem) Objects.requireNonNull(solversPanel.getSolversComboBox().getSelectedItem())).getId());
    }

    private int getChosenExecutionTime() {
        return Integer.parseInt(((ComboBoxItem) Objects.requireNonNull(executionTimeComboBox.getSelectedItem())).getId());
    }

    private int getChosenPopulationSize() {
        return (int) solversPanel.getGaParamsPanel().getPopulationSizeSpinner().getValue();
    }

    private int getChosenMaxIterations() {
        return (int) solversPanel.getGaParamsPanel().getMaxIterationSpinner().getValue();
    }

    private int getChosenCrossoverRate() {
        return Integer.parseInt(
            ((ComboBoxItem) Objects.requireNonNull(solversPanel.getGaParamsPanel().getCrossoverRateComboBox().getSelectedItem())
        ).getId());
    }

    private int getChosenMutationRate() {
        return Integer.parseInt(
            ((ComboBoxItem) Objects.requireNonNull(solversPanel.getGaParamsPanel().getMutationRateComboBox().getSelectedItem())
        ).getId());
    }

    private StoppingCriteria getChosenStoppingCriteria() {
        return StoppingCriteria.valueOf(
            solversPanel.getGaParamsPanel().getStoppingCriteriaButtonGroup().getSelection().getActionCommand()
        );
    }
}
