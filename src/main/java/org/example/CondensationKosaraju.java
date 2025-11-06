// java
package org.example;

import org.example.Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class CondensationKosaraju {

    private static long firstPassNodeVisits = 0;
    private static long firstPassEdges = 0;
    private static long sccCount = 0;

    public static void resetCounters() {
        firstPassNodeVisits = 0;
        firstPassEdges = 0;
        sccCount = 0;
    }

    public static long getFirstPassNodeVisits() { return firstPassNodeVisits; }
    public static long getFirstPassEdges() { return firstPassEdges; }
    public static long getSccCount() { return sccCount; }

    public static class CondensationResult {
        public final Graph condGraph;
        public final int[] compId;
        public final List<List<Integer>> components;
        public final int componentsCount;

        public CondensationResult(Graph condGraph, int[] compId, List<List<Integer>> components, int componentsCount) {
            this.condGraph = condGraph;
            this.compId = compId;
            this.components = components;
            this.componentsCount = componentsCount;
        }
    }

    private static void fillOrder(int v, boolean[] visited, Stack<Integer> stack, List<Integer>[] adj) {
        visited[v] = true;
        firstPassNodeVisits++;
        for (Integer nei : adj[v]) {
            firstPassEdges++;
            if (!visited[nei]) fillOrder(nei, visited, stack, adj);
        }
        stack.push(v);
    }

    private static void assignComponentDFS(int v, boolean[] visited, List<Integer>[] adj,
                                           int cid, int[] compId, List<List<Integer>> components) {
        visited[v] = true;
        compId[v] = cid;
        components.get(cid).add(v);
        for (Integer nei : adj[v]) {
            if (!visited[nei]) assignComponentDFS(nei, visited, adj, cid, compId, components);
        }
    }

    private static List<Integer>[] buildTransposeAdj(Graph g) {
        List<Integer>[] trans = new List[g.V];
        for (int i = 0; i < g.V; i++) trans[i] = new java.util.LinkedList<>();
        for (int u = 0; u < g.V; u++) {
            for (Integer v : g.adj[u]) {
                trans[v].add(u);
            }
        }
        return trans;
    }

    public static CondensationResult buildCondensation(Graph graph) {
        resetCounters();

        // first pass
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[graph.V];
        for (int v = 0; v < graph.V; v++) {
            if (!visited[v]) fillOrder(v, visited, stack, graph.adj);
        }

        List<Integer>[] transAdj = buildTransposeAdj(graph);
        Arrays.fill(visited, false);

        List<List<Integer>> components = new ArrayList<>();
        int[] compId = new int[graph.V];
        Arrays.fill(compId, -1);
        int cid = 0;

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                components.add(new ArrayList<>());
                assignComponentDFS(v, visited, transAdj, cid, compId, components);
                cid++;
            }
        }

        sccCount = cid;

        Graph cond = new Graph(cid);
        Set<Integer>[] seen = new Set[cid];
        for (int u = 0; u < graph.V; u++) {
            for (Integer v : graph.adj[u]) {
                int cu = compId[u];
                int cv = compId[v];

                if (cu != cv) {
                    if (seen[cu] == null) seen[cu] = new HashSet<>();
                    if (seen[cu].add(cv)) {
                        cond.addEdge(cu, cv, 1);
                    }
                }
            }
        }

        return new CondensationResult(cond, compId, components, cid);
    }

    public static Graph buildCondensationGraph(Graph graph) {
        return buildCondensation(graph).condGraph;
    }

    public static Graph buildCondensationGraph(Graph graph, int[] compIdOut) {
        CondensationResult res = buildCondensation(graph);
        if (compIdOut != null) {
            if (compIdOut.length < res.compId.length) {
                throw new IllegalArgumentException("compIdOut length must be >= graph.V");
            }
            System.arraycopy(res.compId, 0, compIdOut, 0, res.compId.length);
        }
        return res.condGraph;
    }

}
