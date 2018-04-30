This project was made for 4th laboratory exercise for course Design Patterns in Software Design (https://www.fer.unizg.hr/en/course/dpisd) for academic year 2015/2016 at University of Zagreb, Faculty of Electrical Engineering and Computing.

DrawingEditor project is a Java swing application.

User can:
- add geometrical shapes like lines and ovals
- group shapes
- delete shapes
- change sequence of drawing (important for overllaping shapes)
- modifying objects (translation, resize)
- saving and loading drawing in "native" format
- export into an SVG file
	
Following design patterns are used for this application:
- Observer - describes relationship between data model and drawn components
- Composite - enables transparent execution of actions on single and grouped elements
- Iterator - iterating through elements of drawing
- Prototype - enables creation of toolbar for adding new shapes and is not dependent on current concrete geometrical shapes
- Factory - creation of concrete shapes based on shape's symbolic name
- State - enables adding new tools without modifying component which is processing user input (mouse, keyboard)
- Bridge - used for transparent rendering of shapes and exporting into different image formats like SVG
	
Usage:
- open project and run Test class
- clicking on the Line or Oval button and then clicking in editor will render Line (Oval) shape
- clicking Select and then clicking shape in editor will select that shape (multiple shape selecting with Ctrl)
- when one shape is selected it can be:
	- resized by clicking on bound box and dragging mouse
	- using arrows translated
	- using + and - it can come in front or in back when two shapes are overlapped
- clicking on Delete and then press and drag mouse over shapes you want to delete (dotted line will be shown)