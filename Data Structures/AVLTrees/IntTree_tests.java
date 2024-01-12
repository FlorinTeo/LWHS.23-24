package AVLTrees;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IntTree_tests {

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
    public void test_bstTree_add() {
        IntTree tree = newTree(1,2,3,4,5,6);
        String expected = 
        "[1]               \n" +
        "   \\              \n" +
        "   [2]            \n" +
        "      \\           \n" +
        "      [3]         \n" +
        "         \\        \n" +
        "         [4]      \n" +
        "            \\     \n" +
        "            [5]   \n" +
        "               \\  \n" +
        "               [6]\n" +
        "                  \n";
        String output = tree.toPrettyPrint();
        System.out.println(output);
        assertEquals(expected, output);
    }

    @Test
    public void test_avlTree_add() {
        IntTree tree = newTreeAVL(1, 2, 3, 4, 5, 6);
        String expected =
        "      ___[4]      \n" +
        "     /      \\     \n" +
        "   [2]      [5]   \n" +
        "  /   \\        \\  \n" +
        "[1]   [3]      [6]\n" +
        "                  \n";
        String output = tree.toPrettyPrint();
        System.out.println(output);
        assertEquals(expected, output);
    }

    @Test
    public void test_bstTree_remove() {
        IntTree tree = newTree(6,4,5,1,3,8,7,10,9,11);
        String expected = 
        "         ___[6]___           \n" +
        "        /         \\          \n" +
        "   ___[4]         [9]        \n" +
        "  /      \\       /   \\       \n" +
        "[1]      [5]   [7]   [10]    \n" +
        "   \\                     \\   \n" +
        "   [3]                   [11]\n" +
        "                             \n";
        System.out.println(tree.toPrettyPrint());
        tree.removeValue(8);
        String output = tree.toPrettyPrint();
        System.out.printf("-8\n%s\n",output);
        assertEquals(expected, output);
    }
}
