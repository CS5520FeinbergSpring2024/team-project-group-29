package edu.northeastern.moodtide.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CustomDotDrawable extends Drawable {

    private static final int MAX_DOTS = 6;
    private static final int ROWS = 2;
    private static final int COLS = 3;
    private static final int DOT_SIZE_PERCENTAGE = 30; // Percentage of canvas width for dot size

    private int numDots;
    private int[] dotColors;

    public CustomDotDrawable(Context context, int[] dotColors) {
        this.numDots=dotColors.length;
        this.dotColors = new int[numDots];
        for (int i = 0; i < numDots; i++) {
            this.dotColors[i] = ContextCompat.getColor(context, dotColors[i]);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        Log.e("width",Integer.toString(canvasWidth));
        Log.e("height",Integer.toString(canvasHeight));

        // Calculate dot size based on canvas width
        float dotSize = canvasWidth * DOT_SIZE_PERCENTAGE / 100;
        Log.e("canvas",Float.toString(dotSize));

        // Calculate spacing between dots
        float horizontalSpacing = (canvasWidth - COLS * dotSize) / (COLS + 1);
        float verticalSpacing = (canvasHeight - ROWS * dotSize) / (ROWS + 1);

        // Create paint for drawing dots
        Paint dotPaint = new Paint();
        dotPaint.setStyle(Paint.Style.FILL);

        // Draw dots
        for (int i = 0; i < MAX_DOTS; i++) {
            int row = i / COLS;
            int col = i % COLS;

            //Log.e("row",Integer.toString(row));
            //Log.e("column",Integer.toString(col));


            float x = (col + 1) * horizontalSpacing + col * dotSize;
            float y = (row + 1) * verticalSpacing + row * dotSize;
            //Log.e("x",Float.toString(x));
            //Log.e("y",Float.toString(y));

            if (i < numDots) {
                dotPaint.setColor(dotColors[i]);
                //Log.e("color",Integer.toString(dotColors[i]));
                canvas.drawCircle(x + dotSize / 2, y + dotSize / 2, dotSize / 2, dotPaint);
            } else {
                // Draw transparent dot (or white dot) to hide excess dots
                dotPaint.setColor(Color.TRANSPARENT); // Transparent color
                canvas.drawCircle(x + dotSize / 2, y + dotSize / 2, dotSize / 2, dotPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required to implement due to Drawable class, but we can leave it empty
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        // This method is required to implement due to Drawable class, but we can return a default value
        return PixelFormat.OPAQUE;
    }
}
