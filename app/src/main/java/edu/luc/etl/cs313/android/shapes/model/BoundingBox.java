package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // Implementation of BoundingBox visitor

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        // Fill doesn't change the bounding box, so delegate to the decorated shape
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        // Handle empty group case
        if (g.getShapes().isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        // Initialize boundaries with the first shape
        Location firstBox = g.getShapes().get(0).accept(this);
        int left = firstBox.getX();
        int top = firstBox.getY();
        int right = left + ((Rectangle) firstBox.getShape()).getWidth();
        int bottom = top + ((Rectangle) firstBox.getShape()).getHeight();

        // Find min and max boundaries across all shapes in the group
        for (int i = 1; i < g.getShapes().size(); i++) {
            Location box = g.getShapes().get(i).accept(this);
            int boxLeft = box.getX();
            int boxTop = box.getY();
            int boxRight = boxLeft + ((Rectangle) box.getShape()).getWidth();
            int boxBottom = boxTop + ((Rectangle) box.getShape()).getHeight();

            // Update boundaries if needed
            left = Math.min(left, boxLeft);
            top = Math.min(top, boxTop);
            right = Math.max(right, boxRight);
            bottom = Math.max(bottom, boxBottom);
        }

        // Create a rectangle with the calculated dimensions
        int width = right - left;
        int height = bottom - top;
        return new Location(left, top, new Rectangle(width, height));
    }

    @Override
    public Location onLocation(final Location l) {
        // Get the bounding box of the contained shape
        Location innerBoundingBox = l.getShape().accept(this);

        // Adjust the location by adding the current location's coordinates
        return new Location(
                l.getX() + innerBoundingBox.getX(),
                l.getY() + innerBoundingBox.getY(),
                innerBoundingBox.getShape()
        );
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        // The bounding box of a rectangle is the rectangle itself
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        // StrokeColor doesn't change the bounding box, so delegate to the decorated shape
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        // Outline doesn't change the bounding box, so delegate to the decorated shape
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        // Since Polygon is implemented as a special case of Group,
        // we can delegate to the onGroup method
        return onGroup(s);
    }
}