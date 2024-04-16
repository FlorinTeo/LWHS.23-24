package PriorityQueues_Solved.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import PriorityQueues_Solved.main.HeapIntPriorityQueue;

public class HeapIntPriorityQueueTests {
    @Test
    public void HeapIntPQ_tests() {
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
}
