package hr.fer.zemris.ooup.lab4.redner;

import hr.fer.zemris.ooup.lab4.model.Point;

public interface Renderer {
    void drawLine(Point s, Point e);

    void fillPolygon(Point[] points);
}
