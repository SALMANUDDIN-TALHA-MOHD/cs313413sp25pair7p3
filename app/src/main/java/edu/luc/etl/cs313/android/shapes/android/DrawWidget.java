package edu.luc.etl.cs313.android.shapes.android;

import edu.luc.etl.cs313.android.shapes.model.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawWidget extends View {

    private final Paint paint = new Paint();

    // Flag to determine whether to use the visual fixtures or the test fixtures
    private boolean useVisualOutput = true;

    public DrawWidget(final Context context, final AttributeSet attrs, final int defStyle) {
        this(context);
    }

    public DrawWidget(final Context context, final AttributeSet attrs) {
        this(context);
    }

    public DrawWidget(final Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);


        // Define paint for the outer big rectangle
        Paint outerBoxPaint = new Paint();
        outerBoxPaint.setStyle(Paint.Style.STROKE); // Outline only
        outerBoxPaint.setColor(Color.BLACK); // Black border
        outerBoxPaint.setStrokeWidth(1); // Thickness of the border

        // Draw the outer big rectangle (Adjust size & position if needed)
        int left = 1, top = 1, right = 475, bottom = 323;
        canvas.drawRect(left, top, right, bottom, outerBoxPaint);

        // Choose which model to draw based on the flag
        final Shape shape = useVisualOutput ? Fixtures.complexGroup : Fixtures.complexGroup;

        // Draw the shapes inside the big rectangle
        canvas.save();
        // Apply a translation to move the small rectangle and circle to the extreme top-left
        canvas.translate(-28, -78); // Adjust values as needed to remove gaps
        shape.accept(new Draw(canvas, paint));
        canvas.restore();
    }

    // Optional: Method to toggle between visual and test fixtures
    public void setUseVisualOutput(boolean useVisual) {
        this.useVisualOutput = useVisual;
        invalidate(); // Trigger redraw
    }
}