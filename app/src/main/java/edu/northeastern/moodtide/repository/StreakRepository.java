package edu.northeastern.moodtide.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StreakRepository {

    String uid;
    DatabaseReference databaseReference;


    public StreakRepository(String uid) {
        this.uid = uid;
    }

    public void getStreak(DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("streak");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long streakLong = dataSnapshot.getValue(Long.class);
                dataStatus.DataIsLoaded(streakLong.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    public interface DataStatus {
        void DataIsLoaded(String streak);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
    }
}
