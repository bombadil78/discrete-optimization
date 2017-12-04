package ch.bullsoft.coloring;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {

    private Map<Integer, NodeData> nodes = new HashMap<>();
    private SortedMap<Integer, List<Integer>> domainSizes = new TreeMap<>();
    private int numberOfNodes;
    private int numberOfColors;
    private List<Pair<Integer, Integer>> edges = new ArrayList<>();
    private List<Integer> allColors = new ArrayList<>();

    public Graph(Graph other) {
        numberOfNodes = other.getNumberOfNodes();
        numberOfColors = other.getNumberOfColors();
        initializeColors();
        copyNodeData(other);
        copyDomainSizes(other);
    }

    public Graph(int noOfNodes, int noOfColors, List<Pair<Integer, Integer>> someEdges) {
        numberOfNodes = noOfNodes;
        numberOfColors = noOfColors;
        edges.addAll(someEdges);
        initializeColors();
        initializeNodeData(edges);
        initializeDomainSizes();
    }

    private void initializeColors() {
        allColors = Stream.iterate(0, x -> x + 1).limit(numberOfColors).collect(Collectors.toList());
    }

    private void initializeNodeData(List<Pair<Integer, Integer>> edges) {
        for (int i = 0; i < numberOfNodes; i++) {
            nodes.put(i, new NodeData(allColors));
        }

        connectNeighbours(edges);
    }

    private void copyNodeData(Graph other) {
        for (int i = 0; i < other.getNumberOfNodes(); i++) {
            nodes.put(i, new NodeData(other.getNodeData(i)));
        }
    }

    private void connectNeighbours(List<Pair<Integer, Integer>> edges) {
        for (Pair<Integer, Integer> edge : edges) {
            Integer nodeOne = edge.getLeft();
            Integer nodeTwo = edge.getRight();
            linkNodesAsNeighbours(nodeOne, nodeTwo);
        }
    }

    private void initializeDomainSizes() {
        for (int i = 0; i <= numberOfColors; i++) {
            List<Integer> initialContent = new ArrayList<>();
            if (i == numberOfColors) {
                initialContent.addAll(nodes.keySet());
            }
            domainSizes.put(i, initialContent);
        }
    }

    private void copyDomainSizes(Graph other) {
        for (int i = 0; i <= other.numberOfColors; i++) {
            domainSizes.put(i, new ArrayList<>(other.domainSizes.get(i)));
        }
    }

    private void linkNodesAsNeighbours(Integer nodeOne, Integer nodeTwo) {
        nodes.get(nodeOne).neighbours.add(nodeTwo);
        nodes.get(nodeTwo).neighbours.add(nodeOne);
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getNumberOfColors() {
        return numberOfColors;
    }

    public List<Integer> getAllColors() {
        return allColors;
    }

    public List<Pair<Integer, Integer>> getEdges() {
        return edges;
    }

    public List<Integer> getUsedColors() {
        Set<Integer> usedColors = new HashSet<>();
        for (Map.Entry<Integer, NodeData> entry : nodes.entrySet()) {
            Integer c = entry.getValue().color;
            if (c != null) usedColors.add(c);
        }
        return new ArrayList<>(usedColors);
    }

    public List<Integer> getNeighboursOf(Integer node) {
        return nodes.get(node).neighbours;
    }

    public int getNodeDegree(Integer node) {
        return getNeighboursOf(node).size();
    }

    public List<Integer> getDomainOf(Integer node) {
        return nodes.get(node).domain;
    }

    public Integer getColor(Integer node) {
        return nodes.get(node).color;
    }

    public void color(Selection selection) throws ColoringException {
        checkInput(selection);
        checkDomain(selection);
        propagate(selection);
    }

    public NodeData getNodeData(Integer node) {
        return nodes.get(node);
    }

    public List<Integer> getMinDomainNodes() {
        for (Map.Entry<Integer, List<Integer>> entry : domainSizes.entrySet()) {
            if (entry.getKey() < 2 || entry.getValue().isEmpty()) continue;
            return entry.getValue();
        }

        throw new RuntimeException("Illegal state of domainSizes");
    }

    public boolean isFullyColored() {
        return coloredNodes().size() == numberOfNodes;
    }

    public List<Integer> failedNodes() {
        return domainSizes.get(0);
    }

    public List<Integer> coloredNodes() {
        return domainSizes.get(1);
    }

    public List<Integer> uncoloredNodes() {
        return domainSizes.tailMap(2).values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Map<Integer, List<Integer>> getDomainSizes() {
        return domainSizes;
    }

    private void checkInput(Selection selection) {
        Integer node = selection.getNode();
        Integer color = selection.getColor();
        if (node == null || color == null) throw new IllegalArgumentException("Node or color cannot be null!");
        if (node < 0 || node >= numberOfNodes) throw new IllegalArgumentException("Node unknown: " + node);
        if (color < 0 || color >= numberOfColors) throw new IllegalArgumentException("Color unknown: " + color);
    }

    private void checkDomain(Selection selection) {
        Integer node = selection.getNode();
        Integer color = selection.getColor();
        if (!nodes.get(node).domain.contains(color)) throw new IllegalStateException(String.format("Node %d does not contain color %d in its domain", node, color));
    }

    private void propagate(Selection initialSelection) throws ColoringException {
        if (initialSelection == null) throw new IllegalArgumentException("Selection cannot be null");
        List<Selection> worklist = new ArrayList<>(Arrays.asList(initialSelection));

        while (!worklist.isEmpty()) {
            Selection selection = worklist.remove(0);
            applyColoring(selection);

            Integer nextNode = selection.getNode();
            Integer nextColor = selection.getColor();
            for (Integer nb : nodes.get(nextNode).neighbours) {
                if (nodes.get(nb).color != null) continue;

                List<Integer> nbDomain = nodes.get(nb).domain;
                int oldDomainSize = nbDomain.size();
                nbDomain.remove(nextColor);
                updateDomainSizes(nb, oldDomainSize, nbDomain.size());

                if (nbDomain.isEmpty()) {
                    throw new ColoringException(String.format("Cannot color node %d, domain empty", nb));
                } else if (nbDomain.size() == 1) {
                    worklist.add(new Selection(nb, nbDomain.get(0)));
                } else {
                    // no further propagation possible
                }
            }
        }
    }

    private void applyColoring(Selection selection) {
        Integer nextNode = selection.getNode();
        Integer color = selection.getColor();

        List<Integer> domain = nodes.get(nextNode).domain;
        int oldDomainSize = domain.size();
        domain.clear();
        domain.add(color);
        updateDomainSizes(nextNode, oldDomainSize, 1);

        nodes.get(nextNode).color = color;
    }

    private void updateDomainSizes(Integer someNode, int oldDomainSize, int newDomainSize) {
        if (oldDomainSize == newDomainSize) return;
        domainSizes.get(oldDomainSize).remove(someNode);
        domainSizes.get(newDomainSize).add(someNode);
    }

    private static class NodeData {
        public List<Integer> neighbours = new ArrayList<>();
        public List<Integer> domain = new ArrayList<>();
        public Integer color;

        public NodeData(NodeData other) {
            neighbours = new ArrayList<>(other.neighbours);
            domain = new ArrayList<>(other.domain);
            color = other.color;
        }

        public NodeData(List<Integer> initialDomain) {
            domain.addAll(initialDomain);
        }
    }
}