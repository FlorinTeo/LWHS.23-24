package AVLTrees;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class IntTreeNode {
    private int data;           // data stored at this node
    private IntTreeNode left;   // reference to left subtree
    private IntTreeNode right;  // reference to right subtree
    private int height;         // AVL balance factor: height(right) - height(left)
    
    /**
     * Constructs a node with no (null) left/right subtrees,
     * and containing {data} in its data field.
     * @param data - value stored in the node's data field.
     */
    public IntTreeNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    /**
     * Adds data to the binary search tree in a simplistic way: data smaller than the one in the node is
     * added to the left subtree, larger than the one in the node is added to the right subtree.
     * @param data - data value to be added.
     */
    public void addValue(int data) {
        if (data <= this.data) {
           if (left == null) {
               left = new IntTreeNode(data);
           } else {
               left.addValue(data);
           }
        } else {
            if (right == null) {
                right = new IntTreeNode(data);
            } else {
                right.addValue(data);
            }
        }
        // update height
        refreshHeight();
    }

    /**
     * Removes data from the binary search tree in a simplistic way: data smaller than the one in the node is
     * removed from the left subtree, larger than the one in the node is removed from the right subtree.
     * If node matches the data, it is removed from the tree and the root of the new tree is returned.
     * @param data - data value to be added.
     * @return root of the new tree.
     */
    public IntTreeNode removeValue(int data) {
        IntTreeNode newRoot = this;
        if (data < this.data) {
            if (left != null) {
                left = left.removeValue(data);
            }
        } else if (data > this.data) {
            if (right != null) {
                right = right.removeValue(data);
            }
        } else {
            // the node to be removed is *this*
            if (left == null || right == null) {
                // the trivial case: the node to be removed has only one subtree
                // so that subtree, whichever it is, replaces the current tree.
                newRoot = (left == null) ? right : left;
            } else {
                // the node to be removed has two children!
                if (right.left == null) {
                    // the smallest node greater than *this* one IS the immediate right child
                    right.left = left;
                    newRoot = right;
                } else {
                    // the smallest node greater than *this* is NOT the immediate right child
                    newRoot = right.removeSmallestNode();
                    newRoot.left = left;
                    newRoot.right = right;
                }
            }
        }
        // refresh the height for the new root before returning it
        newRoot.refreshHeight();
        return newRoot;
    }

    /**
     * Removes and returns the smallest node from the tree with the root in this node.
     * Precondition: *this* node has valid (non-null) left and right subtrees.
     * This means the smallest node is somewhere in the left subtree which also means
     * the root of the resulting tree (with the node removed) does not change.
     * @return - the node that had been removed.
     */
    private IntTreeNode removeSmallestNode() {
        IntTreeNode removedNode = null;
        if (left.left == null) {
            // the immediate left child is the smallest node!
            removedNode = left;
            left = null;
        } else {
            // the immediate left child is not the smallest, so we'll recursively remove on the left.
            removedNode = left.removeSmallestNode();
        }
        refreshHeight();
        return removedNode;
    }

    /**
     * Refreshes the height of the tree with this node as root.
     */
    public void refreshHeight() {
        height = Math.max((right != null ? right.height : 0), (left != null ? left.height : 0)) + 1;
    }
    
    /**
     * Returns the balance factor (height of the right subtree minus height of the left subtree) of this node.
     * @return
     */
    public int getBF() {
        return (right != null ? right.height : 0) - (left != null ? left.height : 0);
    }

    /**
     * Adds data to the binary search tree just like in the simplistic case, but it includes
     * an AVL rebalancing of the tree on the backtracking path of the recursion.
     * @param data - data value to be added.
     * @return the rebalanced tree after the data had been inserted in the node.
     */
    public IntTreeNode addValueAVL(int data) {
        if (data <= this.data) {
           if (left == null) {
               left = new IntTreeNode(data);
           } else {
               left = left.addValueAVL(data);
           }
        } else {
            if (right == null) {
                right = new IntTreeNode(data);
            } else {
                right = right.addValueAVL(data);
            }
        }
        // update height
        refreshHeight();
        return rebalance();
    }

    /**
     * Rebalances the subtree under this node and returns the new root of the balanced tree.
     * @return new root of the reorganized tree originally under this node.
     */
    public IntTreeNode rebalance() {
        int bf = getBF();
        if (bf == -2) {
            // left heavy
            if (left.getBF() < 0) {
                // left-left heavy
                IntTreeNode a = this;
                IntTreeNode b = a.left;
                IntTreeNode c = a.right;
                IntTreeNode d = b.left;
                IntTreeNode e = b.right;
                a.left = e;
                a.right = c;
                a.refreshHeight();
                b.left = d;
                b.right = a;
                b.refreshHeight();
                return b;
            } else {
                // left-right heavy
                IntTreeNode a = this;
                IntTreeNode b = a.left;
                IntTreeNode c = a.right;
                IntTreeNode d = b.left;
                IntTreeNode e = b.right;
                IntTreeNode f = e.left;
                IntTreeNode g = e.right;
                b.left = d;
                b.right = f;
                b.refreshHeight();
                a.left = g;
                a.right = c;
                a.refreshHeight();
                e.left = b;
                e.right = a;
                e.refreshHeight();
                return e;
            }
        } else if (bf == 2) {
            // right heavy
            if (right.getBF() > 0) {
                // right-right heavy
                IntTreeNode a = this;
                IntTreeNode b = a.left;
                IntTreeNode c = a.right;
                IntTreeNode d = c.left;
                IntTreeNode e = c.right;
                a.left = b;
                a.right = d;
                a.refreshHeight();
                c.left = a;
                c.right = e;
                c.refreshHeight();
                return c;
            } else {
                // right-left heavy
                IntTreeNode a = this;
                IntTreeNode b = a.left;
                IntTreeNode c = a.right;
                IntTreeNode d = c.left;
                IntTreeNode e = c.right;
                IntTreeNode f = d.left;
                IntTreeNode g = d.right;
                a.left = b;
                a.right = f;
                a.refreshHeight();
                c.left = g;
                c.right = e;
                c.refreshHeight();
                d.left = a;
                d.right = c;
                d.refreshHeight();
                return d;
            }
        } else {
            // tree is balanced
            return this;
        }
    } 

    //#region - PrettyPrint methods
    /**
     * Returns a string containing a character repeated a given number of times.
     * @param ch - character to be repeated.
     * @param count - number of times to repeat the character. 
     * @return string containing {ch} repeated {count} number of times.
     */
    public static String newString(char ch, int count) {
        char[] chArr = new char[count];
        Arrays.fill(chArr, ch);
        return new String(chArr);
    }
    
    /**
     * Combines all the lines from two queues adding them to the end of the merged queue.
     * The combined line contains the line from the left queue, followed by an empty string
     * of the same length as label, followed by the line from the right queue. For example:
     * If the top line from qLeft is "    [12]  " (length 10), the label is "[18]" (4 characters)
     * and the top line from qRight is "  [23]   " (length 9) then the combined line is
     * "    [12]        [23]   " (length 10 + 4 + 9).
     * If the content of any of the two queues is finished ahead of the other, the remaining combined
     * lines contain strings of spaces of the same length as the initial ones in the same queue.
     * @param qLeft - queue with all pretty-printing lines of the left subtree.
     * @param label - label of this node.
     * @param qRight - queue with all pretty-printing lines of the right subtree.
     * @param qMerged - queue with all pretty-printing lines of this subtree.
     */
    public static void mergeQueues(Queue<String> qLeft, String label, Queue<String> qRight, Queue<String> qMerged) {
        int len1 = qLeft.isEmpty() ? 0 : qLeft.peek().length();
        int len2 = qRight.isEmpty() ? 0 : qRight.peek().length();
        String spaces = newString(' ', label.length());
        while(!qLeft.isEmpty() && !qRight.isEmpty()) {
            qMerged.add(qLeft.remove() + spaces + qRight.remove());
        }
        while(!qLeft.isEmpty()) {
            qMerged.add(qLeft.remove() + spaces + newString(' ', len2));
        }
        while(!qRight.isEmpty()) {
            qMerged.add(newString(' ', len1) + spaces + qRight.remove());
        }
    }
    
    /**
     * Generates the top lines of the pretty print of this node, from the top lines
     * of its left and right children. The resulting lines are added to a queue which
     * is returned to the caller. For example:
     * sLeft: "    [12]  " (length 10, with ']' at index 7)
     * label: "[18]" (length 4)
     * sRight:"  [23]   " (length 9, with '[' at index 2)
     * then the method returns a new queue containing exactly two strings:
     * ["        __[18]__       ", (length 10+4+9)
     *  "       /        \\      " (length 10+4+9, with '/' at index 7
     *                                             and '\\' at index 10+4+2)
     * ]
     * @param sLeft - left string; can be "" or contain "[number]"
     * @param label - middle label
     * @param sRight - right string; can be "" or contain "[number]"
     * @return a new Queue<String> containing two lines combined from left and right.
     */
    public static Queue<String> topLines(String sLeft, String label, String sRight) {
        int iLeft = sLeft.indexOf(']');
        int iRight = sRight.indexOf('[');
        String line1 = "";
        String line2 = "";
        if (iLeft >= 0) {
            line1 += newString(' ', iLeft) + " " + newString('_', sLeft.length() - iLeft - 1);
            line2 += newString(' ', iLeft) + "/" + newString(' ', sLeft.length() - iLeft - 1);
        }
        line1 += label;
        line2 += newString(' ', label.length());
        if (iRight >= 0) {
            line1 += newString('_', iRight) + " " + newString(' ', sRight.length() - iRight - 1);
            line2 += newString(' ', iRight) + "\\" + newString(' ', sRight.length() - iRight - 1);
        }

        Queue<String> queue = new LinkedList<String>();
        queue.add(line1);
        queue.add(line2);
        return queue;
    }
    
    public static Queue<String> initializeQueue(Queue<String> qLeft, String label, Queue<String>qRight) {
        String sLeft = qLeft.isEmpty() ? "" : qLeft.peek();
        String sRight = qRight.isEmpty() ? "" : qRight.peek();       
        return topLines(sLeft, label, sRight);
    }
    
    /**
     * Constructs all lines for the pretty-print of this node and
     * add then to a queue. First line in the queue is the line 
     * corresponding to the tree with the root in this node.
     * @return - the queue with all pretty-print lines for this node.
     */
    public Queue<String> toPrettyPrint() {
        String nodeLabel = toString();
        Queue<String> qLeft = (left != null) ? left.toPrettyPrint() : new LinkedList<String>();
        Queue<String> qRight = (right != null) ? right.toPrettyPrint() : new LinkedList<String>();
        Queue<String> qOutput = initializeQueue(qLeft, nodeLabel, qRight);
        mergeQueues(qLeft, nodeLabel, qRight, qOutput);
        return qOutput;
    }
    //#endregion
    
    public String toString() {
        return String.format("[%d]", data);
    }
}
