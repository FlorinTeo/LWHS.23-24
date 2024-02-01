package Hashes.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Hashes.main.HashStringSet;

public class HashStringSet_tests {

    private HashStringSet _hss;
    
    @Before
    public void test_setup() {
        _hss = new HashStringSet();
        _hss.add("abc");
        _hss.add("def");
    }
    
    @Test
    public void test_add() {
        assertEquals("[def, abc]", _hss.toString());
        _hss.add("xyz");
        assertEquals("[xyz, def, abc]", _hss.toString());
        _hss.add("def");
        assertEquals("[xyz, def, abc]", _hss.toString());
    }
    
    @Test
    public void test_size() {
        assertEquals(2, _hss.size());
        _hss.add("xyz");
        assertEquals(3, _hss.size());
        _hss.add("def");
        assertEquals(3, _hss.size());
    }
    
    @Test
    public void test_buckets() {
        assertEquals(10, _hss.buckets());
    }
    
    @Test
    public void test_loadFactor() {
        assertEquals(0.2, _hss.loadFactor(), 0.0);
        _hss.add("xyz");
        assertEquals(0.3, _hss.loadFactor(), 0.0);
    }
    
    @Test
    public void test_memoryFactor() {
        // payload = 6
        // memory = 4 + 4 + 4*10 + (4 + 4 + 3) + (4 + 4 + 3) = 70
        assertEquals(6.0/70, _hss.memoryFactor(), 0.0);
    }

}
