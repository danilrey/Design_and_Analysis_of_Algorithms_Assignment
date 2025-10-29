package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("json/large_graphs.json")) {
            if (is == null)
                throw new IllegalStateException("Resource not found: json/input_example.json");

            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(jsonText);
            JSONArray graphsArray = root.getJSONArray("graphs");

            Prim prim = new Prim();
            KruskalsMST kruskal = new KruskalsMST();

            JSONArray results = new JSONArray();

            for (int i = 0; i < graphsArray.length(); i++) {
                JSONObject g = graphsArray.getJSONObject(i);
                int id = g.getInt("id");

                Map<Character, Map<Character, Integer>> graphMap = new HashMap<>();
                JSONArray nodes = g.getJSONArray("nodes");
                for (int j = 0; j < nodes.length(); j++) {
                    char node = nodes.getString(j).charAt(0);
                    graphMap.put(node, new HashMap<>());
                }

                JSONArray edges = g.getJSONArray("edges");
                for (int j = 0; j < edges.length(); j++) {
                    JSONObject edge = edges.getJSONObject(j);
                    char u = edge.getString("from").charAt(0);
                    char v = edge.getString("to").charAt(0);
                    int w = edge.getInt("weight");
                    graphMap.get(u).put(v, w);
                    graphMap.get(v).put(u, w);
                }

                JSONObject inputStats = new JSONObject();
                inputStats.put("vertices", nodes.length());
                inputStats.put("edges", countUniqueEdges(graphMap));

                long p0 = System.nanoTime();
                Map<Character, Map<Character, Integer>> mstPrim = prim.primMST(graphMap);
                long p1 = System.nanoTime();

                JSONArray primEdges = mstEdgesJson(mstPrim);
                JSONObject primObj = new JSONObject();
                primObj.put("mst_edges", primEdges);
                primObj.put("total_cost", calculateCost(mstPrim));
                primObj.put("operations_count", Prim.getOperationsCount());
                primObj.put("execution_time_ms", format2((p1 - p0) / 1_000_000.0));

                KruskalsMST.comparisonCount = 0;
                DSU.findCount = 0;
                DSU.unionCount = 0;

                long k0 = System.nanoTime();
                Map<Character, Map<Character, Integer>> mstKruskal = kruskal.kruskalsMST(graphMap);
                long k1 = System.nanoTime();

                JSONArray kruskalEdges = mstEdgesJson(mstKruskal);
                JSONObject kruskalObj = new JSONObject();
                kruskalObj.put("mst_edges", kruskalEdges);
                kruskalObj.put("total_cost", calculateCost(mstKruskal));
                kruskalObj.put("operations_count", KruskalsMST.comparisonCount + DSU.findCount + DSU.unionCount);
                kruskalObj.put("execution_time_ms", format2((k1 - k0) / 1_000_000.0));

                JSONObject resultObj = new JSONObject(new LinkedHashMap<>());
                resultObj.put("graph_id", id);
                resultObj.put("input_stats", inputStats);
                resultObj.put("prim", primObj);
                resultObj.put("kruskal", kruskalObj);

                results.put(resultObj);
            }

            JSONObject outRoot = new JSONObject(new LinkedHashMap<>());
            outRoot.put("results", results);

            Path outPath = Path.of("output/result.json");
            Files.createDirectories(outPath.getParent());
            Files.writeString(outPath, outRoot.toString(2), StandardCharsets.UTF_8);

            System.out.println("JSON in: " + outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int countUniqueEdges(Map<Character, Map<Character, Integer>> graph) {
        int count = 0;
        for (Map.Entry<Character, Map<Character, Integer>> entry : graph.entrySet()) {
            char u = entry.getKey();
            for (char v : entry.getValue().keySet()) {
                if (u < v) count++;
            }
        }
        return count;
    }

    private static JSONArray mstEdgesJson(Map<Character, Map<Character, Integer>> mst) {
        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<Character, Map<Character, Integer>> entry : mst.entrySet()) {
            char u = entry.getKey();
            for (Map.Entry<Character, Integer> e : entry.getValue().entrySet()) {
                char v = e.getKey();
                int w = e.getValue();
                if (u < v) edges.add(new Edge(u, v, w));
            }
        }
        edges.sort(Comparator
                .comparingInt((Edge e) -> e.w)
                .thenComparing(e -> e.u)
                .thenComparing(e -> e.v));

        JSONArray arr = new JSONArray();
        for (Edge e : edges) {
            JSONObject obj = new JSONObject(new LinkedHashMap<>());
            obj.put("from", String.valueOf(e.u));
            obj.put("to", String.valueOf(e.v));
            obj.put("weight", e.w);
            arr.put(obj);
        }
        return arr;
    }

    private static int calculateCost(Map<Character, Map<Character, Integer>> mst) {
        int cost = 0;
        Set<String> seen = new HashSet<>();
        for (Map.Entry<Character, Map<Character, Integer>> entry : mst.entrySet()) {
            char u = entry.getKey();
            for (Map.Entry<Character, Integer> e : entry.getValue().entrySet()) {
                char v = e.getKey();
                int w = e.getValue();
                String key = Math.min(u, v) + "-" + Math.max(u, v);
                if (seen.add(key)) cost += w;
            }
        }
        return cost;
    }

    private static double format2(double value) {
        return Double.parseDouble(String.format(Locale.US, "%.2f", value));
    }

    private static class Edge {
        final char u, v;
        final int w;
        Edge(char u, char v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}
