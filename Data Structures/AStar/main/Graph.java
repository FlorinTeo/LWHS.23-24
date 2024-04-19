package AStar.main;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

public class Graph {
    private Map<String, Node> _nodes;
    
    public Graph() {
        _nodes = new TreeMap<String, Node>();
    }
    
    public int size() {
        return _nodes.size();
    }
    
    public boolean checkState(Object state) {
        for (Node n : _nodes.values()) {
            if (!n.checkState(state)) {
                return false;
            }
        }
        return true;
    }

    public void setState(Object state) {
        for (Node n : _nodes.values()) {
            n.setState(state);
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

    public LinkedList<String> routeDijkstra(String fromLabel, String toLabel) {
        // Check nodes exist in the graph, otherwise throw exception
        Node fromNode = _nodes.get(fromLabel);
        Node toNode = _nodes.get(toLabel);
        if (fromNode == null || toNode == null) {
            throw new RuntimeException("Node(s) not in the graph!");
        }

        // Reset all Node states to null
        setState(null);

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode);
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(fromNode);

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
        for(Node crt = toNode; crt != fromNode; crt = (Node)crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());

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
        setState(null);

        // Mark fromNode Node (set its state) with a reference to itself and add it to a queue
        fromNode.setState(fromNode);
        fromNode.setCost(0);
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(fromNode);

        // We start with a boolean tracking whether we found the route or not (initially false)..
        boolean found = false;
        Node lastNode = null;
        double distanceSoFar = 0;
        // .. then we loop until the queue is emptied out.
        while(!queue.isEmpty()) {
            // Remove the first node from the queue.
            Node node = queue.remove();
            if (lastNode != null) {
                distanceSoFar += node.getDistance(lastNode);
                lastNode = node;
            }
            // If the node is the target, we're done, mark that we found the route and break out.
            if (node == toNode) {
                found = true;
                break;
            }
            // We're not done, so loop through all the neighbors of node.
            for(Node neighbor : node.getNeighbors()) {
                // If node had already been visited (it's state is not null), just skip it
                if (!neighbor.checkState(null)) {
                    continue;
                }
                // Otherwise mark it with a reference to this node and add it to the queue.
                neighbor.setState(node);
                neighbor.setCost(distanceSoFar + neighbor.getDistance(node) + neighbor.getDistance(toNode));
                queue.add(neighbor);
            }
        }

        // if we couldn't find a route, just return null
        if (!found) {
            return null;
        }

        // We found a route, so retrace it from target to start, using the reference from the nodes' state.
        LinkedList<String> result = new LinkedList<String>();
        for(Node crt = toNode; crt != fromNode; crt = (Node)crt.getState()) {
            result.add(0, crt.getLabel());
        }
        result.add(0, fromNode.getLabel());

        // Return the list with all the node labels in the route, from start to target.
        return result;
    }
}
