package edu.northeastern.moodtide.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;


//Customized spinnable circle shape for selection activities
public class CircleMain extends View {

    private int numSectors;
    private int[] sectorColors;
    private String[] sectorTexts;
    private Paint paint;
    private Paint textPaint;
    private float mLastTouchAngle;
    private int selectedSectorIndex = -1;
    String sectorTitle ="";
    int selectedColor= -1;
    private OnSectorSelectedListener listener;

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

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(45);
        textPaint.setTextAlign(Paint.Align.CENTER);
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
            float x = (float) (centerX + (radius * 0.6) * Math.cos(Math.toRadians(textAngle))); // Adjust radius for text positioning
            float y = (float) (centerY + (radius * 0.6) * Math.sin(Math.toRadians(textAngle))); // Adjust radius for text positioning
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

    public void setOnSectorSelectedListener(OnSectorSelectedListener listener) {
        this.listener = listener;
    }

    private float getPositiveRotation() {
        float angle = getRotation() % 360;
        if (angle < 0) {
            angle += 360; // Ensure positive rotation within [0, 360)
        }
        return angle;
    }

    //handle onTouchEvent to show rotational status and selected sector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        // Calculate the distance from the touch point to the center of the circle
        float distanceToCenter = calculateDistance(x, y, getWidth() / 2f, getHeight() / 2f);
        float radius = calculateRadius();
        if (distanceToCenter <= radius) {
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
                    if(selectedSectorIndex == touchedSectorIndex){
                        selectedSectorIndex=-1;
                    }else{
                        selectedSectorIndex = touchedSectorIndex;
                        sectorTitle = sectorTexts[selectedSectorIndex];
                        selectedColor = sectorColors[selectedSectorIndex];
                    }
                    invalidate();
                    if (listener != null) {
                        listener.onSectorSelected(selectedSectorIndex, sectorTitle, selectedColor);
                    }
                    return true;
            }
            return super.onTouchEvent(event);
        }
        return false;
    }
    public int getSelection(){
        return selectedSectorIndex;
    }
    private float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private float calculateRadius() {
        return Math.min(getWidth(), getHeight()) / 2f-20;
    }

    public interface OnSectorSelectedListener {
        void onSectorSelected(int sectorIndex, String sectorTitle, int selectedColor);
    }

    public float getCurrentRotationAngle() {
        return getRotation();
    }

    public void setRotationAngle(float angle) {
        setRotation(angle);
    }

    //handled configuration change
    public void setSelectedSectorIndex(int index) {
        selectedSectorIndex = index;
        sectorTitle=sectorTexts[selectedSectorIndex];
        selectedColor=sectorColors[selectedSectorIndex];
        invalidate();
    }
    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putFloat("rotationAngle", getCurrentRotationAngle());
        bundle.putInt("selectedSectorIndex", getSelection());
        bundle.putParcelable("superState", super.onSaveInstanceState());
        return bundle;
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable("superState"));
            setRotationAngle(bundle.getFloat("rotationAngle"));
            setSelectedSectorIndex(bundle.getInt("selectedSectorIndex"));
            if (listener != null) {
                listener.onSectorSelected(selectedSectorIndex, sectorTitle, selectedColor);
            }
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
