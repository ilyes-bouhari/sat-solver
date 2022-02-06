package command;

import common.ClausesSet;
import common.Solution;
import enums.Solvers;
import enums.StoppingCriteria;
import gui.ClausesPanel;
import com.opencsv.CSVWriter;
import solvers.BlindSearch.BlindSearch;
import solvers.HeuristicSearch.HeuristicSearch;
import solvers.MetaheuristicSearch.MetaheuristicSearch;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

public class GenerateResults {

    private static String[] gaParamsTuningResultsFileHeader = new String[] {"value", "satisfied"};
    private static String[] solversResultsPerFileHeaders = {"filename", "time of execution", "satisfied", "satisfiability rate"};
    private static String[] solversResultsAverageHeaders = {"solver", "time of execution", "satisfied", "satisfiability rate"};

    public static void main(String[] args) throws IOException {

        generateGAResults();
    }

    public static void generateAllSolversResults() {
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
                                    String filePath = "/" + filePathSplit[filePathSplit.length - 2] + "/" + filePathSplit[filePathSplit.length - 1];
                                    String filename = filePathSplit[filePathSplit.length - 1].split("\\.")[0];

                                    Instant start = Instant.now();
                                    String count = String.valueOf(satisfiedClausesCount(filePath, solver));
                                    Instant finish = Instant.now();

                                    String timeOfExecution = solver == Solvers.GA ? String.valueOf((float) Duration.between(start, finish).toMillis() / 1000) : "5";
                                    String satisfiabilityRate = String.format("%.2f", (Float.parseFloat(count)*100/325));

                                    records.addLast(new String[]{filename, timeOfExecution, count, satisfiabilityRate});
                                    System.out.println("'" + filename + "' file processed with " + solver + " solver result to " + count + " clause satisfied in " + timeOfExecution + " seconds.");
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // solver average result
            float averageTimeOfExecution = ((float) records.stream().mapToDouble(record -> Float.parseFloat(record[1])).sum()) / records.size();
            int averageSatisfiedClausesCount = records.stream().mapToInt(record -> Integer.parseInt(record[2])).sum() / records.size();
            String averageSatisfiabilityRate = String.format("%.2f", ((float) averageSatisfiedClausesCount*100)/325);
            solversAverageResults.addLast(
                    new String[] {String.valueOf(solver),
                            String.valueOf(averageTimeOfExecution),
                            String.valueOf(averageSatisfiedClausesCount),
                            averageSatisfiabilityRate}
            );

            // solver result for each file
            createResultsCSVFile(solversResultsPerFileHeaders, records, (solver + ".csv"));
        });

        createResultsCSVFile(solversResultsAverageHeaders, solversAverageResults, "average.csv");
    }

    public static int satisfiedClausesCount(String file, Solvers solver) {

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream(file));

        int executionTimeInSeconds = 5;
        Solution solution = null;

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

                solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    clausesPanel.getClausesSet(),
                    null,
                    null,
                    null,

                    50,
                    4000,
                    50,
                    5,
                    StoppingCriteria.MAX_GENERATION,
                    executionTimeInSeconds,

                    null
                ).process();

                break;
        }

        return solution.countSatisfiedClauses(clausesPanel.getClausesSet(), null);
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

    public static void generateGAResults() {

        Integer[] numberOfIterations = IntStream.iterate(0, i -> i + 100).limit(61).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);


        Integer[] sizeOfPopulations = IntStream.iterate(0, i -> i + 10).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        Integer[] crossoverRates = IntStream.iterate(0, i -> i + 10).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        Integer[] mutationRates = IntStream.iterate(0, i -> i + 5).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream("/uf75-325/uf75-01.cnf"));

        generateGAResultsForNumberOfIterations(clausesPanel.getClausesSet(), numberOfIterations);
        generateGAResultsForSizeOfPopulations(clausesPanel.getClausesSet(), sizeOfPopulations);
        generateGAResultsForCrossoverRates(clausesPanel.getClausesSet(), crossoverRates);
        generateGAResultsForMutationRates(clausesPanel.getClausesSet(), mutationRates);
    }

    public static void generateGAResultsForNumberOfIterations(ClausesSet clausesSet, Integer[] numberOfIterations) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int iteration : numberOfIterations) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    clausesSet,
                    null,
                    null,
                    null,

                    50,
                    iteration,
                    50,
                    5,
                    StoppingCriteria.MAX_GENERATION,
                    1,

                    null
            ).process();

            records.add(new String[] {
                    String.valueOf(iteration),
                    String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(gaParamsTuningResultsFileHeader, records, "ga-params-tuning-iterations.csv");
    }

    public static void generateGAResultsForSizeOfPopulations(ClausesSet clausesSet, Integer[] sizeOfPopulations) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int size : sizeOfPopulations) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    clausesSet,
                    null,
                    null,
                    null,

                    size,
                    5000,
                    50,
                    5,
                    StoppingCriteria.MAX_GENERATION,
                    1,

                    null
            ).process();

            records.add(new String[] {
                String.valueOf(size),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(gaParamsTuningResultsFileHeader, records, "ga-params-tuning-population-size.csv");
    }

    public static void generateGAResultsForCrossoverRates(ClausesSet clausesSet, Integer[] crossoverRates) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int crossoverRate : crossoverRates) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    clausesSet,
                    null,
                    null,
                    null,

                    50,
                    5000,
                    crossoverRate,
                    5,
                    StoppingCriteria.MAX_GENERATION,
                    1,

                    null
            ).process();

            records.add(new String[] {
                String.valueOf(crossoverRate),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(gaParamsTuningResultsFileHeader, records, "ga-params-tuning-crossover-rate.csv");
    }

    public static void generateGAResultsForMutationRates(ClausesSet clausesSet, Integer[] mutationRates) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int mutationRate : mutationRates) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    clausesSet,
                    null,
                    null,
                    null,

                    50,
                    5000,
                    50,
                    mutationRate,
                    StoppingCriteria.MAX_GENERATION,
                    1,

                    null
            ).process();

            records.add(new String[] {
                String.valueOf(mutationRate),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(gaParamsTuningResultsFileHeader, records, "ga-params-tuning-mutation-rate.csv");
    }
}
