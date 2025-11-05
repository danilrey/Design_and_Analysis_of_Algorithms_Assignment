package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class KosarajuTest {

    @Test
    public void testSCCsSimple() {
        Graph g = new Graph(3);
        g.addEdge(0,1, 3);
        g.addEdge(1,0, 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(baos));
        try {
            Kosaraju.printSCC(g);
        } finally {
            System.setOut(old);
        }

        String out = baos.toString();
        assertTrue(out.contains("2"));
        assertTrue(out.contains("0") || out.contains("1"));
    }
}

