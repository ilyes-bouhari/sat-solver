package command;

import common.Solution;
import enums.Solvers;
import gui.ClausesPanel;
import com.opencsv.CSVWriter;
import solvers.BlindSearch.BlindSearch;
import solvers.HeuristicSearch.HeuristicSearch;
import solvers.MetaheuristicSearch.GeneticAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GenerateResults {

    private static String[] solversResultsPerFileHeaders = {"filename", "satisfied"};
    private static String[] solversResultsAverageHeaders = {"solver", "value"};

    public static void main(String[] args) throws IOException {

        String[] directories = {"/uf75-325", "/uuf75-325"};
        Solvers[] solvers = {Solvers.GA, Solvers.AStar, Solvers.DFS};
        ArrayDeque<String[]> solversAverageResults = new ArrayDeque<>();

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

            // solver average result
            int sum = records.stream().mapToInt(record -> Integer.parseInt(record[1])).sum();
            solversAverageResults.addLast(new String[] {String.valueOf(solver), String.valueOf(sum/records.size())});

            // solver result for each file
            createResultsCSVFile(solversResultsPerFileHeaders, records, (solver + ".csv"));
        });

        createResultsCSVFile(solversResultsAverageHeaders, solversAverageResults, "average.csv");
    }

    public static int satisfiedClausesCount(File file, Solvers solver) {

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(file.toString());

        int executionTimeInSeconds = 5;
        Solution solution = null;

        // TODO : add execution time
        /*Instant start = Instant.now();
        // solver goes here !
        Instant finish = Instant.now();
        System.out.println((float) Duration.between(start, finish).toMillis() / 1000);*/
        
        switch (solver) {
            case DFS:

                solution = (new BlindSearch(
                    clausesPanel.getClausesSet(),
                    null,
                    null,
                    executionTimeInSeconds,
                    null,
                    null
                )).DepthFirstSearch();

                break;

            case AStar:

                solution = (new HeuristicSearch(
                    clausesPanel.getClausesSet(),
                    null,
                    null,
                    executionTimeInSeconds,
                    null,
                    null
                )).AStar();

                break;

            case GA:

                solution = (new GeneticAlgorithm(
                    clausesPanel.getClausesSet(),
                    50,
                    100,
                    50,
                    5,
                    executionTimeInSeconds,
                    0
                )).process();

                break;
        }

        return solution.satisfiedClauses(clausesPanel.getClausesSet(), null);
    }

    public static void createResultsCSVFile(String[] headers, ArrayDeque<String[]> records, String filename) {

        records.addFirst(headers);

        String filePath = "/exports/" + filename;

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
