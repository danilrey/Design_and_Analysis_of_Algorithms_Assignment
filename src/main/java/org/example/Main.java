package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("json/large/large_1.json")) {

            if (is == null)
                throw new IllegalStateException("Resource not found");
            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(jsonText);

            Graph graph = buildGraph(root);
            int src = getSource(root);

            System.out.println("Graph built with V= " + graph.V);
            System.out.println("Adjacency lists:");
            for (int i = 0; i < graph.V; i++) {
                System.out.print(i + ": ");
                for (Integer nbr : graph.adj[i]) {
                    System.out.print(nbr + " ");
                }
                System.out.println();
            }

            long start = System.nanoTime();
//                runKosaraju(graph);
//            Graph graph2 = buildCondensation(graph);
//            List<Integer> result = runTopological(graph2);
//            int[] result = runShortestPath(graph2, src);
            long end = System.nanoTime();
//            System.out.println(result);
            System.out.println((end - start) / 1000000.0 + " ms");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Graph buildGraph(JSONObject root) {
        int n = root.optInt("n", -1);
        if (n < 0) throw new IllegalArgumentException("Missing or invalid 'n' in JSON");

        boolean directed = root.optBoolean("directed", true);
        JSONArray edges = root.optJSONArray("edges");

        Graph graph = new Graph(n);

        if (edges != null) {
            for (int i = 0; i < edges.length(); i++) {
                JSONObject e = edges.getJSONObject(i);
                int u = e.getInt("u");
                int v = e.getInt("v");
                int w = e.optInt("w", 1);


                if (u < 0 || u >= n || v < 0 || v >= n) {
                    throw new IllegalArgumentException("Edge endpoint out of range: " + u + " -> " + v);
                }

                graph.addEdge(u, v, w);
                if (!directed) graph.addEdge(v, u, w);
            }
        }

        return graph;
    }

    static int getSource(JSONObject root) {
        int n = root.optInt("n", -1);
        if (n < 0) throw new IllegalArgumentException("Missing or invalid 'n' in JSON");
        return root.optInt("source", -1);
    }

    static void runKosaraju(Graph graph) {
        System.out.println("Kosaraju");
        Kosaraju.printSCC(graph);
        System.out.println("Number of operations: " + (Kosaraju.getFirstPassEdges() + Kosaraju.getFirstPassNodeVisits()));
    }

    static List<Integer> runTopological(Graph graph) {
        System.out.println("Topological sort");
        List<Integer> result = TopologicalDFS.topologicalSort(graph.adj);
        System.out.println("Number of operations: " +(TopologicalDFS.getEdgeTraversals() + TopologicalDFS.getNodeVisits()));
        return result;
    }

    static Graph buildCondensation(Graph graph) {
        if (graph == null) throw new IllegalArgumentException("graph must not be null");

        int[] compId = new int[graph.V];
        Graph cond = CondensationKosaraju.buildCondensationGraph(graph, compId);

        System.out.println("Condensed graph V= " + cond.V);
        for (int i = 0; i < cond.V; i++) {
            System.out.print(i + ": ");
            for (Object nbr : cond.adj[i]) {
                System.out.print(nbr + " ");
            }
            System.out.println();
        }

        return cond;
    }

    static int[] runShortestPath(Graph graph, int src) {
        System.out.println("Shortest path");
        int[] path = ShortestPath.shortestPathDAG(graph, src);
        ShortestPath.printDistances(path,src);
        System.out.println("Number of Operations: " + (ShortestPath.getRelaxations() + ShortestPath.getSuccessfulRelaxations()));
        return path;
    }

    static int[] runLongestPath(Graph graph, int src) {
        System.out.println("Shortest path");
        int[] path = ShortestPath.longestPathDAG(graph, src);
        ShortestPath.printDistances(path,src);
        System.out.println("Number of Operations: " + (ShortestPath.getRelaxations() + ShortestPath.getSuccessfulRelaxations()));
        return path;
    }

}
