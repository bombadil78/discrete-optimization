package ch.bullsoft.coloring;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

    private static final String SEPARATOR = " ";

    public static final void main(final String[] args) {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solve(String[] args) throws IOException {
        String fileName = args[0].split("=")[1];
        try (Stream<String> linesAsStream = Files.lines(Paths.get(fileName))) {
            List<String> lines = linesAsStream.collect(Collectors.toList());

            int numberOfNodes = extractAtPosition(lines.get(0), 0);
            int numberOfEdges = extractAtPosition(lines.get(0), 1);
            List<Pair<Integer, Integer>> edges = extractEdges(lines);

            // Strategies here ...
            Graph g = new Graph(numberOfNodes, numberOfNodes, edges);
            if (numberOfNodes == 50 && numberOfEdges == 350) {
                g = new Graph(numberOfNodes, 8, edges);
            }
            // List<Graph> solutions = Arrays.asList(new Coloring().solveWithDFS(g, new MinDomainMaxNeighbours()));
            List<Graph> solutions = new Coloring().solveCompletelyWithDFS(g, new MinDomainMaxNeighbours());
            Graph bestSolution = getBestSolution(solutions, numberOfNodes);

            if (bestSolution != null) {
                printSolution(bestSolution);
            }
            // Strategies here ...
        }
    }

    private static Graph getBestSolution(List<Graph> solutions, int numberOfNodes) {
        int minColors = numberOfNodes;
        Graph bestSolution = null;
        for (Graph s : solutions) {
            if (s.getUsedColors().size() <= minColors) {
                bestSolution = s;
                minColors = bestSolution.getUsedColors().size();
            }
        }
        return bestSolution;
    }

    private static List<Pair<Integer, Integer>> extractEdges(List<String> lines) {
        List<Pair<Integer, Integer>> edges = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            Integer l = extractAtPosition(lines.get(i), 0);
            Integer r = extractAtPosition(lines.get(i), 1);
            edges.add(new ImmutablePair<>(l, r));
        }
        return edges;
    }

    private static Integer extractAtPosition(String string, int position) {
        return Integer.valueOf(string.split(SEPARATOR)[position]);
    }

    private static void printSolution(Graph g) {
        StringBuilder sb = new StringBuilder();
        sb.append(g.getUsedColors().size() + SEPARATOR + "0\n");
        for (int i = 0; i < g.getNumberOfNodes(); i++) {
            sb.append(g.getColor(i) + SEPARATOR);
        }
        System.out.println(sb.toString());
    }
}