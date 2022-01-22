package gui;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

    ClausesPanel clausesPanel;
    LaunchPanel launchPanel;
    SolversPanel solversPanel;
    SummaryPanel summaryPanel;
    SolutionPanel solutionPanel;

    public Main(String title) {
        try {
            Image icon = ImageIO.read(getClass().getResource("/images/icon.jpeg"));
            setIconImage(icon);
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth() / 4, (int) screenSize.getHeight() / 4, 700, 650);
        setResizable(false);

        // Content Panel
        JPanel contentPanel = new JPanel();
        setContentPane(contentPanel);
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // Data Panel
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        dataPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        contentPanel.add(dataPanel, gridBagConstraints);


        // Clauses Panel
        gridBagConstraints = new GridBagConstraints();
        clausesPanel = new ClausesPanel();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        dataPanel.add(clausesPanel, gridBagConstraints);

        // Solvers Panel
        gridBagConstraints = new GridBagConstraints();
        solversPanel = new SolversPanel();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 5, 5, 0);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        dataPanel.add(solversPanel, gridBagConstraints);

        // Launch Panel
        gridBagConstraints = new GridBagConstraints();
        launchPanel = new LaunchPanel();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.25;
        dataPanel.add(launchPanel, gridBagConstraints);

        // Summary Panel
        gridBagConstraints = new GridBagConstraints();
        summaryPanel = new SummaryPanel();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 1;
        dataPanel.add(summaryPanel, gridBagConstraints);

        // Solution Panel
        gridBagConstraints = new GridBagConstraints();
        solutionPanel = new SolutionPanel();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 1;
        dataPanel.add(solutionPanel, gridBagConstraints);

        // Share panels from state sharing & more control
        clausesPanel.setLaunchPanel(launchPanel);
        launchPanel.setClausesPanel(clausesPanel);
        launchPanel.setSolversPanel(solversPanel);
        launchPanel.setSummaryPanel(summaryPanel);
        launchPanel.setSolutionPanel(solutionPanel);
        solversPanel.setLaunchPanel(launchPanel);
    }
}
