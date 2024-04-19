package PriorityQueues_Solved;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PriorityQueuesTests {

    @Test
    public void tests_HeapIntPriorityQueue() {
        HeapIntPriorityQueue intPQ = new HeapIntPriorityQueue();
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
    public void test_Point() {
        Point p = new Point(0, 0);
        assertEquals(0, p.compareTo(Point.ORIGIN));
        assertEquals(0, Point.ORIGIN.compareTo(p));
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
    }

    @Test
    public void test_HeapPriorityQueue() {
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
