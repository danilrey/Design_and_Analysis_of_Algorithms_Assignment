package org.example;

import java.util.*;

class DSU {
    int[] parent;
    int[] rank;
    static int findCount = 0;
    static int unionCount = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        findCount++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        unionCount++;
        int px = find(x);
        int py = find(y);
        if (px == py) return;
        if (rank[px] < rank[py]) {
            parent[px] = py;
        } else {
            parent[py] = px;
            if (rank[px] == rank[py]) rank[px]++;
        }
    }
}

public class KruskalsMST {
    public static int comparisonCount = 0;

    public static Map<Character, Map<Character, Integer>> kruskalsMST(Map<Character, Map<Character, Integer>> graph) {
        List<Character> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);
        int V = vertices.size();

        List<int[]> edgesList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            Character u = vertices.get(i);
            for (Map.Entry<Character, Integer> entry : graph.getOrDefault(u, new HashMap<>()).entrySet()) {
                Character v = entry.getKey();
                int j = vertices.indexOf(v);
                if (i < j) {
                    edgesList.add(new int[]{i, j, entry.getValue()});
                }
            }
        }

        edgesList.sort((e1, e2) -> {
            comparisonCount++;
            return Integer.compare(e1[2], e2[2]);
        });

        DSU.findCount = 0;
        DSU.unionCount = 0;

        DSU dsu = new DSU(V);
        List<int[]> mstEdges = new ArrayList<>();
        int count = 0;

        for (int[] e : edgesList) {
            int x = e[0], y = e[1], w = e[2];

            if (dsu.find(x) != dsu.find(y)) {
                dsu.union(x, y);
                mstEdges.add(e);
                if (++count == V - 1) break;
            }
        }

        Map<Character, Map<Character, Integer>> mstMap = new HashMap<>();
        for (Character vertex : vertices) {
            mstMap.computeIfAbsent(vertex, k -> new HashMap<>());
        }

        for (int[] e : mstEdges) {
            Character u = vertices.get(e[0]);
            Character v = vertices.get(e[1]);
            int w = e[2];
            mstMap.get(u).put(v, w);
            mstMap.get(v).put(u, w);
        }

        return mstMap;
    }
}