package SimpleList.main;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings({"rawtypes", "unused"})
public class SimpleList implements List {
    
    /**
     * SimpleNode definition of a single-linked list of Objects 
     */
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
    
    /**
     * TODO: Class fields: Keep track of the head and tail of the list
     * TODO: and the number of nodes it contains.
     */
    public SimpleList() {
        // TODO: Initialize class fields
    }

    /**
     * Appends the specified element to the end of this list (optional operation).
     * @param element - element to be appended to this list.
     * @return true
     */
    @Override
    public boolean add(Object element) {
        // TODO: Implement interface method add
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Inserts the specified element at the specified position in this list.
     * @param index - index at which the specified element is to be inserted.
     * @param element - element to be inserted.
     */
    @Override
    public void add(int index, Object element) {
        // TODO: Implement interface method add
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Removes all of the elements from this list (optional operation).
     */
    @Override
    public void clear() {
        // TODO: Implement interface method clear
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Returns the element at the specified position in this list.
     * @param index - index of the element to return.
     * @return the element at the specified position in this list.
     */
    @Override
    public Object get(int index) {
        // TODO: Implement interface method get
        throw new RuntimeException("Not yet implemented");
    }
    
    /**
     * Removes the element at the specified position in this list.
     * @param index - the index of the element to be removed.
     * @return the element previously at the specified position.
     */
    @Override
    public Object remove(int index) {
        // TODO: Implement interface method remove
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     * @param index - index of the element to replace.
     * @param element - element to be stored at the specified position.
     * @return the element previously at the specified position.
     */
    @Override
    public Object set(int index, Object element) {
        // TODO: Implement interface method set
        throw new RuntimeException("Not yet implemented");
    }

    /**
     * Returns the number of elements in this list.
     * @return the number of elements in this list.
     */
    @Override
    public int size() {
        // TODO: Implement interface method size
        throw new RuntimeException("Not yet implemented");
    }

    @Override
    public boolean isEmpty() {
        // TODO: Implement interface method isEmpty
        throw new RuntimeException("Not yet implemented");
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
