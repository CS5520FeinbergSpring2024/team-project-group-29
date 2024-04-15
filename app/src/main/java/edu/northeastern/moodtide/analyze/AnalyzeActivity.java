package edu.northeastern.moodtide.analyze;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import edu.northeastern.moodtide.colorMap.CategoryColors;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.Trigger;
import edu.northeastern.moodtide.viewModel.EntryOfMonthViewModel;
import edu.northeastern.moodtide.viewModel.EntryOfMonthViewModelFactory;
import edu.northeastern.moodtide.viewModel.TriggerViewModel;
import edu.northeastern.moodtide.viewModel.TriggerViewModelFactory;

public class AnalyzeActivity extends AppCompatActivity {
    String uid;
    int selectedMonth; // 1- based
    View  home, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.stats);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.ocean_theme), android.graphics.PorterDuff.Mode.SRC_IN);
        ImageView analyzeIcon = findViewById(R.id.analyze_icon);
        analyzeIcon.setImageDrawable(drawable);
        TextView calendarTitle = findViewById(R.id.analyze_text);
        calendarTitle.setTextColor(getColor(R.color.ocean_theme));


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

                viewModel.getEntries().observe(AnalyzeActivity.this, entries -> {
                    // display pie chart
                    getPieChart(entries);
                    // display bar chart
                    getBarChart(entries);

                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });



    }

    public void getBarChart(List<Entry> entriesOfSelectedMonth) {
        // get the triggers first
        TriggerViewModelFactory triggerViewModelFactory = new TriggerViewModelFactory(uid);
        TriggerViewModel triggerViewModel = new ViewModelProvider(AnalyzeActivity.this, triggerViewModelFactory).get(TriggerViewModel.class);


        triggerViewModel.getTriggers().observe(AnalyzeActivity.this, triggers -> {


            Log.e("TRIGGERS EMPTY", triggers.isEmpty() + "");
            Map<Trigger, Map<String, Integer>> triggerCategoryCount = EntryCalculator.calculateCategoryCountPerTrigger(entriesOfSelectedMonth, triggers);
//            Log.e("MAP EMPTY", triggerCategoryCount.isEmpty() + "");
            List<BarEntry> barEntries = new ArrayList<>();
            List<List<Integer>> allColors = new ArrayList<>();
            String[] labels = new String[triggers.size()];
            int index = 0;
            for (Trigger trigger : triggers) {


                // xaxis labels
                labels[index] = trigger.getName();

                Map<String, Integer> categoriesMap = triggerCategoryCount.get(trigger);
                if(categoriesMap.isEmpty()) {
                    Log.e("TRIGGER MAP EMPTRY", trigger.getName() + "is emptry");
                    continue; // skip the unselected triggers
                }
                List<Integer> categoryCounts = new ArrayList<>();
                List<Integer> categoryColors = new ArrayList<>();


                // color for each bar
                for (String category :  categoriesMap.keySet()) {
                    Log.e("Category", category + "");
                    categoryCounts.add(categoriesMap.get(category));
                    int color = CategoryColors.getColor(this, category);
                    Log.e("COLOR", String.format("#%08X", (color & 0xFFFFFFFF)));
                    categoryColors.add(color);

                }

                // convert to list of float
                float[] emotionValues = new float[categoryCounts.size()];
                for (int i = 0; i < categoryCounts.size(); i++) {
                    emotionValues[i] = categoryCounts.get(i);
                }

                barEntries.add(new BarEntry(index, emotionValues));
                allColors.add(categoryColors);
                index++;
            }


            BarDataSet dataSet = new BarDataSet(barEntries, "Emotion by Trigger");
            dataSet.setColors(new ArrayList<Integer>()); // Clear existing colors
            // Setting colors for each segment in each bar individually
            for (int i = 0; i < barEntries.size(); i++) {
                BarEntry entry = barEntries.get(i);
                List<Integer> colors = allColors.get(i);
                for (int valueIndex = 0; valueIndex < entry.getYVals().length; valueIndex++) {
                    dataSet.addColor(colors.get(valueIndex));  // Directly setting each segment's color
                    Log.e("SET COLOR", String.format("#%08X", (colors.get(valueIndex) & 0xFFFFFFFF)));

                }
            }
            BarData barData = new BarData(dataSet);
            BarChart barChart = findViewById(R.id.barChart);
            barChart.setData(barData);

            // set labels for xaxis
            XAxis xAxis = barChart.getXAxis();
            xAxis.setGranularity(1f); // interval 1
            xAxis.setValueFormatter(new MyXAxisFormatter(labels));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set the labels to appear at the bottom
            xAxis.setDrawGridLines(false); // turn off grid lines
//            barChart.setFitBars(true); // make the x-axis fit exactly all bars

            // display percantage of each category
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarStackedLabel(float value, BarEntry entry) {
                    float[] vals = entry.getYVals();
                    if (vals == null) {
                        return ""; // When not stacked, this won't trigger, but just in case
                    }

                    // Calculate the total sum of the stack
                    float total = 0f;
                    for (float val : vals) {
                        total += val;
                    }

                    // Calculate percentage
                    if (total > 0) {
                        float percentage = (value / total) * 100f;
                        return String.format("%.1f%%", percentage); // One decimal place
                    } else {
                        return "";
                    }
                }
            });
            barChart.getDescription().setEnabled(false); // Disable the description label
            barChart.getLegend().setEnabled(false); // Disable the legend
            barChart.invalidate(); // refresh

        });


    }
    public void getPieChart(List<Entry> entriesOfSelectedMonth) {
        PieChart pieChart = findViewById(R.id.pieChart);
        List<PieEntry> pieEntries = new ArrayList<>();
        List<Integer> categoryColors = new ArrayList<>();
        // get map - key being category, value being count
        Map<String, Integer> categoryCountsMap = EntryCalculator.calculateCategoryCount(entriesOfSelectedMonth);
        Log.e("MAP IS EMPTY", categoryCountsMap.isEmpty() + "");
        for (Map.Entry<String, Integer> entry : categoryCountsMap.entrySet()) {
            String category = entry.getKey();
            pieEntries.add(new PieEntry(entry.getValue().floatValue(), category));
            categoryColors.add(CategoryColors.getColor(this, category));

        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "Emotion Categories");
        dataSet.setColors(categoryColors); // set segments color
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