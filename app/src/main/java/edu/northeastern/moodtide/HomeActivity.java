package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.analyze.AnalyzeActivity;
import edu.northeastern.moodtide.getData.GetQuote;
import edu.northeastern.moodtide.calendarView.CalendarActivity;
import edu.northeastern.moodtide.viewModel.StreakViewModel;
import edu.northeastern.moodtide.viewModel.StreakViewModelFactory;
import edu.northeastern.moodtide.viewModel.TodayCountViewModel;
import edu.northeastern.moodtide.viewModel.TodayCountViewModelFactory;

public class HomeActivity extends AppCompatActivity {

    LinearLayout calendar, home, analyze;
    TextView homeTitle;
    ImageView homeIcon;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        Log.e("UID", uid);
        userRef = database.getReference(uid);

        // display daily quote
        Thread thread = new Thread(new GetQuote(this));
        thread.start();

        // display number of streaks
        StreakViewModelFactory factory = new StreakViewModelFactory(uid);
        StreakViewModel viewModel = new ViewModelProvider(this, factory).get(StreakViewModel.class);
        viewModel.getStreak().observe(this, streak -> {
            updateStreakUI(streak);
        });

        // display today counts
        View todayCountCard = findViewById(R.id.today_count_card);
        TextView cardTextView = todayCountCard.findViewById(R.id.streakText);
        cardTextView.setText("emotions today");
        TodayCountViewModelFactory todayCountViewModelFactory = new TodayCountViewModelFactory(uid);
        TodayCountViewModel todayCountViewModel = new TodayCountViewModel(uid);
        todayCountViewModel.getTodayCount().observe(this, todayCount ->{
            Log.e("TODAYCOUNT", todayCount + "");
            updateTodayCountUI(todayCount);
        });


        // add entry when clicking "+"
        home = findViewById(R.id.home_container);
        homeIcon = findViewById(R.id.home_icon);
        homeTitle = findViewById(R.id.home_title);
        homeIcon.setImageResource(R.drawable.add);
        homeTitle.setText("Add");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });

        // calendar activity
        calendar = findViewById(R.id.calendar_container);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // analyze activity
        analyze = findViewById(R.id.stats_container);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AnalyzeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void updateStreakUI(String streak) {
        View streakCard = findViewById(R.id.streak_count_card);
        TextView streakTextView = streakCard.findViewById(R.id.streakCount);
        streakTextView.setText(streak);
    }
    public void updateTodayCountUI(String todayCount) {
        View todayCountCard = findViewById(R.id.today_count_card);
        TextView todayCountTextView = todayCountCard.findViewById(R.id.streakCount);
        todayCountTextView.setText(todayCount);
    }
}