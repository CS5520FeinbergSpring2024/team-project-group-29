package edu.northeastern.moodtide.addEntry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.TextView;

import java.text.BreakIterator;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.shapes.CircleMain;

public class SelectionActivity2 extends AppCompatActivity {

    String currentCategory;
    TextView test;

    private static final int NUM_ANGER = 10, NUM_JOY = 16, NUM_LOVE = 10, NUM_SURPRISE = 10, NUM_SADNESS = 12, NUM_FEAR = 10;
    private static final int[] COLORS_ANGER = {R.color.pink1, R.color.pink2,R.color.pink3,R.color.pink4,
            R.color.pink5,R.color.pink6, R.color.pink7, R.color.pink8, R.color.pink9, R.color.pink10};
    private static final int[] COLORS_JOY = {R.color.green1,R.color.green2,R.color.green3,R.color.green4,R.color.green5,
            R.color.green6,R.color.green7,R.color.green8,R.color.green9,R.color.green10,R.color.green11,R.color.green12,
            R.color.green13,R.color.green14,R.color.green15,R.color.green16};
    private static final int[] COLORS_LOVE = {R.color.yellow1,R.color.yellow2,R.color.yellow3,R.color.yellow4,R.color.yellow5,
            R.color.yellow6,R.color.yellow7,R.color.yellow8,R.color.yellow9,R.color.yellow10};
    private static final int[] COLORS_SURPRISE = {R.color.aqua1,R.color.aqua2,R.color.aqua3,R.color.aqua4,R.color.aqua5,
            R.color.aqua6,R.color.aqua7,R.color.aqua8,R.color.aqua9,R.color.aqua10};
    private static final int[] COLORS_SADNESS = {R.color.ocean1,R.color.ocean2,R.color.ocean3,R.color.ocean4,R.color.ocean5,
            R.color.ocean6,R.color.ocean7,R.color.ocean8,R.color.ocean9,R.color.ocean10,R.color.ocean11,R.color.ocean12};
    private static final int[] COLORS_FEAR = {R.color.orange1,R.color.orange2,R.color.orange3,R.color.orange4,R.color.orange5,
            R.color.orange6,R.color.orange7,R.color.orange8,R.color.orange9,R.color.orange10};
    private static final String[] TITLES_ANGER = {"Revolted","Contempt","Jealous","Resentful","Aggravated","Annoyed",
            "Frustrated","Agitated","Hostile","Hate"};
    private static final String[] TITLES_JOY = {"Rapture","Enchanted","Jubilation","Euphoric","Zeal","Excited","Hopeful","Eager",
            "Illustrious","Triumphant","Blissful","Jovial","Delighted","Amused","Satisfied","Pleased"};
    private static final String[] TITLES_LOVE = {"Peaceful","Relieved","Compassionate","Caring","Infatuation","Passion","Attracted",
            "Sentimental","Fondness","Romantic"};
    private static final String[] TITLES_SURPRISE = {"Shocked","Dismayed","Disillusioned","Perplexed","Astonished","Awe-struck","Speechless",
            "Astounded","Stimulated","Touched"};
    private static final String[] TITLES_SADNESS = {"Agony","Hurt","Depressed","Sorrow","Dismayed","Displeased","Regretful","Guilty","Isolated",
            "Lonely","Grief","Powerless"};
    private static final String[] TITLES_FEAR = {"Dread","Mortified","Anxious","Worried","Inadequate","Inferior","Hysterical","Panic",
            "Helpless","Frightened"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection2);

        currentCategory = getIntent().getStringExtra("category");
        test=(TextView) findViewById(R.id.testView);
        test.setText(currentCategory);

        switch(currentCategory){
            case "Anger":
                drawPlate(NUM_ANGER,COLORS_ANGER,TITLES_ANGER);
                break;

            case "Joy":
                drawPlate(NUM_JOY,COLORS_JOY,TITLES_JOY);
                break;

            case "Love":
                drawPlate(NUM_LOVE,COLORS_LOVE,TITLES_LOVE);
                break;

            case "Surprise":
                drawPlate(NUM_SURPRISE,COLORS_SURPRISE,TITLES_SURPRISE);
                break;

            case "Sadness":
                drawPlate(NUM_SADNESS,COLORS_SADNESS,TITLES_SADNESS);
                break;

            case "Fear":
                drawPlate(NUM_FEAR,COLORS_FEAR,TITLES_FEAR);

        }




    }
    private void drawPlate(int count, int[] colors, String[] texts){
        CircleMain current = new CircleMain(this, count, colors, texts);
        ConstraintLayout constraintLayout = findViewById(R.id.selection_sub);
        constraintLayout.addView(current);
    }
}