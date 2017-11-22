package ch.bullsoft.knapsack.bb;

import ch.bullsoft.knapsack.Knapsack;
import ch.bullsoft.knapsack.KnapsackSolution;
import ch.bullsoft.knapsack.KnapsackStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class BranchAndBoundTests {

    @Test
    public void solve_ShouldSolveMinimum() {
        Knapsack veryVerySimple = new Knapsack(
                new int[] { 1 },
                new int[] { 1 },
                1
        );
        KnapsackStrategy bb = new BranchAndBound(BranchAndBound.LINEAR_ESTIMATE.getEstimate(veryVerySimple));
        KnapsackSolution solution = bb.solve(veryVerySimple);
        assertTrue(solution.isTaken(0));
        assertEquals(1, solution.getValue());
    }

    @Test
    public void solve_ShouldSolve() {
        Knapsack verySimple = new Knapsack(
                new int[] { 1, 1 },
                new int[] { 1, 1 },
                3
        );

        KnapsackStrategy dp = new BranchAndBound(BranchAndBound.LINEAR_ESTIMATE.getEstimate(verySimple));
        KnapsackSolution solution = dp.solve(verySimple);

        assertTrue(solution.isTaken(0));
        assertTrue(solution.isTaken(1));
        assertEquals(2, solution.getValue());
    }

    @Test
    public void solve_ShouldSolveSimpleExample() {
        Knapsack simpleProblem = new Knapsack(
                new int[] { 5, 4, 3 },
                new int[] { 4, 5, 2 },
                10
        );
        KnapsackStrategy dp = new BranchAndBound(BranchAndBound.LINEAR_ESTIMATE.getEstimate(simpleProblem));
        KnapsackSolution solution = dp.solve(simpleProblem);

        assertTrue(solution.isTaken(0));
        assertTrue(solution.isTaken(1));
        assertFalse(solution.isTaken(2));
        assertEquals(9, solution.getValue());
    }
}
