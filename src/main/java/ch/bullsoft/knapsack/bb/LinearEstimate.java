package ch.bullsoft.knapsack.bb;

import ch.bullsoft.knapsack.Knapsack;

import java.util.*;
import java.util.stream.Collectors;

public class LinearEstimate implements EstimationStrategy {

    @Override
    public int getEstimate(Knapsack knapsack) {
        List<MyPair> items = new ArrayList<>();
        for (int i = 0; i < knapsack.getNumberOfElements(); i++) {
            Double relativeValue = (double) knapsack.getValue(i) / knapsack.getWeight(i);
            items.add(new MyPair(i, relativeValue));
        }

        List<Integer> sorted = items
                .stream()
                .sorted(Comparator.comparing(x -> x.density))
                .map(x -> x.number)
                .collect(Collectors.toList());
        Collections.reverse(sorted);

        double currentWeight = 0;
        double currentValue = 0;
        int i = 0;
        while (i < knapsack.getNumberOfElements() && currentWeight < knapsack.getCapacity()) {
            int next = sorted.get(i++);
            int value = knapsack.getValue(next);
            int weight = knapsack.getWeight(next);
            double weightLeft = knapsack.getCapacity() - currentWeight;
            double fraction = Math.min(1, (weightLeft / weight));
            currentWeight = currentWeight + fraction * weight;
            currentValue = currentValue + fraction * value;
        }

        return (int) currentValue;
    }

    private static final class MyPair {
        public final Integer number;
        public final Double density;

        public MyPair(Integer number, Double density) {
            this.number = number;
            this.density = density;
        }
    }
}
