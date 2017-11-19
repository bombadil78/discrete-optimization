public final class Knapsack {

    private final int[] weights;
    private final int[] values;
    private final int numberOfElements;
    private final int capacity;

    public Knapsack(int[] weights, int[] values, int capacity) {
        if (weights.length != values.length) {
            throw new IllegalArgumentException("Invalid input, lenght of input differs");
        }
        this.numberOfElements = weights.length;
        this.capacity = capacity;
        this.weights = weights;
        this.values = values;
    }

    public int getWeight(int index) {
        return weights[index];
    }

    public int getValue(int index) {
        return values[index];
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public String toString() {
        return "Knapsack with capacity: " + capacity;
    }
}