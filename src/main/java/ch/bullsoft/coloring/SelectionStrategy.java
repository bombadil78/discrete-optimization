package ch.bullsoft.coloring;

import java.util.List;

public interface SelectionStrategy {

    List<Selection> getNextSelections(Graph graph);

}
