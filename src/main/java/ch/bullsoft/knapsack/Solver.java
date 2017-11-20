package ch.bullsoft.knapsack;

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
        // KnapsackStrategy strategy = new Greedy();
        KnapsackStrategy strategy = new DynamicProgramming();
        KnapsackSolution solution = strategy.solve(knapsack);
        // STRATEGY END

        // prepare the solution in the specified output format
        System.out.println(solution.getValue() +" 0");
        for (int i=0; i < items; i++){
            System.out.print((solution.isTaken(i) ? "1" : "0") + " ");
        }
    }
}