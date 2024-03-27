package edu.northeastern.moodtide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.object.Trigger;

public class SelectTrigger extends AppCompatActivity {
    List<Trigger> triggerList;
    FlexboxLayout flexboxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trigger);

        // find the flexbox layout
        flexboxLayout = findViewById(R.id.flexlayout_triggers);
        initiateDefinedTriggers();

        // click button to add a trigger


    }

    public void initiateDefinedTriggers() {
        // predefined triggers
        triggerList = new ArrayList<>();
        triggerList.add(new Trigger("Food"));
        triggerList.add(new Trigger("Weather"));
        triggerList.add(new Trigger("Friend"));
        triggerList.add(new Trigger("Family"));
        triggerList.add(new Trigger("Work"));
        triggerList.add(new Trigger("School"));


        // generate flexbox layout
        for (int i = 0; i <triggerList.size(); i++) {
            Trigger trigger = triggerList.get(i);
            TextView textView = new TextView(new ContextThemeWrapper(this, R.style.TextViewWithBorder), null, 0);
            textView.setText(trigger.getName());

            // Set layout params for the TextView
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                    FlexboxLayout.LayoutParams.WRAP_CONTENT,
                    FlexboxLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 10, 10, 10); // Optional: set margins
            textView.setLayoutParams(layoutParams);
            flexboxLayout.addView(textView);
        }

    }

//    public void addTrigger(View view) {
//        int id = view.getId();
//        if(id == R.id.button_add_trigger) {
//            triggerList.add(new Trigger(""))
//        }
//    }
}