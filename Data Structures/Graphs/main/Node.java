package Graphs.main;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Class definition for a generic Node in a Graph.
 * The node contains a collection of references to other nodes
 * representing outgoing (egress) edges in the graph.
 * @param <T> - reference type of the node.
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
     * Collection of outgoing (egress) Edges originating in this Node.
     * <br>This is a private Map keying each of the neighboring Nodes by 
     * the hashCode() of their data.
     * @see java.lang.Object#hashCode()
     */
    private Map<Integer, Node<T>> _edges;

    /**
     * Collection of incoming (ingress) edges, targeting this node.
     * <br>This is a private Map keying each of the remote (egress) nodes
     * by the hashCode() of their data;
     */
    private Map<Integer, Node<T>> _inEdges;
    
    /**
     * The generic data contained in this Node. The type of the data
     * needs to be a refrence, Comparable type such that Nodes can be
     * compared to each other based on the data they contain.
     */
    private T _data;
    
    /**
     * State metadata contained in this Node. This can be used as needed
     * either to mark nodes as "visited" or to tag them with a numerical
     * value depending on the algorithm being implemented.
     * @see Node#getState()
     * @see Node#reset()
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
        _edges = new HashMap<Integer, Node<T>>();
        _inEdges = new HashMap<Integer, Node<T>>();
        _state = 0;
    }
    
    /**
     * Gets the data embedded in this Node.
     * @return - reference to an object of the Comparable type T.
     * @see Node#_data
     */
    public T getData() {
        return _data;
    }
    
    /**
     * Gets the state of this Node. The state value is initially set to 0
     * and is intended to be used by various graph algorithms as they are
     * implemented in the Node class.
     * @return - the integer value standing for the Node's state.
     * @see Node#_state
     * @see Node#Node(Comparable)
     * @see Node#reset()
     */
    public int getState() {
        return _state;
    }
    
    /**
     * Resets the state of this Node to its initial value (0).
     * @see Node#_state
     * @see Node#Node(Comparable)
     * @see Node#reset()
     */
    public void reset() {
        _state = 0;
    }
    
    /**
     * Resets the state of this Node to a given value.
     * @param value - the value to be set into the _state
     * @see Node#_state
     */
    public void reset(int value) {
        _state = value;
    }
    
    /**
     * Adds a new directed graph Edge linking this Node to the otherNode.
     * @param otherNode - reference to the Node at the other end of the Edge.
     * @see Node#removeEdge(Node)
     */
    public void addEdge(Node<T> otherNode) {
        _edges.put(otherNode._data.hashCode(), otherNode);
        otherNode._inEdges.put(this._data.hashCode(), this);
    }
    
    /**
     * Removes the directed graph Edge linking this Node to the otherNode.
     * <br><u>Note:</u> The <i>otherNode</i> does not get removed from the Graph, nor does
     * an Edge that may link <i>otherNode</i> (as an origin) and this Node (as a target).
     * @param otherNode - reference to The node at the other end of the Edge.
     * @see Node#addEdge(Node)
     */
    public void removeEdge(Node<T> otherNode) {
        otherNode._inEdges.remove(this._data.hashCode());
        _edges.remove(otherNode.getData().hashCode());
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

    public boolean isUNode() {
        boolean uNode = true;
        for (Node<?> n : _edges.values()) {
            if (n._state != 1 && n._edges.get(_data.hashCode()) != this) {
                uNode = false;
                break;
            }
        }
        _state = 1;
        return uNode;
    }
    
    public void traverse() {
        _state = 1;
        for (Node<?> n : _edges.values()) {
            if (n.getState() == 0) {
                n.traverse();
            }
        }
    }

    public void mark(int state) {
        _state = state;
        for (Node<T> n : _edges.values()) {
            if (n.getState() != state) {
                n.mark(state);
            }
        }
    }

    public boolean nextToMark(int state) {
        for (Node<T> n : _edges.values()) {
            if (n.getState() == state) {
                return true;
            }
        }
        return false;
    }

    /**
     * Expand the marking in any of the children of this node
     * (if any) to this node itself, then propagate it further 
     * in the graph through traversal.
     * @return true of the state of this node has changed.
     */
    public boolean expand() {
        if (_state == 1) {
            return false;
        }
        for (Node<?> n : _edges.values()) {
            if (n.getState() == 1) {
                traverse();
                return true;
            }
        }
        return false;
    }
    
    public boolean loops(Node<T> root) {
        _state = 1;
        for (Node<T> n : _edges.values()) {
            if (n == root || (n._state == 0 && n.loops(root))) {
                return true;
            }
        }
        return false;
    }
        
    public Collection<Node<T>> getNeighbors() {
        return _edges.values();
    }
    
    public Queue<Node<T>> topoCheck() {
        Queue<Node<T>> changedNodes = new LinkedList<Node<T>>();
        for(Node<T> n : _edges.values()) {
            if (n._state <= _state) {
                n._state = (_state + 1);
                changedNodes.add(n);
            }
        }
        return changedNodes;
    }
    
    public void dijkstra(int distance) {
        if (_state <= distance) {
            return;
        }
        _state = distance;
        for(Node<T> neighbor : _edges.values()) {
            neighbor.dijkstra(distance+1);
        }
    }
    
    public boolean hasPath(Node<T> toNode) {
        if (this == toNode) {
            return true;
        }
        _state = 1;
        for(Node<T> node : _edges.values()) {
            if (node._state == 0) {
                if (node.hasPath(toNode)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
