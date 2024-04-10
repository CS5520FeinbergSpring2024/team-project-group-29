package edu.northeastern.moodtide.getData;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;

import edu.northeastern.moodtide.R;

public class GetTodayCount implements Runnable {
    Activity activity;
    DatabaseReference userRef;

    public GetTodayCount(Activity activity, DatabaseReference userRef) {
        this.activity = activity;
        this.userRef = userRef;
    }

    @Override
    public void run() {
        String todayDate = LocalDate.now().toString();
        DatabaseReference todayRef = userRef.child("data").child(todayDate);
        final int[] todayCount = new int[1];
        todayRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                todayCount[0] = (int)snapshot.getChildrenCount();
                Log.e("TODAYCOUNT", Integer.toString(todayCount[0]));
                View todayCountCard = activity.findViewById(R.id.today_count_card);
                TextView streakTextView = todayCountCard.findViewById(R.id.streakCount);
                streakTextView.setText(String.valueOf(todayCount[0]));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
