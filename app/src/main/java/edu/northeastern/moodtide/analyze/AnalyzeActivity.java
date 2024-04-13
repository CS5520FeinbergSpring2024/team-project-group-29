package edu.northeastern.moodtide.analyze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class AnalyzeActivity extends AppCompatActivity {
    String uid;
    DatabaseReference dataRef;
    int selectedMonth; // 1- based
    View  home, calendar;
    List<Entry> entriesOfSelectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);



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
                // handle selected month
                getEntriesOfSelectedMonth();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });



    }
    public void getEntriesOfSelectedMonth() {
        entriesOfSelectedMonth = new ArrayList<>();
        uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        dataRef = FirebaseDatabase.getInstance().getReference(uid).child("data");
        dataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LocalDate date = LocalDate.parse(snapshot.getKey());
                if(date.getMonthValue() == selectedMonth) {
                    for(DataSnapshot entryShot: snapshot.getChildren()) {
                        entriesOfSelectedMonth.add(entryShot.getValue(Entry.class));
//                        Log.e("CATEGPRIES", entryShot.getValue(Entry.class).getCategory());
                    }

                }
                getPieChart();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }
    public void getPieChart() {
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
                    categoryColors.add(R.color.pink_theme);
                    Log.e("Anger here", "anger here");
                    break;
                case "Fear":
                    categoryColors.add(R.color.orange_theme);
                case "Love":
                    categoryColors.add(R.color.yellow_theme);
                    break;
                case "Joy":
                    categoryColors.add(R.color.green_theme);
                    break;
                case "Surprise":
                    categoryColors.add(R.color.aqua_theme);
                    break;
                case "Sadness":
                    categoryColors.add(R.color.ocean_theme);
                    break;
                default:
                    categoryColors.add(R.color.white);
            }
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Emotion Categories");
        PieData data = new PieData(dataSet);
        dataSet.setColors(categoryColors);
        pieChart.setData(data);
        pieChart.setTransparentCircleAlpha(255);
        pieChart.invalidate(); // refresh the chart;


    }



}