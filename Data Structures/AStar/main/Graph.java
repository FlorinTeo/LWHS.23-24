package AStar.main;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Class definition for a generic (Directed) Graph.
 * The Graph contains a collection of Nodes, each Node contains
 * a collection of references (edges) to their neighboring Nodes.
 * @param <T> - reference type of Nodes contained in the Graph.
 * The type T is expected to implement the Comparable interface, 
 * such that Nodes can be compared to each other.<br>
 * E.g.:<pre>Graph&lt;String&gt; g = new Graph&ltString&gt();</pre>
 * @see Node
 */
public class Graph<T extends Comparable<T>> {

    /**
     * Private Map keying each Node in the Graph by its data label
     * <br>E.g: Given <pre>Node<String> n = new Node<String>("abc");</pre> added to the graph,
     * the _nodes map contains a Map.Entry with
     * <pre>key="abc"<br>value=n</pre>
     */
    private Map<String, Node<T>> _nodes;
    
    /**
     * Constructs a new Graph as an empty container fit for Nodes of the type T.
     * @see Graph
     * @see Node
     */
    public Graph() {
        _nodes = new TreeMap<String, Node<T>>();
    }
    
    /**
     * Gets the size of this Graph. The size of the Graph is equal to the number
     * of Nodes it contains.
     * @return number of Nodes in this Graph.
     */
    public int size() {
        return _nodes.size();
    }
    
    /**
     * Checks if the state of all the Nodes in the Graph matches a given value.
     * @param state - the value to check against all Nodes in the Graph.
     * @return true if all the Nodes in the Graph have a state matching the
     * given value, false otherwise.
     * @see Node#getState()
     */
    public boolean checkState(Object state) {
        for (Node<T> n : _nodes.values()) {
            if (!n.checkState(state)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Set the state within each of the nodes in the graph to the given value.
     * @param state - value to be set in each node's state.
     */
    public void setState(Object state) {
        for (Node<T> n : _nodes.values()) {
            n.setState(state);
        }
    }
    
    /**
     * Adds a new Node to the Graph containing the <i>data</i>. The method throws
     * if the Graph already contains a Node with the same data.
     * @param data - the data reference (of type T) contained in the new Node.
     * @throws RuntimeException if the Graph already contains a Node for the given data.
     * @see Graph#removeNode(Object)
     */
    public void addNode(T data) {
        Node<T> node = new Node<T>(data);
        String label = node.getLabel();
        if (_nodes.containsKey(label)) {
            throw new RuntimeException("Ambiguous graph!");
        }
        _nodes.put(label, node);
    }

    /**
     * Removes the Node identified by a given key. The key can be either a
     * data (instance of T) or the label (string) within the target node. 
     * @param key - node data or label.
     * @return the data in the node being removed, if one exist, or null otherwise.
     * @throws RuntimeException if the Node does not exist in the Graph.
     * @see Graph#addNode(Object)
     */
    public T removeNode(Object key) {
        Node<T> node = _nodes.get(Node.getLabel(key));
        if (node == null) {
            throw new RuntimeException("Node does not exist in graph!");
        }
        for(Node<T> n : _nodes.values()) {
            n.removeEdge(node);
        }
        _nodes.remove(node.getLabel());
        return node.getData();
    }
    
    /**
     * Adds a new directed Edge to the Graph, linking the Nodes identified by
     * <i>fromKey</i> and <i>toKey</i>. The keys can be either data instances or
     * labels (Strings) from within the target nodes. It is expected the two 
     * nodes exist otherwise the method throws an exception.
     * @param fromLabel - Label of the node where the Edge is starting.
     * @param toLabel - Label of the node where the Edge is ending.
     * @throws RuntimeException if either of the two Nodes are not present in the Graph.
     * @see Node
     * @see Graph#removeEdge(Object, Object)
     */
    public void addEdge(Object fromKey, Object toKey) {
        Node<T> fromNode = _nodes.get(Node.getLabel(fromKey));
        Node<T> toNode = _nodes.get(Node.getLabel(toKey));
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        fromNode.addEdge(toNode);
    }
    
    /**
     * Removes an existent directed Edge from the Graph, if one exists. 
     * The Edge to be removed is linking the nodes labeled <i>fromLabel</i> 
     * and <i>toLabel</i>. The two Nodes must exist, otherwise the 
     * method throws an exception.
     * @param fromLabel - Label of the node at the starting point of the Edge.
     * @param toLabel - Label of the node at the ending point of the Edge.
     * @throws RuntimeException if either of the two Nodes are not present in the Graph.
     * @see Node
     * @see Graph#addEdge(Object, Object)
     */
    public void removeEdge(Object fromKey, Object toKey) {
        Node<T> fromNode = _nodes.get(Node.getLabel(fromKey));
        Node<T> toNode = _nodes.get(Node.getLabel(toKey));
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        fromNode.removeEdge(toNode);
    }
    
    /**
     * Gives a multi-line String representation of this Graph. Each line in the returned
     * string represent a Node in the graph, followed by its outgoing (egress) Edges.
     * E.g: If the graph contains 3 nodes, A, B an C, where A and B point to each other and
     * both of them point to C, the value returned by toString() will be as follows:
     * <pre>
     * A > B C
     * B > A C
     * C > 
     * </pre>
     * <u>Note:</u> Each line is a space-separated sequence of token. A Node with no
     * outgoing (egress) edges, like C in the example above still has a line where 
     * the ' > ' token is surrounded by the space characters.
     * @return multi-line String reflecting the content and structure of this Graph.
     */
    @Override
    public String toString() {
        String output = "";
        boolean first = true;
        for(Node<T> n : _nodes.values()) {
            if (!first) {
                output += "\n";
            }
            output += n.toString();
            first = false;
        }
        return output;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<String> routeDijkstra(Object fromKey, Object toKey) {
        // Check nodes exist in the graph, otherwise throw exception
        Node<T> fromNode = _nodes.get(Node.getLabel(fromKey));
        Node<T> toNode = _nodes.get(Node.getLabel(toKey));
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }

        // Reset all Node states to null
        setState(null);

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode);
        Queue<Node<T>> queue = new LinkedList<Node<T>>();
        queue.add(fromNode);

        // We start with a boolean tracking whether we found the route or not (initially false)..
        boolean found = false;
        // .. then we loop until the queue is emptied out.
        while(!queue.isEmpty()) {
            // Remove the first node from the queue.
            Node<T> node = queue.remove();
            // If the node is the target, we're done, mark that we found the route and break out.
            if (node == toNode) {
                found = true;
                break;
            }
            // We're not done, so loop through all the neighbors of node.
            for(Node<T> neighbor : node.getNeighbors()) {
                // If node had already been visited (it's state is not null), just skip it
                if (!neighbor.checkState(null)) {
                    continue;
                }
                // Otherwise mark it with a reference to this node and add it to the queue.
                neighbor.setState(node);
                queue.add(neighbor);
            }
        }

        // if we couldn't find a route, just return null
        if (!found) {
            return null;
        }

        // We found a route, so retrace it from target to start, using the reference from the nodes' state.
        LinkedList<String> result = new LinkedList<String>();
        for(Node<T> crt = toNode; crt != fromNode; crt = (Node<T>)crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());

        // Return the list with all the node labels in the route, from start to target.
        return result;
    }
}
