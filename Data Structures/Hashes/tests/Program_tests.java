package Hashes.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Hashes.main.Program;

class Program_tests {

    @Test
    public void test_normalize() {
        assertEquals("ABC", Program.normalize("abc"));
        assertEquals("ABC", Program.normalize("abc!"));
        assertEquals("ABC", Program.normalize("~AbC#"));
        assertEquals("ABC", Program.normalize("   ~AbC# "));
        assertEquals("AB-C", Program.normalize("   Ab-C@"));
    }

}
