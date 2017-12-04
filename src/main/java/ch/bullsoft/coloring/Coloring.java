package ch.bullsoft.coloring;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class Coloring {

    public List<Graph> solveCompletelyWithDFS(Graph graph, SelectionStrategy selectionStrategy) {
        return doSolveWithDFS(graph, selectionStrategy, true);
    }

    public Graph solveWithDFS(Graph graph, SelectionStrategy selectionStrategy) {
        return doSolveWithDFS(graph, selectionStrategy, false).get(0);
    }

    private List<Graph> doSolveWithDFS(Graph graph, SelectionStrategy selectionStrategy, boolean isComplete) {
        List<Graph> solutions = new ArrayList<>();

        Stack<Search> stack = new Stack<>();
        stack.push(new Search(graph, selectionStrategy.getNextSelections(graph)));
        do {
            Search s = stack.pop();
            if (s.graph.isFullyColored()) {
                solutions.add(s.graph);
                return solutions;
            } else {
                while (s.hasMoreSelections()) {
                    try {
                        Selection selection = s.getNextSelection();
                        Graph newGraph = new Graph(s.graph);
                        newGraph.color(selection);

                        if (newGraph.isFullyColored()) {
                            solutions.add(newGraph);
                            return solutions;
                        }

                        List<Selection> newSelections = selectionStrategy.getNextSelections(newGraph);
                        stack.push(new Search(newGraph, newSelections));
                    } catch (ColoringException ex) {
                        System.out.println("Selection failed: " + s);
                    }
                }
            }
        } while (!stack.isEmpty());

        return solutions;
    }

    private class Search {
        private Graph graph;
        private List<Selection> selections;
        private int atSelection;

        public Search(Graph g, List<Selection> selections) {
            this.graph = g;
            this.selections = selections;
            this.atSelection = 0;
        }

        public boolean hasMoreSelections() {
            return atSelection < selections.size();
        }

        public Selection getNextSelection() {
            return selections.get(atSelection++);
        }
    }
}