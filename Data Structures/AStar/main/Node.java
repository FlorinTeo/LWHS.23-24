package AStar.main;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class definition for a generic Node in a Graph.
 * The node contains a collection of references to other nodes
 * representing outgoing (egress) edges in the graph.
 * @param <T> reference type of the node.
 * The type T needs to implement the Comparable interface, such as nodes can
 * be compared to each other.<br>
 * E.g.: Node&lt;Integer&gt; n = new Node&ltInteger&gt(16);
 * @see Node#_data
 * @see Node#_edges
 * @see Node#_state
 * @see Graph
 */
public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
    /**
     * The generic data contained in this Node. The type of the data
     * needs to be a refrence, Comparable type such that Nodes can be
     * compared to each other based on the data they contain.
     */
    private T _data;

    /**
     * Collection of outgoing (egress) Edges originating in this Node.
     * <br>This is a private Map keying each of the neighboring Nodes by their data.
     */
    private Map<T, Node<T>> _edges;
    
    /**
     * State metadata contained in this Node. This can be used as needed
     * either to mark nodes as "visited" or to tag them with a numerical
     * value depending on the algorithm being implemented.
     * @see Node#getState()
     * @see Node#setState()
     */
    private int _state;
    
    /**
     * Constructs a new Node containing the given <i>data</i> object.
     * The new Node is created with _state value 0 and with an empty
     * collection of _edges. The data object needs to be a reference,
     * Comparable type to allow Nodes to compare to each other.
     * E.g: <pre>Node&ltCharacter&gt n = new Node&ltCharacter&gt('X');</pre>
     * @param data - the data object contained in this node.
     * @see Node
     * @see Node#_edges
     * @see Node#_state
     */
    public Node(T data) {
        _data = data;
        _edges = new HashMap<T, Node<T>>();
        _state = 0;
    }
    
    /**
     * Gets the data embedded in this Node.
     * @return reference to an object of the Comparable type T.
     * @see Node#_data
     */
    public T getData() {
        return _data;
    }
    
    /**
     * Gets the state of this Node. The state value is initially set to 0
     * and is intended to be used by various graph algorithms as they are
     * implemented in the Node class.
     * @return the integer value standing for the Node's state.
     * @see Node#_state
     * @see Node#Node(Comparable)
     * @see Node#setState()
     */
    public int getState() {
        return _state;
    }
    
    /**
     * Sets the state of this Node to a given value.
     * @param value - the value to be set into the _state
     * @see Node#_state
     */
    public void setState(int value) {
        // initialize the _unvisited queue with all the nodes from the outgoing edges
        _state = value;
    }
    
    /**
     * Adds a new directed graph Edge linking this Node to the otherNode.
     * @param otherNode - reference to the Node at the other end of the Edge.
     * @see Node#removeEdge(Node)
     */
    public void addEdge(Node<T> otherNode) {
        _edges.put(otherNode._data, otherNode);
    }
    
    /**
     * Removes the directed graph Edge linking this Node to the otherNode.
     * <br><u>Note:</u> The <i>otherNode</i> does not get removed from the Graph, nor does
     * an Edge that may link <i>otherNode</i> (as an origin) and this Node (as a target).
     * @param otherNode - reference to The node at the other end of the Edge.
     * @see Node#addEdge(Node)
     */
    public void removeEdge(Node<T> otherNode) {
        _edges.remove(otherNode.getData());
    }
    
    /**
     * Gives a String representation of this Node as a space-separated sequence of token:
     * The string representation of the <i>_data</i> followed by ' > ' followed by a space
     * separated sequence of tokens, one for each of this Node's neighbors.
     * <br>E.g: If this node is A and is linked to nodes B and C, this method returns:
     * <pre>"A > B C"</pre>
     * If this node is A and has no neighbors (no outogoing / egress Edges), this method returns:
     * <pre>"A > "</pre>
     * @return String reflecting the content and structure of this Node.
     */
    @Override
    public String toString() {
        String output = _data.toString() + " > ";
        boolean first = true;
        for(Node<?> n : _edges.values()) {
            if (!first) {
                output += " ";
            }
            output += n._data.toString();
            first = false;
        }
        return output;
    }

    /**
     * Compares this Node to the other Node. The result of comparing two Nodes is 
     * identical to the result of comparing the <i>_data</i> they contain.
     * @return -1, 0 or 1 depending on how this Node is lesser, equal or greater
     * to the other Node.
     */
    @Override
    public int compareTo(Node<T> other) {
        return _data.compareTo(other._data);
    }

    /**
     * Returns the neighboring nodes of this Node.
     * @return collection of Node<T> neighboring nodes.
     */
    public Collection<Node<T>> getNeighbors() {
        return new LinkedList<Node<T>>(_edges.values());
    }
}
