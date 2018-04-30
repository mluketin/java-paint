package hr.fer.zemris.ooup.lab4.state;

import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.Point;
import hr.fer.zemris.ooup.lab4.redner.Renderer;

public interface State {
    // poziva se kad progam registrira da je pritisnuta lijeva tipka mi�a
    void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    // poziva se kad progam registrira da je otpu�tena lijeva tipka mi�a
    void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);

    // poziva se kad progam registrira da korisnik pomi�e mi� dok je tipka pritisnuta
    void mouseDragged(Point mousePoint);

    // poziva se kad progam registrira da je korisnik pritisnuo tipku na tipkovnici
    void keyPressed(int keyCode);

    // Poziva se nakon �to je platno nacrtalo grafi�ki objekt predan kao argument
    void afterDraw(Renderer r, GraphicalObject go);

    // Poziva se nakon �to je platno nacrtalo �itav crte�
    void afterDraw(Renderer r);

    // Poziva se kada program napu�ta ovo stanje kako bi pre�lo u neko drugo
    void onLeaving();
}
