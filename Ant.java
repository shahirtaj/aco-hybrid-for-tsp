/**
 * This class represents a single ant in a colony.
 *
 * @author Shahir Taj
 */
import java.util.Random;
import java.util.ArrayList;

public class Ant {
    private static final Random rand = new Random();

    private final int numCities;

    private final double[][] distances;

    private final double alpha;

    private final double beta;

    private double[][] pheromone_levels;

    private int initialCityIndex;

    private int currentCityIndex;

    private double[] cityProbabilities;

    private ArrayList<Integer> tour;

    private double tourLength;

    /**
     * The constructor for the Ant class.
     *
     * @param numCities The number of cities in the TSP.
     * @param distances An array containing the distances between each city in the TSP.
     * @param alpha A positive constant.
     * @param beta A positive constant.
     * @param pheromone_levels An array containing the pheromone level on each leg in the TSP.
     */
    public Ant(int numCities, double[][] distances, double alpha, double beta, double[][] pheromone_levels) {
        this.numCities = numCities;
        this.distances = distances;
        this.alpha = alpha;
        this.beta = beta;
        this.pheromone_levels = pheromone_levels;
        initialCityIndex = rand.nextInt(numCities);
        currentCityIndex = initialCityIndex;
        cityProbabilities = new double[numCities];
        tour = new ArrayList<>(numCities);

        for (int i = 0; i < numCities; i++) {
            tour.add(-1);
        }

        tourLength = 0;
    }

    /**
     * Probabilistically select the next leg in the tour of this ant.
     */
    public void move() {
        cityProbabilities = getCityProbabilities();
        double randProbability = Math.random();
        double sumProbabilities = 0;

        for (int i = 0; i < numCities; i++) {
            sumProbabilities += cityProbabilities[i];
            if (randProbability <= sumProbabilities) {
                tour.set(currentCityIndex, i);
                tourLength += distances[currentCityIndex][i];
                currentCityIndex = i;
                break;
            }
        }
    }

    /**
     * Calculate the probabilities of choosing the next city to go to using the pheromone level on the leg between the
     * current city and each unvisited city and the distance from the current city to each unvisited city. If a city has
     * been visited, the probability of choosing it is 0.0.
     *
     * @return An array containing the probabilities of choosing each unvisited city from the current city.
     */
    public double[] getCityProbabilities() {
        double denominator = 0;

        for (int i = 0; i < numCities; i++) {
            if (currentCityIndex != i && tour.get(i) == -1) {
                denominator += Math.pow(pheromone_levels[currentCityIndex][i], alpha) * Math.pow((1 /
                        distances[currentCityIndex][i]), beta);
            }
        }

        for (int i = 0; i < numCities; i++) {
            if (currentCityIndex != i && tour.get(i) == -1) {
                double numerator = Math.pow(pheromone_levels[currentCityIndex][i], alpha) * Math.pow((1 /
                        distances[currentCityIndex][i]), beta);
                cityProbabilities[i] = numerator / denominator;
            }
            else {
                cityProbabilities[i] = 0;
            }
        }

        return cityProbabilities;
    }

    /**
     * Move from the last city to the first city, to complete the tour of this ant.
     */
    public void moveToStart() {
        tour.set(currentCityIndex, initialCityIndex);
        tourLength += distances[currentCityIndex][initialCityIndex];
        currentCityIndex = initialCityIndex;
    }

    /**
     * Get the tour of this ant.
     *
     * @return An ArrayList containing the tour of this ant.
     */
    public ArrayList<Integer> getTour() {
        return tour;
    }

    /**
     * Get the length of the tour of this ant.
     *
     * @return The length of the tour of this ant.
     */
    public double getTourLength() {
        return tourLength;
    }

    /**
     * Reset the pheromone level on each leg of the TSP, the start and current city of this ant, the probabilities of
     * choosing each unvisited city from the current city, the tour of this ant, and the length of the tour of this ant.
     * @param pheromone_levels
     */
    public void reset(double[][] pheromone_levels) {
        this.pheromone_levels = pheromone_levels;
        initialCityIndex = rand.nextInt(numCities);
        currentCityIndex = initialCityIndex;
        cityProbabilities = new double[numCities];

        for (int i = 0; i < numCities; i++) {
            tour.set(i, -1);
        }

        tourLength = 0;
    }
}
