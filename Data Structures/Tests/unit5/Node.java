package Tests.unit5;
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
     * Checks if the otherNode is a neighbor of this node.
     * @param otherNode - node to be tested.
     * @return true if otherNode is a neighbor of this node, false otherwise.
     */
    public boolean hasNeighbor(Node<T> otherNode) {
        return _edges.get(otherNode._data.hashCode()) != null;
    }
    
    /**
     * Adds a new directed graph Edge linking this Node to the otherNode.
     * @param otherNode - reference to the Node at the other end of the Edge.
     * @see Node#removeEdge(Node)
     */
    public void addEdge(Node<T> otherNode) {
        _edges.put(otherNode._data.hashCode(), otherNode);
    }
    
    /**
     * Removes the directed graph Edge linking this Node to the otherNode.
     * <br><u>Note:</u> The <i>otherNode</i> does not get removed from the Graph, nor does
     * an Edge that may link <i>otherNode</i> (as an origin) and this Node (as a target).
     * @param otherNode - reference to The node at the other end of the Edge.
     * @see Node#addEdge(Node)
     */
    public void removeEdge(Node<T> otherNode) {
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
    
    public int countUndirected() {
        int count = 0;
        for(Node<T> n : _edges.values()) {
            if (n.hasNeighbor(this)) {
                count++;
            }
        }
        return count;
    }
    
    private int _hops;
    public int countHops(Node<T> otherN) {
      if (_state == 1) {
        return _hops;
      }
      if (otherN == this) {
        return 0;
      }
      _state = 1;
      _hops = Integer.MAX_VALUE;
      for (Node<T> n : _edges.values()) {
        int otherH = n.countHops(otherN);
          if (otherH != Integer.MAX_VALUE) {
            _hops = Math.min(_hops, otherH + 1);
          }
      }        
      return _hops;
    }

    public boolean hasPath(Node<T> target) {
        _state = 1;
        for(Node<T> n : _edges.values()) {
            if (n == target || (n._state == 0 && n.hasPath(target))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPathQ(Node<T> target) {
        _state = 1;
        Queue<Node<T>> q = new LinkedList<Node<T>>();
        q.add(this);
        while(!q.isEmpty()) {
            Node<T> n = q.remove();
            if (n == target) {
                return true;
            }
            for (Node<T> neighbor : n._edges.values()) {
                if (neighbor._state == 0) {
                    neighbor._state = 1;
                    q.add(neighbor);
                }
            }
        }
        return false;
    }
}
