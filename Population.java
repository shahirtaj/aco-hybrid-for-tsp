/**
 * This class represents a population of individuals.
 *
 * @author Shahir Taj
 */
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Population {
    protected static final Random rand = new Random();

    private final int numIndividuals;

    private final int numLegs;

    private List<ArrayList<Integer>> tours;

    private double[][] distances;

    private final double crossoverProbability;

    private final double mutationProbability;

    private ArrayList<Individual> individuals;

    private double bestFitness;

    private double[] tourLengths;

    /**
     * The constructor for the Population class.
     *
     * @param numAnts The number of ants in the colony.
     * @param numCities The number of cities in the TSP.
     * @param tours An ArrayList containing the tour of each ant in the colony.
     * @param distances An array containing the distances between each city in the TSP.
     * @param crossoverProbability The crossover probability.
     * @param mutationProbability The mutation probability.
     */
    public Population(int numAnts, int numCities, List<ArrayList<Integer>> tours, double[][] distances, double crossoverProbability, double mutationProbability) {
        numIndividuals = numAnts;
        numLegs = numCities;
        this.tours = tours;
        this.distances = distances;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        individuals = new ArrayList<>();
        bestFitness = Double.MAX_VALUE;
        tourLengths = new double[numIndividuals];
    }

    /**
     * Create individuals and add them to the population.
     */
    public void initialize() {
        for (int i = 0; i < numIndividuals; i++) {
            Individual individual = new Individual(numLegs, tours.get(i), distances);
            addIndividual(individual);
        }
    }

    /**
     * Add an individual to the population.
     *
     * @param individual The individual to be added.
     */
    public void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    /**
     * Select a breeding pool from the initial population by tournaments, i.e., choose two individuals from the initial
     * population randomly and add the one with the best fitness to the breeding pool until the number of individuals in
     * the breeding pool is equal to the number of individuals in the initial population.
     *
     * @return A population containing the breeding pool.
     */
    public Population tournamentSelection() {
        Population breedingPool = new Population(numIndividuals, numLegs, tours, distances, crossoverProbability, mutationProbability);

        for (int i = 0; i < numIndividuals; i++) {
            Individual individualOne = getIndividual(rand.nextInt(numIndividuals));
            Individual individualTwo = getIndividual(rand.nextInt(numIndividuals));
            if (individualOne.getFitness() < individualTwo.getFitness()) {
                breedingPool.addIndividual(individualOne);
            } else {
                breedingPool.addIndividual(individualTwo);
            }
        }

        return breedingPool;
    }

    /**
     * Recombine individuals from the breeding pool using a modified version of 1-point crossover, i.e., choose two
     * individuals from the breeding pool randomly, and probabilistically determine if they are going to be recombined
     * at all. If not, add the individuals directly to the recombined pool. If so, choose a crossover point randomly,
     * and separate the tours of both individuals at this point. Then, for each individual, iterate through the other
     * individual's tour and sequentially add the cities to its own tour, if they are not already in its own tour. Add
     * individuals to the recombined pool until the number of individuals in the recombined pool is equal to the number
     * of individuals in the breeding pool and the initial population.
     *
     * @return A population containing the recombined pool.
     */
    public Population onePointCrossover() {
        Population recombinedPool = new Population(numIndividuals, numLegs, tours, distances, crossoverProbability, mutationProbability);
            
        for (int i = 0; i < numIndividuals / 2; i++) {
            Individual individualOne = getIndividual(rand.nextInt(numIndividuals));
            Individual individualTwo = getIndividual(rand.nextInt(numIndividuals));
            Individual childOne = new Individual(individualOne);
            Individual childTwo = new Individual(individualTwo);
            if (Math.random() <= crossoverProbability) {
                int crossoverPoint = rand.nextInt(numLegs - 1) + 1;
                ArrayList<Integer> tourOne = new ArrayList<>(numLegs);
                ArrayList<Integer> tourTwo = new ArrayList<>(numLegs);
                List<Integer> tourOneStart = childOne.getTour().subList(0, crossoverPoint);
                List<Integer> tourOneEnd = childTwo.getTour();
                List<Integer> tourTwoStart = childTwo.getTour().subList(0, crossoverPoint);
                List<Integer> tourTwoEnd = childOne.getTour();
                for (Integer cityIndex : tourOneStart) {
                    tourOne.add(cityIndex);
                }

                for (Integer cityIndex : tourOneEnd) {
                    if (!tourOne.contains(cityIndex)) {
                        tourOne.add(cityIndex);
                    }
                }

                for (Integer cityIndex : tourTwoStart) {
                    tourTwo.add(cityIndex);
                }

                for (Integer cityIndex : tourTwoEnd) {
                    if (!tourTwo.contains(cityIndex)) {
                        tourTwo.add(cityIndex);
                    }
                }

                childOne.setTour(tourOne);
                childTwo.setTour(tourTwo);
            }

            recombinedPool.addIndividual(childOne);
            recombinedPool.addIndividual(childTwo);
        }

        if ((numIndividuals & 1) != 0) {
            Individual randomIndividual = getIndividual(rand.nextInt(numIndividuals));
            recombinedPool.addIndividual(randomIndividual);
        }
        
        return recombinedPool;
    }

    /**
     * Mutate individuals from the recombined pool using a modified version of mutation, i.e., for each individual in
     * the recombined pool, and probabilistically determine if it is going to be mutated at all. If not, add the
     * individual directly to the mutated pool. If so, choose two different cities in the individual's randomly and swap
     * them.Add individuals to the mutated pool until the number of individuals in the mutated pool is equal to the
     * number of individuals in the recombined pool, the breeding pool, and the initial population.
     *
     * @return A population containing the mutated pool.
     */
    public Population mutation() {
        Population mutatedPool = new Population(numIndividuals, numLegs, tours, distances, crossoverProbability, mutationProbability);

        for (Individual individual : individuals) {
            Individual child = new Individual(individual);
            if (Math.random() <= mutationProbability) {
                ArrayList<Integer> tour = individual.getTour();
                ArrayList<Integer> mutatedTour = new ArrayList<>(numLegs);
                int randomCityIndexOne = rand.nextInt(numLegs);
                int randomCityIndexTwo = rand.nextInt(numLegs);
                while (randomCityIndexOne == randomCityIndexTwo) {
                    randomCityIndexTwo = rand.nextInt(numLegs);
                }
                for (int i = 0; i < numLegs; i++) {
                    if (i == randomCityIndexOne) {
                        mutatedTour.add(tour.get(randomCityIndexTwo));
                    }
                    else if (i == randomCityIndexTwo) {
                        mutatedTour.add(tour.get(randomCityIndexOne));
                    }
                    else {
                        mutatedTour.add(tour.get(i));
                    }
                }

                child.setTour(mutatedTour);
            }

            mutatedPool.addIndividual(child);
        }
        
        return mutatedPool;
    }

    /**
     * Get the individual at a specific index in the population.
     *
     * @param individualIndex The index of the individual to get.
     * @return The individual at the specified index.
     */
    public Individual getIndividual(int individualIndex) {
        return individuals.get(individualIndex);
    }

    /**
     * Get the best, or smallest, fitness, or tour length, of an individual in the population.
     *
     * @return The best, or smallest, fitness, or tour length, of an individual in the population.
     */
    public double getBestFitness() {
        for (Individual individual : individuals) {
            double fitness = individual.getFitness();
            if (fitness < bestFitness) {
                bestFitness = fitness;
            }
        }

        return bestFitness;
    }

    /**
     * Get the tour of each individual in the population.
     *
     * @return An ArrayList containing the tour of each individual in the population.
     */
    public List<ArrayList<Integer>> getTours() {
        tours = new ArrayList<>(numIndividuals);
        for (Individual individual : individuals) {
            tours.add(individual.getTour());
        }
        
        return tours;
    }

    /**
     * Get the length of the tour of each individual in the population.
     *
     * @return An ArrayList containing the length of the tour of each individual in the population.
     */
    public double[] getTourLengths() {
        for (int i = 0; i < numIndividuals; i++) {
            tourLengths[i] = individuals.get(i).getFitness();
        }
        
        return tourLengths;
    }
}
