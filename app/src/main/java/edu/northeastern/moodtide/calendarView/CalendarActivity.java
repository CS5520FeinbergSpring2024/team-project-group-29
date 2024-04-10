package edu.northeastern.moodtide.calendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

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

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.User;
import edu.northeastern.moodtide.shapes.CustomDotDrawable;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    DatabaseReference userRef, dateRef;
    HashMap<String, HashMap<String, Entry>> record;
    Set<String> validDates;
    User user;
    List<String> daysWithEntry;
    List<CalendarDay> calendarDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        userRef = FirebaseDatabase.getInstance().getReference(uid);
        getUser();

        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar clickedCalendar = calendarDay.getCalendar();
                Log.e("calendar", "1");
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(clickedCalendar.getTime());
                Log.e("calendar", formattedDate);
                List<Entry> entries = getEntries(formattedDate);
                if(entries!=null){
                    showEntryDialog(entries, formattedDate);
                }
            }
        });
        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                paintCurrentMonth();
            }
        });
        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                paintCurrentMonth();
            }
        });
    }

    public void getUser() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists at the specified path
                    user = dataSnapshot.getValue(User.class);
                    record = user.getData();
                    validDates = record.keySet();
                    paintCurrentMonth();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void paintCurrentMonth() {
        Calendar currentMonthCalendar = calendarView.getCurrentPageDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        String currentMonthYearString = dateFormat.format(currentMonthCalendar.getTime());
        Log.e("Get month",currentMonthYearString);
        calendarDays = new ArrayList<>();
        for(String date:validDates){
            String entryMonthYear = date.substring(0, 7);
            if(currentMonthYearString.equals(entryMonthYear)){
                Log.e("Get month",date);
                paintDate(date);
            }
        }
        Log.e("painted?",Integer.toString(calendarDays.size()));
        calendarView.setCalendarDays(calendarDays);
        //calendarView.clearSelectedDays();

    }
    public void paintDate(String date){
        List<Entry> entries = getEntries(date);
        Set<Integer> colors = new HashSet<>();
        for(Entry e: entries){
            String category=e.getCategory();
            Log.e("Get entry",category);
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
        if(dots!=null){Log.e("paint","paint");}
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            // Parse the date string into a Date object
            Date dateFormatted = dateFormat.parse(date);

            // Convert the Date object into a CalendarDay object
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


    private List<Entry> getEntries(String date){

        if(!record.containsKey(date)){return null;}
        else{
            List<Entry> entries=new ArrayList<>(record.get(date).values());
            return entries;
        }
    }
    private void showEntryDialog(List<Entry> entries, String date) {
        // Inflate custom layout for the dialog
        EntryDialog dialogFragment = new EntryDialog();
        Log.e("calendar", "view");

        // Find views within the custom layout
        Bundle args = new Bundle();
        ArrayList<? extends Parcelable> parcelableList = new ArrayList<>(entries);

        args.putParcelableArrayList("entries",parcelableList);
        args.putString("date", date);
        dialogFragment.setArguments(args);
        Log.e("calendar", date);

        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "EntryDialog");
    }
}