package edu.northeastern.moodtide.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.object.Entry;

public class EntryOfMonthRepository {
    String uid;
    DatabaseReference databaseReference;
    int selectedMonth;


    public EntryOfMonthRepository(String uid, int selectedMonth) {
        this.uid = uid;
        this.selectedMonth = selectedMonth;
    }

    public void getEntries(EntryOfMonthRepository.DataStatus dataStatus) {
        databaseReference = FirebaseDatabase.getInstance().getReference(uid).child("data");
        databaseReference.addChildEventListener(new ChildEventListener() {
            List<Entry> entryList = new ArrayList<>();
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LocalDate date = LocalDate.parse(snapshot.getKey());
                if(date.getMonthValue() == selectedMonth) {
                    for(DataSnapshot entryShot: snapshot.getChildren()) {
                        entryList.add(entryShot.getValue(Entry.class));
//                        Log.e("CATEGPRIES", entryShot.getValue(Entry.class).getCategory());
                    }
                    dataStatus.DataIsLoaded(entryList);

                }
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
        void DataIsLoaded(List<Entry> entryList);
//        void DataIsInserted();
//        void DataIsUpdated();
//        void DataIsDeleted();
    }

}
