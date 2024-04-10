package edu.northeastern.moodtide.getData;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edu.northeastern.moodtide.R;

public class GetStreak implements Runnable {
    Activity activity;
    DatabaseReference userRef;
    public GetStreak(Activity activity, DatabaseReference userRef) {
        this.activity = activity;
        this.userRef = userRef;
    }


    @Override
    public void run() {
        DatabaseReference streakRef = userRef.child("streak");
        final long[] streakCount = new long[1];
        streakRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                streakCount[0] = (long) snapshot.getValue();
                View streakCountCard = activity.findViewById(R.id.streak_count_card);
                TextView streakTextView = streakCountCard.findViewById(R.id.streakCount);
                streakTextView.setText(String.valueOf(streakCount[0]));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
