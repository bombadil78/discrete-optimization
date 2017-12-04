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
public class SimpleSelectionStrategyTest {

    @InjectMocks
    private SimpleSelectionStrategy simpleSelectionStrategy;

    // 0 -- 2 -- 3
    // |  /
    // | /
    // 1
    //
    // with 3 colors
    private Graph simpleGraph;

    // 0 -- 1 -- 2
    //      |
    //      3
    //
    // with 3 colors
    private Graph mediumGraph;

    //      1 -- 4
    //    / |  /
    //   /  | /
    // 0 -- 2 -- 5
    //   \  |
    //    \ |
    //      3 -- 6
    //
    // with 3 colors
    private Graph advancedGraph;

    @Before
    public void before() {
        simpleGraph = new Graph(4, 3, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(0, 2),
                new ImmutablePair<>(1, 2),
                new ImmutablePair<>(2, 3)
        ));
        mediumGraph = new Graph(4, 3, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(1, 2),
                new ImmutablePair<>(1, 3)
        ));
        advancedGraph = new Graph(7, 3, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(0, 2),
                new ImmutablePair<>(0, 3),
                new ImmutablePair<>(1, 2),
                new ImmutablePair<>(2, 3),
                new ImmutablePair<>(1, 4),
                new ImmutablePair<>(2, 5),
                new ImmutablePair<>(3, 6)
        ));
    }

    @Test
    public void getNextSelections_WhenGraphIsUncolored_ShouldListAllCombinationsOfNodesAndColors() throws ColoringException {
        List<Selection> selections = simpleSelectionStrategy.getNextSelections(simpleGraph);
        assertThat(selections).containsExactlyInAnyOrder(
                new Selection(0, 0),
                new Selection(0, 1),
                new Selection(0, 2),
                new Selection(1, 0),
                new Selection(1, 1),
                new Selection(1, 2),
                new Selection(2, 0),
                new Selection(2, 1),
                new Selection(2, 2),
                new Selection(3, 0),
                new Selection(3, 1),
                new Selection(3, 2)
        );
    }

    @Test
    public void getNextSelections_WhenGraphIsPartiallyColored_ShouldListCombinations() throws ColoringException {
        advancedGraph.color(new Selection(3, 0));
        assertThat(simpleSelectionStrategy.getNextSelections(advancedGraph)).containsExactlyInAnyOrder(
                new Selection(0, 1),
                new Selection(0, 2),
                new Selection(2, 1),
                new Selection(2, 2),
                new Selection(6, 1),
                new Selection(6, 2),
                new Selection(1, 0),
                new Selection(1, 1),
                new Selection(1, 2),
                new Selection(4, 0),
                new Selection(4, 1),
                new Selection(4, 2),
                new Selection(5, 0),
                new Selection(5, 1),
                new Selection(5, 2)
        );
    }

    @Test
    public void getNextSelections_WhenGraphIsFullyColored_ShouldReturnEmptySelectionList() throws ColoringException {
        simpleGraph.color(new Selection(2, 0));
        simpleGraph.color(new Selection(3, 1));
        simpleGraph.color(new Selection(1, 1));  // will color node 0 with color 2
        assertThat(simpleSelectionStrategy.getNextSelections(simpleGraph)).isEmpty();
    }

    @Test
    public void getNextSelections_WhenWeStartWithOuterNode_ShouldKnockDownSelections() throws ColoringException {
        mediumGraph.color(new Selection(0, 0));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph).size()).isEqualTo(8);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(1);
        assertThat(mediumGraph.getColor(0)).isEqualTo(0);

        mediumGraph.color(new Selection(3, 1));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph)).size().isEqualTo(2);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(3);
        assertThat(mediumGraph.getColor(0)).isEqualTo(0);
        assertThat(mediumGraph.getColor(3)).isEqualTo(1);
        assertThat(mediumGraph.getColor(1)).isEqualTo(2);

        mediumGraph.color(new Selection(2, 0));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph).size()).isEqualTo(0);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(4);
        assertThat(mediumGraph.getColor(0)).isEqualTo(0);
        assertThat(mediumGraph.getColor(3)).isEqualTo(1);
        assertThat(mediumGraph.getColor(1)).isEqualTo(2);
        assertThat(mediumGraph.getColor(2)).isEqualTo(0);
    }

    @Test
    public void getNextSelections_WhenWeStartWithInnerNode_ShouldKnockDownSelections() throws ColoringException {
        mediumGraph.color(new Selection(1, 0));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph).size()).isEqualTo(6);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(1);
        assertThat(mediumGraph.getColor(1)).isEqualTo(0);

        mediumGraph.color(new Selection(0, 1));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph)).size().isEqualTo(4);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(2);
        assertThat(mediumGraph.getColor(1)).isEqualTo(0);
        assertThat(mediumGraph.getColor(0)).isEqualTo(1);

        mediumGraph.color(new Selection(2, 1));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph)).size().isEqualTo(2);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(3);
        assertThat(mediumGraph.getColor(1)).isEqualTo(0);
        assertThat(mediumGraph.getColor(0)).isEqualTo(1);
        assertThat(mediumGraph.getColor(2)).isEqualTo(1);

        mediumGraph.color(new Selection(3, 1));
        assertThat(simpleSelectionStrategy.getNextSelections(mediumGraph)).size().isEqualTo(0);
        assertThat(mediumGraph.coloredNodes().size()).isEqualTo(4);
        assertThat(mediumGraph.getColor(1)).isEqualTo(0);
        assertThat(mediumGraph.getColor(0)).isEqualTo(1);
        assertThat(mediumGraph.getColor(2)).isEqualTo(1);
        assertThat(mediumGraph.getColor(3)).isEqualTo(1);
    }


}
