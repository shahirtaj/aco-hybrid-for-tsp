## Ant Colony Optimization for the Traveling Salesman Problem

This program accepts a Traveling Salesman Problem file (.tsp) containing the coordinates of cities as input, uses the 
Ant System algorithm and a Genetic Algorithm hybrid to solve the problem, and generates the best, or smallest, tour 
length and the iteration in which the best tour length was found as a solution to the problem.

It allows the user to compare the performance of the Ant System algorithm and a Genetic Algorithm Hybrid on 
non-trivial instances of the Traveling Salesman Problem (TSP).

It assumes that the specified TSP file does not take the curvature of the Earth into account, and that the distance 
between two cities is just the Euclidean distance.

In order to run the program, change the values of "algorithm", "filename", and "numIterations" in ACO.java.

## TODO

- Allow the user to run the program from the command line.
- Test different values for the constants in gaHybrid.java.

## License

This project is distributed under the MIT License. See the [LICENSE](LICENSE) file for more information.
