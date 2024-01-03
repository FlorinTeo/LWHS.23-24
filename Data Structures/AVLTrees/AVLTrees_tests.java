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

    @Test
    public void test_toPrettyPrint() {
        IntTree tree = newTree(1,2,3,4,5,6);
        String output = 
        "[1]               \n"+
        "   \\              \n"+
        "   [2]            \n"+
        "      \\           \n"+
        "      [3]         \n"+
        "         \\        \n"+
        "         [4]      \n"+
        "            \\     \n"+
        "            [5]   \n"+
        "               \\  \n"+
        "               [6]\n"+
        "                  \n";
        assertEquals(output, tree.toPrettyPrint());
    }
}
