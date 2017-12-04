package ch.bullsoft.coloring;

import java.util.*;
import java.util.stream.Collectors;

public class MinDomainMaxNeighbours implements SelectionStrategy {

    @Override
    public List<Selection> getNextSelections(Graph graph) {
        Integer node = selectNode(graph);
        List<Integer> colors = selectColors(graph, node);
        List<Selection> selections = colors
                .stream()
                .map(color -> new Selection(node, color))
                .collect(Collectors.toList());
        return selections;
    }

    private Integer selectNode(Graph graph) {
        List<Integer> nodes = graph.getMinDomainNodes();
        if (nodes.isEmpty()) {
            throw new RuntimeException("Empty selection found");
        } else if (nodes.size() == 1) {
            return nodes.get(0);
        } else {
            int maxUncoloredNbs = 0;
            Integer maxUncoloredNbsNode = nodes.get(0);
            for (int i : nodes) {
                Set<Integer> uncoloredNbs = new HashSet<>(graph.getNeighboursOf(i));
                uncoloredNbs.removeAll(graph.coloredNodes());
                if (uncoloredNbs.size() >= maxUncoloredNbs) {
                    maxUncoloredNbsNode = i;
                    maxUncoloredNbs = uncoloredNbs.size();
                }
            }
            return maxUncoloredNbsNode;
        }
    }

    private List<Integer> selectColors(Graph graph, Integer node) {
        List<Integer> colors = new ArrayList<>();

        List<Integer> unusedColors = graph.getAllColors();
        unusedColors.removeAll(graph.getUsedColors());
        if (unusedColors.size() > 0) {
            Collections.sort(unusedColors);
            colors.add(unusedColors.get(0));
        }

        for (int i = 0; i < graph.getUsedColors().size(); i++) {
            colors.add(i);
        }

        colors.retainAll(graph.getDomainOf(node));
        return colors;
    }
}