package edu.northeastern.moodtide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.northeastern.moodtide.shapes.CircleMain;

public class SelectionActivity extends AppCompatActivity {

    ImageButton backBtn, forwardBtn;

    private static final int NUM_SECTORS = 6;
    private static final int[] SECTOR_COLORS = {R.color.pink_theme, R.color.orange_theme,R.color.yellow_theme,R.color.green_theme,R.color.aqua_theme,R.color.ocean_theme};
    private static final String[] SECTOR_TITLES = {"Anger", "Fear", "Love", "Joy", "Surprise", "Sadness"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        backBtn=(ImageButton)findViewById(R.id.button_back);
        forwardBtn=(ImageButton)findViewById(R.id.button_forward);

        CircleMain mainSelect = new CircleMain(this, NUM_SECTORS, SECTOR_COLORS,SECTOR_TITLES);
        ConstraintLayout constraintLayout = findViewById(R.id.selection_main);
        constraintLayout.addView(mainSelect);

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int category=mainSelect.getSelection();
                if(category==-1){
                    Toast.makeText(getApplicationContext(), "Select a category to proceed", Toast.LENGTH_SHORT).show();
                }else{
                    Intent nextPage = new Intent(SelectionActivity.this, SelectionActivity2.class);
                    nextPage.putExtra("category",SECTOR_TITLES[category]);
                    startActivity(nextPage);
                }
            }
        });
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    private void drawPlate(int count, int[] colors, String[] texts){
        CircleMain current = new CircleMain(this, count, colors, texts);
        ConstraintLayout constraintLayout = findViewById(R.id.selection_main);
        constraintLayout.addView(current);
    }
}
