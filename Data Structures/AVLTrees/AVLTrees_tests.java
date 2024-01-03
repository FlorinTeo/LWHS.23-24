package AVLTrees;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AVLTrees_tests {

    private IntTree newTree(int... values) {
        IntTree tree = new IntTree();
        for(int data : values) {
            tree.addValue(data);
        }
        return tree;
    }

    private IntTree newTreeAVL(int... values) {
        IntTree tree = new IntTree();
        for(int data : values) {
            tree.addValueAVL(data);
        }
        return tree;
    }

    @Test
    public void test_addValue() {
        IntTree tree = newTree(1,2,3,4);
        String expected = 
            "[1]         \n" +
            "   \\        \n" +
            "   [2]      \n" +
            "      \\     \n" +
            "      [3]   \n" +
            "         \\  \n" +
            "         [4]\n" +
            "            \n";
        assertEquals(expected, tree.toPrettyPrint());
    }

    @Test
    public void test_addValueAVL() {
        IntTree tree = newTreeAVL(1, 2, 3, 4, 5, 6);
        String expected =
        "      ___[4]      \n" +
        "     /      \\     \n" +
        "   [2]      [5]   \n" +
        "  /   \\        \\  \n" +
        "[1]   [3]      [6]\n" +
        "                  \n";        
        assertEquals(expected, tree.toPrettyPrint());
    }
}
