package PriorityQueues_Solved.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import PriorityQueues_Solved.main.HeapPriorityQueue;
import PriorityQueues_Solved.main.Point;

public class HeapPriorityQueueTests {
    @Test
    public void HeapPQ_IntegerTests() {
        HeapPriorityQueue<Integer> intPQ = new HeapPriorityQueue<Integer>(Integer.class);
        intPQ.add(15);
        assertEquals(15, (int)intPQ.peek());
        intPQ.add(20);
        assertEquals(15, (int)intPQ.peek());
        intPQ.add(10);
        assertEquals(10, (int)intPQ.peek());
        assertEquals(3, intPQ.size());
        assertEquals(10, (int)intPQ.remove());
        assertEquals(15, (int)intPQ.remove());
        assertEquals(20, (int)intPQ.remove());
        assertNull(intPQ.remove());
        assertTrue(intPQ.isEmpty());
    }

    @Test
    public void HeapPQ_PointTests() {
        HeapPriorityQueue<Point> pointPQ = new HeapPriorityQueue<>(Point.class);
        pointPQ.add(new Point(1, 1));
        pointPQ.add(new Point(2, 2));
        pointPQ.add(new Point(3, 3));
        assertEquals("(1, 1)", pointPQ.peek().toString());
        pointPQ.add(new Point(1, 0));
        assertEquals("(1, 0)", pointPQ.peek().toString());
        assertEquals("(1, 0)", pointPQ.remove().toString());
        assertEquals("(1, 1)", pointPQ.remove().toString());
        assertEquals("(2, 2)", pointPQ.remove().toString());
        assertEquals("(3, 3)", pointPQ.peek().toString());
    }
}
