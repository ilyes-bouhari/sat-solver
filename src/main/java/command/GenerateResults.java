package command;

import enums.Solvers;
import gui.ClausesPanel;
import common.BaseSolution;
import com.opencsv.CSVWriter;
import solvers.BlindSearch.BlindSearch;
import solvers.HeuristicSearch.HeuristicSearch;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GenerateResults {

    public static void main(String[] args) throws IOException {

        String[] directories = {"/uf75-325", "/uuf75-325"};
        Solvers[] solvers = {Solvers.AStar, Solvers.DFS};

        Arrays.stream(solvers).forEach(solver -> {
            ArrayDeque<String[]> records = new ArrayDeque<>();

            Arrays.stream(directories).forEach(directory -> {
                try {
                    Files.walk(Paths.get(Objects.requireNonNull(GenerateResults.class.getResource(directory)).getPath()))
                        .map(Path::toFile)
                        .forEach((File file) -> {
                            if (file.isFile()) {

                                String[] filePathSplit = String.valueOf(file).split("/");
                                String filename = filePathSplit[filePathSplit.length - 1].split("\\.")[0];
                                String count = String.valueOf(satisfiedClausesCount(file, solver));

                                records.addLast(new String[]{filename, count});
                                System.out.println("'" + filename + "' file processed with " + solver + " solver result to " + count + " clause satisfied.");
                            }
                        });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            createResultsCSVFile(records, solver);
        });
    }

    public static int satisfiedClausesCount(File file, Solvers solver) {

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(file.toString());

        int executionTimeInSeconds = 1;
        BaseSolution solution = null;

        switch (solver) {
            case DFS:

                solution = BlindSearch.DepthFirstSearch(
                    clausesPanel.getClausesSet(),
                    null,
                    null,
                    executionTimeInSeconds,
                    null,
                    null
                );

                break;
            case AStar:

                solution = HeuristicSearch.AStar(
                    clausesPanel.getClausesSet(),
                    null,
                    executionTimeInSeconds,
                    null,
                    null
                );

                break;
        }

        return solution.satisfiedClausesCount(clausesPanel.getClausesSet(), null);
    }

    public static void createResultsCSVFile(ArrayDeque<String[]> records, Solvers solver) {
        String[] headers = {"filename", "satisfied"};

        records.addFirst(headers);

        String filePath = "/exports/" + solver + ".csv";

        try (
            CSVWriter writer = new CSVWriter(
                    new FileWriter(Objects.requireNonNull(GenerateResults.class.getResource(filePath)).getPath()),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
                )
            ) {
            writer.writeAll(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
