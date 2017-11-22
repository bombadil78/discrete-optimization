package ch.bullsoft.knapsack.bb;

import ch.bullsoft.knapsack.Knapsack;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LinearEstimateTests {

    @Test
    public void getEstimate_WhenAllFitsIn_ShouldTakeAll() {
        Knapsack knapsack = new Knapsack(
                new int[] { 1, 1, 1 },
                new int[] { 1, 1, 1 },
                100
        );

        EstimationStrategy es = BranchAndBound.LINEAR_ESTIMATE;
        assertEquals(3, es.getEstimate(knapsack));
    }

    @Test
    public void getEstimate_WhenNothingFitsIn_ShouldReturnFraction() {
        Knapsack knapsack = new Knapsack(
                new int[] { 2, 2, 2 },
                new int[] { 2, 2, 2 },
                1
        );

        EstimationStrategy es = BranchAndBound.LINEAR_ESTIMATE;
        assertEquals(1, es.getEstimate(knapsack));
    }

    @Test
    public void getEstimate_WhenNothingFitsIn_ShouldReturnFractionRoundDown() {
        Knapsack knapsack = new Knapsack(
                new int[] { 2, 2, 2 },
                new int[] { 1, 1, 1 },
                1
        );

        EstimationStrategy es = BranchAndBound.LINEAR_ESTIMATE;
        assertEquals(0, es.getEstimate(knapsack));
    }

    @Test
    public void getEstimate_WhenPartFitsIn_ShouldTakeMostValuableFirst() {
        Knapsack knapsack = new Knapsack(
                new int[] { 1, 1, 1 },
                new int[] { 1, 100, 10 },
                2
        );

        EstimationStrategy es = BranchAndBound.LINEAR_ESTIMATE;
        assertEquals(110, es.getEstimate(knapsack));
    }

    @Test
    public void getEstimate_WhenFractionFitsIn_ShouldOrderByValue() {
        Knapsack knapsack = new Knapsack(
                new int[] { 2, 2, 2 },
                new int[] { 1, 100, 10 },
                3
        );

        EstimationStrategy es = BranchAndBound.LINEAR_ESTIMATE;
        assertEquals(105, es.getEstimate(knapsack));
    }
}
