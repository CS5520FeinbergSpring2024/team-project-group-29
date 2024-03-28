package edu.northeastern.moodtide.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

public class CircleMain extends View {

    private int numSectors;
    private int[] sectorColors;
    private String[] sectorTexts;
    private Paint paint;
    private Paint textPaint;
    private float mLastTouchAngle;
    private int selectedSectorIndex = -1;

    public CircleMain(Context context, int numSectors, int[] sectorColors,String[] sectorTexts) {
        super(context);
        this.numSectors = numSectors;
        this.sectorColors = new int[numSectors];
        for (int i = 0; i < numSectors; i++) {
            this.sectorColors[i] = ContextCompat.getColor(context, sectorColors[i]);
        }
        this.sectorTexts = sectorTexts;
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

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(45); // Set text size as needed
        textPaint.setTextAlign(Paint.Align.CENTER); // Set text alignment
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2-20;

        // Center of the view
        int centerX = width / 2;
        int centerY = height / 2;

        // Angle for each sector
        float angle = 360f / numSectors;

        // Draw sectors
        for (int i = 0; i < numSectors; i++) {
            paint.setColor(sectorColors[i % sectorColors.length]);
            canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, i * angle, angle, true, paint);
            // Draw text
            float textAngle = i * angle + angle / 2; // Center text in the sector
            float x = (float) (centerX + (radius * 0.7) * Math.cos(Math.toRadians(textAngle))); // Adjust radius for text positioning
            float y = (float) (centerY + (radius * 0.7) * Math.sin(Math.toRadians(textAngle))); // Adjust radius for text positioning
            canvas.save(); // Save canvas state
            canvas.rotate(textAngle, x, y); // Rotate canvas by the desired angle
            canvas.drawText(sectorTexts[i % sectorTexts.length], x, y, textPaint); // Draw text
            canvas.restore();
            if(i==selectedSectorIndex){
                paint.setColor(Color.argb(100, 255, 255, 255));
                canvas.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, i * angle, angle, true, paint);
            }
        }

    }

    private float getPositiveRotation() {
        float angle = getRotation() % 360;
        if (angle < 0) {
            angle += 360; // Ensure positive rotation within [0, 360)
        }
        return angle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float touchedAngle = (float) Math.toDegrees(Math.atan2(y - centerY, x - centerX));
        Log.e("Touched Angle",String.valueOf(touchedAngle));
        Log.e("Rotation",String.valueOf(getPositiveRotation()));

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchAngle = touchedAngle;
                Log.e("DOWN",String.valueOf(mLastTouchAngle));
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaAngle = touchedAngle - mLastTouchAngle;
                setRotation(getRotation() + deltaAngle);
                mLastTouchAngle = touchedAngle;
                Log.e("MOVE",String.valueOf(deltaAngle));
                return true;
            case MotionEvent.ACTION_UP:
                float sectorAngle = 360f / numSectors;
                // Calculate the angle of the touch relative to the start angle of the sectors
                float selectAngle = (360 + touchedAngle) % 360;
                Log.e("UP",String.valueOf(selectAngle));
                // Calculate the touched sector index
                int touchedSectorIndex = (int) (selectAngle / sectorAngle);
                // Get the corresponding sector text
                String touchedSectorText = sectorTexts[touchedSectorIndex];
                // Handle the touched sector here, for example:
                Log.e("Sector Touched", "Index: " + touchedSectorIndex + ", Text: " + touchedSectorText);
                selectedSectorIndex = touchedSectorIndex;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
    public int getSelection(){
        return selectedSectorIndex;
    }
}
