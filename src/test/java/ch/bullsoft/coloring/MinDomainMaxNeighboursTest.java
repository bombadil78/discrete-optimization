package ch.bullsoft.coloring;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MinDomainMaxNeighboursTest {

    @InjectMocks
    private MinDomainMaxNeighbours minDoMaxNb;

    // 0 -- 1 -- 2
    //      |
    //      3
    //
    // with 3 colors
    private Graph simpleGraph;

    @Before
    public void before() {
        simpleGraph = new Graph(4, 3, Arrays.asList(
                new ImmutablePair(0, 1),
                new ImmutablePair(1, 2),
                new ImmutablePair(1, 3)
        ));
    }

    @Test
    public void solveWithDFS() throws ColoringException {
        assertThat(minDoMaxNb.getNextSelections(simpleGraph).get(0)).isEqualTo(new Selection(1, 0));
    }
}
