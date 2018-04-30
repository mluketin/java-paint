package hr.fer.zemris.ooup.lab4.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.ooup.lab4.document.DocumentModel;
import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.Point;
import hr.fer.zemris.ooup.lab4.redner.Renderer;

public class EraserState implements State {

    private DocumentModel model;
    private Set<GraphicalObject> set;
    private List<Point> points;

    public EraserState(DocumentModel model) {
        this.model = model;
        set = new HashSet<>();
        points = new ArrayList<>();
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for (GraphicalObject obj : set) {
            model.removeGraphicalObject(obj);
        }
        set = new HashSet<>();
        points = new ArrayList<>();
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        points.add(mousePoint);
        model.notifyListeners();
        GraphicalObject selectedObject = model.findSelectedGraphicalObject(new Point(mousePoint.getX(), mousePoint.getY()));
        if (selectedObject != null) {
            set.add(selectedObject);
        }
    }

    @Override
    public void keyPressed(int keyCode) {
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
    }

    @Override
    public void afterDraw(Renderer r) {
        for (Point p : points) {
            r.drawLine(p, p);
        }
    }

    @Override
    public void onLeaving() {
        set = new HashSet<>();
        points = new ArrayList<>();
    }
}
