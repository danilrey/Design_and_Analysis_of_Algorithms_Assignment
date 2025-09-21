package org.example;

import java.util.*;

class Point {
    public int x, y;
    public Point(int x, int y) {
        this.x = x; this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

public class ClosestPair {

    public static double closest(Point[] points, int n) {
        Point[] Px = Arrays.copyOf(points, n);
        Arrays.sort(Px, Comparator.comparingInt(p -> p.x));
        Point[] Py = Arrays.copyOf(points, n);
        Arrays.sort(Py, Comparator.comparingInt(p -> p.y));
        return closestUtil(Px, Py, n);
    }

    private static double closestUtil(Point[] Px, Point[] Py, int n) {
        if (n <= 3) return bruteForce(Px, n);

        int mid = n / 2;
        Point midPoint = Px[mid];

        // Разделяем Px на левую и правую части
        Point[] PxLeft = Arrays.copyOfRange(Px, 0, mid);
        Point[] PxRight = Arrays.copyOfRange(Px, mid, n);

        // Делим Py так, чтобы точки попадали в ту же половину, что и в Px
        List<Point> PyLeftList = new ArrayList<>();
        List<Point> PyRightList = new ArrayList<>();
        for (Point p : Py) {
            if (p.x <= midPoint.x) PyLeftList.add(p);
            else PyRightList.add(p);
        }

        Point[] PyLeft = PyLeftList.toArray(new Point[0]);
        Point[] PyRight = PyRightList.toArray(new Point[0]);

        double dl = closestUtil(PxLeft, PyLeft, PxLeft.length);
        double dr = closestUtil(PxRight, PyRight, PxRight.length);

        double d = Math.min(dl, dr);

        // Формируем "strip"
        List<Point> strip = new ArrayList<>();
        for (Point p : Py) {
            if (Math.abs(p.x - midPoint.x) < d)
                strip.add(p);
        }

        return Math.min(d, stripClosest(strip.toArray(new Point[0]), strip.size(), d));
    }

    private static double bruteForce(Point[] P, int n) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                min = Math.min(min, distance(P[i], P[j]));
        return min;
    }

    private static double stripClosest(Point[] strip, int size, double d) {
        double min = d;
        for (int i = 0; i < size; ++i) {
            for (int j = i + 1; j < size && j <= i + 7; ++j) { // ≤ 7 соседей
                min = Math.min(min, distance(strip[i], strip[j]));
            }
        }
        return min;
    }

    private static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) +
                Math.pow(p1.y - p2.y, 2));
    }

}