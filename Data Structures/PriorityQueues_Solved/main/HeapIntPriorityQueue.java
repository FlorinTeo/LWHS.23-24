package PriorityQueues_Solved.main;

import java.util.Arrays;

public class HeapIntPriorityQueue implements IntPriorityQueue {
    private int[] elements;
    private int size;

    public HeapIntPriorityQueue() {
        elements = new int[10];
        size = 0;
    }

    // #region helper methods
    private int parent(int index) {
        return index/2;
    }

    private int leftChild(int index) {
        return 2 * index;
    }

    private int rightChild(int index) {
        return 2 * index + 1;
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
    // #endregion helper methods

    @Override
    public void add(int value) {
        if (size == elements.length - 1) {
            elements = Arrays.copyOf(elements, 2 * elements.length);
        }
        elements[++size] = value;
        int index = size;
        while(hasParent(index)) {
            int parent = parent(index);
            if (elements[index] >= elements[parent]) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public Integer peek() {
        return isEmpty() ? null : elements[1];
    }

    @Override
    public Integer remove() {
        if (isEmpty()) {
            return null;
        }

        int result = elements[1];
        elements[1] = elements[size--];
        int index = 1;
        while(hasLeftChild(index)) {
            int left = leftChild(index);
            int right = rightChild(index);
            int child = hasRightChild(index) && (elements[left] > elements[right]) ? right : left;
            if (elements[index] > elements[child]) {
                swap(index, child);
                index = child;
            } else {
                break;
            }
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }
}
