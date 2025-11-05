package org.example;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalDFSTest {

    @Test
    public void testSimpleDAGOrder() {
        Graph g = new Graph(3);
        g.addEdge(0,1, 3);
        g.addEdge(1,2, 2);

        List<Integer> order = TopologicalDFS.topologicalSort(g.adj);
        assertEquals(Arrays.asList(0,1,2), order);
    }

    @Test
    public void testCycleThrows() {
        Graph g = new Graph(3);
        g.addEdge(0,1, 1);
        g.addEdge(1,2, 3);
        g.addEdge(2,0, 4);

        assertThrows(IllegalStateException.class, () -> TopologicalDFS.topologicalSort(g.adj));
    }
}

