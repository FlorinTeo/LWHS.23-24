package AStar.main;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Node implements Comparable<Node> {
    private Point _point;
    private Map<String, Node> _neighbors;
    private Object _state;
    private double _cost;
    
    public Node(Point data) {
        _point = data;
        _neighbors = new HashMap<String, Node>();
        _state = null;
    }
    
    public Point getPoint() {
        return _point;
    }


    public String getLabel() {
        return _point.getLabel();
    }

    public Object getState() {
        return _state;
    }

    public boolean checkState(Object value) {
        return (_state == null) ? (value == null) : _state.equals(value);
    }
    
    public void setState(Object value) {
        _state = value;
    }

    
    public double getCost() {
        return _cost;
    }

    public void setCost(double cost) {
        _cost = cost;
    }

    public double getDistance(Node otherNode) {
        return _point.distance(otherNode._point);
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
