package org.example;

import java.util.LinkedList;

public class Graph {
    public int V;
    public LinkedList<Integer>[] adj;
    public LinkedList<Integer>[] weights;

    public Graph(int V) {
        this.V = V;
        adj = new LinkedList[V];
        weights = new LinkedList[V];

        for (int i = 0; i < V; i++) {
            adj[i] = new LinkedList<>();
            weights[i] = new LinkedList<>();
        }
    }

    public void addEdge(int u, int v, int w) {
        adj[u].add(v);
        if (weights != null) weights[u].add(w);
    }
}