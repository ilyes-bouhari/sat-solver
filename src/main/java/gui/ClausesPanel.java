package gui;

import common.Clause;
import common.ClausesSet;
import utils.CustomTableCellRenderer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.awt.event.ActionListener;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class ClausesPanel extends JPanel {

    private LaunchPanel launchPanel;
    private JTable clausesTable;
    private ClausesSet clausesSet;
    private JButton loadClausesButton;
    private DefaultTableModel tableModel;

    public JLabel getBenchmarkTypeLabel() {
        return benchmarkTypeLabel;
    }

    private JLabel benchmarkTypeLabel;

    private JComboBox<String> benchmarkTypeComboBox, benchmarkInstanceComboBox;

    public ClausesPanel() {
        setupUI();
        setupListeners();
    }

    public void setupUI() {
        setLayout(new GridBagLayout());
        Border border = BorderFactory.createTitledBorder("Clauses");
        Border margin = new EmptyBorder(8, 8, 8, 8);
        setBorder(new CompoundBorder(border, margin));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        clausesTable = new JTable();
        clausesTable.getTableHeader().setBorder(new LineBorder(Color.BLACK, 1, true));
        clausesTable.getTableHeader().setBackground(Color.WHITE);
        clausesTable.setEnabled(false);
        clausesTable.setShowGrid(true);
        clausesTable.setGridColor(Color.BLACK);
        clausesTable.setRowSelectionAllowed(false);
        clausesTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        clausesTable.getTableHeader().setReorderingAllowed(false);
        clausesTable.getTableHeader().setResizingAllowed(false);
        clausesTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(clausesTable), gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        benchmarkTypeLabel = new JLabel("Benchmark Type : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridwidth = 1;
        add(benchmarkTypeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        benchmarkTypeComboBox = new JComboBox<>(new String[] {"uf75-325", "uuf75-325"});
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridwidth = 1;
        add(benchmarkTypeComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        JLabel benchmarkInstanceLabel = new JLabel("Benchmark Instance : ");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridwidth = 1;
        add(benchmarkInstanceLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        benchmarkInstanceComboBox = new JComboBox<String>(IntStream.rangeClosed(1, 100).boxed().map(Object::toString).toArray(String[]::new));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridwidth = 1;
        add(benchmarkInstanceComboBox, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        loadClausesButton = new JButton("Load Clauses");
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.gridwidth = 1;
        add(loadClausesButton, gridBagConstraints);
    }

    public void setupListeners() {

        loadClausesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String benchmarkType = (String) benchmarkTypeComboBox.getSelectedItem();
                String benchmarkInstance = (String) benchmarkInstanceComboBox.getSelectedItem();

                String filePath = "/" + benchmarkType + "/" + benchmarkType.split("-")[0] + "-" + String.format("%0" + (benchmarkInstance.length()+1) + "d", Integer.parseInt(benchmarkInstance)) + ".cnf";

                try {
                    loadClausesSet(getClass().getResourceAsStream(filePath));
                    launchPanel.setClausesLoaded(true);
                    launchPanel.enableLaunchButton();
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });

        benchmarkTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchPanel.setClausesLoaded(false);
                launchPanel.enableLaunchButton();
            }
        });

        benchmarkInstanceComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                launchPanel.setClausesLoaded(false);
                launchPanel.enableLaunchButton();
            }
        });
    }

    public void loadClausesSet(InputStream inputStream) {
        clausesSet = new ClausesSet(inputStream);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("State");
        tableModel.addColumn("Clause");

        for(int i = 0; i < clausesSet.getClauseSize(); i++)
            tableModel.addColumn("Literal " + (i + 1));

        String[] tableRow = new String[clausesSet.getClauseSize()+2];

        for(int i = 0; i < clausesSet.getNumberOfClause(); i++) {
            tableRow[0] = String.valueOf(0);
            tableRow[1] = String.valueOf(i + 1);

            Clause clause = clausesSet.getClause(i);
            for(int j = 2; j <= clausesSet.getClauseSize() + 1; j++)
                tableRow[j] = String.valueOf(clausesSet.getClause(i).getLiteral(j - 2));

            tableModel.addRow(tableRow);
        }

        clausesTable.setModel(tableModel);
        clausesTable.getColumn("State").setMinWidth(0);
        clausesTable.getColumn("State").setMaxWidth(0);
        clausesTable.getColumn("State").setWidth(0);
    }

    public void setLaunchPanel(LaunchPanel launchPanel) {
        this.launchPanel = launchPanel;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public ClausesSet getClausesSet() {
        return clausesSet;
    }

    public JButton getLoadClausesButton() {
        return loadClausesButton;
    }

    public JComboBox<String> getBenchmarkTypeComboBox() {
        return benchmarkTypeComboBox;
    }

    public JComboBox<String> getBenchmarkInstanceComboBox() {
        return benchmarkInstanceComboBox;
    }
}
