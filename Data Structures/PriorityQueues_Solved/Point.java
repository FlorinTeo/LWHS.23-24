package PriorityQueues_Solved;

public class Point implements Comparable<Point>{

    public static final Point ORIGIN  = new Point(0, 0);
    private int _x;
    private int _y;

    public Point(int x, int y) {
        _x = x;
        _y = y;
    }

    public double distance(Point other) {
        return Math.sqrt(Math.pow(_x - other._x, 2) + Math.pow(_y - other._y, 2));
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", _x, _y);
    }

    @Override
    public int compareTo(Point o) {
        return (int)Math.signum(this.distance(ORIGIN) - o.distance(ORIGIN));
    }
}
