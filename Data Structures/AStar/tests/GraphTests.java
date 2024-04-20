package AStar.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import AStar.main.Graph;

public class GraphTests extends TestsCore {

    /**
     * Unit test for reading a Graph from a file and verifying
     * the resulting Graph has the same structure as in the input file.
     * @throws FileNotFoundException
     */
    @Test
    public void test_readGraph() throws FileNotFoundException {
        Graph gp = readGraph("/AStar/data/graph1.txt");
        assertEquals(12, gp.size());
        assertSameGraph("/AStar/data/graph1.txt", gp);
    }

    @Test
    public void test_routeDijkstra() throws FileNotFoundException {
        Graph gp = readGraph("/AStar/data/graph1.txt");
        assertNull(gp.routeDijkstra("A", "I"));
        assertEquals("[K, L, G, H, E, A, C]", gp.routeDijkstra("K", "C").toString());        
        assertEquals("[A, B, D, G, K]", gp.routeDijkstra("A", "K").toString());
        gp = readGraph("/AStar/data/graph2.txt");
        assertEquals("[A, B, D, G, K]", gp.routeDijkstra("A", "K").toString());
    }

    @Test
    public void test_routeAStar() throws FileNotFoundException {
        Graph gp = readGraph("/AStar/data/graph1.txt");
        assertEquals("[K, L, G, H, E, A, C]", gp.routeAStar("K", "C").toString());
        assertEquals("[A, B, D, G, K]", gp.routeAStar("A", "K").toString());
        assertNull(gp.routeAStar("A", "I"));
        gp = readGraph("/AStar/data/graph2.txt");
        assertEquals("[A, C, E, G, K]", gp.routeAStar("A", "K").toString());
    }
}
