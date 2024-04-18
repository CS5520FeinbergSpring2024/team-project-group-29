package edu.northeastern.moodtide.colorMap;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import edu.northeastern.moodtide.R;

public class CategoryColors {
    public static int getColor(Context context, String category) {
        switch (category) {
            case "Anger":
                return ContextCompat.getColor(context, R.color.pink_theme);
            case "Fear":
                return ContextCompat.getColor(context, R.color.orange_theme);
            case "Love":
                return ContextCompat.getColor(context, R.color.yellow_theme);
            case "Joy":
                return ContextCompat.getColor(context, R.color.green_theme);
            case "Surprise":
                return ContextCompat.getColor(context, R.color.aqua_theme);
            case "Sadness":
                return ContextCompat.getColor(context, R.color.ocean_theme);
            default:
                return ContextCompat.getColor(context, R.color.white);
        }
    }
}
