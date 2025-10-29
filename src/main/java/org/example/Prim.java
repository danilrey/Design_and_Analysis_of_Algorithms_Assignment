package org.example;

import java.util.*;

public class Prim {
    // operation counters (we only count comparisons now)
    public static int comparisonCount = 0;
    public static int keyUpdateCount = 0;
    public static int minKeyCallCount = 0;
    public static int mstSetAssignCount = 0;

    public static void resetCounters() {
        comparisonCount = 0;
        keyUpdateCount = 0;
        minKeyCallCount = 0;
        mstSetAssignCount = 0;
    }

    public static int getOperationsCount() {
        return comparisonCount;
    }

    public int minKey(int[] key, Boolean[] mstSet) {
        minKeyCallCount++;
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int v = 0; v < mstSet.length; v++) {
            comparisonCount++;
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    public Map<Character, Map<Character, Integer>> primMST(Map<Character, Map<Character, Integer>> graph) {
        resetCounters();

        List<Character> vertices = new ArrayList<>(graph.keySet());
        Collections.sort(vertices);
        int V = vertices.size();

        int[][] graphMatrix = new int[V][V];
        for (int i = 0; i < V; i++) {
            Character u = vertices.get(i);
            for (Map.Entry<Character, Integer> entry : graph.getOrDefault(u, new HashMap<>()).entrySet()) {
                Character v = entry.getKey();
                int j = vertices.indexOf(v);
                if (j != -1) {
                    graphMatrix[i][j] = entry.getValue();
                    graphMatrix[j][i] = entry.getValue();
                }
            }
        }

        int[] parent = new int[V];
        int[] key = new int[V];
        Boolean[] mstSet = new Boolean[V];

        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet);
            mstSet[u] = true;

            for (int v = 0; v < V; v++) {
                comparisonCount++;
                if (graphMatrix[u][v] != 0 && !mstSet[v] && graphMatrix[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graphMatrix[u][v];
                }
            }
        }

        Map<Character, Map<Character, Integer>> mstMap = new HashMap<>();
        for (Character vertex : vertices) {
            mstMap.computeIfAbsent(vertex, k -> new HashMap<>());
        }

        for (int i = 1; i < V; i++) {
            int u = parent[i];
            Character us = vertices.get(u);
            Character vs = vertices.get(i);
            int weight = graphMatrix[u][i];
            mstMap.get(us).put(vs, weight);
            mstMap.get(vs).put(us, weight);
        }

        return mstMap;
    }
}
