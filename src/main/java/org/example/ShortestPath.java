package org.example;

import java.util.List;
import java.util.Arrays;

public class ShortestPath {
    public static final int INF = Integer.MAX_VALUE;

    private static long relaxations = 0; // number of relaxation attempts
    private static long successfulRelaxations = 0; // times a distance was updated

    public static void resetCounters() {
        relaxations = 0;
        successfulRelaxations = 0;
    }

    public static long getRelaxations() { return relaxations; }
    public static long getSuccessfulRelaxations() { return successfulRelaxations; }

    public static int[] shortestPathDAG(Graph graph, int src) {
        resetCounters();
        if (graph == null) throw new IllegalArgumentException("Graph is null");
        int n = graph.V;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        if (src < 0 || src >= n) return dist;
        dist[src] = 0;

        List<Integer> topo = TopologicalDFS.topologicalSort(graph.adj);

        for (int u : topo) {
            if (dist[u] == INF) continue;
            List<Integer> neighbors = graph.adj[u];
            List<Integer> ws = graph.weights[u];
            for (int i = 0; i < neighbors.size(); i++) {
                int v = neighbors.get(i);
                int w = 1;
                if (ws != null && i < ws.size()) w = ws.get(i);
                int nd = dist[u] + w;
                relaxations++;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    successfulRelaxations++;
                }
            }
        }

        return dist;
    }

    public static void printDistances(int[] dist, int src) {
        System.out.println("Source: " + src);
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] == INF) System.out.println(i + " -> no way...");
            else System.out.println(i + " -> " + dist[i]);
        }
    }

    public static int[] longestPathDAG(Graph graph, int src) {
        if (graph == null) throw new IllegalArgumentException("Graph is null");
        int n = graph.V;
        Graph neg = new Graph(n);
        for (int u = 0; u < n; u++) {
            List<Integer> nbrs = graph.adj[u];
            List<Integer> ws = graph.weights[u];
            if (nbrs == null) continue;
            for (int i = 0; i < nbrs.size(); i++) {
                int v = nbrs.get(i);
                int w = 1;
                if (ws != null && i < ws.size()) w = ws.get(i);
                neg.addEdge(u, v, -w);
            }
        }

        int[] negDist = shortestPathDAG(neg, src);

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (negDist[i] == INF) {
                res[i] = INF;
            } else {
                if (negDist[i] == Integer.MIN_VALUE) res[i] = Integer.MAX_VALUE;
                else res[i] = -negDist[i];
            }
        }
        return res;
    }

}
