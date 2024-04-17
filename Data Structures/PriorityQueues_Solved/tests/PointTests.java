package PriorityQueues_Solved.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import PriorityQueues_Solved.main.Point;

public class PointTests {
    @Test
    public void point_tests() {
        Point p = new Point(0, 0);
        assertEquals(0, p.compareTo(Point.ORIGIN));
        assertEquals(0, Point.ORIGIN.compareTo(p));
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 2);
        assertEquals(-1, p1.compareTo(p2));
        assertEquals(1, p2.compareTo(p1));
    }
}
