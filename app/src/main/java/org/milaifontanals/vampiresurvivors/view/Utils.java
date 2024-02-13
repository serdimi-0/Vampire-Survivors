package org.milaifontanals.vampiresurvivors.view;

import android.graphics.Point;

public class Utils {

    public static double getDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static Point scale(Point vector, double scale) {
        return new Point((int) (vector.x * scale), (int) (vector.y * scale));
    }

    public static Point suma(Point point, Point vector) {
        return new Point(point.x + vector.x, point.y + vector.y);
    }
}
