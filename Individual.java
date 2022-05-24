/**
 * This class represents a single individual in a population.
 *
 * @author Shahir Taj
 */
import java.util.Random;
import java.util.ArrayList;

public class Individual {
    private int numLegs;

    private ArrayList<Integer> tour;

    private double[][] distances;
    
    private double fitness;

    /**
     * The constructor for the Individual class.
     *
     * @param numLegs The number of legs in a tour.
     * @param tour An ArrayList containing the tour.
     * @param distances An array containing the distances between each city in the TSP.
     */
    public Individual(int numLegs, ArrayList<Integer> tour, double[][] distances) {
        this.numLegs = numLegs;
        this.tour = tour;
        this.distances = distances;
        fitness = 0;
    }

    /**
     * A copy constructor for the Individual class.
     *
     * @param individual The individual to copy.
     */
    public Individual(Individual individual) {
        this.numLegs = individual.numLegs;
        this.tour = individual.tour;
        this.distances = individual.distances;
        fitness = 0;
    }

    /**
     * Get the fitness, or tour length, of this individual's tour.
     *
     * @return The fitness, or tour length, of the individual's tour.
     */
    public double getFitness() {
        for (int i = 0; i < numLegs; i++) {
            fitness += distances[i][tour.get(i)];
        }

        return fitness;
    }

    /**
     * Get the tour of this individual.
     *
     * @return An ArrayList containing the tour of this individual.
     */
    public ArrayList<Integer> getTour() {
        return tour;
    }

    /**
     * Set the tour of this individual.
     *
     * @param tour An ArrayList containing a tour.
     */
    public void setTour(ArrayList<Integer> tour) {
        this.tour = tour;
    }
}
