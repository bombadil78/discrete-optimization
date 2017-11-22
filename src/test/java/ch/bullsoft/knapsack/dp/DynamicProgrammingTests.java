package ch.bullsoft.knapsack.dp;

import ch.bullsoft.knapsack.Knapsack;
import ch.bullsoft.knapsack.KnapsackSolution;
import ch.bullsoft.knapsack.KnapsackStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class DynamicProgrammingTests {

    @Test
    public void solve_ShouldSolve() {
        Knapsack verySimple = new Knapsack(
                new int[] { 1, 1},
                new int[] { 1, 1},
                3
        );

        KnapsackStrategy dp = new DynamicProgramming();
        KnapsackSolution solution = dp.solve(verySimple);

        assertTrue(solution.isTaken(1));
        assertTrue(solution.isTaken(1));
    }

    @Test
    public void solve_ShouldSolveSimpleExample() {
        Knapsack simpleProblem = new Knapsack(
                new int[] { 5, 4, 3 },
                new int[] { 4, 5, 2 },
                10
        );
        KnapsackStrategy dp = new DynamicProgramming();
        KnapsackSolution solution = dp.solve(simpleProblem);

        assertTrue(solution.isTaken(1));
        assertTrue(solution.isTaken(1));
        assertTrue(solution.isTaken(0));
    }
}
