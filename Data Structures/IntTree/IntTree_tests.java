package IntTree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntTree_tests {
    @Test
    public void testContains() {
        IntTree t = IntTree.createSearchable(9,7,4,1,5,8,16,12,11,14,15,18,17);
        assertTrue(t.contains(9));
        assertTrue(t.contains(5));
        assertTrue(t.contains(12));
        assertTrue(t.contains(18));
        assertFalse(t.contains(10));
        assertFalse(t.contains(13));
        t._count = 0;
        assertTrue(t.contains(14));
        assertEquals(10, t._count);
        t._count = 0;
        assertFalse(t.contains(28));
        assertEquals(13, t._count);
    }
}
