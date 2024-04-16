package PriorityQueues_Solved.main;

public class HeapTree<E extends Comparable<E>> implements PriorityQueue<E>{

    /**
     * A node in the heap tree has links to parent, left and
     * right child as well as to the previous and next nodes in the 
     * linear (level by level) ordering.
     */
    private class HeapNode<T extends Comparable<T>> {
        private T _data;
        private HeapNode<T> _parent, _left, _right, _next, _prev;

        private HeapNode(T data) {
            _data = data;
            _parent = _left = _right = _next = _prev = null;
        }
    }
    
    // Pointers to the root and last nodes in the heap tree.
    private HeapNode<E> _root, _last;
    // Number of nodes in the tree.
    private int _size;
    
    /**
     * Swaps the data between the two given nodes.
     */
    private void swapData(HeapNode<E> node1, HeapNode<E> node2) {
        E temp = node1._data;
        node1._data = node2._data;
        node2._data = temp;
    }
    
    /**
     * Removes the last node in the heap tree and adjusts all links.
     */
    private void removeLast() {
        _last._prev._next = null;
        if (_last._parent._right != null) {
            _last._parent._right = null;
        } else {
            _last._parent._left = null;
        }
        _last = _last._prev;
    }
    
    /**
     * Adds a new last node to the heap tree and adjusts all links.
     */
    private void addLast(HeapNode<E> node) {
        _last._next = node;
        node._prev = _last;
        if (_last._parent == null) {
            _last._left = node;
            node._parent = _last;
        } else if (_last._parent._right == null) {
            _last._parent._right = node;
            node._parent = _last._parent;
        } else {
            _last._parent._next._left = node;
            node._parent = _last._parent._next;
        }
        _last = node;
    }
    
    /**
     * Returns the node containing the minimum data value from the given three nodes.
     */
    private HeapNode<E> minOfThree(HeapNode<E> n1, HeapNode<E> n2, HeapNode<E> n3) {
        HeapNode<E> minNode = n1;
        if (n2 != null && minNode._data.compareTo(n2._data) > 0) {
            minNode = n2;
        }
        if (n3 != null && minNode._data.compareTo(n3._data) > 0) {
            minNode = n3;
        }
        return minNode;
    }
    
    public HeapTree() {
        _root = _last = null;
        _size = 0;
    }
    
    @Override
    public void add(E value) {
        HeapNode<E> node = new HeapNode<E>(value);
        if (_root == null) {
            _root = node;
            _last = _root;
        } else {
            addLast(node);
            while(node._parent != null && node._parent._data.compareTo(node._data) > 0) {
                swapData(node, node._parent);
                node = node._parent;
            }
        }
        _size++;
    }

    @Override
    public void clear() {
        _root = null;
        _size = 0;
    }

    @Override
    public boolean isEmpty() {
        return _size == 0;
    }

    @Override
    public E peek() {
        return (_root != null) ? _root._data : null;
    }

    @Override
    public E remove() {
        E data = (_root != null) ? _root._data : null;
        if (_root == _last) {
            clear();
        } else {
            swapData(_root, _last);
            removeLast();
            HeapNode<E> node = _root;
            HeapNode<E> minNode = minOfThree(node, node._left, node._right);
            while(node != minNode) {
                swapData(node, minNode);
                node = minNode;
                minNode = minOfThree(node, node._left, node._right);
            }
            _size--;
        }
        return data;
    }

    @Override
    public int size() {
        return _size;
    }
}
