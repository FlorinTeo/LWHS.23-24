package AStar.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import AStar.main.PriorityQueue;
import AStar.main.HeapArray;
import AStar.main.Point;

public class HeapArrayTests {

    @Test
    public void test_heap() {
        PriorityQueue<Point> pq = new HeapArray<Point>(Point.class);
        pq.add(new Point("Origin", 0, 0));
        pq.add(new Point("A", 1, 1));
        pq.add(new Point("B", -1, -1));
        pq.add(new Point("C", -1, 0));
        assertEquals("Origin", pq.remove().getLabel());
        assertEquals("C", pq.remove().getLabel());
        assertEquals("A", pq.remove().getLabel());
        pq.add(new Point("D", 1, 0));
        assertEquals("D", pq.remove().getLabel());
        assertEquals(1, pq.size());
    }
    
}
