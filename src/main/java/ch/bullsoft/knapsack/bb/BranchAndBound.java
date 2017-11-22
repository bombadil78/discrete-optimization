package ch.bullsoft.knapsack.bb;

import ch.bullsoft.knapsack.Knapsack;
import ch.bullsoft.knapsack.KnapsackSolution;
import ch.bullsoft.knapsack.KnapsackStrategy;
import ch.bullsoft.knapsack.util.Logger;

import java.util.*;

public class BranchAndBound implements KnapsackStrategy {

    private static final Logger logger = Logger.createLogger(BranchAndBound.class);

    public static final EstimationStrategy LINEAR_ESTIMATE = new LinearEstimate();

    private Knapsack knapsack;
    private int currentMaximum;
    private Node bestNode;
    private int estimate;

    public BranchAndBound(int estimate) {
        this.estimate = estimate;
    }

    @Override
    public KnapsackSolution solve(Knapsack knapsack) {
        this.knapsack = knapsack;
        this.currentMaximum = 0;
        this.bestNode = null;

        Node root = new Node();
        root.at = -1;
        root.value = 0;
        root.weightLeft = knapsack.getCapacity();
        root.estimate = estimate;

        depthFirst(root);

        return new KnapsackSolution(traceBack(), currentMaximum, true);
    }

    private void depthFirst(Node root) {
        logger.info("Visiting node " + root);
        if (root.weightLeft < 0) {
            logger.info("Stop visiting, weight left=" + root.weightLeft);
        } else if (root.at == knapsack.getNumberOfElements() - 1) {
            String msg = "At the bottom... ";
            if (root.value > currentMaximum) {
                currentMaximum = root.value;
                msg += "found new maximum: " + currentMaximum;
                bestNode = root;
            }
            logger.info(msg);
        } else if (root.estimate <= currentMaximum) {
            String msg = String.format("Will not explode subtree: estimate=%d, currentMaximum=%d", root.estimate, currentMaximum);
            logger.info(msg);
        } else {
            depthFirst(root.goLeft(knapsack));
            depthFirst(root.goRight(knapsack));
        }
    }

    private boolean[] traceBack() {
        if (bestNode == null) {
            throw new RuntimeException("No solution exploded, check your configuration");
        }

        List<Boolean> path = new ArrayList<>();
        Node currentNode = bestNode;

        while (currentNode.at >= 0) {
            if (currentNode.value == currentNode.parent.value) {
                path.add(false);
            } else {
                path.add(true);
            }
            currentNode = currentNode.parent;
        }

        Collections.reverse(path);
        return asBooleanArray(path);
    }

    private boolean[] asBooleanArray(List<Boolean> list) {
        boolean[] result = new boolean[list.size()];
        int i = 0;
        for (Boolean b : list) {
            result[i++] = b.booleanValue();
        }
        return result;
    }

    private static final class Node {
        private static int counter;
        public int id;
        public int at;
        public int value;
        public int weightLeft;
        public int estimate;
        public Node parent;

        public Node() {
            this.id = counter++;
        }

        public Node goLeft(Knapsack knapsack) {
            Node left = new Node();
            left.at = this.at + 1;
            left.value = this.value + knapsack.getValue(left.at);
            left.weightLeft = this.weightLeft - knapsack.getWeight(left.at);
            left.estimate = this.estimate;
            left.parent = this;
            return left;
        }

        public Node goRight(Knapsack knapsack) {
            Node right = new Node();
            right.at = this.at + 1;
            right.value = this.value;
            right.weightLeft = this.weightLeft;
            right.estimate = this.estimate - knapsack.getWeight(right.at);
            right.parent = this;
            return right;
        }

        private int getId() {
            return id;
        }

        @Override
        public String toString() {
            return String.format(
                    "(id=%d, at=%d, value=%d, weightLeft=%d, estimate=%d, parent=%s)",
                    getId(), at, value, weightLeft, estimate, (parent == null ? "null" : parent.getId())
            );

        }
    }
}