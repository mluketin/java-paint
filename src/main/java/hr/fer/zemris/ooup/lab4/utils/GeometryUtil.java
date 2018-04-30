package hr.fer.zemris.ooup.lab4.utils;

import java.awt.geom.Line2D;

import hr.fer.zemris.ooup.lab4.model.Point;

public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        return Math.hypot(Math.abs(point2.getX() - point1.getX()), Math.abs(point2.getY() - point1.getY()));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        return Line2D.ptSegDist(s.getX(), s.getY(), e.getX(), e.getY(), p.getX(), p.getY());
    }

}
