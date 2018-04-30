package hr.fer.zemris.ooup.lab4.state;


import hr.fer.zemris.ooup.lab4.document.DocumentModel;
import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.Point;
import hr.fer.zemris.ooup.lab4.redner.Renderer;

public class AddShapeState implements State {
    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(DocumentModel model, GraphicalObject prototype) {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!shiftDown && !ctrlDown) {
            GraphicalObject newObject = prototype.duplicate();
            newObject.translate(new hr.fer.zemris.ooup.lab4.model.Point(mousePoint.getX(), mousePoint.getY()));
            model.addGraphicalObject(newObject);
        }
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(int keyCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterDraw(Renderer r) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLeaving() {
        // TODO Auto-generated method stub

    }

}
