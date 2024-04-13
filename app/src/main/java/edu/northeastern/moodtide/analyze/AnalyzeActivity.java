package edu.northeastern.moodtide.analyze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.moodtide.HomeActivity;
import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.calendarView.CalendarActivity;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.viewModel.EntryOfMonthViewModel;
import edu.northeastern.moodtide.viewModel.EntryOfMonthViewModelFactory;

public class AnalyzeActivity extends AppCompatActivity {
    String uid;
    int selectedMonth; // 1- based
    View  home, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");



        // bottom bar
        home = findViewById(R.id.home_container);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnalyzeActivity.this, HomeActivity.class);
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
                selectedMonth = position + 1; // 1- based
                Log.e("SELECTED MONTH", selectedMonth + "");
                // get entries of selected month
                EntryOfMonthViewModelFactory factory = new EntryOfMonthViewModelFactory(uid, selectedMonth);
                EntryOfMonthViewModel viewModel = new ViewModelProvider(AnalyzeActivity.this, factory).get(EntryOfMonthViewModel.class);


                // display pie chart
                viewModel.getEntries().observe(AnalyzeActivity.this, entries -> {
                    getPieChart(entries);

                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });



    }
    public void getPieChart(List<Entry> entriesOfSelectedMonth) {
        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> categoryColors = new ArrayList<>();
        Log.e("HERE", "here");
        // get map - key being category, value being count
        Map<String, Integer> categoryCountsMap = EntryCalculator.calculateCategoryCount(entriesOfSelectedMonth);
        Log.e("MAP IS EMPTY", categoryCountsMap.isEmpty() + "");
        for (Map.Entry<String, Integer> entry : categoryCountsMap.entrySet()) {
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey()));
            switch (entry.getKey()) {
                case "Anger":
                    categoryColors.add(ContextCompat.getColor(this, R.color.pink_theme));
                    Log.e("Anger here", "anger here");
                    break;
                case "Fear":
                    categoryColors.add(ContextCompat.getColor(this, R.color.orange_theme));
                case "Love":
                    categoryColors.add(ContextCompat.getColor(this, R.color.yellow_theme));
                    break;
                case "Joy":
                    categoryColors.add(ContextCompat.getColor(this, R.color.green_theme));
                    break;
                case "Surprise":
                    categoryColors.add(ContextCompat.getColor(this, R.color.aqua_theme));
                    break;
                case "Sadness":
                    categoryColors.add(ContextCompat.getColor(this, R.color.ocean_theme));
                    break;
                default:
                    categoryColors.add(ContextCompat.getColor(this, R.color.white));
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Emotion Categories");
        dataSet.setColors(categoryColors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f); // set percentage text size
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // Enable percentage display
        pieChart.getDescription().setEnabled(false); // Disable the description label
        pieChart.getLegend().setEnabled(false); // Disable the legend
        pieChart.invalidate(); // refresh the chart;


    }



}