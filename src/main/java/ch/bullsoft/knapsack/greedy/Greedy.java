package ch.bullsoft.knapsack.greedy;

import ch.bullsoft.knapsack.Knapsack;
import ch.bullsoft.knapsack.KnapsackSolution;
import ch.bullsoft.knapsack.KnapsackStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Greedy implements KnapsackStrategy {

    public KnapsackSolution solve(Knapsack knapsack) {
        boolean[] taken = new boolean[knapsack.getNumberOfElements()];
        int weight = 0;
        int value = 0;
        int capacity = knapsack.getCapacity();

        for (Entry entry : orderedByDensity(knapsack)) {
            int position = entry.position;
            if (weight + knapsack.getWeight(position) <= capacity){
                taken[position] = true;
                value += knapsack.getValue(position);
                weight += knapsack.getWeight(position);
            } else {
                taken[position] = false;
            }
        }

        return new KnapsackSolution(taken, value);
    }

    private List<Entry> orderedByDensity(Knapsack knapsack) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < knapsack.getNumberOfElements(); i++) {
            double density = (double) knapsack.getValue(i) / knapsack.getWeight(i);
            entries.add(new Entry(i, density));
        }

        entries.sort(Comparator.comparing(Entry::getDensity).reversed());

        return entries
                .stream()
                .collect(Collectors.toList());
    }

    private static final class Entry {
        private final int position;
        private final double density;

        public Entry(int position, double density) {
            this.position = position;
            this.density = density;
        }

        public int getPosition() {
            return this.position;
        }

        public double getDensity() {
            return density;
        }

        @Override
        public String toString() {
            return String.format("density = %s, position = %s", density, position);
        }
    }
}