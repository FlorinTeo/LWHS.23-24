package Hashes.main;
// A HashStringSet object represents a set of strings using a hash table
// as the internal data structure.
// The hash table uses separate chaining (a linked list in each hash bucket
// index) to resolve collisions.

public class HashStringSet {
    private static final double MAX_LOAD = 10000;   // load factor on which to rehash

    private Node[] elementData;
    private int size;

    // Constructs a new empty set of integers.
    public HashStringSet() {
        elementData = (Node[]) new HashStringSet.Node[10];
        size = 0;
    }

    // Constructs a new set of integers.
    public HashStringSet(String... values) {
        this();
        for (String value : values) {
            add(value);
        }
    }

    // Adds the given value to this set,
    // if it was not already contained in the set.
    public void add(String value) {
        // linear probing to find proper index
        if (!contains(value)) {
            int h = hash(value);
            Node newNode = new Node(value);
            newNode.next = elementData[h];
            elementData[h] = newNode;
            size++;
        }

        // resize if necessary
        if (loadFactor() > MAX_LOAD) {
            rehash();
        }
    }

    // Returns true if o refers to another HashIntSet with the same elementData as this set.
    public boolean equals(Object o) {
        return equalsIgnoringSize(o) && size == ((HashStringSet) o).size();
    }

    // Returns true if o refers to another HashIntSet with the same elementData as this set,
    // ignoring the value of the size field.
    public boolean equalsIgnoringSize(Object o) {
        if (o instanceof HashStringSet) {
            HashStringSet other = (HashStringSet) o;
            for (Node front : elementData) {
                Node current = front;
                while (current != null) {
                    if (!other.contains(current.data)) {
                        return false;
                    }
                    current = current.next;
                }
            }
            for (Node front : other.elementData) {
                Node current = front;
                while (current != null) {
                    if (!this.contains(current.data)) {
                        return false;
                    }
                    current = current.next;
                }
            }
            return true;
        } else {
            return false;
        }
    }



    // Returns whether the given value is found in this set.
    public boolean contains(String value) {
        // linear probing to find proper index
        int h = hash(value);
        Node current = elementData[h];
        while (current != null) {
            if (current.data.equals(value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Returns true if there are no elementData in this set.
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the hash table's "load factor", its ratio of size to capacity.
    public double loadFactor() {
        return (double) size / elementData.length;
    }
    
    public double memoryFactor() {
        // memory taken by the *reference* to elementData and size
        long memory = 4 + 4;
        long payload = 0;
        for(Node n : elementData) {
            // each position in the elementData is a reference that needs to be counted.
            memory += 4;
            if (n != null) {
                memory += n.getMemory();
                payload += n.getPayload();
            }
        }
        return (double)payload/memory;
    }

    // Removes the given element value from this set,
    // if it was found in the set.
    public void remove(String value) {
        // linear probing to find proper index
        int h = hash(value);

        if (elementData[h] != null) {
            // front case
            if (elementData[h].data.equals(value)) {
                elementData[h] = elementData[h].next;
                size--;
            } else {
                // non-front case
                Node current = elementData[h];
                while (current.next != null &&
                        current.next.data.equals(value)) {
                    current = current.next;
                }

                // current.next == null
                // || current.next.data == value
                if (current.next != null) {
                    current.next = current.next.next;
                    size--;
                }
            }
        }
    }

    // Returns the number of elementData in this set.
    public int size() {
        return size;
    }
    
    // Returns the number of buckets in this set.
    public int buckets() {
        return elementData.length;
    }

    // Returns a text representation of this set.
    public String toString() {
        String result = "[";
        boolean first = true;
        for (Node node : elementData) {
            Node current = node;
            while (current != null) {
                if (!first) {
                    result += ", ";
                }
                result += current.data;
                first = false;
                current = current.next;
            }
        }
        result += "]";
        return result;
    }

    // Debugging helper that prints the inner hash table.
    public void debug() {
        System.out.println("debug() output:");
        System.out.printf("index   data\n");
        for (int i = 0; i < elementData.length; i++) {
            System.out.printf("%5d   ", i);
            Node node = elementData[i];
            if (node == null) {
                System.out.printf("%6s\n", "null");
            } else {
                boolean first = true;
                while (node != null) {
                    if (!first) {
                        System.out.print("  --> ");
                    }
                    System.out.printf("%8s", node.data);
                    node = node.next;
                    first = false;
                }
                System.out.println();
            }
        }
        System.out.printf("size   %d\n\n", size);
    }

    // hash function for mapping values to indexes
    private int hash(String value) {
        return value.length() % elementData.length;
    }

    // Resizes the hash table to twice its original capacity.
    private void rehash() {
        Node[] newelementData = (Node[]) new HashStringSet.Node[2 * elementData.length];
        Node[] old = elementData;
        elementData = newelementData;
        size = 0;
        for (Node node : old) {
            while (node != null) {
                add(node.data);
                node = node.next;
            }
        }
    }


    private class Node {
        public String data;
        public Node next;

        public Node(String data) {
            this.data = data;
        }
        
        public long getMemory() {
            long memory = 4 + 4 + data.length();
            if (next != null) {
                memory += next.getMemory();
            }
            return memory;
        }
        
        public long getPayload() {
            long payload = data.length();
            if (next != null) {
                payload += next.getPayload();
            }
            return payload;
        }
    }

    public void addAll(HashStringSet otherSet) {
        // iterate through each bucket in the hash
        for (Node node : otherSet.elementData) {
            while (node != null) {
                add(node.data);
                node = node.next;
            }
        }
    }

}