package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.northeastern.moodtide.addEntry.SelectTrigger;
import edu.northeastern.moodtide.addEntry.SelectionActivity;
import edu.northeastern.moodtide.getQuote.GetQuote;

public class HomeActivity extends AppCompatActivity {

    ImageView addEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // display daily quote
        Thread thread = new Thread(new GetQuote(this));
        thread.start();
        // add entry when clicking "+"
        addEntry = (ImageView) findViewById(R.id.nav_home);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}