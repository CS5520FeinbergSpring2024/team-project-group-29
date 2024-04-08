package edu.northeastern.moodtide.calendarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.User;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    DatabaseReference userRef, dateRef;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");
        userRef = FirebaseDatabase.getInstance().getReference(uid);
        getUser();

        calendarView = findViewById(R.id.calendarView);

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
    }

    public void getUser() {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists at the specified path
                    user = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    private List<Entry> getEntries(String date){

        HashMap<String, HashMap<String, Entry>> record = user.getData();
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