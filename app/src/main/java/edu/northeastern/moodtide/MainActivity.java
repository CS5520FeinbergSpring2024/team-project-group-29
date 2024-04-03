package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.northeastern.moodtide.addEntry.SelectTrigger;
import edu.northeastern.moodtide.addEntry.SelectionActivity;

public class MainActivity extends AppCompatActivity {

    ImageView addEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEntry = (ImageView) findViewById(R.id.nav_home);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });

        // just for test purpose
        Button selectTriggerButton = findViewById(R.id.button_select_trigger);
        selectTriggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectTriggerIntent = new Intent(MainActivity.this, SelectTrigger.class);
                startActivity(selectTriggerIntent);
            }
        });
    }
}