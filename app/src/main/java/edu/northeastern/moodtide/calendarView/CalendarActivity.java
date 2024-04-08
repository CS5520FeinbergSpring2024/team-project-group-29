package edu.northeastern.moodtide.calendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.northeastern.moodtide.R;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar clickedCalendar = calendarDay.getCalendar();
                Log.e("calendar", "1");
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(clickedCalendar.getTime());
                Log.e("calendar", formattedDate);

                showEntryDialog(formattedDate);
            }
        });
    }
    private void showEntryDialog(String date) {
        // Inflate custom layout for the dialog
        EntryDialog dialogFragment = new EntryDialog();
        Log.e("calendar", "view");

        // Find views within the custom layout
        Bundle args = new Bundle();
        args.putString("date", date);
        dialogFragment.setArguments(args);
        Log.e("calendar", date);

        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "EntryDialog");
    }
}