package PriorityQueues_Solved.main;

import java.lang.reflect.Array;
import java.util.Arrays;

public class HeapPriorityQueue<E extends Comparable<E>> implements PriorityQueue<E> {
    private E[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public HeapPriorityQueue(Class<E> realType) {
        elements = (E[])Array.newInstance(realType, 10);
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
        E temp = elements[index1];
        elements[index1] = elements[index2];
        elements[index2] = temp;
    }
    // #endregion helper methods

    @Override
    public void add(E value) {
        if (size == elements.length - 1) {
            elements = Arrays.copyOf(elements, 2 * elements.length);
        }
        elements[++size] = value;
        int index = size;
        while(hasParent(index)) {
            int parent = parent(index);
            if (elements[index].compareTo(elements[parent]) >= 0) {
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
    public E peek() {
        return isEmpty() ? null : elements[1];
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            return null;
        }

        E result = elements[1];
        elements[1] = elements[size--];
        int index = 1;
        while(hasLeftChild(index)) {
            int left = leftChild(index);
            int right = rightChild(index);
            int child = hasRightChild(index) && (elements[left].compareTo(elements[right]) > 0) ? right : left;
            if (elements[index].compareTo(elements[child]) > 0) {
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
