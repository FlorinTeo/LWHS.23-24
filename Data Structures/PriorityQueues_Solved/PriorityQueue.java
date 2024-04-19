package PriorityQueues_Solved;

public interface PriorityQueue<E extends Comparable<E>> {
    void add(E value);
    void clear();
    boolean isEmpty();
    E peek();
    E remove();
    int size();
}
