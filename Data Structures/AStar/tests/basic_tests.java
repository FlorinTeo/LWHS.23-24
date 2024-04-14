package AStar.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.junit.Test;

import AStar.main.Point;
import AStar.main.Graph;

public class basic_tests extends TestsCore {

    @Test
    public void test_backwardsCompat() throws FileNotFoundException {
        Graph<Integer> gi = readGraph("/Graphs/data/basic1.txt", Integer.class);
        assertSameGraph("/Graphs/data/basic1.txt", gi);
        gi.addNode(6);
        gi.addNode(7);
        gi.addEdge(6, 7);
        gi.addEdge("7", "6");
        gi.removeNode(6);
        gi.removeNode("7");
        assertSameGraph("/Graphs/data/basic1.txt", gi);

        Graph<String> gs = readGraph("/Graphs/data/basic2.txt");
        assertSameGraph("/Graphs/data/basic2.txt", gs);
        gs.removeNode("X");
        assertEquals(3, gs.size());

        Graph<Double> gd = new Graph<Double>();
        gd.addNode(3.14);
        gd.addNode(2.71);
        gd.addEdge(3.14, "2.71");
        assertEquals(2, gd.size());
    }

    /**
     * Unit test for reading a Graph from a file and verifying
     * the resulting Graph has the same structure as in the input file.
     * @throws FileNotFoundException
     */
    @Test
    public void test_readGraph() throws FileNotFoundException {
        Graph<Point> gp = readGraph("/AStar/data/demo_graph.txt", Point.class);
        assertEquals(12, gp.size());
        assertTrue(gp.checkState(null));
        assertSameGraph("/AStar/data/demo_graph.txt", gp);
    }

    @Test
    public void test_routeDijkstra() throws FileNotFoundException {
        Graph<Point> gp = readGraph("/AStar/data/demo_graph.txt", Point.class);
        assertEquals("[A, B, D, G, K]", gp.routeDijkstra("A", "K").toString());
        assertNull(gp.routeDijkstra("A", "I"));
        assertEquals("[K, L, G, H, E, A, C]", gp.routeDijkstra("K", "C").toString());
    }
}
