package ch.bullsoft.knapsack;

public final class KnapsackSolution {

    private final boolean[] taken;
    private final int value;

    public KnapsackSolution(boolean[] taken, int value) {
        this.taken = taken;
        this.value = value;
    }

    public boolean isTaken(int index) {
        return this.taken[index];
    }

    public int getValue() {
        return this.value;
    }
}