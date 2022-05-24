/**
 * This class implements the basic Ant System algorithm.
 *
 * @author Shahir Taj
 */
public class AntSystem {
    protected static final int NUM_ANTS = 20;

    protected static final double ALPHA = 1.5;

    protected static final double BETA = 3.5;

    protected static final double EVAPORATION_FACTOR = 0.5;

    protected final int numIterations;

    protected final int numCities;

    protected final double[][] distances;

    protected double bestTourLength;

    protected double bestIteration;

    /**
     * The constructor for the AntSystem class.
     *
     * @param numIterations The number of iterations to run the Ant System.
     * @param numCities The number of cities in the TSP.
     * @param distances An array containing the distances between each city in the TSP.
     */
    public AntSystem(int numIterations, int numCities, double[][] distances) {
        this.numIterations = numIterations;
        this.numCities = numCities;
        this.distances = distances;
        bestTourLength = Double.MAX_VALUE;
        bestIteration = -1;
    }

    /**
     * Run the Ant System and keep track of the best, or smallest, tour length and the iteration in which the best tour
     * length was found.
     */
    public void run() {
        Colony colony = new Colony(numCities, distances, NUM_ANTS, ALPHA, BETA, EVAPORATION_FACTOR);
        colony.initialize();

        for (int i = 0; i < numIterations; i++) {
            colony.completeTours();
            double tourLength = colony.getBestTourLength();
            if (tourLength < bestTourLength) {
                bestTourLength = tourLength;
                bestIteration = i + 1;
            }

            colony.updatePheromoneLevels();
            colony.reset();
            
            System.out.println();
            System.out.println("Iteration " + (i + 1));
            System.out.println("Best Tour Length: " + bestTourLength);
        }
    }

    /**
     * Get the results produced by the Ant System on the TSP.
     *
     * @return An array containing the best, or smallest, tour length and the iteration in which the best tour length
     * was found.
     */
    public double[] getResults() {
        return new double[] {bestTourLength, bestIteration};
    }
}
