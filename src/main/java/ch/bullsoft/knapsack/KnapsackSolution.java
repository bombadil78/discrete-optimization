package ch.bullsoft.knapsack;

public final class KnapsackSolution {

    private final boolean[] taken;
    private final int value;
    private final boolean isApproximation;

    public KnapsackSolution(boolean[] taken, int value, boolean isApproximation) {
        this.taken = taken;
        this.value = value;
        this.isApproximation = isApproximation;
    }

    public boolean isTaken(int index) {
        return this.taken[index];
    }

    public int getValue() {
        return this.value;
    }

    public boolean isApproximation() {
        return this.isApproximation;
    }
}