package AStar.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;

import AStar.main.Point;
import AStar.main.Graph;

public class basic_tests extends TestsCore {

    /**
     * Unit test for reading a Graph from a file and verifying
     * the resulting Graph has the same structure as in the input file.
     * @throws FileNotFoundException
     */
    @Test
    public void test_readGraph() throws FileNotFoundException {
        Graph<Point> g = readGraph("/AStar/data/demo_graph.txt", Point.class);
        assertEquals(12, g.size());
        assertTrue(g.checkState(0));
        assertSameGraph("/AStar/data/demo_graph.txt", g);
    }
}
