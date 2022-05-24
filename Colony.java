/**
 * This class represents a colony of ants.
 *
 * @author Shahir Taj
 */
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Colony {
    private static final Random rand = new Random();

    private final int numCities;

    private final double[][] distances;

    private final int numAnts;

    private final double alpha;

    private final double beta;

    private final double evaporationFactor;

    private double[][] pheromone_levels;

    private ArrayList<Ant> ants;

    private List<ArrayList<Integer>> tours;

    private double bestTourLength;

    /**
     * The constructor for the Colony class.
     *
     * @param numCities The number of cities in the TSP.
     * @param distances An array containing the distances between each city in the TSP.
     * @param numAnts The number of ants in the colony.
     * @param alpha A positive constant.
     * @param beta A positive constant.
     * @param evaporationFactor The evaporation factor.
     */
    public Colony(int numCities, double[][] distances, int numAnts, double alpha, double beta, double evaporationFactor) {
        this.numCities = numCities;
        this.distances = distances;
        this.numAnts = numAnts;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationFactor = evaporationFactor;
        pheromone_levels = new double[numCities][numCities];
        ants = new ArrayList<>(numAnts);
        tours = new ArrayList<>(numAnts);
        bestTourLength = Double.MAX_VALUE;
    }

    /**
     * Initialize the pheromone levels of the colony, create ants, and add the ants to the colony.
     */
    public void initialize() {
        initializePheromoneLevels();
        
        for (int i = 0; i < numAnts; i++) {
            Ant ant = new Ant(numCities, distances, alpha, beta, pheromone_levels);
            addAnt(ant);
        }
    }

    /**
     * Construct a nearest neighbor tour to initialize the pheromone level to m/C_nn on each leg of the TSP, where m is
     * the number of ants and C_nn is the length of the nearest neighbor tour. To get a nearest neighbor tour, start at
     * a random city. Then, repeatedly look at the cities that haven’t been visited yet and pick the one closest to the
     * current city. When all the cities have been visited, add the distance from the last city to the first city, since
     * the tour needs to end where it started.
     */
    public void initializePheromoneLevels() {
        double nearest_neighbor_tour_length = 0;
        int initialCityIndex = rand.nextInt(numCities);
        int currentCityIndex = initialCityIndex;
        boolean[] visitedCities = new boolean[numCities];
        visitedCities[currentCityIndex] = true;

        // Look at the cities that haven't been visited yet and pick the one closest to the current city.
        for (int i = 0; i < numCities - 1; i++) {
            double nearestCityDistance = Double.MAX_VALUE;
            int nearestCityIndex = currentCityIndex;
            for (int j = 0; j < numCities; j++) {
                if (!visitedCities[j]) {
                    double distance = distances[currentCityIndex][j];
                    if (distance < nearestCityDistance) {
                        nearestCityDistance = distance;
                        nearestCityIndex = j;
                    }
                }
            }

            nearest_neighbor_tour_length += nearestCityDistance;
            currentCityIndex = nearestCityIndex;
            visitedCities[currentCityIndex] = true;
        }

        nearest_neighbor_tour_length += distances[currentCityIndex][initialCityIndex];

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromone_levels[i][j] = numAnts / nearest_neighbor_tour_length;
            }
        }
    }

    /**
     * Add an ant to the colony.
     *
     * @param ant The ant to be added.
     */
    public void addAnt(Ant ant) {
        ants.add(ant);
    }

    /**
     * Complete a tour for each ant in the colony.
     */
    public void completeTours() {
        for (int j = 0; j < numCities; j++) {
            for (Ant ant : ants) {
                if (j < numCities - 1) {
                    ant.move();
                }
                else {
                    ant.moveToStart();
                }
            }
        }
    }

    /**
     * Get the best, or smallest, tour length of an ant in the colony.
     *
     * @return The best, or smallest, tour length of ant in the colony.
     */
    public double getBestTourLength() {
        for (Ant ant : ants) {
            double tourLength = ant.getTourLength();
            if (tourLength < bestTourLength) {
                bestTourLength = tourLength;
            }
        }

        return bestTourLength;
    }

    /**
     * Update the pheromone level on each leg in the TSP using the evaporation factor and the number of times a specific
     * leg was seen during a tour produced by the Ant System.
     */
    public void updatePheromoneLevels() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                pheromone_levels[i][j] = ((1 - evaporationFactor) * pheromone_levels[i][j]);
            }
        }

        for (Ant ant : ants) {
            ArrayList<Integer> tour = ant.getTour();
            for (int i = 0; i < numCities; i++) {
                pheromone_levels[i][tour.get(i)] = pheromone_levels[i][tour.get(i)] + (1 / ant.getTourLength());
                pheromone_levels[tour.get(i)][i] = pheromone_levels[i][tour.get(i)];
            }
        }
    }

    /**
     * Update the pheromone level on each leg in the TSP using the evaporation factor and the number of times a specific
     * leg was seen during a tour produced by the Genetic Algorithm.
     */
    public void augmentPheromoneLevels(List<ArrayList<Integer>> gaTours, double[] gaTourLengths) {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j <numCities; j++) {
                pheromone_levels[i][j] = ((1 - evaporationFactor) * pheromone_levels[i][j]);
            }
        }

        for (int i = 0; i < numAnts; i++) {
            ArrayList<Integer> gaTour =  gaTours.get(i);
            double gaTourLength = gaTourLengths[i];
            for (int j = 0; j < numCities; j++) {
                pheromone_levels[j][gaTour.get(j)] = pheromone_levels[j][gaTour.get(j)] + (1 / gaTourLength);
                pheromone_levels[gaTour.get(j)][j] = pheromone_levels[j][gaTour.get(j)];
            }
        }
    }

    /**
     * Reset each ant in the colony.
     */
    public void reset() {
        for (Ant ant : ants) {
            ant.reset(pheromone_levels);
        }
    }

    /**
     * Get the number of ants in the colony.
     *
     * @return The number of ants in the colony.
     */
    public int getNumAnts() {
        return numAnts;
    }

    /**
     * Get the ant at a specific index in the colony.
     *
     * @param antIndex The index of the ant to get.
     * @return The ant at the specified index.
     */
    public Ant getAnt(int antIndex) {
        return ants.get(antIndex);
    }

    /**
     * Get the tour of each ant in the colony.
     *
     * @return An ArrayList containing the tour of each ant in the colony.
     */
    public List<ArrayList<Integer>> getTours() {
        for (int i = 0; i < numAnts; i++) {
            tours.add(getAnt(i).getTour());
        }

        return tours;
    }
}
