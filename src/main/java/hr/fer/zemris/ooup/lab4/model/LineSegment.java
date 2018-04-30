package hr.fer.zemris.ooup.lab4.model;


import java.util.List;
import java.util.Stack;

import hr.fer.zemris.ooup.lab4.redner.Renderer;
import hr.fer.zemris.ooup.lab4.utils.GeometryUtil;

public class LineSegment extends AbstractGraphicalObject {

    public LineSegment() {
        super(new Point[]{new Point(0, 0), new Point(10, 0)});
    }

    public LineSegment(Point begin, Point end) {
        super(new Point[]{begin, end});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point begin = getHotPoint(0);
        Point end = getHotPoint(1);

        //koordinate gore lijevo na ekranu (y je obrnut, ovo je minimalno) min X i min Y
        int x = begin.getX() < end.getX() ? begin.getX() : end.getX();
        int y = begin.getY() < end.getY() ? begin.getY() : end.getY();

        //dolje desno max X i max Y
        int x2 = begin.getX() > end.getX() ? begin.getX() : end.getX();
        int y2 = begin.getY() > end.getY() ? begin.getY() : end.getY();

        return new Rectangle(x, y, x2 - x, y2 - y);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public String getShapeName() {
        return "Line";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0).duplicate(), getHotPoint(1).duplicate());
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

    @Override
    public String toString() {
        String prvi = "(" + getHotPoint(0).getX() + ", " + getHotPoint(0).getY() + ")";
        String drugi = "(" + getHotPoint(1).getX() + ", " + getHotPoint(1).getY() + ")";
        return "LineSegment( " + prvi + ", " + drugi + " )";
    }

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(getShapeID()).append(" ")
                .append(getHotPoint(0).getX()).append(" ")
                .append(getHotPoint(0).getY()).append(" ")
                .append(getHotPoint(1).getX()).append(" ")
                .append(getHotPoint(1).getY()).append("\n");
        rows.add(sb.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String parts[] = data.split(" ");
        Point begin = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        Point end = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        stack.push(new LineSegment(begin, end));
    }

}
