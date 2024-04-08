package edu.northeastern.moodtide.addEntry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.northeastern.moodtide.MainActivity;
import edu.northeastern.moodtide.R;

public class InputNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_note);

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
                Toast.makeText(InputNote.this, "Your mood has been recorded", Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(InputNote.this, MainActivity.class);
                startActivity(mainIntent);

                // add entry to database


            }
        });
    }
}