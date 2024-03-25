package edu.northeastern.moodtide.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
public class CircleMain extends View {

    private int numSectors;
    private int[] sectorColors;
    private Paint paint;

    public CircleMain(Context context, int numSectors, int[] sectorColors) {
        super(context);
        this.numSectors = numSectors;
        this.sectorColors = sectorColors;
        init();
    }

    public CircleMain(Context context, AttributeSet attrs, int numSectors, int[] sectorColors) {
        super(context, attrs);
        this.numSectors = numSectors;
        this.sectorColors = sectorColors;
        init();
    }

    public CircleMain(Context context, AttributeSet attrs, int defStyle, int numSectors, int[] sectorColors) {
        super(context, attrs, defStyle);
        this.numSectors = numSectors;
        this.sectorColors = sectorColors;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        // Center of the view
        int centerX = width / 2;
        int centerY = height / 2;

        // Angle for each sector
        float angle = 360f / numSectors;

        // Draw sectors
        for (int i = 0; i < numSectors; i++) {
            paint.setColor(sectorColors[i % sectorColors.length]);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, i * angle, angle, true, paint);
        }
    }
}
