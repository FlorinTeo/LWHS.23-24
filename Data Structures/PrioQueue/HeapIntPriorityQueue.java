package PrioQueue;

import java.util.Arrays;

public class HeapIntPriorityQueue {

    private int[] elements;
    private int size;
    
    // #region: Helper methods
    private int parent(int index) {
        return index/2;
    }
    
    private int leftChild(int index) {
        return index * 2;
    }
    
    private int rightChild(int index) {
        return index * 2 + 1;
    }
    
    private boolean hasParent(int index) {
        return index > 1;
    }
    
    private boolean hasLeftChild(int index) {
        return leftChild(index) <= size;
    }
    
    private boolean hasRightChild(int index) {
        return rightChild(index) <= size;
    }
    
    private void swap(int index1, int index2) {
        int temp = elements[index1];
        elements[index1] = elements[index2];
        elements[index2] = temp;
    }
    
    private void checkResize(int i) {
        if (i >= elements.length) {
            elements = Arrays.copyOf(elements, 2 * elements.length);
        }
    }
    // #endregion: Helper methods
    
    public HeapIntPriorityQueue() {
        elements = new int[2];
        size = 0;
    }
    
    public void add(int value) {
        // add the value to the end of the array and resize as needed
        int i = size + 1;
        checkResize(i);
        elements[i] = value;
        size++;
        
        // bubble that value up until the parent becomes smaller 
        while (hasParent(i)) {
            int iParent = parent(i);
            if (elements[iParent] <= elements[i]) {
                break;
            }
            swap(i, iParent);
            i = iParent;
        }
    }

    public void clear() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Integer peek() {
        return size > 0 ? elements[1] : null;
    }

    public Integer remove() {
        // return null if there's nothing to remove
        if (isEmpty()) {
            return null;
        }
        
        // save the first element, which is the root of the tree, which will be returned
        int elem = elements[1];
        // bring the last element in the queue (lowest, right most leaf) to the root
        elements[1] = elements[size];
        size--;
        // push down the root for as long as either of its children is smaller than itself.
        // the node may have 0, 1 (left) or 2 (left and right) children.
        int i = 1;
        while(hasLeftChild(i)){
            // node has left child, assume that's what the value will be compared against
            int iChild = leftChild(i);
            if (hasRightChild(i) && elements[rightChild(i)] < elements[leftChild(i)]) {
                // however if there is a right child as well, which is even smaller than the left
                // will keep this as the child to compare against.
                iChild = rightChild(i);
            }
            
            // if the child to compare against is already larger than the value, we're done. 
            if (elements[i] < elements[iChild]) {
                break;
            }
            
            // otherwise swap the value with the child's value and keep going.
            swap(i, iChild);
            i = iChild;
        }
        
        // return the element that was saved at the very beginning.
        return elem;
    }

    public int size() {
        return size;
    }
}
