package edu.northeastern.moodtide.calendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import edu.northeastern.moodtide.HomeActivity;
import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.analyze.AnalyzeActivity;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.User;
import edu.northeastern.moodtide.shapes.CustomDotDrawable;

//Activity to show calendar view and available days with entries
public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    DatabaseReference userRef;
    List<CalendarDay> calendarDays;
    LinearLayout home, analyze;
    HashMap<String, HashMap<String, Entry>> record;
    User user;
    Set<String> validDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //Retrieve user's data
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        userRef = FirebaseDatabase.getInstance().getReference(uid);
        getUser();

        //Set up calendarView and onClick to open the entries of the day
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar clickedCalendar = calendarDay.getCalendar();
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(clickedCalendar.getTime());
                List<Entry> entries = getEntries(formattedDate);
                if(entries!=null){
                    showEntryDialog(entries, formattedDate);
                }
            }
        });

        //Change the color of icon and text to signify the current page
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.calendar);
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.ocean_theme), android.graphics.PorterDuff.Mode.SRC_IN);
        ImageView calendarIcon = findViewById(R.id.calendar_icon);
        calendarIcon.setImageDrawable(drawable);
        TextView calendarTitle = findViewById(R.id.calendar_title);
        calendarTitle.setTextColor(getColor(R.color.ocean_theme));

        //Handled onClick events to navigate to other activities
        home = findViewById(R.id.home_container);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        analyze = findViewById(R.id.stats_container);
        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, AnalyzeActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method to retrieve user data from database and fill the calendar grid with corresponding icons
    public void getUser() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                    record = user.getData();
                    validDates = record.keySet();
                    drawIcons();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    //Method to loop through dates with entries and gather calendarDays with icons to show on the calendar
    public void drawIcons() {

        calendarDays = new ArrayList<>();
        for(String date:validDates){
            drawIcon(date);
        }
        calendarView.setCalendarDays(calendarDays);

    }

    //Method to draw the corresponding icon for each day based on the categories of emotions in the day' entries
    public void drawIcon(String date){
        List<Entry> entries = getEntries(date);
        Set<Integer> colors = new HashSet<>();
        for(Entry e: entries){
            String category=e.getCategory();
            int colorID=-1;
            switch (category){
                case "Love":
                    colorID=R.color.yellow_theme;
                    break;
                case "Anger":
                    colorID=R.color.pink_theme;
                    break;
                case "Joy":
                    colorID=R.color.green_theme;
                    break;
                case "Surprise":
                    colorID=R.color.aqua_theme;
                    break;
                case "Sadness":
                    colorID=R.color.ocean_theme;
                    break;
                case "Fear":
                    colorID=R.color.orange_theme;
            }
            colors.add(colorID);
        }
        int[] colorList = new int[colors.size()];
        int i=0;
        for(int color:colors){
            colorList[i++]=color;
        }
        CustomDotDrawable dots = new CustomDotDrawable(this,colorList);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date dateFormatted = dateFormat.parse(date);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormatted);
                CalendarDay calendarDay= new CalendarDay(calendar);
                calendarDay.setImageDrawable(dots);
                calendarDays.add(calendarDay);
            }

        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception if necessary
        }
    }

    //Method to get the day's entries from the database
    private List<Entry> getEntries(String date){

        if(!record.containsKey(date)){return null;}
        else{
            List<Entry> entries=new ArrayList<>(record.get(date).values());
            return entries;
        }
    }

    //Method to show the entry dialog with option to turn to next or previous page if applicable
    private void showEntryDialog(List<Entry> entries, String date) {
        // Inflate custom layout for the dialog
        EntryDialog dialogFragment = new EntryDialog();
        Log.e("calendar", "view");

        // pass the data to the custom dialog class
        Bundle args = new Bundle();
        ArrayList<? extends Parcelable> parcelableList = new ArrayList<>(entries);

        args.putParcelableArrayList("entries",parcelableList);
        args.putString("date", date);
        dialogFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "EntryDialog");
    }
}