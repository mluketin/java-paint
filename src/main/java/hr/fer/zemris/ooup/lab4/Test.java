package hr.fer.zemris.ooup.lab4;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.ooup.lab4.model.GraphicalObject;
import hr.fer.zemris.ooup.lab4.model.LineSegment;
import hr.fer.zemris.ooup.lab4.model.Oval;

public class Test {

	public static void main(String[] args) {

		List<GraphicalObject> objects  = new ArrayList<>();

		objects.add(new LineSegment());
		objects.add(new Oval());

		GUI gui = new GUI(objects);
		gui.setVisible(true);

	}

}
