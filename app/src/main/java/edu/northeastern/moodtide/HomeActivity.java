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
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.analyze.AnalyzeActivity;
import edu.northeastern.moodtide.welcome.GetQuote;
import edu.northeastern.moodtide.calendarView.CalendarActivity;
import edu.northeastern.moodtide.notification.NotificationReceiver;
import edu.northeastern.moodtide.viewModel.StreakViewModel;
import edu.northeastern.moodtide.viewModel.StreakViewModelFactory;
import edu.northeastern.moodtide.viewModel.TodayCountViewModel;
import edu.northeastern.moodtide.viewModel.TodayCountViewModelFactory;

public class HomeActivity extends AppCompatActivity {

    LinearLayout calendar, home, analyze;
    TextView homeTitle;
    ImageView homeIcon;
    DatabaseReference userRef;
    int selectedHour, selectedMinute;
    private static final int PERMISSION_REQUEST_POST_NOTIFICATIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        Log.e("UID", uid);
        userRef = database.getReference(uid);

        // fingerprint animation
        ImageView fingerprint = findViewById(R.id.fingerprintImage);
        fingerprint.animate().alpha(0.5f).setDuration(500);

        fingerprint.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Start the animation to fade in and enlarge the fingerprint image
                    fingerprint.animate()
                            .scaleX(1.1f).scaleY(1.1f) // Scale up to 110%
                            .alpha(1.0f) // Animate alpha to fully visible
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    // Optionally trigger something on animation end, like transitioning to another activity
                                    startActivity(new Intent(HomeActivity.this, SelectionActivity.class));
                                }
                            });
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Reverse the animation on finger lift
                    fingerprint.animate()
                            .scaleX(1.0f).scaleY(1.0f) // Scale back to normal size
                            .alpha(0.5f) // Animate alpha back to semi-transparent
                            .setDuration(500);
                    return true;

                }
                return false;
            }
        });







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
    protected void onStart(){
        super.onStart();
        //check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("NOTI","ask for permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_POST_NOTIFICATIONS);
            }else{
                Log.e("NOTI","permission already granted");
                getNotificationTime();
                setUpNotification();
            }
        }else{
            Log.e("NOTI","no need to ask for permission");
            getNotificationTime();
            setUpNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("NOTI","ask for permission successful");
                getNotificationTime();
                setUpNotification();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "You will not be receiving your daily reminder", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getNotificationTime(){
        selectedHour = getSharedPreferences("memory", Context.MODE_PRIVATE).getInt("hour", -1);
        selectedMinute = getSharedPreferences("memory", Context.MODE_PRIVATE).getInt("minute", -1);
        Log.e("TIME",selectedHour+" : "+selectedMinute);
        if(selectedHour==-1 ||selectedMinute==-1){
            // Get current time
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // Display the selected time
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    SharedPreferences sharedPreferences = getSharedPreferences("memory", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("hour", selectedHour);
                    editor.putInt("minute", selectedMinute);
                    editor.apply();
                    Log.e("TIME",selectedHour+" : "+selectedMinute);
                }
            }, hour, minute, true);

            timePickerDialog.setTitle("Select a time for your daily reminder");
            timePickerDialog.setCancelable(false);

            // Show the dialog
            timePickerDialog.show();
        }
    }

    public void setUpNotification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
        //calendar.set(Calendar.MINUTE, selectedMinute);
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 0);

       long triggerTimeMillis = System.currentTimeMillis() + 20000;
       // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }
}