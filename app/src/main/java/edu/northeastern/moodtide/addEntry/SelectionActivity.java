package edu.northeastern.moodtide.addEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.shapes.CircleMain;

public class SelectionActivity extends AppCompatActivity {

    ImageButton backBtn, forwardBtn;
    CircleMain mainSelect;

    private static final int NUM_SECTORS = 6;
    private static final int[] SECTOR_COLORS = {R.color.pink_theme, R.color.orange_theme,R.color.yellow_theme,R.color.green_theme,R.color.aqua_theme,R.color.ocean_theme};
    private static final String[] SECTOR_TITLES = {"Anger", "Fear", "Love", "Joy", "Surprise", "Sadness"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        backBtn=(ImageButton)findViewById(R.id.button_back);
        forwardBtn=(ImageButton)findViewById(R.id.button_forward);

        mainSelect = new CircleMain(this, NUM_SECTORS, SECTOR_COLORS,SECTOR_TITLES);
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
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    public void onBackButtonClick(View view) {
        finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainSelect != null) {
            outState.putParcelable("circleViewState", mainSelect.onSaveInstanceState());
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mainSelect != null) {
            mainSelect.onRestoreInstanceState(savedInstanceState.getParcelable("circleViewState"));
        }
    }

}
