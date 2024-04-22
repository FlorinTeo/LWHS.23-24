package AStar_Solved.main;

public class Point implements Comparable<Point> {
    private final static Point _ORIGIN = new Point("Origin", 0, 0);

    private String _label;
    private int _x;
    private int _y;

    public Point(String label, int x, int y) {
        _label = label;
        _x = x;
        _y = y;
    }

    public String getLabel() {
        return _label;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(_x - other._x, 2) + Math.pow(_y - other._y, 2));
    }

    @Override
    public int compareTo(Point other) {
        return (int)Math.signum(this.distance(_ORIGIN) - other.distance(_ORIGIN));
    }

    @Override
    public String toString() {
        return String.format("%s : %d,%d", _label, _x, _y);
    }
}
