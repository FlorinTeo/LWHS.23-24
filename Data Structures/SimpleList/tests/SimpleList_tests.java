package SimpleList.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import SimpleList.main.SimpleList;

class SimpleList_tests {

    @Test
    void test_add() {
        // Create a simple list: [11, 22, 33]
        SimpleList sList = new SimpleList();
        sList.add(11);
        sList.add(22);
        sList.add(33);
        
        // Verify the list has size 3, and each element has the expected value
        assertEquals(3, sList.size());
        assertEquals(11, sList.get(0));
        assertEquals(22, sList.get(1));
        assertEquals(33, sList.get(2));
    }
    
    @Test
    void test_addAtIndex() {
        // Create a simple list
        SimpleList sList = new SimpleList();
        sList.add(0, "abc"); // ["abc"]
        sList.add(1, "def"); // ["abc", "def"]
        sList.add(0, "ghi"); // ["ghi", "abc", "def"]
        
        // List should have 3 elements, and each element has the expected value
        assertEquals(3, sList.size());
        assertEquals("ghi", sList.get(0));
        assertEquals("abc", sList.get(1));
        assertEquals("def", sList.get(2));
    }
    
    @Test
    void test_remove() {
        // Create a simple list ["x", "y", "z"]
        SimpleList sList = new SimpleList();
        sList.add(0, "x");
        sList.add(1, "y");
        sList.add(2, "z");
        
        // Remove element at index 1 -> "y", list remains ["x", "z"]
        String str = (String)sList.remove(1);
        // Verify the removed element is "y"
        // and the list is left with "x" and "z" at expected positions
        assertEquals("y", str);
        assertEquals(2, sList.size());
        assertEquals("x", sList.get(0));
        assertEquals("z", sList.get(1));
        
        // Remove element at index 0 -> "x", list remains ["z"]
        str = (String)sList.remove(0);
        // Verify the removed element is "x"
        // and the list is left with "z" as the only element
        assertEquals(1, sList.size());
        assertEquals("x", str);
        assertEquals("z", sList.get(0));
        
        // Remove the last element -> "z", list remains empty
        str = (String)sList.remove(0);
        assertEquals(0, sList.size());
        assertEquals("z", str);
    }
}
