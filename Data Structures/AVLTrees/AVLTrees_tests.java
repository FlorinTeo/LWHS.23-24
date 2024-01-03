package AVLTrees;

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
        System.out.println(tree.toPrettyPrint());
    }
}
