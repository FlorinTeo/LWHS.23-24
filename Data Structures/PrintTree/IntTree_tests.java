package PrintTree;

import org.junit.Assert;
import org.junit.Test;

public class IntTree_tests {

    private IntTree newTree(int... values) {
        IntTree tree = new IntTree();
        for(int data : values) {
            tree.addValue(data);
        }
        return tree;
    }

    @Test
    public void test_toPreOrderString() {
        IntTree tree = new IntTree();
        Assert.assertEquals("", tree.toPreOrderString());
        tree = newTree(2);
        Assert.assertEquals("[2]()()", tree.toPreOrderString());
        tree = newTree(2, 1, 3);
        Assert.assertEquals("[2]([1]()())([3]()())", tree.toPreOrderString());
        tree = newTree(7, 5, 9, 4, 12, 6, 10);
        Assert.assertEquals("[7]([5]([4]()())([6]()()))([9]()([12]([10]()())()))", tree.toPreOrderString());
    }

    @Test
    public void test_toInOrderString() {
        IntTree tree = new IntTree();
        Assert.assertEquals("", tree.toInOrderString());
        tree = newTree(2);
        Assert.assertEquals("()[2]()", tree.toInOrderString());
        tree = newTree(2, 1, 3);
        Assert.assertEquals("(()[1]())[2](()[3]())", tree.toInOrderString());
        tree = newTree(7, 5, 9, 4, 12, 6, 10);
        Assert.assertEquals("((()[4]())[5](()[6]()))[7](()[9]((()[10]())[12]()))", tree.toInOrderString());
    }
    
    @Test
    public void test_toPostOrderString() {
        IntTree tree = new IntTree();
        Assert.assertEquals("", tree.toPostOrderString());
        tree = newTree(2);
        Assert.assertEquals("()()[2]", tree.toPostOrderString());
        tree = newTree(2, 1, 3);
        Assert.assertEquals("(()()[1])(()()[3])[2]", tree.toPostOrderString());
        tree = newTree(7, 5, 9, 4, 12, 6, 10);
        Assert.assertEquals("((()()[4])(()()[6])[5])(()((()()[10])()[12])[9])[7]", tree.toPostOrderString());
    }
    
    @Test
    public void test_toPrettyPrint() {
        IntTree tree = newTree(7, 4, 10, 2, 5, 8, 12, 9, 6, 11, 15);
        String expected = 
                  "      ______[7]______                \n"
                + "     /               \\               \n"
                + "   [4]            ___[10]____        \n"
                + "  /   \\          /           \\       \n"
                + "[2]   [5]      [8]           [12]    \n"
                + "         \\        \\         /    \\   \n"
                + "         [6]      [9]    [11]    [15]\n"
                + "                                     \n";
        String output = tree.toPrettyPrint();
        Assert.assertEquals(expected, output);
    }
    
    @Test
    public void test_Output() {
        IntTree tree = newTree(2, 1, 4, 3, 5);
        System.out.println(tree.toPreOrderString());
        System.out.println(tree.toInOrderString());
        System.out.println(tree.toPostOrderString());
        System.out.println(tree.toPrettyPrint());
    }
}
