package edu.northeastern.moodtide.notification;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import edu.northeastern.moodtide.R;


//Customized time picker dialog to pick time of daily reminder
public class CustomTimePickerDialog {

    public static void show(Context context, final OnTimeSetListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_time_picker, null);
        final NumberPicker hoursPicker = view.findViewById(R.id.hoursPicker);
        final NumberPicker minutesPicker = view.findViewById(R.id.minutesPicker);

        //set value range within hour and minute
        hoursPicker.setMaxValue(23);
        hoursPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setTitle("Select time for your daily reminder")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hours = hoursPicker.getValue();
                        int minutes = minutesPicker.getValue();
                        listener.onTimeSet(hours, minutes);
                    }
                });
        builder.show();
    }

    //pass the selected hour and minute to be handled
    public interface OnTimeSetListener {
        void onTimeSet(int hours, int minutes);
    }
}
