package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathTest {

    @Test
    public void testShortestPathSimple() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 5);
        g.addEdge(1, 2, 1);

        int[] expected = new int[] {0, 2, 3};
        int[] dist = ShortestPath.shortestPathDAG(g, 0);
        assertArrayEquals(expected, dist);
    }

    @Test
    public void testLongestPathSimple() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 5);
        g.addEdge(1, 2, 1);

        int[] expected = new int[] {0, 2, 5};
        int[] lp = ShortestPath.longestPathDAG(g, 0);
        assertArrayEquals(expected, lp);
    }

    @Test
    public void testSourceOutOfRangeReturnsINFArray() {
        Graph g = new Graph(2);
        g.addEdge(0, 1, 1);
        int[] d = ShortestPath.shortestPathDAG(g, -1);

        int[] expected = new int[] { ShortestPath.INF, ShortestPath.INF };
        assertArrayEquals(expected, d);
    }
}
