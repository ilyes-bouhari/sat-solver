package solvers.MetaheuristicSearch.GeneticAlgorithm;

import common.ClausesSet;
import common.Solution;
import enums.StoppingCriteria;
import gui.ClausesPanel;
import gui.LaunchPanel;
import gui.SolutionPanel;
import tasks.GeneticAlgorithmTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm {

    private ClausesSet clausesSet;

    private final ClausesPanel clausesPanel;
    private final SolutionPanel solutionPanel;
    private final LaunchPanel launchPanel;

    private final int populationSize;
    private final int maxIterations;
    private final int crossoverRate;
    private final int mutationRate;
    private final StoppingCriteria stoppingCriteria;
    private final int executionTimeInSeconds;
    private final GeneticAlgorithmTask task;

    private ArrayList<Individual> population;
    private Individual firstParent, secondParent, firstChild, secondChild, bestIndividual;
    private int iteration = 0;

    private final Random random = new Random();

    public GeneticAlgorithm(
        ClausesSet clausesSet,
        ClausesPanel clausesPanel,
        SolutionPanel solutionPanel,
        LaunchPanel launchPanel,

        int populationSize,
        int maxIterations,
        int crossoverRate,
        int mutationRate,
        StoppingCriteria stoppingCriteria,
        int executionTimeInSeconds,

        GeneticAlgorithmTask task
    ) {
        this.clausesSet = clausesSet;
        this.clausesPanel = clausesPanel;
        this.solutionPanel = solutionPanel;
        this.launchPanel = launchPanel;

        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.stoppingCriteria = stoppingCriteria;
        this.executionTimeInSeconds = executionTimeInSeconds;
        this.task = task;
    }

    public Solution process() {

        population = generatePopulation(clausesSet, populationSize);

        population.sort(Collections.reverseOrder());
        bestIndividual = population.get(0);

        long startTime = System.currentTimeMillis();

        do {
            iteration++;

            if (((((System.currentTimeMillis() - startTime)/1000) >= executionTimeInSeconds) && stoppingCriteria == StoppingCriteria.EXECUTION_TIME) || (task != null && (task.isCancelled() || task.isDone())))
                break;

            /**
             * Selection
             * Option 1: randomSelection()
             * Option 2: rouletteWheelSelection()
             * Option 3: fittestParentSelection()
             */
            fittestParentSelection();

            /**
             * Crossover
             * Option 1 : onePointCrossover()
             */
            onePointCrossover();

            /**
             * Mutation
             * Option 1 : bitFlipMutation()
             */
            bitFlipMutation();

            if (firstChild.getFitness() > bestIndividual.getFitness() | secondChild.getFitness() > bestIndividual.getFitness()) {
                if (firstChild.getFitness() > secondChild.getFitness())
                    bestIndividual = firstChild;
                else
                    bestIndividual = secondChild;
            }

            /**
             * Insertion
             * Option 1 : randomInsertion()
             * Option 1 : fitnessBasedInsertion()
             */
            fitnessBasedInsertion();

            if (launchPanel != null) launchPanel.getSummaryPanel().updateSummary(clausesSet, bestIndividual.getSolution());
            if (solutionPanel != null) solutionPanel.setSolution(bestIndividual.getSolution());

            boolean response = bestIndividual.getSolution().isSolution(clausesSet, clausesPanel != null ? clausesPanel.getTableModel() : null);
            if (response) break;

            // System.out.println(bestIndividual.getFitness());
        } while (stoppingCriteria(stoppingCriteria));

        return bestIndividual.getSolution();
    }

    public boolean stoppingCriteria(StoppingCriteria choice) {

        switch (choice) {
            case EXECUTION_TIME:
                return true;
            case MAX_GENERATION:
                return iteration < maxIterations;
            case MAX_FITNESS:
                return bestIndividual.getFitness() < clausesSet.getNumberOfClause();
            default:
                throw new IllegalArgumentException();
        }
    }

    public ArrayList<Individual> generatePopulation(ClausesSet clausesSet, int populationSize) {

        ArrayList<Individual> population = new ArrayList<>();
        int counter = 0, index = 0;
        Individual tempIndividual;

        while (counter < populationSize) {

            tempIndividual = new Individual(clausesSet);

            for (index = 0; index < population.size(); index++) {
                if (tempIndividual.getSolution().equals(population.get(index).getSolution())) break;
            }

            if (index == population.size()) {
                population.add(tempIndividual);
                counter++;
            }
        }

        return population;
    }

    public void randomSelection() {
        firstParent = population.get(random.nextInt(population.size()));
        secondParent = population.get(random.nextInt(population.size()));

        if (secondParent.getFitness() > firstParent.getFitness()) {
            Individual temp = firstParent;
            firstParent = secondParent;
            secondParent = temp;
        }
    }

    public void rouletteWheelSelection() {
        firstParent = rouletteWheel();
        secondParent = rouletteWheel();

        if (secondParent.getFitness() > firstParent.getFitness()) {
            Individual temp = firstParent;
            firstParent = secondParent;
            secondParent = temp;
        }
    }

    public Individual rouletteWheel() {

        Individual value = null;
        population.sort(Collections.reverseOrder());
        int max = population.stream().mapToInt(Individual::getFitness).sum();
        int pick = ThreadLocalRandom.current().nextInt(0, max);

        int current = 0;
        for (Individual individual : population) {
            current += individual.getFitness();
            if (current > pick) {
                value = individual;
                break;
            }
        }

        return value;
    }

    public void fittestParentSelection() {
        population.sort(Collections.reverseOrder());
        firstParent = population.get(0);
        secondParent = population.get(1);
    }

    public int rateAsValue(int rate) {
        return (Math.round((float) (rate*clausesSet.getNumberOfVariables())/100));
    }

    public void onePointCrossover() {

        int numberOfVariables = clausesSet.getNumberOfVariables();

        Solution firstChildSolution = new Solution(numberOfVariables);
        Solution secondChildSolution = new Solution(numberOfVariables);

        for (int i = 0; i < rateAsValue(crossoverRate); i++) {
            firstChildSolution.changeLiteral(i, firstParent.getSolution().getLiteral(i));
            secondChildSolution.changeLiteral(i, secondParent.getSolution().getLiteral(i));
        }

        for (int i = rateAsValue(crossoverRate); i < numberOfVariables; i++) {
            firstChildSolution.changeLiteral(i, secondParent.getSolution().getLiteral(i));
            secondChildSolution.changeLiteral(i, firstParent.getSolution().getLiteral(i));
        }

        firstChild = new Individual(clausesSet, firstChildSolution);
        secondChild = new Individual(clausesSet, secondChildSolution);
    }

    public void bitFlipMutation() {

        ThreadLocalRandom.current()
            .ints(0, 75)
            .distinct()
            .limit(rateAsValue(mutationRate))
            .forEach(i -> {
                firstChild.mutate(clausesSet, i);
                secondChild.mutate(clausesSet, i);
            });
    }

    public void randomInsertion() {

        for (int i = 0; i < population.size(); i++) {
            if ((firstChild != null) && (firstChild.getFitness() > population.get(i).getFitness())) {
                population.set(i, firstChild);
                firstChild = null;
            } else if ((secondChild != null) && secondChild.getFitness() > population.get(i).getFitness()) {
                population.set(i, secondChild);
                secondChild = null;
            }

            if (firstChild == null && secondChild == null) break;
        }
    }

    public void fitnessBasedInsertion() {

        Collections.sort(population);

        population.set(0, firstChild);
        population.set(1, secondChild);
    }

    /*public static void main(String[] args) {

        String file = "/Users/ilyes/Code/master/s3/bio-inspired/project/target/classes/uf75-325/uf75-01.cnf";

        ClausesSet clausesSet = new ClausesSet(file);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(clausesSet, 50, 100, 50, 5, 10, 1);

        Instant start = Instant.now();
        geneticAlgorithm.process();
        Instant finish = Instant.now();
        System.out.println((float) Duration.between(start, finish).toMillis() / 1000);
    }*/
}
