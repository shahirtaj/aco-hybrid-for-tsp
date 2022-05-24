/**
 * Ant Colony Optimization for the Traveling Salesman Problem
 * 
 * This program allows the user to compare the performance of the Ant System algorithm and a Genetic Algorithm Hybrid on
 * non-trivial instances of the Traveling Salesman Problem (TSP).
 *
 * It assumes that the specified TSP file does not take the curvature of the Earth into account, and that the distance
 * between two cities is just the Euclidean distance.
 *
 * @author Shahir Taj
 */
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;

public class ACO {
    private static final int NUM_LINES_BEFORE_CITIES = 6;

    /**
     * Allow the user to run Ant System or the Genetic Algorithm hybrid on a specific TSP problem for a specific number
     * of iterations.
     *
     * @param args An array containing the algorithm to run, the filename of the TSP to solve, and the number of
     *             iterations.
     * @throws Exception FileNotFoundException if the TSP filename is invalid.
     */
    public static void main(String[] args) throws Exception {
        String algorithm = "gaHybrid"; // args[0]
        String filename = "rl5934.tsp"; // args[1]
        int numIterations = 10; // Integer.parseInt(args[2])
        
        List<ArrayList<Double>> cities = getCities(filename);
        int numCities = cities.size();
        double[][] distances = getDistances(cities, numCities);
        double[] results;

        if (algorithm.equals("antSystem")) {
            AntSystem antSystem = new AntSystem(numIterations, numCities, distances);
            antSystem.run();
            results = antSystem.getResults();
        }
        else if (algorithm.equals("gaHybrid")) {
            gaHybrid gaHybrid = new gaHybrid(numIterations, numCities, distances);
            gaHybrid.run();
            results = gaHybrid.getResults();
        }
        else {
            return;
        }

        System.out.println();
        System.out.println("Best Tour Length: " + results[0]);
        System.out.println("Best Iteration: " + (int) results[1]);
    }

    /**
     * Get the coordinates of each city in the TSP.
     *
     * @param filename The filename of the TSP to solve.
     * @return An ArrayList containing the coordinates of each city in the TSP.
     * @throws FileNotFoundException An Exception if the TSP filename is invalid.
     */
    private static List<ArrayList<Double>> getCities(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("ALL_tsp/" + filename));
        List<ArrayList<Double>> cities = new ArrayList<>();

        for (int i = 0; i < NUM_LINES_BEFORE_CITIES; i++) {
            sc.nextLine();
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.equals("EOF")) {
                String[] data = line.trim().split("\\s+");
                ArrayList<Double> city = new ArrayList<>();
                city.add(Double.parseDouble(data[1]));
                city.add(Double.parseDouble(data[2]));
                cities.add(city);
            }
        }

        sc.close();

        return cities;
    }

    /**
     * Get the Euclidean distances between each city in the TSP.
     *
     * @param cities An ArrayList containing the coordinates of each city in the TSP.
     * @param numCities The number of cities in the TSP.
     * @return An array containing the distances between each city in the TSP.
     */
    private static double[][] getDistances(List<ArrayList<Double>> cities, int numCities) {
        double[][] distances = new double[numCities][numCities];

        for (int i = 0; i < numCities; i++) {
            double x0 = cities.get(i).get(0);
            double y0 = cities.get(i).get(1);
            for (int j = 0; j < numCities; j++) {
                double x1 = cities.get(j).get(0);
                double y1 = cities.get(j).get(1);
                distances[i][j] = Math.sqrt((y1 - y0) * (y1 - y0) + (x1 - x0) * (x1 - x0));
            }
        }

        return distances;
    }
}
