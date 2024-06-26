package edu.northeastern.moodtide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.analyze.AnalyzeActivity;
import edu.northeastern.moodtide.welcome.GetQuote;
import edu.northeastern.moodtide.calendarView.CalendarActivity;
import edu.northeastern.moodtide.notification.CustomTimePickerDialog;
import edu.northeastern.moodtide.notification.NotificationReceiver;
import edu.northeastern.moodtide.viewModel.StreakViewModel;
import edu.northeastern.moodtide.viewModel.StreakViewModelFactory;
import edu.northeastern.moodtide.viewModel.TodayCountViewModel;
import edu.northeastern.moodtide.viewModel.TodayCountViewModelFactory;

//Home page
public class HomeActivity extends AppCompatActivity {

    LinearLayout calendar, analyze;
    DatabaseReference userRef, timeRef;
    int selectedHour, selectedMinute;
    String savedTime ="null";
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Connect database of the current base
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        Log.e("UID", uid);
        userRef = database.getReference(uid);

        // fingerprint animation
        ImageView fingerprint = findViewById(R.id.fingerprintImage);
        fingerprint.animate().alpha(0.5f).setDuration(500);

        // display daily quote
        Thread thread = new Thread(new GetQuote(this));
        thread.start();

        // display number of streaks
        View streakCountCard = findViewById(R.id.streak_count_card);
        LinearLayout streakView = streakCountCard.findViewById(R.id.streak_card);
        GradientDrawable background = (GradientDrawable) streakView.getBackground();
        background.setStroke(4,  getColor(R.color.ocean_theme));

        View todayCountCard = findViewById(R.id.today_count_card);
        LinearLayout todayView = todayCountCard.findViewById(R.id.streak_card);
        GradientDrawable background2 = (GradientDrawable) todayView.getBackground();
        background2.setStroke(4,  getColor(R.color.ocean_theme));

        StreakViewModelFactory factory = new StreakViewModelFactory(uid);
        StreakViewModel viewModel = new ViewModelProvider(this, factory).get(StreakViewModel.class);
        viewModel.getStreak().observe(this, streak -> {
            updateStreakUI(streak);
        });

        ImageView imageView = todayCountCard.findViewById(R.id.streakText);
        imageView.setImageResource(R.drawable.emotion_today_text);

        TodayCountViewModelFactory todayCountViewModelFactory = new TodayCountViewModelFactory(uid);
        TodayCountViewModel todayCountViewModel = new TodayCountViewModel(uid);
        todayCountViewModel.getTodayCount().observe(this, todayCount ->{
            Log.e("TODAYCOUNT", todayCount + "");
            updateTodayCountUI(todayCount);
        });

        //Change the color of icon and text to signify the current page
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.home);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.ocean_theme), android.graphics.PorterDuff.Mode.SRC_IN);
        ImageView homeIcon = findViewById(R.id.home_icon);
        homeIcon.setImageDrawable(drawable);
        TextView homeTitle = findViewById(R.id.home_title);
        homeTitle.setTextColor(getColor(R.color.ocean_theme));

        //Handle onClick events to navigate to other pages
        calendar = findViewById(R.id.calendar_container);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
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

    //check notification permission when initialized and set up notification
    protected void onStart(){
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_POST_NOTIFICATIONS);
            }else{
                getNotificationTime();
            }
        }else{
            getNotificationTime();
        }
    }

    //handle action when permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getNotificationTime();
            } else {
                Toast.makeText(this, "You will not be receiving your daily reminder", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to retrieve notification time from database or ask for user when not initialized
    public void getNotificationTime(){

        timeRef = userRef.child("notificationTime");
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedTime = dataSnapshot.getValue(String.class);
                if(savedTime.equals("null")){
                    CustomTimePickerDialog.show(HomeActivity.this, new CustomTimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(int hours, int minutes) {
                            selectedHour = hours;
                            selectedMinute = minutes;
                            timeRef.setValue(selectedHour+":"+selectedMinute);
                            setUpNotification(selectedHour, selectedMinute);
                        }
                    });
                }else{
                    String[] timeComponents = savedTime.split(":");
                    selectedHour = Integer.parseInt(timeComponents[0]);
                    selectedMinute = Integer.parseInt(timeComponents[1]);
                    setUpNotification(selectedHour, selectedMinute);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    //method to set up notification timer and call the notification receiver class to send out and schedule further notifications
    public void setUpNotification(int hour, int minute){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }
}