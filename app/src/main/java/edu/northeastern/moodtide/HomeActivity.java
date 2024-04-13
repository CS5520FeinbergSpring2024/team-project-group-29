package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.getData.GetQuote;
import edu.northeastern.moodtide.calendarView.CalendarActivity;
import edu.northeastern.moodtide.getData.GetStreak;
import edu.northeastern.moodtide.getData.GetTodayCount;

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
        userRef = database.getReference(uid);

        // display daily quote
        Thread thread = new Thread(new GetQuote(this));
        thread.start();

        // display number of streaks
        Thread thread1 = new Thread(new GetStreak(this, userRef));
        thread1.start();

        // display today counts
        Thread thread2 = new Thread(new GetTodayCount(this, userRef));
        thread2.start();



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
        calendar = findViewById(R.id.calendar_container);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }
}