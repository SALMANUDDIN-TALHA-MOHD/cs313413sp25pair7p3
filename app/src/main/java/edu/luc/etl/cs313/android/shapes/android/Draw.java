package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        // IMPORTANT: Set the default style to STROKE
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int oldColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        // Outline should already be using STROKE style
        // but just to be safe, ensure it here
        Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        if (s.getPoints().size() < 2) return null;

        final float[] pts = new float[s.getPoints().size() * 4];

        for (int i = 0; i < s.getPoints().size(); i++) {
            Point p1 = s.getPoints().get(i);
            Point p2 = s.getPoints().get((i + 1) % s.getPoints().size());

            pts[i * 4] = p1.getX();
            pts[i * 4 + 1] = p1.getY();
            pts[i * 4 + 2] = p2.getX();
            pts[i * 4 + 3] = p2.getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }
}