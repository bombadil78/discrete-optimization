package ch.bullsoft.knapsack.dp;

import ch.bullsoft.knapsack.Knapsack;
import ch.bullsoft.knapsack.KnapsackSolution;
import ch.bullsoft.knapsack.KnapsackStrategy;

import java.util.Arrays;

public class DynamicProgramming implements KnapsackStrategy {

    @Override
    public KnapsackSolution solve(Knapsack knapsack) {
        Table table = new Table(knapsack.getNumberOfElements(), knapsack.getCapacity() + 1);

        // fill
        for (int i = 0; i < knapsack.getNumberOfElements(); i++) {
            int value = knapsack.getValue(i);
            int weight = knapsack.getWeight(i);

            for (int j = 0; j <= knapsack.getCapacity(); j++) {
                int without = table.getContent(i - 1, j);

                if (weight > j) {
                    table.setContent(i, j, without);
                } else {
                    int with = table.getContent(i - 1, j - weight) + value;
                    table.setContent(i, j, Math.max(with, without));
                }
            }
        }

        // traceback
        boolean[] taken = new boolean[knapsack.getNumberOfElements()];
        int optimum = table.getContent(knapsack.getNumberOfElements() - 1, knapsack.getCapacity());

        int yPos = knapsack.getCapacity();
        for (int i = knapsack.getNumberOfElements() - 1; i >= 0; i--) {
            int current = table.getContent(i, yPos);
            int without = table.getContent(i - 1, yPos - 1);

            if (current == without) {
                taken[i] = false;
                yPos = yPos - 1;
            } else {
                taken[i] = true;
                yPos = yPos - knapsack.getWeight(i);
            }
        }

        KnapsackSolution solution = new KnapsackSolution(taken, optimum, false);
        return solution;
    }

    public static final class Table {
        private final int width;
        private final int height;
        private final int[] content;

        public Table(int width, int height) {
            this.width = width;
            this.height = height;
            this.content = new int[width * height];
            Arrays.fill(this.content, 0);
        }

        public void setContent(int x, int y, int value) {
            if (x < 0 || x >= width) {
                throw new IllegalArgumentException("Cannot set x to position " + x);
            } else if (y < 0 || y >= height) {
                throw new IllegalArgumentException("Cannot set y to position " + y);
            } else {
                this.content[(x * height) + y] = value;
            }
        }

        public int getContent(int x, int y) {
            if (x < 0 || y < 0) {
                return 0;
            } else if (x >= width || y >= height) {
                throw new IllegalArgumentException(String.format("Outside of table: x=%d, y=%d", x, y));
            } else {
                return this.content[(x * height) + y];
            }
        }
    }
}
