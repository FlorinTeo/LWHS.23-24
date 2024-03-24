package Graphs.main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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
     * Private Map keying each Node in the Graph by the hashCode of its data
     * E.g: Given <pre>Node<String> n = new Node<String>("abc");</pre> added to the graph,
     * the _nodes map contains a Map.Entry with
     * <pre>key="abc".hashCode()<br>value=n<pre>
     * @see java.lang.Object#hashCode()
     */
    private Map<Integer, Node<T>> _nodes;
    
    /**
     * Constructs a new Graph as an empty container fit for Nodes of the type T.
     * @see Graph
     * @see Node
     */
    public Graph() {
        _nodes = new TreeMap<Integer, Node<T>>();
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
    public boolean checkState(int state) {
        for (Node<?> n : _nodes.values()) {
            if (state != n.getState()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Adds a new Node to the Graph containing the <i>data</i>. The method 
     * throws if the Graph already contains a Node with data having the same
     * hashCode().
     * @param data - the data reference (of type T) contained in the new Node.
     * @throws RuntimeException if the Graph already contains a Node for the
     * given data.
     * @see java.lang.Object#hashCode()
     */
    public void addNode(T data) {
        int nodeHash = data.hashCode();
        if (_nodes.containsKey(nodeHash)) {
            throw new RuntimeException("Ambiguous graph!");
        }
        
        _nodes.put(nodeHash, new Node<T>(data));
    }
    
    /**
     * Adds a new directed Edge to the Graph, linking the Nodes containing
     * <i>from</i> and <i>to</i> data. It is expected the two Nodes exist
     * otherwise the method throws an exception.
     * @param from - Node where the Edge is starting.
     * @param to - Node where the Edge is ending.
     * @throws RuntimeException if either of the two Nodes are not present in the Graph.
     * @see Node
     * @see Graph#removeEdge(Comparable, Comparable)
     */
    public void addEdge(T from, T to) {
        Node<T> fromNode = _nodes.get(from.hashCode());
        Node<T> toNode = _nodes.get(to.hashCode());
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        fromNode.addEdge(toNode);
    }
    
    /**
     * Removes an existent directed Edge from the Graph, if one exists. 
     * The Edge to be removed is linking the nodes containing the <i>from</i> 
     * and <i>to</i> data references. The two Nodes must exist, otherwise the 
     * method throws an exception.
     * @param from - Node at the starting point of the Edge.
     * @param to - Node at the ending point of the Edge.
     * @throws RuntimeException if either of the two Nodes are not present in the Graph.
     * @see Node
     * @see Graph#addEdge(Comparable, Comparable)
     */
    public void removeEdge(T from, T to) {
        Node<T> fromNode = _nodes.get(from.hashCode());
        Node<T> toNode = _nodes.get(to.hashCode());
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        fromNode.removeEdge(toNode);
    }
    
    /**
     * Removes a Node from the Graph if one exists, along with all
     * its outgoing (egress) and incoming (ingress) edges. If there
     * is no Node hosting the <i>data</i> reference the method does
     * nothing.
     * @param data - Node to be removed from the Graph.
     */
    public void removeNode(T data) {
        Node<T> node = _nodes.get(data.hashCode());
        for(Node<T> n : _nodes.values()) {
            n.removeEdge(node);
        }
        _nodes.remove(data.hashCode());
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
        for(Node<?> n : _nodes.values()) {
            if (!first) {
                output += "\n";
            }
            output += n.toString();
            first = false;
        }
        
        return output;
    }
    
    public void reset() {
        reset(0);
    }
    
    public void reset(int value) {
        for(Node<T> node : _nodes.values()) {
            node.reset(value);
        }
    }
    
    public boolean isUGraph() {
        boolean uGraph = true;
        for(Node<?> node : _nodes.values()) {
            if (!node.isUNode()) {
                uGraph = false;
                break;
            }
        }
        
        reset();
        return uGraph;
    }
    
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
                if (n.getState() != 1) {
                    connected = false;
                    break;
                }
            }
        }
        
        reset();
        return connected;
    }
    
    public boolean isDAGraph() {
        boolean dag = true;
        Iterator<Node<T>> iNodes = _nodes.values().iterator();
        while(dag && iNodes.hasNext()) {
            Node<T> n = iNodes.next();
            dag = !n.loops(n);
            reset();
        }        
        return dag;
    }

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
    
    public TreeMap<Integer, TreeSet<T>> topoSort() {
        // return promptly if the graph is not directed, acyclic
        if (!this.isDAGraph()) {
            return null;
        }
        
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
            int topoSort = n.getState();
            TreeSet<T> set = map.get(topoSort);
            if (set == null) {
                set = new TreeSet<T>();
                map.put(topoSort, set);
            }
            set.add(n.getData());
        }

        reset();
        return map;
    }
    
    public int countPartitions() {
        // counter to receive the final number of partitions
        int partition = 0;

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
                    if (n.getState() == 0 && n.nextToMark(partition)) {
                        n.mark(partition);
                        again = true;
                    }
                }
            }

            // All nodes that could be marked, were marked. Time to collect them
            partitions.put(partition, new LinkedList<Node<T>>());
            for (int i = q.size(); i > 0; i--) {
                Node<T> n = q. remove();
                if (n.getState() == partition) {
                    partitions.get(partition).add(n);
                } else {
                    q.add(n);
                }
            }
        }

        reset();
        System.out.println(partitions);
        return partitions.keySet().size();
    }
    
    public TreeMap<T, Integer> dijkstra(T fromData) {
        TreeMap<T, Integer> distances = null;
        Node<T> fromNode = _nodes.get(fromData.hashCode());
        if (fromNode != null) {
            reset(Integer.MAX_VALUE);
            distances = new TreeMap<T, Integer>();
            // calculate dijkstra distances starting fromNode
            fromNode.dijkstra(0);
            // build map
            for(Node<T> n : _nodes.values()) {
                int distance = n.getState();
                if (distance == Integer.MAX_VALUE) {
                    distance = -1;
                }
                distances.put(n.getData(), distance);
            }
            reset();
        }
        return distances;
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
        Node<T> fromNode = _nodes.get(fromData.hashCode());
        Node<T> toNode = _nodes.get(toData.hashCode());
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        reset();
        return fromNode.hasPath(toNode);
    }

    /**
     * Determines if the graph is eulerian: It is
     * strongly connected (can reach each node from any other node) and
     * each node verifies the eulerian property (has as many incoming as
     * outgoing edges).
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
     * Determines a cycle in the graph, starting from the node
     * containing data. If no such path exists returns null, otherwise
     * the path is returned as a queue, with the target node at the bottom.
     * @return the path in the form 
     */
    public ArrayList<T> getCycle(T data) {
        Node<T> node = _nodes.get(data.hashCode());
        if (node == null) {
            throw new RuntimeException("Node not in the graph!");
        }
        // reset the graph
        reset();
        // get the path starting from node and leading back to the same node 
        return node.getPath(node);
    }

    public ArrayList<T> getEulerianCircuit() {
        // if graph is not eulerian, return null
        if (!isEulerian()) {
            return null;
        }
        // reset the graph
        reset();
        // initialize the resulting cycle with any node, if it exists
        ArrayList<T> cycle = new ArrayList<T>();
        if (_nodes.size() != 0) {
            cycle.add(_nodes.values().iterator().next().getData());
        }
        for(int i = 0; i < cycle.size(); i++) {
            Node<T> n = _nodes.get(cycle.get(i).hashCode());
            if (n.hasUnvisited()) {
                ArrayList<T> newCycle = n.getPath(n);
                cycle.addAll(i+1, newCycle);
            }
        }
        return cycle;
    }
}
