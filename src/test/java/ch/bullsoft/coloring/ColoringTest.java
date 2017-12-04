package ch.bullsoft.coloring;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ColoringTest {

    @InjectMocks
    private Coloring coloring;

    // 0 -- 1
    //
    // with 2 colors
    private Graph simpleGraph;

    // 0 -- 1 -- 2
    //      |
    //      3
    //
    // with 2 colors
    private Graph advancedGraph;

    // some as above, but with 3 colors
    private Graph advancedGraphWith3Colors;

    @Before
    public void before() {
        simpleGraph = new Graph(2, 2, Arrays.asList(
                new ImmutablePair<>(0, 1)
        ));
        advancedGraph = new Graph(4, 2, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(1, 2),
                new ImmutablePair<>(1, 3)
        ));
        advancedGraphWith3Colors = new Graph(4, 3, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(1, 2),
                new ImmutablePair<>(1, 3)
        ));
    }

    @Test
    public void solveCompletelyWithDFS_SimpleGraph() {
        printSolutions(coloring.solveCompletelyWithDFS(simpleGraph, new SimpleSelectionStrategy()));
    }

    @Test
    public void solveCompletelyWithDFS_AdvancedGraph() {
        printSolutions(coloring.solveCompletelyWithDFS(advancedGraph, new SimpleSelectionStrategy()));
    }

    @Test
    public void solveCompletelyWithDFS_AdvanceGraphWith3Colors() {
        printSolutions(coloring.solveCompletelyWithDFS(advancedGraphWith3Colors, new SimpleSelectionStrategy()));
    }

    @Test
    public void solveFindFirstWithDFS() {
        Graph solution = coloring.solveWithDFS(simpleGraph, new SimpleSelectionStrategy());
        assertThat(solution.coloredNodes()).containsExactlyInAnyOrder(0, 1);
        assertThat(solution.getColor(0)).isEqualTo(0);
        assertThat(solution.getColor(1)).isEqualTo(1);
    }

    @Test
    public void solveFindFirstWithDFS_WhenUsingMinDomainMaxNbs_ForSimpleGraph() {
        Graph solution = coloring.solveWithDFS(simpleGraph, new MinDomainMaxNeighbours());
        assertThat(solution.getColor(1)).isEqualTo(0);
        assertThat(solution.getColor(0)).isEqualTo(1);
    }

    @Test
    public void solveFindFirstWithDFS_WhenUsingMinDomainMaxNbs_ForAdvancedGraph() {
        Graph solution = coloring.solveWithDFS(advancedGraph, new MinDomainMaxNeighbours());
        assertThat(solution.getColor(1)).isEqualTo(0);
        assertThat(solution.getColor(0)).isEqualTo(1);
        assertThat(solution.getColor(2)).isEqualTo(1);
        assertThat(solution.getColor(3)).isEqualTo(1);
    }

    @Test
    public void solveFindFirstWithDFS_WhenUsingMinDomainMaxNbs_ForAdvancedGraphWith3Colors() {
        Graph solution = coloring.solveWithDFS(advancedGraphWith3Colors, new MinDomainMaxNeighbours());
        assertThat(solution.getColor(1)).isEqualTo(0);
        assertThat(solution.getColor(0)).isEqualTo(2);
        assertThat(solution.getColor(2)).isEqualTo(1);
        assertThat(solution.getColor(3)).isEqualTo(1);
    }

    private void printSolutions(List<Graph> solutions) {
        for (Graph solution : solutions) {
            System.out.println("\n----------------------");
            System.out.print("[ ");
            for (int i = 0; i < solution.getNumberOfNodes(); i++) {
                System.out.print(i + "=" + solution.getColor(i) + " ");
            }
            System.out.print("]");
        }
    }
}