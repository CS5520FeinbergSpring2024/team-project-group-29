package edu.northeastern.moodtide.analyze;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyXAxisFormatter extends ValueFormatter {
    private final String[] mValues;

    public MyXAxisFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if (index >= 0 && index < mValues.length) {
            return mValues[index];
        }
        return "";
    }
}
