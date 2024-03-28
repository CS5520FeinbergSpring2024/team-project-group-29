package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SelectionActivity2 extends AppCompatActivity {

    String currentCategory;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection2);

        currentCategory = getIntent().getStringExtra("category");
        test=(TextView) findViewById(R.id.testView);
        test.setText(currentCategory);


    }
}