package hr.fer.zemris.ooup.lab4.state;

import java.awt.event.KeyEvent;
import java.util.List;

import hr.fer.zemris.ooup.lab4.document.DocumentModel;
import hr.fer.zemris.ooup.lab4.model.CompositeShape;
import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.Point;
import hr.fer.zemris.ooup.lab4.model.Rectangle;
import hr.fer.zemris.ooup.lab4.redner.Renderer;

public class SelectShapeState implements State {

    private DocumentModel model;
    private GraphicalObject selected;
    private int indexOfSelectedHotPoint;


    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject selectedObject = model
                .findSelectedGraphicalObject(new Point(mousePoint.getX(), mousePoint.getY()));

        if (ctrlDown) {
            // ako je ctrl down kliknem na objekt i kazen mu da se
            // selektira/odselektira, ako je klik na prazno nista se ne dogodi
            if (selectedObject != null) {
                if (selectedObject.isSelected() == false) {
                    selectedObject.setSelected(true);
                } else {
                    selectedObject.setSelected(false);
                }
                if (model.getSelectedObjects().size() > 1) {
                    selected = null;
                    indexOfSelectedHotPoint = -1;
                }
                return;
            }
        } else {
            model.deselectAll();
            if (selectedObject != null) {
                selectedObject.setSelected(true);
                selected = selectedObject;
                indexOfSelectedHotPoint = model.findSelectedHotPoint(selected, mousePoint);
                return;
            }
        }
        selected = null;
        indexOfSelectedHotPoint = -1;
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (indexOfSelectedHotPoint != -1 && selected != null) {
            selected.setHotPoint(indexOfSelectedHotPoint, mousePoint);
        }
    }

    @Override
    public void keyPressed(int keyCode) {

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                translateAllSelectedObjects(new Point(-1, 0));
                break;

            case KeyEvent.VK_RIGHT:
                translateAllSelectedObjects(new Point(1, 0));
                break;

            case KeyEvent.VK_UP:
                translateAllSelectedObjects(new Point(0, -1));
                break;

            case KeyEvent.VK_DOWN:
                translateAllSelectedObjects(new Point(0, 1));
                break;

            case KeyEvent.VK_PLUS:
                for (GraphicalObject go : model.getSelectedObjects()) {
                    model.increaseZ(go);
                }
                break;

            case KeyEvent.VK_MINUS:
                for (GraphicalObject go : model.getSelectedObjects()) {
                    model.decreaseZ(go);
                }
                break;

            case KeyEvent.VK_G:
                if (model.getSelectedObjects().size() > 0) {
                    GraphicalObject composit = new CompositeShape(model.getSelectedObjects());
                    while (model.getSelectedObjects().size() > 0) {
                        model.removeGraphicalObject(model.getSelectedObjects().get(0));
                    }
                    model.addGraphicalObject(composit);
                }
                break;

            case KeyEvent.VK_U:
                if (model.getSelectedObjects().size() == 1) {
                    GraphicalObject selectedObject = model.getSelectedObjects().get(0);

                    if (selectedObject.getShapeName().equals("Composite")) {
                        List<GraphicalObject> children = ((CompositeShape) selectedObject).getChildren();
                        model.removeGraphicalObject(selectedObject);

                        for (GraphicalObject obj : children) {
                            model.addGraphicalObject(obj);
                        }
                    }
                }
                break;

            default:
                break;
        }

    }

    private void translateAllSelectedObjects(Point point) {
        System.out.println("TRANSLATE ALL");
        for (GraphicalObject go : model.getSelectedObjects()) {
            go.translate(point);
        }
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected() == true) {
            Rectangle rectangle = go.getBoundingBox();
            drawRectangle(r, rectangle);
        }

        // provjera je li to jedini selektirani element
        if (selected != null) {

            if (model.getSelectedObjects().indexOf(selected) == model.getSelectedObjects().indexOf(go)) {
                // crtanje kvadarata oko hotpointa
                Point hp0 = go.getHotPoint(0);
                Point hp0First = new Point(hp0.getX() - 2, hp0.getY() - 2);
                Rectangle firstRectangle = new Rectangle(hp0First.getX(), hp0First.getY(), 4, 4);
                drawRectangle(r, firstRectangle);

                Point hp1 = go.getHotPoint(1);
                Point hp1First = new Point(hp1.getX() - 2, hp1.getY() - 2);
                Rectangle secondRectangle = new Rectangle(hp1First.getX(), hp1First.getY(), 4, 4);
                drawRectangle(r, secondRectangle);
            }
        }
    }

    private void drawRectangle(Renderer r, Rectangle rectangle) {
        // nacrtat 4 linije
        hr.fer.zemris.ooup.lab4.model.Point first = new hr.fer.zemris.ooup.lab4.model.Point(rectangle.getX(),
                rectangle.getY());
        hr.fer.zemris.ooup.lab4.model.Point second = new hr.fer.zemris.ooup.lab4.model.Point(rectangle.getX(),
                rectangle.getY() + rectangle.getHeight());
        hr.fer.zemris.ooup.lab4.model.Point third = new hr.fer.zemris.ooup.lab4.model.Point(
                rectangle.getX() + rectangle.getWidth(), rectangle.getY() + rectangle.getHeight());
        hr.fer.zemris.ooup.lab4.model.Point fourth = new hr.fer.zemris.ooup.lab4.model.Point(
                rectangle.getX() + rectangle.getWidth(), rectangle.getY());

        r.drawLine(first, second);
        r.drawLine(second, third);
        r.drawLine(third, fourth);
        r.drawLine(fourth, first);
    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {
        model.deselectAll();
    }

}
