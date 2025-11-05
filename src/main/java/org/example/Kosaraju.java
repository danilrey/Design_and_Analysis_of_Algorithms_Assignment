package org.example;

import org.example.Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Kosaraju {

    // first pass (fillOrder)
    private static long firstPassNodeVisits = 0;
    private static long firstPassEdges = 0;
    // number of strongly connected components found
    private static long sccCount = 0;

    public static void resetCounters() {
        firstPassNodeVisits = 0;
        firstPassEdges = 0;
        sccCount = 0;
    }

    public static long getFirstPassNodeVisits() { return firstPassNodeVisits; }
    public static long getFirstPassEdges() { return firstPassEdges; }
    public static long getSccCount() { return sccCount; }

    static void DFSUtil(int v, boolean[] visited, LinkedList<Integer>[] adj) {
        visited[v] = true;
        System.out.print(v + " ");

        for (Integer i : adj[v]) {
            if (!visited[i]) {
                DFSUtil(i, visited, adj);
            }
        }
    }

    static Graph getTranspose(Graph g) {
        Graph gr = new Graph(g.V);

        for (int v = 0; v < g.V; v++) {
            for (Integer i : g.adj[v]) {
                gr.adj[i].add(v);
            }
        }
        return gr;
    }

    static void fillOrder(int v, boolean[] visited, Stack<Integer> stack, LinkedList<Integer>[] adj) {
        visited[v] = true;
        firstPassNodeVisits++;
        for (Integer i : adj[v]) {
            firstPassEdges++;
            if (!visited[i]) {
                fillOrder(i, visited, stack, adj);
            }
        }
        stack.push(v);
    }

    public static void printSCC(Graph graph) {
        resetCounters();

        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[graph.V];

        for (int v = 0; v < graph.V; v++) {
            if (!visited[v]) {
                fillOrder(v, visited, stack, graph.adj);
            }
        }

        Graph gr = getTranspose(graph);

        Arrays.fill(visited, false);

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                sccCount++;
                DFSUtil(v, visited, gr.adj);
                System.out.println();
            }
        }
    }
}
