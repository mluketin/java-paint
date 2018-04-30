package hr.fer.zemris.ooup.lab4.model;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Point translate(Point dp) {
        return new Point(x + dp.getX(), y + dp.getY());
    }

    public Point difference(Point p) {
        return new Point(x - p.getX(), y - p.getY());
    }

    @Override
    public String toString() {
        return "Point: " + x + " ," + y;
    }

    public Point duplicate() {
        return new Point(x, y);
    }
}
