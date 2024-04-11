package AStar.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Point implements Comparable<Point> {
    private static final Pattern _POINT_REGEX = Pattern.compile("([A-Za-z])\\s*:\\s*(\\d+),(\\d+)");

    private String _label;
    private int _x;
    private int _y;

    public static Point parsePoint(String strPoint) {
        Matcher matcher = _POINT_REGEX.matcher(strPoint);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("Invalid format: '%s' is not a Point.", strPoint));
        }
        String label = matcher.group(1);
        int x = Integer.parseInt(matcher.group(2));
        int y = Integer.parseInt(matcher.group(3));
        return new Point(label, x, y);
    }

    public Point(String label, int x, int y) {
        _label = label;
        _x = x;
        _y = y;
    }

    public String toString(boolean brief) {
        return brief
            ? _label
            : String.format("%s : {%d,%d}", _label, _x, _y);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public int compareTo(Point other) {
        if (_x == other._x && _y == other._y) {
            return 0;
        }
        double d = Math.pow(_x, 2) + Math.pow(_y, 2);
        double dOther = Math.pow(other._x, 2) + Math.pow(other._y,2);
        return (int)Math.signum(d - dOther);
    }
}
