package edu.northeastern.moodtide.addEntry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.HomeActivity;
import edu.northeastern.moodtide.MainActivity;
import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.Trigger;
import edu.northeastern.moodtide.object.User;

public class InputNote extends AppCompatActivity {
    String category, mood;
    List<Trigger> triggers;
    String note;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_note);

        // datebase
        database = FirebaseDatabase.getInstance();

        // top bar text
        TextView topBarText = findViewById(R.id.text_current_step);
        topBarText.setText("3");

        // retrieve values from last activities
        triggers = (ArrayList<Trigger>) getIntent().getSerializableExtra("triggers");
        mood = getIntent().getStringExtra("mood");
        category = getIntent().getStringExtra("category");



        // back to last page
        ImageButton backButton = findViewById(R.id.button_back_arrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // check mark to complete
        ImageButton buttonComplete = findViewById(R.id.button_complete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get note
                EditText noteEditTextView = findViewById(R.id.edit_text_note);
                note = noteEditTextView.getText().toString();
                Log.e("NOTE", note);

                Intent mainIntent = new Intent(InputNote.this, HomeActivity.class);


                // get local date and time
                String timeStr = LocalTime.now().toString();
                String time = timeStr.replace(":", "_").replace(".", "_");
                String date = LocalDate.now().toString();


                Entry entry;
                // add entry to database
                if(note.equals("")) {
                    entry = new Entry(category,mood, triggers);
                } else {
                    entry = new Entry(category, mood, triggers, note);
                }

                // get user id
                String uid = getSharedPreferences("memory", Context.MODE_PRIVATE).getString("uid", "");

                // add new entry to database
                DatabaseReference userRef = database.getReference(uid);
                DatabaseReference dataRef = userRef.child("data");
                DatabaseReference dateRef = dataRef.child(date);
                dateRef.child(time).setValue(entry);

                // update days of streak

                DatabaseReference streakRef = userRef.child("streak");
                DatabaseReference lastDateRef = userRef.child("lastDate");
                final String[] lastDate = {""};
                // retrive lastDate
                lastDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        lastDate[0] = snapshot.getValue().toString();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Log.e("LASTDATE", lastDate[0]);
                // Parse the date strings into LocalDate objects
                if(!lastDate[0].equals("")) {
                    LocalDate lastDay = LocalDate.parse(lastDate[0]);
                    LocalDate today = LocalDate.parse(date);
                    boolean isNextDay = lastDay.plusDays(1).equals(today);
                    if(isNextDay) {
                        streakRef.setValue(streakRef.toString() + 1);
                    } else {
                        streakRef.setValue(1);
                    }

                } else {
                    streakRef.setValue(1);

                }

                lastDateRef.setValue(date);


                Toast.makeText(InputNote.this, "Your mood has been recorded", Toast.LENGTH_SHORT).show();
                startActivity(mainIntent);
            }
        });
    }
}