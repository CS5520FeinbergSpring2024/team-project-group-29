package edu.northeastern.moodtide.addEntry;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Trigger;

public class SelectTrigger extends AppCompatActivity {
    ObservableTriggerList triggerList;
    FlexboxLayout flexboxLayout;
    String currentCategory, currentMood;
    int currentColor;

    ArrayList<Trigger> selectedTriggers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trigger);

        currentCategory = getIntent().getStringExtra("category");
        currentMood = getIntent().getStringExtra("emotion");
        currentColor = getIntent().getIntExtra("color",0);

        //set the title and color according to the selected mood
        TextView textViewMood = findViewById(R.id.text_trigger_emotion);
        textViewMood.setText(currentMood + "?");
        textViewMood.setBackgroundColor(currentColor);




        // find the flexbox layout
        flexboxLayout = findViewById(R.id.flexlayout_triggers);

        // add your own trigger
        addYourOwnTrigger();

        // initiate predefined triggers
        initiateDefinedTriggers();

        // forward to next page
        ImageButton forwardButton = findViewById(R.id.button_forward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pass on the variables
                Intent nextIntent = new Intent(SelectTrigger.this, InputNote.class);
                nextIntent.putExtra("category", currentCategory);
                nextIntent.putExtra("mood", currentMood);
                nextIntent.putExtra("triggers", selectedTriggers);
                startActivity(nextIntent);
            }
        });


        // back to last page
        ImageButton backButton = findViewById(R.id.button_back_arrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







    }



//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
////        String currentCategory, currentMood;
////        int currentColor;
////
////        ArrayList<Trigger> selectedTriggers;
//        outState.putString("currentCategory", currentCategory);
//        outState.putString("currentMood", currentMood);
//        outState.putInt("currentColor", currentColor);
//        outState.putParcelableArrayList("selectedTriggers", selectedTriggers);
//    }
//
//    @Override
//    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//        if(currentCategory == null) {
//            currentCategory = savedInstanceState.getString("currentCategory");
//        }
//
//        if(currentMood == null) {
//            currentMood = savedInstanceState.getString("currentMood");
//        }
//
//        if(currentColor == 0) {
//            currentColor = savedInstanceState.getInt("currentColor");
//
//        }
//
//        if(selectedTriggers == null) {
//            selectedTriggers = savedInstanceState.getParcelableArrayList("selectedTriggers");
//        }
//    }

    public void initiateDefinedTriggers() {
        // predefined triggers
        triggerList = new ObservableTriggerList();

        // set listener when a trigger added, generate a textview add to flexbox
        triggerList.setOnItemAddedListener(new ObservableTriggerList.OnItemAddedListener() {
            @Override
            public void onItemAdded(Trigger trigger) {
                // Create and add a TextView for the new trigger
                TextView textView = new TextView(new ContextThemeWrapper(SelectTrigger.this, R.style.TextViewWithBorder), null, 0);
                textView.setText(trigger.getName());
                GradientDrawable background = (GradientDrawable) textView.getBackground();
                background.setStroke(3,  currentColor);

                // click to select multiple triggers
                selectedTriggers = new ArrayList<>();
                final boolean[] isHighlighted = {false};
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = flexboxLayout.indexOfChild(view);
                        GradientDrawable background = (GradientDrawable) textView.getBackground();
                        if(index != flexboxLayout.getChildCount() - 1) {
                            if(!isHighlighted[0]) {
                                // change
                                isHighlighted[0] = true;
                                // add selected trigger to the list
                                selectedTriggers.add(triggerList.getTriggerAt(index));
                                Log.d("SELECTED TRIGGER", triggerList.getTriggerAt(index).getName());
                                // change the color of the border

                                background.setStroke(3, getResources().getColor(R.color.selected_trigger_highlighted_border)); // New border color
                            } else {
                                selectedTriggers.remove(triggerList.getTriggerAt(index));
                                Log.d("REMOVED TRIGGER", triggerList.getTriggerAt(index).getName());
                                background.setStroke(3, Color.BLACK); // New border color

                            }


                        }


                    }
                });

                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        FlexboxLayout.LayoutParams.WRAP_CONTENT,
                        FlexboxLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(10, 10, 10, 10); // Optional: set margins
                textView.setLayoutParams(layoutParams);


                // always add to the last but one
                flexboxLayout.addView(textView, flexboxLayout.getChildCount() - 1);


            }
        });

        // add predefined triggers to the list
        triggerList.addTrigger(new Trigger("Food"));
        triggerList.addTrigger(new Trigger("Weather"));
        triggerList.addTrigger(new Trigger("Friend"));
        triggerList.addTrigger(new Trigger("Family"));
        triggerList.addTrigger(new Trigger("Work"));
        triggerList.addTrigger(new Trigger("School"));




    }

    public void addYourOwnTrigger() {
        // append the addTriggerView at the end
        EditText addTriggerView = new EditText(new ContextThemeWrapper(this, R.style.TextViewWithBorder), null, 0);
        addTriggerView.setText(" + ");
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(10, 10, 10, 10); // Optional: set margins
        addTriggerView.setLayoutParams(layoutParams);
        flexboxLayout.addView(addTriggerView);
        addTriggerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the tect to empty
                addTriggerView.setText("");
                // Make sure the EditText can gain focus
                addTriggerView.setFocusableInTouchMode(true);
                addTriggerView.requestFocus();

                // Open the keyboard for text input
                addTriggerView.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(addTriggerView, InputMethodManager.SHOW_IMPLICIT);


            }
        });

        // when the edit action is done (press th return )
        addTriggerView.setInputType(InputType.TYPE_CLASS_TEXT);
        addTriggerView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        addTriggerView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Check if the action is a "Done" action
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newTrigger = addTriggerView.getText().toString();
                    if(!newTrigger.equals("")) {
                        // add trigger
                        triggerList.addTrigger(new Trigger(newTrigger));
                    }
                    addTriggerView.setText(" + ");
                    Log.e("edit done", "edit done");
                    // hide the keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Return true to indicate you have handled the event
                    return true;
                }
                // Return false to let the keyboard handle the event (e.g., hiding itself)
                return false;
            }
        });

    }

    // observer design pattern detecting the new trigger added and generate new textView to flexbox layout
    private static class ObservableTriggerList {
        private ArrayList<Trigger> list = new ArrayList<>();
        private OnItemAddedListener listener;

        public interface OnItemAddedListener {
            void onItemAdded(Trigger trigger);
        }

        public void setOnItemAddedListener(OnItemAddedListener listener) {
            this.listener = listener;
        }

        public void addTrigger(Trigger trigger) {
            list.add(trigger);
            if (listener != null) {
                listener.onItemAdded(trigger);
            }
        }

        public Trigger getTriggerAt(int index) {
            return list.get(index);
        }

        // Other necessary list methods can be added here (e.g., get, remove)
    }
}

