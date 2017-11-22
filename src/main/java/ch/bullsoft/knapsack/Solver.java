package ch.bullsoft.knapsack;

import ch.bullsoft.knapsack.bb.BranchAndBound;
import ch.bullsoft.knapsack.dp.DynamicProgramming;
import ch.bullsoft.knapsack.greedy.Greedy;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class Solver {
    
    /**
     * The main class
     */
    public static final void main(final String[] args) {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     */
    public static void solve(String[] args) throws IOException {
        String fileName = null;
        
        // get the temp file name
        for(String arg : args){
            if(arg.startsWith("-file=")){
                fileName = arg.substring(6);
            } 
        }
        if(fileName == null)
            return;
        
        // read the lines out of the file
        List<String> lines = new ArrayList<String>();

        BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }
        
        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        int items = Integer.parseInt(firstLine[0]);
        int capacity = Integer.parseInt(firstLine[1]);

        int[] values = new int[items];
        int[] weights = new int[items];

        for(int i=1; i < items+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");

          values[i-1] = Integer.parseInt(parts[0]);
          weights[i-1] = Integer.parseInt(parts[1]);
        }

        // STRATEGY BEGIN
        Knapsack knapsack = new Knapsack(weights, values, capacity);
        KnapsackSolution solution = null;

        /*
        if (knapsack.getNumberOfElements() == 30) {
            solution = trySolvingWithDynamicProgramming(fileName, knapsack);
        } else {
            solution = trySolvingWithGreedy(solution, knapsack);
        }
        */
        solution = trySolvingWithBranchAndBound(solution, knapsack);
        if (solution == null) throw new RuntimeException("No solution found!");
        // STRATEGY END

        // prepare the solution in the specified output format
        System.out.println(solution.getValue() + (solution.isApproximation() ? " 1" : " 0"));
        for (int i=0; i < items; i++){
            System.out.print((solution.isTaken(i) ? "1" : "0") + " ");
        }
    }

    private static KnapsackSolution trySolvingWithDynamicProgramming(String fileName, Knapsack knapsack) {
        // System.out.println("---------------------------");
        // System.out.println("Using dynamic programming");
        // System.out.println("---------------------------");
        try {
            KnapsackStrategy strategy = new DynamicProgramming();
            return strategy.solve(knapsack);
        } catch (Exception ex) {
            // System.out.println("Unable to solve with dynamic programming");
            // System.out.println(ex);
        }
        return null;
    }

    private static KnapsackSolution trySolvingWithBranchAndBound(KnapsackSolution solution, Knapsack knapsack) {
        if (solution != null) return solution;
        // System.out.println("---------------------------");
        // System.out.println("Using branch & bound");
        // System.out.println("---------------------------");
        int initialEstimate = BranchAndBound.LINEAR_ESTIMATE.getEstimate(knapsack);
        // System.out.println("Capacity: " + knapsack.getCapacity());
        // System.out.println("Initial estimate: " + initialEstimate);
        for (int i = 0; i < 8; i++) {
            try {
                int estimate = (i == 0 ? initialEstimate : initialEstimate + (int) Math.pow(10, i - 1));
                // System.out.println("Checking with estimate: " + estimate);
                KnapsackStrategy strategy = new BranchAndBound(estimate);
                solution = strategy.solve(knapsack);
                return solution;
            } catch (Exception ex) {
                // System.out.println("Error on solving knapsack, increasing estimate");
            }
        }
        return null;
    }

    private static KnapsackSolution trySolvingWithGreedy(KnapsackSolution solution, Knapsack knapsack) {
        if (solution != null) return solution;
        // System.out.println("---------------------------");
        // System.out.println("Using greedy algorithm");
        // System.out.println("---------------------------");
        KnapsackStrategy strategy = new Greedy();
        return strategy.solve(knapsack);
    }
}