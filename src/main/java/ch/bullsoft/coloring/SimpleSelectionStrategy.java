package ch.bullsoft.coloring;

import java.util.ArrayList;
import java.util.List;

public class SimpleSelectionStrategy implements SelectionStrategy {

    @Override
    public List<Selection> getNextSelections(Graph graph) {
        List<Selection> selections = new ArrayList<>();

        for (Integer node : graph.uncoloredNodes()) {
            for (Integer color : graph.getDomainOf(node)) {
                selections.add(new Selection(node, color));
            }
        }

        return selections;
    }
}
