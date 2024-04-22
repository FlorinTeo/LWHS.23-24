package AStar_Solved.main;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Node implements Comparable<Node> {
    private Point _point;
    private Map<String, Node> _neighbors;

    private Node _previous;
    private double _distanceSoFar;
    private double _cost;
    
    public Node(Point data) {
        _point = data;
        _neighbors = new HashMap<String, Node>();
        _previous = null;
        _distanceSoFar = 0;
        _cost = 0;
    }
    
    public Point getPoint() {
        return _point;
    }


    public String getLabel() {
        return _point.getLabel();
    }
        
    public Node getState() {
        return _previous;
    }

    public void setState(Node previous) {
        _previous = previous;
        _distanceSoFar = 0;
        _cost = 0;
    }

    public void setState(Node previous, Node target) {
        _previous = previous;
        _distanceSoFar = previous._distanceSoFar + _point.distance(previous._point);
        _cost = _distanceSoFar + _point.distance(target._point);
    }
    
    public void addNeighbor(Node otherNode) {
        _neighbors.put(otherNode.getLabel(), otherNode);
    }

    public void removeNeighbor(Node otherNode) {
        _neighbors.remove(otherNode.getLabel());
    }

    public Collection<Node> getNeighbors() {
        return new LinkedList<Node>(_neighbors.values());
    }
    
   @Override
    public String toString() {
        String output = _point.toString() + " > ";
        boolean first = true;
        for(Node n : _neighbors.values()) {
            if (!first) {
                output += " ";
            }
            output += n.getLabel();
            first = false;
        }
        return output;
    }

    @Override
    public int compareTo(Node other) {
        return (int)Math.signum(this._cost - other._cost);
    }
}
