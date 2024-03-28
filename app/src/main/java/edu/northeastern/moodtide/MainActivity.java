package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView addEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEntry = (ImageView) findViewById(R.id.nav_home);
        addEntry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
//                startActivity(intent);
//            }

            @Override
            public void onClick(View view) {
                Intent intent_select_trigger = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent_select_trigger);
            }
        });
    }
}