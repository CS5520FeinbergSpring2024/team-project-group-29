package edu.northeastern.moodtide.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

public class TodayCountRepository {

    String uid;
    DatabaseReference databaseReference;


    public TodayCountRepository(String uid) {
        this.uid = uid;
    }

    public void getTodayCount(TodayCountRepository.DataStatus dataStatus) {
        String todayDate = LocalDate.now().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("data").child(todayDate);
        long todayCount = 0;
        databaseReference.addChildEventListener(new ChildEventListener() {
            long todayCount = 0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                todayCount++;
                dataStatus.DataIsLoaded(Long.toString(todayCount));
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

    public interface DataStatus {
        void DataIsLoaded(String todayCount);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
    }

}
