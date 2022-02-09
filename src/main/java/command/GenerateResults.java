package command;

import common.ClausesSet;
import common.Solution;
import enums.Solvers;
import enums.StoppingCriteria;
import gui.ClausesPanel;
import com.opencsv.CSVWriter;
import gui.LaunchPanel;
import org.paukov.combinatorics3.Generator;
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
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class GenerateResults {

    private LaunchPanel launchPanel;
    private ClausesPanel clausesPanel;

    /** GA Params */
    private Integer[] numberOfIterations;
    private Integer[] sizeOfPopulations;
    private Integer[] crossoverRates;
    private Integer[] mutationRates;

    /** ACS Params */
    private Double[] alphas;
    private Double[] betas;
    private Integer[] maxIterations;
    private Integer[] numberOfAnts ;
    private Double[] pheromoneInits;
    private Double[] evaporationRates;
    private Double[] q0s;
    private Integer[] maxSteps;

    private final double bestAlpha = .2;
    private final double bestBeta = .1;
    private final int bestMaxIterations = 500;
    private final int bestNumberOfAnts = 20;
    private final double bestPheromoneInit = 0.001;
    private final double bestEvaporationRate = 0.3;
    private final double bestQ0 = 0.7;
    private final int bestMaxStep = 60;

    private ArrayDeque<String[]> records = new ArrayDeque<>();
    private static String[] paramsTuningResultsFileHeader = new String[] {"value", "satisfied"};
    private static String[] solversResultsPerFileHeaders = {"filename", "time of execution", "satisfied", "satisfiability rate"};
    private static String[] solversResultsAverageHeaders = {"solver", "time of execution", "satisfied", "satisfiability rate"};

    public static void main(String[] args) throws IOException {

        GenerateResults generator = new GenerateResults();

        generator.generateAllSolversResults();

        // GA
        // generator.setGAParams();
        // generator.runGAParamsTuning();
        // generator.generateGAResultsForEachParams();

        // ACS
        // generator.setACSParams();
        // generator.runACSParamsTuning();
        // generator.generateACSResultsForEachParams();
    }

    public void generateAllSolversResults() {
        String[] directories = {"/uf75-325", "/uuf75-325"};
        Solvers[] solvers = {Solvers.ACS, Solvers.GA, Solvers.AStar, Solvers.DFS};
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

                                    String timeOfExecution = "5";
                                    if (solver == Solvers.GA || solver == Solvers.ACS) {
                                        timeOfExecution = String.valueOf((float) Duration.between(start, finish).toMillis() / 1000);
                                    }

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

    public int satisfiedClausesCount(String file, Solvers solver) {

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream(file));

        int executionTimeInSeconds = 5;

        LaunchPanel launchPanel = new LaunchPanel();
        launchPanel.setClausesPanel(clausesPanel);
        launchPanel.setExecutionTimeInSeconds(executionTimeInSeconds);

        Solution solution = null;

        switch (solver) {
            case DFS: {

                solution = (new BlindSearch(
                    launchPanel,
                    null
                )).DepthFirstSearch();

                break;
            }

            case AStar: {

                solution = (new HeuristicSearch(
                    launchPanel,
                    null
                )).AStar();

                break;
            }

            case GA: {

                solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                    launchPanel,
                    50,
                    4000,
                    50,
                    5,
                    StoppingCriteria.MAX_GENERATION,
                    null
                ).process();

                break;
            }

            case ACS: {

                launchPanel.setExecutionTimeInSeconds(1);

                solution = (new HeuristicSearch(
                        launchPanel,
                        null
                )).AStar();

                launchPanel.setExecutionTimeInSeconds(120);

                solution = (new MetaheuristicSearch()).AntColonySystem(
                    launchPanel,
                    solution,
                    bestAlpha,
                    bestBeta,
                    bestMaxIterations,
                    bestNumberOfAnts,
                    bestPheromoneInit,
                    bestEvaporationRate,
                    bestQ0,
                    bestMaxStep,
                    null
                ).run();
            }
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

    public void setGAParams() {

        clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream("/uf75-325/uf75-04.cnf"));

        launchPanel = new LaunchPanel();
        launchPanel.setClausesPanel(clausesPanel);

        numberOfIterations = IntStream.iterate(0, i -> i + 100).limit(61).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        sizeOfPopulations = IntStream.iterate(0, i -> i + 10).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        crossoverRates = IntStream.iterate(0, i -> i + 10).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);

        mutationRates = IntStream.iterate(0, i -> i + 5).limit(11).boxed()
                .filter(time -> time != 0).toArray(Integer[]::new);
    }

    public void runGAParamsTuning() {

        System.out.println("numberOfIterations : " + Arrays.toString(numberOfIterations));
        System.out.println("sizeOfPopulations : " + Arrays.toString(sizeOfPopulations));
        System.out.println("crossoverRates : " + Arrays.toString(crossoverRates));
        System.out.println("mutationRates : " + Arrays.toString(mutationRates));

        final Solution[] bestSolution = {null};
        final Integer[][] bestParams = {null};

        Generator.cartesianProduct(
            Arrays.asList(numberOfIterations),
            Arrays.asList(sizeOfPopulations),
            Arrays.asList(crossoverRates),
            Arrays.asList(mutationRates)
        )
        .stream()
        .forEach(params -> {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                launchPanel,
                Integer.parseInt(String.valueOf(params.get(0))),
                Integer.parseInt(String.valueOf(params.get(1))),
                Integer.parseInt(String.valueOf(params.get(2))),
                Integer.parseInt(String.valueOf(params.get(3))),
                StoppingCriteria.MAX_GENERATION,
                null
            ).process();

            if (bestSolution[0] == null || solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null) > bestSolution[0].countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null)) {
                bestSolution[0] = new Solution(solution);
                bestParams[0] = new Integer[] {params.get(0), params.get(1), params.get(2), params.get(3)};
            }

            System.out.println("Satisfied : " + bestSolution[0].countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));
            System.out.println("Params : " + Arrays.toString(bestParams[0]));
        });
    }

    public void generateGAResultsForEachParams() {
        generateGAResultsForNumberOfIterations(clausesPanel.getClausesSet(), numberOfIterations);
        generateGAResultsForSizeOfPopulations(clausesPanel.getClausesSet(), sizeOfPopulations);
        generateGAResultsForCrossoverRates(clausesPanel.getClausesSet(), crossoverRates);
        generateGAResultsForMutationRates(clausesPanel.getClausesSet(), mutationRates);
    }

    public void generateGAResultsForNumberOfIterations(ClausesSet clausesSet, Integer[] numberOfIterations) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int iteration : numberOfIterations) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                null,
                50,
                iteration,
                50,
                5,
                StoppingCriteria.MAX_GENERATION,
                null
            ).process();

            records.add(new String[] {
                    String.valueOf(iteration),
                    String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "ga-params-tuning-iterations.csv");
    }

    public void generateGAResultsForSizeOfPopulations(ClausesSet clausesSet, Integer[] sizeOfPopulations) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int size : sizeOfPopulations) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                null,
                size,
                5000,
                50,
                5,
                StoppingCriteria.MAX_GENERATION,
                null
            ).process();

            records.add(new String[] {
                String.valueOf(size),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "ga-params-tuning-population-size.csv");
    }

    public void generateGAResultsForCrossoverRates(ClausesSet clausesSet, Integer[] crossoverRates) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int crossoverRate : crossoverRates) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                null,
                50,
                5000,
                crossoverRate,
                5,
                StoppingCriteria.MAX_GENERATION,
                null
            ).process();

            records.add(new String[] {
                String.valueOf(crossoverRate),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "ga-params-tuning-crossover-rate.csv");
    }

    public void generateGAResultsForMutationRates(ClausesSet clausesSet, Integer[] mutationRates) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for (int mutationRate : mutationRates) {

            Solution solution = (new MetaheuristicSearch()).GeneticAlgorithm(
                null,
                50,
                5000,
                50,
                mutationRate,
                StoppingCriteria.MAX_GENERATION,
                null
            ).process();

            records.add(new String[] {
                String.valueOf(mutationRate),
                String.valueOf(solution.countSatisfiedClauses(clausesSet, null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "ga-params-tuning-mutation-rate.csv");
    }

    public void setACSParams() {

        clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream("/uf75-325/uf75-04.cnf"));

        launchPanel = new LaunchPanel();
        launchPanel.setClausesPanel(clausesPanel);

        alphas = DoubleStream.iterate(0.0, i -> i + .1).limit(11).boxed()
                .filter(alpha -> alpha != 0.0).map(i -> Math.round(i * 10.0) / 10.0).toArray(Double[]::new);

        betas = DoubleStream.iterate(0.0, i -> i + .1).limit(11).boxed()
                .filter(beta -> beta != 0.0).map(i -> Math.round(i * 10.0) / 10.0).toArray(Double[]::new);

        maxIterations = IntStream.iterate(0, i -> i + 500).limit(11).boxed()
                .filter(iteration -> iteration != 0).toArray(Integer[]::new);

        numberOfAnts = IntStream.iterate(0, i -> i + 10).limit(6).boxed()
                .filter(iteration -> iteration != 0).toArray(Integer[]::new);

        pheromoneInits = new Double[] {.01, .001, .0001, .00001};
        /*DoubleStream.iterate(0.0, i -> i + .1).limit(11).boxed().filter(beta -> beta != 0.0).map(i -> Math.round(i * 10.0) / 10.0).toArray(Double[]::new);*/

        evaporationRates = DoubleStream.iterate(0.0, i -> i + .1).limit(10).boxed()
                .filter(beta -> beta != 0.0).map(i -> Math.round(i * 10.0) / 10.0).toArray(Double[]::new);

        q0s = DoubleStream.iterate(0.0, i -> i + .1).limit(11).boxed()
                .map(i -> Math.round(i * 10.0) / 10.0).toArray(Double[]::new);

        maxSteps = IntStream.iterate(0, i -> i + 10).limit(9).boxed()
                .toArray(Integer[]::new);
    }

    public void runACSParamsTuning() {

        System.out.println("alphas : " + Arrays.toString(alphas));
        System.out.println("betas : " + Arrays.toString(betas));
        System.out.println("maxIterations : " + Arrays.toString(maxIterations));
        System.out.println("numberOfAnts : " + Arrays.toString(numberOfAnts));
        System.out.println("pheromoneInits : " + Arrays.toString(pheromoneInits));
        System.out.println("evaporationRates : " + Arrays.toString(evaporationRates));
        System.out.println("q0s : " + Arrays.toString(q0s));
        System.out.println("maxSteps : " + Arrays.toString(maxSteps));

        ClausesPanel clausesPanel = new ClausesPanel();
        clausesPanel.loadClausesSet(GenerateResults.class.getResourceAsStream("/uf75-325/uf75-04.cnf"));

        LaunchPanel launchPanel = new LaunchPanel();
        launchPanel.setClausesPanel(clausesPanel);
        launchPanel.setExecutionTimeInSeconds(1);

        Solution baseSolution = (new HeuristicSearch(
            launchPanel,
            null
        )).AStar();

        launchPanel.setExecutionTimeInSeconds(240);

        Generator.cartesianProduct(
            Arrays.asList(alphas),
            Arrays.asList(betas),
            Arrays.asList(maxIterations),
            Arrays.asList(numberOfAnts),
            Arrays.asList(pheromoneInits),
            Arrays.asList(evaporationRates),
            Arrays.asList(q0s),
            Arrays.asList(maxSteps)
        )
        .stream()
        .forEach(params -> {

            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                Double.parseDouble(String.valueOf(params.get(0))),
                Double.parseDouble(String.valueOf(params.get(1))),
                Integer.parseInt(String.valueOf(params.get(2))),
                Integer.parseInt(String.valueOf(params.get(3))),
                Double.parseDouble(String.valueOf(params.get(4))),
                Double.parseDouble(String.valueOf(params.get(5))),
                Double.parseDouble(String.valueOf(params.get(6))),
                Integer.parseInt(String.valueOf(params.get(7))),
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));
            System.out.println(
                "alpha = " + params.get(0) + ", " +
                "beta = " + params.get(1) + ", " +
                "maxIterations = " + params.get(2) + ", " +
                "numberOfAnts = " + params.get(3) + ", " +
                "pheromoneInit = " + params.get(4) + ", " +
                "evaporationRate = " + params.get(5) + ", " +
                "q0 = " + params.get(6) + ", " +
                "maxStep = " + params.get(7)
            );
        });
    }

    public void generateACSResultsForEachParams() {

        launchPanel.setExecutionTimeInSeconds(1);
        Solution baseSolution = (new HeuristicSearch(
                launchPanel,
                null
        )).AStar();

        generateACSResultsForAlpha(baseSolution);
        generateACSResultsForBeta(baseSolution);
        generateACSResultsForMaxIterations(baseSolution);
        generateACSResultsForNumberOfAnts(baseSolution);
        generateACSResultsForPheromoneInits(baseSolution);
        generateACSResultsForEvaporationRates(baseSolution);
        generateACSResultsForQ0s(baseSolution);
        generateACSResultsForMaxSteps(baseSolution);
    }

    public void generateACSResultsForAlpha(Solution baseSolution) {

        records.clear();
        for(double alpha : alphas) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                alpha,
                bestBeta,
                bestMaxIterations,
                bestNumberOfAnts,
                bestPheromoneInit,
                bestEvaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(alpha),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-alpha.csv");
    }

    public void generateACSResultsForBeta(Solution baseSolution) {

        records.clear();
        for(double beta : betas) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                beta,
                bestMaxIterations,
                bestNumberOfAnts,
                bestPheromoneInit,
                bestEvaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(beta),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-beta.csv");
    }

    public void generateACSResultsForMaxIterations(Solution baseSolution) {

        ArrayDeque<String[]> records = new ArrayDeque<>();
        for(int maxIteration : maxIterations) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                maxIteration,
                bestNumberOfAnts,
                bestPheromoneInit,
                bestEvaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(maxIteration),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-max-iterations.csv");
    }

    public void generateACSResultsForNumberOfAnts(Solution baseSolution) {

        records.clear();
        for(int numberOfAnt : numberOfAnts) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                bestMaxIterations,
                numberOfAnt,
                bestPheromoneInit,
                bestEvaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(numberOfAnt),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-number-of-ants.csv");
    }

    public void generateACSResultsForPheromoneInits(Solution baseSolution) {

        records.clear();
        for(double pheromoneInit : pheromoneInits) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                bestMaxIterations,
                bestNumberOfAnts,
                pheromoneInit,
                bestEvaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(pheromoneInit),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-pheromone-init.csv");
    }

    public void generateACSResultsForEvaporationRates(Solution baseSolution) {

        records.clear();
        for(double evaporationRate : evaporationRates) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                bestMaxIterations,
                bestNumberOfAnts,
                bestPheromoneInit,
                evaporationRate,
                bestQ0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(evaporationRate),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-evaporation-rate.csv");
    }

    public void generateACSResultsForQ0s(Solution baseSolution) {

        records.clear();
        for(double q0 : q0s) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                bestMaxIterations,
                bestNumberOfAnts,
                bestPheromoneInit,
                bestEvaporationRate,
                q0,
                bestMaxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(q0),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-q0.csv");
    }

    public void generateACSResultsForMaxSteps(Solution baseSolution) {

        records.clear();
        for(int maxStep : maxSteps) {
            Solution solution = (new MetaheuristicSearch()).AntColonySystem(
                launchPanel,
                baseSolution,
                bestAlpha,
                bestBeta,
                bestMaxIterations,
                bestNumberOfAnts,
                bestPheromoneInit,
                bestEvaporationRate,
                bestQ0,
                maxStep,
                null
            ).run();

            System.out.println(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null));

            records.add(new String[] {
                String.valueOf(maxStep),
                String.valueOf(solution.countSatisfiedClauses(launchPanel.getClausesPanel().getClausesSet(), null))
            });
        }

        createResultsCSVFile(paramsTuningResultsFileHeader, records, "acs-params-tuning-max-step.csv");
    }
}
