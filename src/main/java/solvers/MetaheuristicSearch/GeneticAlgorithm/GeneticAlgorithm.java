package solvers.MetaheuristicSearch.GeneticAlgorithm;

import common.Solution;
import gui.LaunchPanel;
import gui.ClausesPanel;
import common.ClausesSet;
import gui.SolutionPanel;
import solvers.BaseSolver;
import enums.StoppingCriteria;
import tasks.GeneticAlgorithmTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithm extends BaseSolver {

    private final int populationSize;
    private final int maxIterations;
    private final int crossoverRate;
    private final int mutationRate;
    private final StoppingCriteria stoppingCriteria;

    private ArrayList<Individual> population;
    private Individual firstParent, secondParent, firstChild, secondChild, bestIndividual;
    private int iteration = 0;

    private final Random random = new Random();

    public GeneticAlgorithm(
        LaunchPanel launchPanel,
        int populationSize,
        int maxIterations,
        int crossoverRate,
        int mutationRate,
        StoppingCriteria stoppingCriteria,
        int executionTimeInSeconds,
        GeneticAlgorithmTask task
    ) {
        super(launchPanel, executionTimeInSeconds, task);
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.stoppingCriteria = stoppingCriteria;
    }

    public Solution process() {

        population = generatePopulation(getClausesSet(), populationSize);

        population.sort(Collections.reverseOrder());
        bestIndividual = population.get(0);

        startTimer();

        do {
            iteration++;

            if (maxProcessingTimeIsReached()) break;

            /**
             * Selection
             * Option 1: randomSelection()
             * Option 2: rouletteWheelSelection()
             * Option 3: fittestParentSelection()
             */
            randomSelection();

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

            updateUI(bestIndividual.getSolution());

            if (targetIsReached(bestIndividual.getSolution())) break;

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
                return bestIndividual.getFitness() < getClausesSet().getNumberOfClause();
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
        return (Math.round((float) (rate*getClausesSet().getNumberOfVariables())/100));
    }

    public void onePointCrossover() {

        int numberOfVariables = getClausesSet().getNumberOfVariables();

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

        firstChild = new Individual(getClausesSet(), firstChildSolution);
        secondChild = new Individual(getClausesSet(), secondChildSolution);
    }

    public void bitFlipMutation() {

        ThreadLocalRandom.current()
            .ints(0, 75)
            .distinct()
            .limit(rateAsValue(mutationRate))
            .forEach(i -> {
                firstChild.mutate(getClausesSet(), i);
                secondChild.mutate(getClausesSet(), i);
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
}
