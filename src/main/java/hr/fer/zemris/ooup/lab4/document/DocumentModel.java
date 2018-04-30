package hr.fer.zemris.ooup.lab4.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.GraphicalObjectListener;
import hr.fer.zemris.ooup.lab4.model.LineSegment;
import hr.fer.zemris.ooup.lab4.model.Oval;
import hr.fer.zemris.ooup.lab4.model.Point;

public class DocumentModel {
    public final static double SELECTION_PROXIMITY = 10;

    private List<GraphicalObject> objects = new ArrayList<>();
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    private List<DocumentModelListener> listeners = new ArrayList<>();
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {

        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            //kad se objektu promijeni selekcija stavin ili maknem i notify
            if (go.isSelected()) {
                if (selectedObjects.indexOf(go) == -1) {
                    selectedObjects.add(go);
                    notifyListeners();
                }
            } else {
                selectedObjects.remove(go);
                notifyListeners();
            }
        }
    };

    public DocumentModel() {}

    public DocumentModel(List<GraphicalObject> objects, List<GraphicalObject> roObjects,
                         List<DocumentModelListener> listeners, List<GraphicalObject> selectedObjects,
                         List<GraphicalObject> roSelectedObjects) {
        this.objects = objects;
        this.roObjects = roObjects;
        this.listeners = listeners;
        this.selectedObjects = selectedObjects;
        this.roSelectedObjects = roSelectedObjects;
    }

    public void deselectAll() {
        while (selectedObjects.size() > 0) {
            selectedObjects.get(0).setSelected(false);
        }
    }

    public void clear() {
        for (GraphicalObject obj : objects) {
            obj.removeGraphicalObjectListener(goListener);
        }
        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    public void addGraphicalObject(GraphicalObject obj) {
        if (obj.isSelected()) {
            selectedObjects.add(obj);
        }
        objects.add(obj);
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject obj) {
        if (obj.isSelected()) {
            selectedObjects.remove(obj);
        }
        objects.remove(obj);
        obj.removeGraphicalObjectListener(goListener);
        notifyListeners();
    }

    public List<GraphicalObject> list() {
        return roObjects;
    }

    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    public void notifyListeners() {
        for (DocumentModelListener l : listeners) {
            l.documentChange();
        }
    }

    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);

        if (index < objects.size() - 1) {
            GraphicalObject secondGo = objects.get(index + 1);
            objects.set(index, secondGo);
            objects.set(index + 1, go);
        }
        notifyListeners();
    }

    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);

        if (index > 0) {
            GraphicalObject secondGo = objects.get(index - 1);
            objects.set(index - 1, go);
            objects.set(index, secondGo);
        }
        notifyListeners();
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject selectedObject = null;
        double minDistance = Double.MAX_VALUE;
        for (GraphicalObject go : objects) {
            double selectionDist = go.selectionDistance(mousePoint);
            if (selectionDist < SELECTION_PROXIMITY) {
                if (selectionDist < minDistance) {
                    minDistance = selectionDist;
                    selectedObject = go;
                }
            }
        }
        return selectedObject;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki
    // hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet
    // da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status
    // selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int index = -1;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double dist = object.getHotPointDistance(i, mousePoint);
            if (dist < SELECTION_PROXIMITY) {
                if (dist < minDistance) {
                    minDistance = dist;
                    index = i;
                }
            }
        }
        return index;
    }
}
