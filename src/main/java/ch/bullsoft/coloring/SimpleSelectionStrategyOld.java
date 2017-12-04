package ch.bullsoft.coloring;

import java.util.List;

public class SimpleSelectionStrategyOld implements SelectionStrategy {

    @Override
    public List<Selection> getNextSelections(Graph graph) {
        return  null;
        /*
        List<Integer> minDomainNodes = graph.getMinDomainNodes();
        int selectedNode = 0;
        if (minDomainNodes.size() > 1) {
            int maxNbs = 0;
            for (int i : minDomainNodes) {
                int noOfNbs = graph.getNeighboursOf(i).size();
                if (noOfNbs > maxNbs) {
                    maxNbs = noOfNbs;
                    selectedNode = i;
                }
            }
        }
        int color = pickColor(selectedNode);
        return new Selection(selectedNode, color);
        */
    }
}
