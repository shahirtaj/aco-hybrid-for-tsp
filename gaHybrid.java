/**
 * This class extends the AntSystem class and implements a Genetic Algorithm Hybrid.
 *
 * @author Shahir Taj
 */
import java.util.List;
import java.util.ArrayList;

public class gaHybrid extends AntSystem {
    private static final int NUM_ANT_SYSTEM_ITERATIONS = 10;

    private static final double CROSSOVER_PROBABILITY = 0.9; // change?

    private static final double MUTATION_PROBABILITY = 0.5; // change?

    private static final int NUM_GENETIC_ALGORITHM_GENERATIONS = 10; // increase?

    public gaHybrid(int numIterations, int numCities, double[][] distances) {
        super(numIterations, numCities, distances);
    }

    /**
     * Run the Genetic Algorithm Hybrid and keep track of the best, or smallest, tour length and the iteration in which
     * the best tour length was found.
     */
    public void run() {
        Colony colony = new Colony(numCities, distances, NUM_ANTS, ALPHA, BETA, EVAPORATION_FACTOR);
        colony.initialize();

        for (int i = 0; i < numIterations; i++) {
            for (int j = 0; j < NUM_ANT_SYSTEM_ITERATIONS; j++) {
                colony.completeTours();
                double tourLength = colony.getBestTourLength();
                if (tourLength < bestTourLength) {
                    bestTourLength = tourLength;
                    bestIteration = i + 1;
                }

                colony.updatePheromoneLevels();

                if (j < NUM_ANT_SYSTEM_ITERATIONS - 1) {
                    colony.reset();
                }

                System.out.println();
                System.out.println("Ant System Iteration " + (j + 1));
                System.out.println("Best Tour Length: " + bestTourLength);
            }

            // Convert the tours produced by the Ant System thus far to a population of individual tours and run the
            // Genetic Algorithm on this population.
            int numAnts = colony.getNumAnts();
            List<ArrayList<Integer>> tours = colony.getTours();
            Population population = new Population(numAnts, numCities, tours, distances, CROSSOVER_PROBABILITY,
                    MUTATION_PROBABILITY);
            population.initialize();

            for (int j = 0; j < NUM_GENETIC_ALGORITHM_GENERATIONS; j++) {
                Population breedingPool = population.tournamentSelection();
                Population recombinedPool = breedingPool.onePointCrossover();
                population = recombinedPool.mutation();
                double fitness = population.getBestFitness();
                if (fitness < bestTourLength) {
                    bestTourLength = fitness;
                    bestIteration = i + 1;
                }

                System.out.println();
                System.out.println("Genetic Algorithm Generation " + (j + 1));
                System.out.println("Best Tour Length: " + bestTourLength);
            }

            // Update the pheromone levels of the colony using the tours produced by the Genetic Algorithm if there are
            // more Ant System iterations to come.
            if (i < numIterations - 1) {
                List<ArrayList<Integer>> gaTours = population.getTours();
                double[] gaTourLengths = population.getTourLengths();
                colony.augmentPheromoneLevels(gaTours, gaTourLengths);
                colony.reset();
            }

            System.out.println();
            System.out.println("Hybrid Iteration " + (i + 1));
            System.out.println("Best Tour Length: " + bestTourLength);
        }
    }
}