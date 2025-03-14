package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

    @Override
    public Integer onPolygon(final Polygon p) {
        // Polygon is one basic shape
        return 1;
    }

    @Override
    public Integer onCircle(final Circle c) {
        // Circle is one basic shape
        return 1;
    }

    @Override
    public Integer onGroup(final Group g) {
        // Sum up the count of all shapes in the group
        int result = 0;
        for (Shape s : g.getShapes()) {
            result += s.accept(this);
        }
        return result;
    }

    @Override
    public Integer onRectangle(final Rectangle q) {
        // Rectangle is one basic shape
        return 1;
    }

    @Override
    public Integer onOutline(final Outline o) {
        // Outline is a decorator, so delegate to the decorated shape
        return o.getShape().accept(this);
    }

    @Override
    public Integer onFill(final Fill c) {
        // Fill is a decorator, so delegate to the decorated shape
        return c.getShape().accept(this);
    }

    @Override
    public Integer onLocation(final Location l) {
        // Location is a decorator, so delegate to the decorated shape
        return l.getShape().accept(this);
    }

    @Override
    public Integer onStrokeColor(final StrokeColor c) {
        // StrokeColor is a decorator, so delegate to the decorated shape
        return c.getShape().accept(this);
    }
}