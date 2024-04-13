package edu.northeastern.moodtide.analyze;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import edu.northeastern.moodtide.R;

public class AnalyzeActivity extends AppCompatActivity {
    String selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        // spinner to select which month
        Spinner monthSpinner = findViewById(R.id.month_spinner);
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