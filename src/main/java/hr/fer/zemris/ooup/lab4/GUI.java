package hr.fer.zemris.ooup.lab4;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import hr.fer.zemris.ooup.lab4.document.DocumentModel;
import hr.fer.zemris.ooup.lab4.document.DocumentModelListener;
import hr.fer.zemris.ooup.lab4.model.CompositeShape;
import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.Point;
import hr.fer.zemris.ooup.lab4.redner.G2DRendererImpl;
import hr.fer.zemris.ooup.lab4.redner.Renderer;
import hr.fer.zemris.ooup.lab4.redner.SVGRendererImpl;
import hr.fer.zemris.ooup.lab4.state.AddShapeState;
import hr.fer.zemris.ooup.lab4.state.EraserState;
import hr.fer.zemris.ooup.lab4.state.IdleState;
import hr.fer.zemris.ooup.lab4.state.SelectShapeState;
import hr.fer.zemris.ooup.lab4.state.State;

public class GUI extends JFrame {
    private List<GraphicalObject> objects;
    private JToolBar toolBar;
    private JPanel panel;
    private Canvas canvas;
    private State currentState;

    private Map<String, GraphicalObject> mapPrototype;

    private DocumentModel documentModel;

    public GUI(List<GraphicalObject> objects) {
        this.objects = objects;
        mapPrototype = new HashMap<String, GraphicalObject>();
        for (GraphicalObject obj : objects) {
            mapPrototype.put(obj.getShapeID(), obj);
        }
        GraphicalObject compositePrototype = new CompositeShape();
        mapPrototype.put(compositePrototype.getShapeID(), compositePrototype);

        documentModel = new DocumentModel();

        panel = new JPanel();
        canvas = new Canvas(documentModel);
        currentState = new IdleState();

        // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(canvas, BorderLayout.CENTER);
        setSize(800, 400);

        initJToolBar();
        setCanvasListeners();
    }

    private void setCanvasListeners() {

        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        currentState.onLeaving();
                        currentState = new IdleState();
                        break;

                    default:
                        currentState.keyPressed(e.getKeyCode());
                        break;
                }
            }
        });

        canvas.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                currentState.mouseUp(new Point(e.getPoint().x, e.getPoint().y), e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                currentState.mouseDown(new Point(e.getPoint().x, e.getPoint().y), e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        canvas.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentState.mouseDragged(new Point(e.getPoint().x, e.getPoint().y));
            }
        });
    }

    private void initJToolBar() {
        toolBar = new JToolBar();

        JButton loadBtn = new JButton();
        Action loadAction = new LoadAction(documentModel);
        loadAction.putValue(Action.NAME, "Load");
        loadBtn.setAction(loadAction);
        toolBar.add(loadBtn);

        JButton saveBtn = new JButton();
        Action saveAction = new SaveAction(documentModel);
        saveAction.putValue(Action.NAME, "Save");
        saveBtn.setAction(saveAction);
        toolBar.add(saveBtn);

        JButton svgBtn = new JButton();
        Action svgAction = new SvgAction(documentModel);
        svgAction.putValue(Action.NAME, "SVG Export");
        svgBtn.setAction(svgAction);
        toolBar.add(svgBtn);

        for (GraphicalObject obj : objects) {
            JButton btn = new JButton(obj.getShapeName());
            Action act = new AddShapeAction(documentModel, obj);
            act.putValue(Action.NAME, obj.getShapeName());

            btn.setAction(act);
            toolBar.add(btn);
        }

        JButton selectBtn = new JButton();
        Action selectAction = new SelectAction(documentModel);
        selectAction.putValue(Action.NAME, "Select");
        selectBtn.setAction(selectAction);
        toolBar.add(selectBtn);

        JButton eraseBtn = new JButton();
        Action eraseAction = new EraseAction(documentModel);
        eraseAction.putValue(Action.NAME, "Delete");
        eraseBtn.setAction(eraseAction);
        toolBar.add(eraseBtn);

        panel.add(toolBar);
    }

    private class Canvas extends JComponent implements DocumentModelListener {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public Canvas(DocumentModel model) {
            setFocusable(true);
            requestFocusInWindow();

            this.model = model;
            model.addDocumentModelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Renderer r = new G2DRendererImpl(g2d);
            for (GraphicalObject o : model.list()) {
                o.render(r);
                currentState.afterDraw(r, o);
            }
            currentState.afterDraw(r);
            requestFocusInWindow();
        }

        @Override
        public void documentChange() {
            repaint();
        }
    }

    private class AddShapeAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        private GraphicalObject prototype;
        private DocumentModel model;

        public AddShapeAction(DocumentModel model, GraphicalObject prototype) {
            this.prototype = prototype;
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentState.onLeaving();
            currentState = new AddShapeState(model, prototype);
        }
    }

    private class SelectAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public SelectAction(DocumentModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentState.onLeaving();
            currentState = new SelectShapeState(model);

        }
    }

    private class EraseAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public EraseAction(DocumentModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentState.onLeaving();
            currentState = new EraserState(model);

        }
    }

    private class SvgAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public SvgAction(DocumentModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("SVG export");
            if (fc.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String fileName = fc.getSelectedFile().getPath();
            SVGRendererImpl r = new SVGRendererImpl(fileName);
            for (GraphicalObject o : model.list()) {
                o.render(r);
            }
            try {
                r.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private class SaveAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public SaveAction(DocumentModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Native Saving");
            if (fc.showSaveDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String fileName = fc.getSelectedFile().getPath();
            List<String> rows = new ArrayList<>();
            for (GraphicalObject obj : model.list()) {
                obj.save(rows);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)))) {

                for (String line : rows) {
                    bw.write(line);
                }
                bw.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

        }

    }

    private class LoadAction extends AbstractAction {
        private static final long serialVersionUID = 1L;
        private DocumentModel model;

        public LoadAction(DocumentModel model) {
            this.model = model;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            List<String> lines;
            try {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Load Native");
                if (fc.showOpenDialog(GUI.this) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = fc.getSelectedFile();


                lines = Files.readAllLines(Paths.get(file.getPath()));

                if (lines.size() > 0) {
                    if (!lines.get(0).startsWith("@")) { //SVG
                        System.out.println("SVG not supported for loading");
                    } else {    //NATIVE

                        Stack<GraphicalObject> stack = new Stack<GraphicalObject>();
                        model.clear();
                        //svi retci
                        //redak po redak
                        //izvuc identifikator
                        //u mapi pronac prototip
                        //nad prototipom pozvat load
                        for (String line : lines) {
                            if (line.startsWith("@")) {
                                int spaceIndex = line.indexOf(" ");
                                String name = line.substring(0, spaceIndex);
                                System.out.println("NAME: " + name);
                                GraphicalObject obj = mapPrototype.get(name).duplicate();
                                obj.load(stack, line.substring(spaceIndex + 1));
                            }
                        }

                        for (GraphicalObject go : stack) {
                            model.addGraphicalObject(go);
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
