package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.CondensationKosaraju.buildCondensation;
import static org.junit.jupiter.api.Assertions.*;

public class CondensationTest {

    @Test
    public void testBuildCondensationSimple() {
        // Graph with two SCCs: {0,1} and {2,3,4}, and an edge from SCC{0,1} -> SCC{2,3,4}
        Graph g = new Graph(5);
        g.addEdge(0,1,1);
        g.addEdge(1,0,1);

        g.addEdge(2,3,1);
        g.addEdge(3,4,1);
        g.addEdge(4,2,1);

        g.addEdge(1,2,1);

        CondensationKosaraju.CondensationResult res = buildCondensation(g);
        assertNotNull(res);
        assertEquals(2, res.componentsCount);

        int compOf0 = res.compId[0];
        int compOf2 = res.compId[2];
        assertNotEquals(compOf0, compOf2);

        List<List<Integer>> comps = res.components;
        Set<Integer> setA = new HashSet<>(comps.get(compOf0));
        Set<Integer> setB = new HashSet<>(comps.get(compOf2));

        assertTrue(setA.contains(0) && setA.contains(1));
        assertEquals(2, setA.size());

        assertTrue(setB.contains(2) && setB.contains(3) && setB.contains(4));
        assertEquals(3, setB.size());

        Graph cond = res.condGraph;
        boolean found = false;
        for (int u = 0; u < cond.V; u++) {
            for (Integer v : cond.adj[u]) {
                if (u == compOf0 && v == compOf2) found = true;
            }
        }
        assertTrue(found, "Condensation should have an edge from compOf0 to compOf2");
    }
}

