package edu.northeastern.moodtide.analyze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Calendar;

import edu.northeastern.moodtide.HomeActivity;
import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.calendarView.CalendarActivity;

public class AnalyzeActivity extends AppCompatActivity {
    String selectedMonth;
    View  home, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);


        // bottom bar
        home = findViewById(R.id.home_container);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyzeActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });

        // calendar activity
        calendar = findViewById(R.id.calendar_container);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyzeActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // spinner to select which month
        Spinner monthSpinner = findViewById(R.id.month_spinner);
        // set the default selected month to current month
        Calendar calendar = Calendar.getInstance();
        int currentMonthIndex = calendar.get(Calendar.MONTH); // January is 0, December is 11
        monthSpinner.setSelection(currentMonthIndex);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected month
                selectedMonth = (String) parent.getItemAtPosition(position);
                // handle selected month
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

    }
}