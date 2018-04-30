package hr.fer.zemris.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.ooup.lab4.utils.GeometryUtil;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] points) {
        this.hotPoints = points;
    }

    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
        notifyListeners();
    }

    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    public double getHotPointDistance(int index, Point point) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], point);
    }

    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    public void setHotPointSelected(int index, boolean value) {
        hotPointSelected[index] = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        this.selected = value;
        notifySelectionListeners();
    }

    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(delta);
        }
        notifyListeners();
    }

    public void addGraphicalObjectListener(GraphicalObjectListener gol) {
        listeners.add(gol);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener gol) {
        listeners.remove(gol);
    }

    protected void notifyListeners() {
        for (GraphicalObjectListener l : listeners) {
            l.graphicalObjectChanged(this);
        }
    }

    protected void notifySelectionListeners() {
        for (GraphicalObjectListener l : listeners) {
            l.graphicalObjectSelectionChanged(this);
        }
    }


}
