package PriorityQueues_Solved.main;

public interface PriorityQueue<E extends Comparable<E>> {
    void add(E value);
    void clear();
    boolean isEmpty();
    E peek();
    E remove();
    int size();
}
