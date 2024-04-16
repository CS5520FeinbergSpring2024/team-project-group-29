package edu.northeastern.moodtide.welcome;



import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;

import edu.northeastern.moodtide.HomeActivity;
import edu.northeastern.moodtide.addEntry.SelectionActivity;

public class FingerPrintImageView extends AppCompatImageView {
    public FingerPrintImageView(Context context) {
        super(context);
    }

    public FingerPrintImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FingerPrintImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (super.onTouchEvent(event)) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .alpha(1.0f)
                        .setDuration(500)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        // Optionally trigger something on animation end, like transitioning to another activity
                                        getContext().startActivity(new Intent(getContext(), SelectionActivity.class));
                                    }
                                })
                        .start();

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .alpha(0.5f)
                        .setDuration(500)
                        .start();
                return true;
        }
        return false;
    }


}

