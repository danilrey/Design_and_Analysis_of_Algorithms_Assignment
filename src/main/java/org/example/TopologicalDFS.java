package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TopologicalDFS {

    private static long nodeVisits = 0;
    private static long edgeTraversals = 0;

    public static void resetCounters() {
        nodeVisits = 0;
        edgeTraversals = 0;
    }

    public static long getNodeVisits() { return nodeVisits; }
    public static long getEdgeTraversals() { return edgeTraversals; }

    private static void dfs(int v, boolean[] visited, boolean[] onStack, List<List<Integer>> adj, List<Integer> out) {
        visited[v] = true;
        nodeVisits++;
        onStack[v] = true;

        for (Integer nbr : adj.get(v)) {
            edgeTraversals++;
            if (!visited[nbr]) {
                dfs(nbr, visited, onStack, adj, out);
            } else if (onStack[nbr]) {
                throw new IllegalStateException("Graph contains a cycle - no topological order available.");
            }
        }

        onStack[v] = false;
        out.add(v);
    }

    public static List<Integer> topologicalSort(List<List<Integer>> adj) {
        resetCounters();
        int n = adj.size();
        boolean[] visited = new boolean[n];
        boolean[] onStack = new boolean[n];
        List<Integer> order = new ArrayList<>();

        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs(v, visited, onStack, adj, order);
            }
        }

        Collections.reverse(order);
        return order;
    }

    public static List<Integer> topologicalSort(List<Integer>[] adjArray) {
        return topologicalSort(Arrays.asList(adjArray));
    }

}
