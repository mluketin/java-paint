package hr.fer.zemris.ooup.lab4.model;


import java.util.List;
import java.util.Stack;

import hr.fer.zemris.ooup.lab4.redner.Renderer;
import hr.fer.zemris.ooup.lab4.utils.GeometryUtil;

//donji, desni
public class Oval extends AbstractGraphicalObject {

    //center sam makao jer mi je prouzrocio bug, jer nakon translatea treba center update a to nemogu iz nadklase
    public Oval() {
        super(new Point[]{new Point(0, 10), new Point(10, 0)});
    }

    public Oval(Point bottom, Point right) {
        super(new Point[]{bottom, right});
    }


    //na crtezu gore lijevo jer je Y obrnuto
    @Override
    public Rectangle getBoundingBox() {
        Point bottom = getHotPoint(0);
        Point right = getHotPoint(1);

        int x = bottom.getX() - (right.getX() - bottom.getX());
        int y = right.getY() - (bottom.getY() - right.getY());

        int width = (right.getX() - bottom.getX()) * 2;
        int height = (bottom.getY() - right.getY()) * 2;

        return new Rectangle(x, y, width, height);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        //hot pointovi
        Point bottom = getHotPoint(0);
        Point right = getHotPoint(1);

        //elipsa karakteristike
        int a = right.getX() - bottom.getX();
        int b = bottom.getY() - right.getY();
        int p = bottom.getX();
        int q = right.getY();

        //metoda elipse
        double elipseEquation = Math.pow(mousePoint.getX() - p, 2) / Math.pow(a, 2) + Math.pow(mousePoint.getY() - q, 2) / Math.pow(b, 2);
        if (elipseEquation <= 1) {
            return 0;//click is inside oval
        }

        Point[] points = createPoints();
        double min = GeometryUtil.distanceFromPoint(points[0], mousePoint);
        for (int i = 1; i < points.length; i++) {
            double distance = GeometryUtil.distanceFromPoint(points[i], mousePoint);
            if (distance < min) {
                min = distance;
            }
        }
        return min;
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0).duplicate(), getHotPoint(1).duplicate());
    }

    @Override
    public void render(Renderer r) {
        r.fillPolygon(createPoints());
    }

    private Point[] createPoints() {
        Point bottom = getHotPoint(0);
        Point right = getHotPoint(1);
        Point center = new Point(bottom.getX(), right.getY());


        int a = right.getX() - bottom.getX();
        int b = bottom.getY() - right.getY();

        int pointsNumber = 60;
        Point[] points = new Point[pointsNumber];
        for (int i = 0; i < pointsNumber; i++) {
            double t = (2 * Math.PI / pointsNumber) * i;
            int x = (int) (a * Math.cos(t)) + center.getX();
            int y = (int) (b * Math.sin(t)) + center.getY();
            points[i] = new Point(x, y);
        }
        return points;
    }

    @Override
    public String getShapeID() {
        return "@OVAL";

    }

    @Override
    public void save(List<String> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(getShapeID()).append(" ")
                .append(getHotPoint(1).getX()).append(" ")
                .append(getHotPoint(1).getY()).append(" ")
                .append(getHotPoint(0).getX()).append(" ")
                .append(getHotPoint(0).getY()).append("\n");
        rows.add(sb.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String parts[] = data.split(" ");
        Point right = new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        Point bottom = new Point(Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        stack.push(new Oval(bottom, right));
    }

}
