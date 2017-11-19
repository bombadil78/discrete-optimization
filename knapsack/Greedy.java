public class Greedy implements KnapsackStrategy {

    public KnapsackSolution solve(Knapsack knapsack) {
        boolean[] taken = new boolean[knapsack.getNumberOfElements()];
        int weight = 0;
        int value = 0;
        int capacity = knapsack.getCapacity();

        for (int i = 0; i < knapsack.getNumberOfElements(); i++){
            if (weight + knapsack.getWeight(i) <= capacity){
                taken[i] = true;
                value += knapsack.getValue(i);
                weight += knapsack.getWeight(i);
            } else {
                taken[i] = false;
            }
        }

        return new KnapsackSolution(taken, value);
    }
}