package ch.bullsoft.coloring;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {

    // 0 -- 1 -- 2
    //      |
    //      3
    //
    // with 3 colors
    private Graph simpleGraph;

    // 0 -- 1 -- 2
    //
    // with two colors
    private Graph lineGraph;

    // 0 -- 1
    // |  /
    // | /
    // 2
    //
    // with 2 colors
    private Graph infeasibleGraph;

    @Before
    public void init() {
        simpleGraph = new Graph(4, 3, Arrays.asList(
                new ImmutablePair(0, 1),
                new ImmutablePair(1, 2),
                new ImmutablePair(1, 3)
        ));
        lineGraph = new Graph(3, 2, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(1, 2)
        ));
        infeasibleGraph = new Graph(3, 2, Arrays.asList(
                new ImmutablePair<>(0, 1),
                new ImmutablePair<>(0, 2),
                new ImmutablePair<>(1, 2)
        ));
    }

    @Test
    public void getNumberOfNodes_simpleGraph() {
        assertEquals(4, simpleGraph.getNumberOfNodes());
    }

    @Test
    public void getColors_simpleGraph() {
        assertEquals(3, simpleGraph.getNumberOfColors());
    }

    @Test
    public void getAllColors_simpleGraph() {
        assertThat(simpleGraph.getAllColors()).isEqualTo(Arrays.asList(0, 1, 2));
    }

    @Test
    public void getNeighboursOf_simpleGraph() {
        assertThat(simpleGraph.getNeighboursOf(0)).containsExactly(1);
        assertThat(simpleGraph.getNeighboursOf(1)).containsExactly(0, 2, 3);
        assertThat(simpleGraph.getNeighboursOf(2)).containsExactly(1);
        assertThat(simpleGraph.getNeighboursOf(3)).containsExactly(1);
    }

    @Test
    public void getDomainOf_containsAllColorsInitially() {
        List<Integer> allColors = simpleGraph.getAllColors();
        assertThat(allColors).containsExactly(0, 1, 2);
        assertThat(simpleGraph.getDomainOf(0)).isEqualTo(allColors);
        assertThat(simpleGraph.getDomainOf(1)).isEqualTo(allColors);
        assertThat(simpleGraph.getDomainOf(2)).isEqualTo(allColors);
        assertThat(simpleGraph.getDomainOf(3)).isEqualTo(allColors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void color_WhenUnknownNodeIsSet_ShouldThrowException() throws ColoringException {
        simpleGraph.color(new Selection(99, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void color_WhenUnknownColorIsSet_ShouldThrowException() throws ColoringException {
        simpleGraph.color(new Selection(0, 99));
    }

    @Test(expected = IllegalArgumentException.class)
    public void color_WhenNullNodeIsSet_ShouldThrowException() throws ColoringException {
        simpleGraph.color(new Selection(null, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void color_WhenNullColorIsSet_ShouldThrowException() throws ColoringException {
        simpleGraph.color(new Selection(0, null));
    }

    @Test(expected = IllegalStateException.class)
    public void color_WhenColorIsNotInDomainOfNode_ShouldThrowException() throws ColoringException {
        simpleGraph.color(new Selection(0, 0));
        simpleGraph.color(new Selection(1, 0));
    }

    @Test
    public void color_setsColorAndRemovesColorFromNeigbourDomains() throws ColoringException {
        simpleGraph.color(new Selection(1, 0));

        assertThat(simpleGraph.getColor(1)).isEqualTo(0);
        assertThat(simpleGraph.getColor(0)).isNull();
        assertThat(simpleGraph.getColor(2)).isNull();
        assertThat(simpleGraph.getColor(3)).isNull();

        assertThat(simpleGraph.getDomainOf(1)).containsExactly(0);
        assertThat(simpleGraph.getDomainOf(0)).containsExactly(1, 2);
        assertThat(simpleGraph.getDomainOf(2)).containsExactly(1, 2);
        assertThat(simpleGraph.getDomainOf(3)).containsExactly(1, 2);
    }

    @Test
    public void color_propagatesDomainsAndColors() throws ColoringException {
        lineGraph.color(new Selection(0, 0));

        assertThat(lineGraph.getColor(0)).isEqualTo(0);
        assertThat(lineGraph.getColor(1)).isEqualTo(1);
        assertThat(lineGraph.getColor(2)).isEqualTo(0);

        assertThat(lineGraph.getDomainOf(0)).containsExactly(0);
        assertThat(lineGraph.getDomainOf(1)).containsExactly(1);
        assertThat(lineGraph.getDomainOf(2)).containsExactly(0);
    }

    @Test(expected = ColoringException.class)
    public void color_infeasibleGraph() throws ColoringException {
        infeasibleGraph.color(new Selection(0, 0));
    }

    @Test
    public void getNodesWithMinDomain_ShouldWorkDynamically() throws ColoringException {
        assertThat(simpleGraph.getMinDomainNodes()).containsExactly(0, 1, 2, 3);

        simpleGraph.color(new Selection(0, 0));
        assertThat(simpleGraph.getMinDomainNodes()).containsExactly(1);

        simpleGraph.color(new Selection(1, 1));
        assertThat(simpleGraph.getMinDomainNodes()).containsExactly(2, 3);
    }

    @Test
    public void copyConstructor() throws ColoringException {
        Graph g = new Graph(lineGraph);
        assertThat(g.getNumberOfNodes()).isEqualTo(3);
        assertThat(g.getNumberOfColors()).isEqualTo(2);
        assertThat(g.getDomainOf(0)).containsExactlyInAnyOrder(0, 1);
        assertThat(g.getDomainOf(1)).containsExactlyInAnyOrder(0, 1);
        assertThat(g.getDomainOf(2)).containsExactlyInAnyOrder(0, 1);
        assertThat(g.getNeighboursOf(0)).containsExactlyInAnyOrder(1);
        assertThat(g.getNeighboursOf(1)).containsExactlyInAnyOrder(0, 2);
        assertThat(g.getNeighboursOf(2)).containsExactlyInAnyOrder(1);
        assertThat(g.getMinDomainNodes()).containsExactlyInAnyOrder(0, 1, 2);
        assertThat(g.uncoloredNodes().size()).isEqualTo(3);

        lineGraph.color(new Selection(0, 0));
        Graph h = new Graph(lineGraph);
        assertThat(h.getNumberOfNodes()).isEqualTo(3);
        assertThat(h.getNumberOfColors()).isEqualTo(2);
        assertThat(h.getDomainOf(0)).containsExactlyInAnyOrder(0);
        assertThat(h.getDomainOf(1)).containsExactlyInAnyOrder(1);
        assertThat(h.getDomainOf(2)).containsExactlyInAnyOrder(0);
        assertThat(h.getNeighboursOf(0)).containsExactlyInAnyOrder(1);
        assertThat(h.getNeighboursOf(1)).containsExactlyInAnyOrder(0, 2);
        assertThat(h.getNeighboursOf(2)).containsExactlyInAnyOrder(1);
        assertThat(h.uncoloredNodes().size()).isEqualTo(0);
    }
}