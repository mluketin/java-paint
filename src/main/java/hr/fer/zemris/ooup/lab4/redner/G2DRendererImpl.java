package hr.fer.zemris.ooup.lab4.redner;

import java.awt.Color;
import java.awt.Graphics2D;

import hr.fer.zemris.ooup.lab4.model.Point;

public class G2DRendererImpl implements Renderer {
    private Graphics2D g2d;

    public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(Point s, Point e) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());
    }

    //za polygon treba 2 reda x i y vrijednosti i njihov broj (tocaka)
    @Override
    public void fillPolygon(Point[] points) {
        int number = points.length;
        int[] arrayX = new int[number];
        int[] arrayY = new int[number];

        for (int i = 0; i < number; i++) {
            arrayX[i] = points[i].getX();
            arrayY[i] = points[i].getY();
        }

        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(arrayX, arrayY, number);
        g2d.setColor(Color.RED);
        g2d.drawPolygon(arrayX, arrayY, number);
    }
}
