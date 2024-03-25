package edu.northeastern.moodtide;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.northeastern.moodtide.shapes.CircleMain;

public class SelectionActivity extends AppCompatActivity {

    private static final int NUM_SECTORS = 6;
    private static final int[] SECTOR_COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        CircleMain mainSelect = new CircleMain(this, NUM_SECTORS, SECTOR_COLORS);

        ConstraintLayout constraintLayout = findViewById(R.id.selection_main);

        constraintLayout.addView(mainSelect);

    }
}