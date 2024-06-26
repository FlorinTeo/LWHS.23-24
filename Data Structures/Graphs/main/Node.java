package Graphs.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
     * needs to be a reference, Comparable type such that Nodes can be
     * compared to each other based on the data they contain.
     */
    private T _data;

    /**
     * Collection of outgoing (egress) Edges originating in this Node.
     * <br>This is a private Map keying each of the neighboring Nodes by their label.
     */
    private Map<String, Node<T>> _edges;

    /**
     * Collection of incoming (ingress) edges, targeting this node.
     * <br>This is a private Map keying each of the remote (egress) nodes
     * by the data label;
     */
    private Map<String, Node<T>> _inEdges;

    /**
     * Queue of nodes which had not been reached from this node.
     * Used as an internal state tracking for eulerian circuit.
     */
    private Queue<Node<T>> _unvisited;
    
    /**
     * State metadata contained in this Node. This can be used as needed
     * either to mark nodes as "visited" or to tag them with a numerical
     * value depending on the algorithm being implemented.
     * @see Node#getState()
     * @see Node#setState()
     */
    private Object _state;
    
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
        _edges = new HashMap<String, Node<T>>();
        _inEdges = new HashMap<String, Node<T>>();
        _state = null;
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
     * Gets the unique label from the given content object. The label
     * is the substring of content's string, starting at the 0 and ending at
     * the first occurrence of ':'. If no ':' separator exists, the entire
     * string is assumed to be the label.
     * @param content - object from which the label is extracted.
     * @return the label extracted from content.
     */
    public static String getLabel(Object content) {
        return content.toString().split("\\s+:\\s+")[0];
    }

    /**
     * Gets the label (lookup key) associated with this node. The label
     * is expected to be the string preceding the first ':' in the
     * node's data field.
     * @return the label parsed from the node's data.
     */
    public String getLabel() {
        return Node.getLabel(_data.toString());
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
    public Object getState() {
        return _state;
    }

    /**
     * Checks if the state of this Node matches a given one.
     * @param state - state object to check.
     * @return
     */
    public boolean checkState(Object state) {
        return (_state == null) 
            ? (state == null)
            : _state.equals(state);
    }
    
    /**
     * Sets the state of this Node to a given value.
     * @param value - the value to be set into the _state
     * @see Node#_state
     */
    public void setState(Object value) {
        // initialize the _unvisited queue with all the nodes from the outgoing edges
        _unvisited = new LinkedList<Node<T>>();
        _unvisited.addAll(_edges.values());
        _state = value;
    }
    
    /**
     * Adds a new directed graph Edge linking this Node to the otherNode.
     * @param otherNode - reference to the Node at the other end of the Edge.
     * @see Node#removeEdge(Node)
     */
    public void addEdge(Node<T> otherNode) {
        _edges.put(otherNode.getLabel(), otherNode);
        otherNode._inEdges.put(this.getLabel(), this);
    }
    
    /**
     * Removes the directed graph Edge linking this Node to the otherNode.
     * <br><u>Note:</u> The <i>otherNode</i> does not get removed from the Graph, nor does
     * an Edge that may link <i>otherNode</i> (as an origin) and this Node (as a target).
     * @param otherNode - reference to The node at the other end of the Edge.
     * @see Node#addEdge(Node)
     */
    public void removeEdge(Node<T> otherNode) {
        otherNode._inEdges.remove(this.getLabel());
        _edges.remove(otherNode.getLabel());
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
        for(Node<T> n : _edges.values()) {
            if (!first) {
                output += " ";
            }
            output += n.getLabel();
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

    public boolean isUNode() {
        boolean uNode = true;
        for (Node<T> n : _edges.values()) {
            if (n._state == null && n._edges.get(this.getLabel()) != this) {
                uNode = false;
                break;
            }
        }
        _state = 1;
        return uNode;
    }

    public void traverse() {
        _state = 1;
        for (Node<T> n : _edges.values()) {
            if (n.getState() == null) {
                n.traverse();
            }
        }
    }

     /**
     * Expand the marking in any of the children of this node
     * (if any) to this node itself, then propagate it further 
     * in the graph through traversal.
     * @return true of the state of this node has changed.
     */
    public boolean expand() {
        if (_state != null) {
            return false;
        }
        for (Node<T> n : _edges.values()) {
            if (n.getState() != null) {
                traverse();
                return true;
            }
        }
        return false;
    }

    public boolean loops(Node<T> root) {
        _state = 1;
        for (Node<T> n : _edges.values()) {
            if (n == root || (n._state == null && n.loops(root))) {
                return true;
            }
        }
        return false;
    }

    public Queue<Node<T>> topoCheck() {
        Queue<Node<T>> changedNodes = new LinkedList<Node<T>>();
        for(Node<T> n : _edges.values()) {
            if ((int)n._state <= (int)_state) {
                n._state = ((int)_state + 1);
                changedNodes.add(n);
            }
        }
        return changedNodes;
    }

    public void mark(int state) {
        _state = state;
        for (Node<T> n : _edges.values()) {
            if ((int)n.getState() != state) {
                n.mark(state);
            }
        }
    }

    public boolean nextToMark(int state) {
        for (Node<T> n : _edges.values()) {
            if ((int)n.getState() == state) {
                return true;
            }
        }
        return false;
    }

    public void dijkstra(int distance) {
        if ((int)_state <= distance) {
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
            if ((int)node._state == 0) {
                if (node.hasPath(toNode)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Determines if the node verifies the eulerian property:
     * has as many outgoing (egress) as incoming (ingress) edges.
     * @return true if node verifies the eulerian property, false otherwise.
     */
    public boolean isEulerian() {
        return (this._edges.size() == this._inEdges.size());
    }

    /**
     * Determines the path from this node to the target node, if one exists.
     * If it does, returns the array list of all nodes in the path, with the 
     * target node at the last index.
     * @return array list of nodes in the path if one exists, null otherwise.
     */
    public ArrayList<T> getPath(Node<T> targetNode) {
        ArrayList<T> path = null;
        for(int i = _unvisited.size(); i > 0; i--) {
            Node<T> n = _unvisited.remove();
            path = (n == targetNode) ? new ArrayList<T>() : n.getPath(targetNode);
            if (path != null) {
                path.add(0, n._data);
                break;
            }
            _unvisited.add(n);
        }
        return path;
    }

    public boolean hasUnvisited() {
        return !_unvisited.isEmpty();
    }
}
