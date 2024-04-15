package Graphs.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;

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
     * Checks if the Graph is undirected.
     * @return true if Graph is undirected, false otherwise.
     */
    public boolean isUGraph() {
        boolean uGraph = true;
        for(Node<T> node : _nodes.values()) {
            if (!node.isUNode()) {
                uGraph = false;
                break;
            }
        }
        setState(null);
        return uGraph;
    }

    /**
     * Checks is the Graph is connected.
     * @return true if the Graph is connected, false otherwise.
     */
    public boolean isConnected() {
        boolean connected = true;
        Iterator<Node<T>> iNodes = _nodes.values().iterator();
        while(connected && iNodes.hasNext()) {
            Node<T> node = iNodes.next();
            // traverse the grap starting from node
            node.traverse();
            
            // expand the visited nodes based on proximity
            // to other visited nodes. Stop when no expansion happened.
            boolean again = true;
            while (again) {
                again = false;
                for (Node<?> n : _nodes.values()) {
                    again = again || n.expand();
                }
            }
            
            // verify if any node was left not visited
            for (Node<?> n : _nodes.values()) {
                if (n.getState() == null) {
                    connected = false;
                    break;
                }
            }
        }
        
        setState(null);
        return connected;
    }

    /**
     * Checks if the Graph is Directed Acyclic graph.
     * @return true if Graph is Directed Acyclic, false otherwise.
     */
    public boolean isDAGraph() {
        boolean dag = true;
        Iterator<Node<T>> iNodes = _nodes.values().iterator();
        while(dag && iNodes.hasNext()) {
            Node<T> n = iNodes.next();
            dag = !n.loops(n);
            setState(null);
        }        
        return dag;
    }

    /**
     * Generates the adjacency matrix for this Graph.
     * @return the adjacency matrix.
     */
    public int[][] getAdjacencyMatrix() {
        int[][] arr = new int[this.size()][this.size()];
        Map<Node<T>, Integer> map = new HashMap<Node<T>, Integer>();
        int iN = 0;
        for(Node<T> n : _nodes.values()) {
            map.put(n, iN++);
        }
        for(Node<T> n : _nodes.values()) {
            int i = map.get(n);
            for(Node<T> nn : n.getNeighbors()) {
                int j = map.get(nn);
                arr[i][j] = 1;
            }
        }
        return arr;
    }
    
        /**
     * Generates a map grouping all nodes in the graph by their out-degree.
     * @return a map where each out-degree value in the graph (key) is associated
     * with the set of nodes (value) having that out-degree.
     */
    public TreeMap<Integer, TreeSet<T>> getOutDegrees() {
        TreeMap<Integer, TreeSet<T>> map = new TreeMap<Integer, TreeSet<T>>();
        for(Node<T> n : _nodes.values()) {
            int outDegree = n.getNeighbors().size();
            TreeSet<T> set = map.get(outDegree);
            if (set == null) {
                set = new TreeSet<T>();
                map.put(outDegree, set);
            }
            set.add(n.getData());
        }
        return map;
    }
    
    /**
     * Generates a map grouping all nodes in the graph by their in-degree.
     * @return a map where each in-degree value in the graph (key) is associated
     * with the set of nodes (value) having that in-degree.
     */
    public TreeMap<Integer, TreeSet<T>> getInDegrees() {
        TreeMap<Integer, TreeSet<T>> map = new TreeMap<Integer, TreeSet<T>>();
        for(Node<T> node : _nodes.values()) {
            int inDegree = 0;
            for (Node<T> other : _nodes.values()) {
                if (node == other) {
                    continue;
                }
                if (other.getNeighbors().contains(node)) {
                    inDegree++;
                }
            }
            
            TreeSet<T> set = map.get(inDegree);
            if (set == null) {
                set = new TreeSet<T>();
                map.put(inDegree, set);
            }
            set.add(node.getData());
        }
        
        return map;
    }

    /**
     * Generates the topological sort of this graph, where all nodes in the graph
     * are grouped by their index in topological order. The first index is 0.
     * @return a map associating each position in the topological sort (key)
     * with the set of Nodes at that position (value). If the Graph is not DAG, the method 
     * returns null.
     */
    public TreeMap<Integer, TreeSet<T>> topoSort() {
        // return promptly if the graph is not directed, acyclic
        if (!this.isDAGraph()) {
            return null;
        }
        
        // Set state for all nodes to (int)0
        setState(0);
        // place all nodes in a queue
        Queue<Node<T>> queue = new LinkedList<Node<T>>(this._nodes.values());
        // as long as the queue is not empty ...
        while(!queue.isEmpty()) {
            // ... remove the first node from the queue
            // and have it check all its neighbors that need touched.
            // If any, they are returned back to be added to the queue for another check.
            Node<T> node = queue.remove();
            Queue<Node<T>> modifiedNodes = node.topoCheck();
            queue.addAll(modifiedNodes);
        }
        
        // All nodes have their topo order in the state field. Group them in the returning map
        TreeMap<Integer, TreeSet<T>> map = new TreeMap<Integer, TreeSet<T>>();
        for (Node<T> n : _nodes.values()) {
            int topoSort = (int)n.getState();
            TreeSet<T> set = map.get(topoSort);
            if (set == null) {
                set = new TreeSet<T>();
                map.put(topoSort, set);
            }
            set.add(n.getData());
        }

        setState(null);
        return map;
    }

    /**
     * Generates the count of the partitions in the graph.
     * @return count of partitions.
     */
    public int countPartitions() {
        // counter to receive the final number of partitions
        int partition = 0;
        // Set state for all nodes to (int)0
        setState(partition);

        // Group all nodes by their partition
        Map<Integer, List<Node<T>>> partitions = new TreeMap<Integer, List<Node<T>>>();

        // Put all the graph's nodes in a queue
        Queue<Node<T>> q = new LinkedList<Node<T>>(_nodes.values());

        // q contains all unmarked nodes. Loop through it until it drains out
        while(!q.isEmpty()) {
            // increment the partition count and mark all nodes with that partition
            partition++;
            q.peek().mark(partition);

            // iterate through all nodes, and if they are not already marked
            // and are linked to any of the other mark nodes, mark them as well.
            // And do this until there's no change in the graph.
            boolean again = true;
            while (again) {
                again = false;
                for(Node<T> n : _nodes.values()) {
                    if ((int)n.getState() == 0 && n.nextToMark(partition)) {
                        n.mark(partition);
                        again = true;
                    }
                }
            }

            // All nodes that could be marked, were marked. Time to collect them
            partitions.put(partition, new LinkedList<Node<T>>());
            for (int i = q.size(); i > 0; i--) {
                Node<T> n = q. remove();
                if ((int)n.getState() == partition) {
                    partitions.get(partition).add(n);
                } else {
                    q.add(n);
                }
            }
        }

        setState(null);
        System.out.println(partitions);
        return partitions.keySet().size();
    }
    
    /**
     * Generates the Dijkstra distances between the node containing fromData and all the
     * other nodes in the graph.
     * @param fromData
     * @return a map where the key is each Node in the Graph (given by its data)
     * and the value is the Dijkstra distance from the <i>source</i> Node to that node.
     */
    public TreeMap<T, Integer> dijkstra(T fromData) {
        setState(0);
        TreeMap<T, Integer> distances = null;
        Node<T> fromNode = _nodes.get(Node.getLabel(fromData));
        if (fromNode != null) {
            setState(Integer.MAX_VALUE);
            distances = new TreeMap<T, Integer>();
            // calculate dijkstra distances starting fromNode
            fromNode.dijkstra(0);
            // build map
            for(Node<T> n : _nodes.values()) {
                int distance = (int)n.getState();
                if (distance == Integer.MAX_VALUE) {
                    distance = -1;
                }
                distances.put(n.getData(), distance);
            }
            setState(null);
        }
        return distances;
    }
    
    /**
     * A graph is eulerian if it is strongly connected (can reach each node from any other node)
     * and each node verifies the eulerian property (has as many incoming as outgoing edges).
     * Determines if the graph is eulerian
     * @return true if graph is eulerian, false otherwise.
     */
    public boolean isEulerian() {
        if (!isConnected()) {
            return false;
        }

        for(Node<T> n : this._nodes.values()) {
            if (!n.isEulerian()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines a cycle in the graph, starting and ending at the node containing given data.
     * If no such path exists returns null, otherwise the path is returned as a queue, with
     * the target node at the bottom of the queue.
     * @param data - data label pointing to the starting and ending node in the cycle
     * @return the path in the form 
     */
    public ArrayList<T> getCycle(T data) {
        Node<T> node = _nodes.get(Node.getLabel(data));
        if (node == null) {
            throw new RuntimeException("Node not in the graph!");
        }
        // reset the graph
        setState(null);
        // get the path starting from node and leading back to the same node 
        return node.getPath(node);
    }

    /**
     * An eulerian circuit is a path in the graph traversing all edges but exactly once.
     * Determine an eulerian circuit in the graph.
     * @return - array list of nodes' data, in the order of the circuit.
     */
    public ArrayList<T> getEulerianCircuit() {
        // if graph is not eulerian, return null
        if (!isEulerian()) {
            return null;
        }
        // reset the graph
        setState(null);
        // initialize the resulting cycle with any node, if it exists
        ArrayList<T> cycle = new ArrayList<T>();
        if (_nodes.size() != 0) {
            cycle.add(_nodes.values().iterator().next().getData());
        }
        for(int i = 0; i < cycle.size(); i++) {
            Node<T> n = _nodes.get(Node.getLabel(cycle.get(i)));
            if (n.hasUnvisited()) {
                ArrayList<T> newCycle = n.getPath(n);
                cycle.addAll(i+1, newCycle);
            }
        }
        return cycle;
    }

        /**
     * Determines if a path exists between the nodes containing
     * fromData and toData.
     * @param fromData - data value of the origin node.
     * @param toData - data value of the target node.
     * @return true if a path exists, false otherwise
     * @throws RuntimeException if a node cannot be found in the graph.
     */
    public boolean hasPath(T fromData, T toData) throws RuntimeException {
        Node<T> fromNode = _nodes.get(Node.getLabel(fromData));
        Node<T> toNode = _nodes.get(Node.getLabel(toData));
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        setState(null);
        return fromNode.hasPath(toNode);
    }
}
