package IntTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class IntTree {
    public static int _count;
    
    private IntTreeNode _root;

    public int sumAll() {
        if (_root == null) {
            return 0;
        }
        return _root.sumAll();
    }
    
    public void mystery() {
        if (_root == null) {
            return;
        }
        Queue<IntTreeNode> q = new LinkedList<IntTreeNode>();
        q.add(_root);
        while(!q.isEmpty()) {
            IntTreeNode n = q.remove();
            System.out.print(n.getData() + " ");
            if (n._left != null) {
                q.add(n._left);
            }
            if (n._right != null) {
                q.add(n._right);
            }
        }
    }
    
    public void mystery2() {
        if (_root == null) {
            return;
        }
        Stack<IntTreeNode> s = new Stack<IntTreeNode>();
        s.push(_root);
        while(!s.isEmpty()) {
            IntTreeNode n = s.pop();
            System.out.print(n.getData() + " ");
            if (n._right != null) {
                s.push(n._right);
            }
            if (n._left != null) {
                s.push(n._left);
            }
        }
    }

    public boolean contains(int number) {
        return (_root != null) && (_root.contains(number));
    }

    // Region: constructors and other fields and methods not shown
    public IntTree() {
        _root = null;
    }
    
    public static IntTree createBalanced(Integer... nums) {
        IntTree t = new IntTree();
        
        Queue<Integer> numsQueue = new LinkedList<Integer>();
        numsQueue.addAll(Arrays.asList(nums));
        
        t._root = new IntTreeNode(numsQueue.remove(), null, null);
        Queue<IntTreeNode> nodesQueue = (Queue<IntTreeNode>)new LinkedList<IntTreeNode>();
        nodesQueue.add(t._root);
        
        while(nodesQueue.size() > 0) {
            IntTreeNode node = nodesQueue.remove();
            if (numsQueue.size() > 0) {
                node._left = new IntTreeNode(numsQueue.remove(), null, null);
                nodesQueue.add(node._left);
            }
            if (numsQueue.size() > 0) {
                node._right = new IntTreeNode(numsQueue.remove(), null, null);
                nodesQueue.add(node._right);
            }
        }
        
        return t;
    }

    public static IntTree createSearchable(Integer... nums) {
        IntTree t = new IntTree();
        
        Queue<Integer> numsQueue = new LinkedList<Integer>();
        numsQueue.addAll(Arrays.asList(nums));
        
        t._root = new IntTreeNode(numsQueue.remove(), null, null);
        while(!numsQueue.isEmpty()) {
            t._root.insert(numsQueue.remove());
        }
        
        return t;
    }
    
    @Override
    public String toString() {
        return _root.toString("");
    }
    // EndRegion: constructors and other fields and methods not shown
}
