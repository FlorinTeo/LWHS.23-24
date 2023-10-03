package SimpleList.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("rawtypes")
public class SimpleList implements List {
    
    private class SimpleNode {
        private Object _data;
        private SimpleNode _next;
        
        private SimpleNode(Object data) {
            this(data,  null);
        }
        
        private SimpleNode(Object data, SimpleNode next) {
            _data = data;
            _next = next;
        }
    }
    
    private SimpleNode _head;
    private SimpleNode _tail;
    private int _size;
    
    private SimpleNode nodeAtIndex(int index) {
        if (index < 0 || index >= _size) {
            throw new IndexOutOfBoundsException();
        }
        SimpleNode node = _head;
        while(node != null && index > 0) {
            node = node._next;
            index--;
        }
        return node;
    }
    
    public SimpleList() {
        _head = null;
        _tail = null;
        _size = 0;
    }

    @Override
    public boolean add(Object element) {
        SimpleNode newNode = new SimpleNode(element);
        if (_size == 0) {
            _head = _tail = newNode;
        } else {
            _tail._next = newNode;
            _tail = newNode;
        }
        _size++;
        return true;
    }

    @Override
    public void add(int index, Object element) {
        SimpleNode newNode = new SimpleNode(element);
        if (index == 0) {
            newNode._next = _head;
            _head = newNode;
        } else {
            SimpleNode node = nodeAtIndex(index-1);
            newNode._next = node._next;
            node._next = newNode;
        }
        if (newNode._next == null) {
            _tail = newNode;
        }
        _size++;
    }
    
    @Override
    public void clear() {
        _head = null;
        _tail = null;
        _size = 0;
    }
    
    @Override
    public Object get(int index) {
        SimpleNode node = nodeAtIndex(index);
        return node._data;
    }
    
    @Override
    public Object remove(int index) {
        if (index < 0 || index >= _size) {
            throw new IndexOutOfBoundsException();
        }
        
        SimpleNode delNode = null;
        if (index == 0) {
            delNode = _head;
            if (_head == _tail) {
                _head = _tail = null;
            } else {
                _head = _head._next;
            }
        } else {
            SimpleNode prevNode = nodeAtIndex(index - 1);
            delNode = prevNode._next;
            prevNode._next = delNode._next;
            if (prevNode._next == null) {
                _tail = prevNode;
            }
        }
        _size--;
        return delNode._data;
    }

    @Override
    public Object set(int index, Object element) {
        SimpleNode node = nodeAtIndex(index);
        Object prevElement = node._data;
        node._data = element;
        return prevElement;
    }

    @Override
    public int size() {
        return _size;
    }

    @Override
    public boolean isEmpty() {
        return (_size == 0);
    }

    // Region: Overrides not supported by the SimpleList
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
    // EndRegion: Overrides not supported by the SimpleList
}
