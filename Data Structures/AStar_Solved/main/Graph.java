package AStar_Solved.main;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * The Graph represents a collection of Nodes (each of which is a Point) along with all their connections.
 * A Graph is akin to a road network in the real life, in which street intersections are represented by Nodes,
 * and each street segment is represented by the connections between the nodes.
 */
public class Graph {

    // Map of all the nodes in the graph, indexed by the label of the Point object inside the node
    private Map<String, Node> _nodes;
    // Count of all the queue.add() calls, as a measure of algorithm complexity
    private int _queueAddCount;
    // Distance traveled during the last route calculation as a measure of algorithm optimality
    private double _traveledDistance;
    
    public Graph() {
        _nodes = new TreeMap<String, Node>();
    }

    public int size() {
        return _nodes.size();
    }

    public int getQueueAddCount() {
        return _queueAddCount;
    }

    public double getTraveledDistance() {
        return _traveledDistance;
    }
    
    private void reset() {
        _queueAddCount = 0;
        _traveledDistance = 0.0;
        for (Node n : _nodes.values()) {
            n.setState(null);
        }
    }
    
    public void addNode(Point point) {
        Node node = new Node(point);
        String label = node.getLabel();
        if (_nodes.containsKey(label)) {
            throw new RuntimeException("Ambiguous graph!");
        }
        _nodes.put(label, node);
    }

    public Point removeNode(String label) {
        Node node = _nodes.get(label);
        if (node == null) {
            throw new RuntimeException("Node does not exist in graph!");
        }
        for(Node n : _nodes.values()) {
            n.removeNeighbor(node);
        }
        _nodes.remove(node.getLabel());
        return node.getPoint();
    }
    
    public void addEdge(String fromLabel, Object toLabel) {
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        fromNode.addNeighbor(toNode);
    }
    
    public void removeEdge(String fromLabel, String toLabel) {
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }
        
        fromNode.removeNeighbor(toNode);
    }
    
    @Override
    public String toString() {
        String output = "";
        boolean first = true;
        for(Node n : _nodes.values()) {
            if (!first) {
                output += "\n";
            }
            output += n.toString();
            first = false;
        }
        return output;
    }

    public LinkedList<String> routeFirstPath(String fromLabel, String toLabel) {
        // Check nodes exist in the graph, otherwise throw exception
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }

        // Reset all Node states to null
        reset();

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode);
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(fromNode);
        _queueAddCount++;

        // We start with a boolean tracking whether we found the route or not (initially false)..
        boolean found = false;
        // .. then we loop until the queue is emptied out.
        while(!queue.isEmpty()) {
            // Remove the first node from the queue.
            Node node = queue.remove();
            // If the node is the target, we're done, mark that we found the route and break out.
            if (node == toNode) {
                found = true;
                break;
            }
            // We're not done, so loop through all the neighbors of node.
            for(Node neighbor : node.getNeighbors()) {
                // If node had already been visited (it's state is not null), just skip it
                if (neighbor.getState() != null) {
                    continue;
                }
                // Otherwise mark it with a reference to this node and add it to the queue.
                neighbor.setState(node);
                queue.add(neighbor);
                _queueAddCount++;
            }
        }

        // if we couldn't find a route, just return null
        if (!found) {
            return null;
        }

        // We found a route, so retrace it from target to start, using the reference from the nodes' state.
        LinkedList<String> result = new LinkedList<String>();
        for(Node crt = toNode; crt != fromNode; crt = crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());
        _traveledDistance = toNode.getDistanceSoFar();

        // Return the list with all the node labels in the route, from start to target.
        return result;
    }

    public LinkedList<String> routeDijkstra(String fromLabel, String toLabel) {
        // Check nodes exist in the graph, otherwise throw exception
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }

        // Reset all Node states to null
        reset();

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode, toNode);
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(fromNode);
        _queueAddCount++;

        // We start with a boolean tracking whether we found the route or not (initially false)..
        boolean found = false;
        // .. then we loop until the queue is emptied out.
        while(!queue.isEmpty()) {
            // Remove the first node from the queue.
            Node node = queue.remove();
            // If the node is the target, we're done, mark that we found the route and break out.
            if (node == toNode) {
                found = true;
                break;
            }
            // We're not done, so loop through all the neighbors of node.
            for(Node neighbor : node.getNeighbors()) {
                // If node had already been visited (it's state is not null), just skip it
                if (neighbor.setState(node)) {
                    queue.add(neighbor);
                    _queueAddCount++;
                }
            }
        }

        // if we couldn't find a route, just return null
        if (!found) {
            return null;
        }

        // We found a route, so retrace it from target to start, using the reference from the nodes' state.
        LinkedList<String> result = new LinkedList<String>();
        for(Node crt = toNode; crt != fromNode; crt = crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());

        _traveledDistance = toNode.getDistanceSoFar();

        // Return the list with all the node labels in the route, from start to target.
        return result;
    }

    public LinkedList<String> routeAStar(String fromLabel, String toLabel) {
        // Check nodes exist in the graph, otherwise throw exception
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }

        // Reset all Node states to null
        reset();

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode);
        PriorityQueue<Node> queue = new HeapPriorityQueue<Node>(Node.class);
        queue.add(fromNode);
        _queueAddCount++;

        // We start with a boolean tracking whether we found the route or not (initially false)..
        boolean found = false;
        // .. then we loop until the queue is emptied out.
        while(!queue.isEmpty()) {
            // Remove the first node from the queue.
            Node node = queue.remove();
            // If the node is the target, we're done, mark that we found the route and break out.
            if (node == toNode) {
                found = true;
                break;
            }
            // We're not done, so loop through all the neighbors of node.
            for(Node neighbor : node.getNeighbors()) {
                // If node had already been visited (it's state is not null), just skip it
                if (neighbor.getState() != null) {
                    continue;
                }
                // Otherwise mark it with a reference to this node and add it to the queue.
                neighbor.setState(node, toNode);
                queue.add(neighbor);
                _queueAddCount++;
            }
        }

        // if we couldn't find a route, just return null
        if (!found) {
            return null;
        }

        // We found a route, so retrace it from target to start, using the reference from the nodes' state.
        LinkedList<String> result = new LinkedList<String>();
        for(Node crt = toNode; crt != fromNode; crt = crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());

        _traveledDistance = toNode.getDistanceSoFar();

        // Return the list with all the node labels in the route, from start to target.
        return result;
    }
}
