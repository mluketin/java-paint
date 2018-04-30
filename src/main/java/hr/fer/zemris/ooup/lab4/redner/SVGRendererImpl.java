package hr.fer.zemris.ooup.lab4.redner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.ooup.lab4.model.Point;

public class SVGRendererImpl implements Renderer {
    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        this.fileName = fileName;
        // zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
        // <svg xmlns=... >
        // ...
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
    }

    public void close() throws IOException {
        lines.add("</svg>");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {

            for (String line : lines) {
                bw.write(line);
            }
            bw.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void drawLine(Point s, Point e) {
        String line = String.format("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:#0000ff;\"/>", s.getX(), s.getY(), e.getX(), e.getY());
        lines.add(line);
    }

    @Override
    public void fillPolygon(Point[] points) {
        StringBuilder sb = new StringBuilder();
        sb.append("<polygon points=\"");
        for (Point point : points) {
            sb.append(point.getX()).append(',').append(point.getY()).append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\"/>");
        lines.add(sb.toString());
    }

}
