package ClosestNPoints;
import java.awt.Point;
import java.util.Random;

public class ClosestNPoints {

    public static Point[] createPoints(int n) {
        Point[] points = new Point[n];
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            int x = rnd.nextInt(-99, 100);
            int y = rnd.nextInt(-99, 100);
            points[i] = new Point(x, y);
        }
        return points;
    }

    public static void printPoints(Point[] points) {
        for (int i = 0; i < points.length; i++)  {
            if (i != 0 && i % 8 == 0) {
                System.out.println();
            }
            System.out.printf("[%3d,%3d] ", (int)points[i].getX(), (int)points[i].getY());
        }
        System.err.println();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to ClosestNPoints program!");
        Point[] points = createPoints(83);
        printPoints(points);
        System.out.println("Goodbye");
    }
}