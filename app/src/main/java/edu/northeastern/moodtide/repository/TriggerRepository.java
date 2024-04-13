package edu.northeastern.moodtide.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.object.Trigger;

public class TriggerRepository {
    private String uid;
    private DatabaseReference databaseReference;

    public TriggerRepository(String uid) {
        this.uid = uid;
    }

    public void getTriggers(DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("myTriggers");
        databaseReference.addChildEventListener(new ChildEventListener() {
            List<Trigger> triggers = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Trigger trigger = snapshot.getValue(Trigger.class);
                triggers.add(trigger);
                dataStatus.DataIsLoaded(triggers);
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
        void DataIsLoaded(List<Trigger> triggers);
        void DataIsFailed(Exception e);
    }
}

