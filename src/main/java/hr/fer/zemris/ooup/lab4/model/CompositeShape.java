package hr.fer.zemris.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import hr.fer.zemris.ooup.lab4.redner.Renderer;

public class CompositeShape implements GraphicalObject {
    private List<GraphicalObject> children;
    private boolean selected;
    private List<GraphicalObjectListener> listeners;

    public CompositeShape() {
        children = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public CompositeShape(List<GraphicalObject> children) {
        this.children = new ArrayList<>(children);

        listeners = new ArrayList<>();
        selected = true;
        notifyObservers();
    }

    public List<GraphicalObject> getChildren() {
        return children;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public int getNumberOfHotPoints() {
        return 0;
    }

    @Override
    public Point getHotPoint(int index) {
        return null;
    }

    @Override
    public void setHotPoint(int index, Point point) {
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return false;
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return Double.MAX_VALUE;
    }

    @Override
    public void translate(Point delta) {
        for (GraphicalObject obj : children) {
            obj.translate(delta);
        }
        notifyObservers();
    }

    @Override
    public Rectangle getBoundingBox() {
        if (children.size() == 0) {
            return null;
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (GraphicalObject obj : children) {

            Rectangle rec = obj.getBoundingBox();
            if (rec.getX() < minX) {
                minX = rec.getX();
            }

            if (rec.getY() < minY) {
                minY = rec.getY();
            }

            if (rec.getX() + rec.getWidth() > maxX) {
                maxX = rec.getX() + rec.getWidth();
            }

            if (rec.getY() + rec.getHeight() > maxY) {
                maxY = rec.getY() + rec.getHeight();
            }
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        double min = Double.MAX_VALUE;

        for (GraphicalObject obj : children) {
            double dist = obj.selectionDistance(mousePoint);
            if (dist < min) {
                min = dist;
            }
        }

        return min;
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    public void notifyObservers() {
        for (GraphicalObjectListener graphicalObjectListener : listeners) {
            graphicalObjectListener.graphicalObjectChanged(this);
        }
    }

    public void notifySelectionListeners() {
        for (GraphicalObjectListener listener : listeners) {
            listener.graphicalObjectSelectionChanged(this);
        }
    }

    @Override
    public String getShapeName() {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate() {
        List<GraphicalObject> list = new ArrayList<>(children);
        return new CompositeShape(list);
    }

    @Override
    public void render(Renderer r) {
        System.out.println("RENDER CHILDREN size " + children.size());
        for (GraphicalObject obj : children) {
            obj.render(r);
        }
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void save(List<String> rows) {

        for (GraphicalObject obj : children) {
            obj.save(rows);
        }

        StringBuilder sb = new StringBuilder();

        sb.append(getShapeID()).append(" ")
                .append(children.size()).append("\n");
        rows.add(sb.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        int childrenSize = Integer.parseInt(data);
        List<GraphicalObject> list = new ArrayList<>();

        for (int i = 0; i < childrenSize; i++) {
            list.add(stack.pop());
        }
        CompositeShape c = new CompositeShape(list);
        stack.push(c);
    }
}
