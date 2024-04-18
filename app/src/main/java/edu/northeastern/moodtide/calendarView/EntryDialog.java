package edu.northeastern.moodtide.calendarView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;
import java.util.Locale;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.Trigger;


//Customized dialog fragment class
//that shows the entry dialog with option to turn to next or previous page if applicable
public class EntryDialog extends DialogFragment {

    private TextView category, emotion, triggers, note, index;
    private Button close;
    private ImageView previous, next;
    private List<Entry> entries;
    private int currentIndex=0;


    //customize the dialog appearance and behavior
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        entries = getArguments().getParcelableArrayList("entries");
        String date = getArguments().getString("date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(date);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.show_entry, null);
        builder.setView(dialogView);
        category=dialogView.findViewById(R.id.category);
        emotion=dialogView.findViewById(R.id.emotion);
        triggers=dialogView.findViewById(R.id.trigger);
        note=dialogView.findViewById(R.id.note);
        index=dialogView.findViewById(R.id.index);
        previous=dialogView.findViewById(R.id.previous);
        next=dialogView.findViewById(R.id.next);
        close=dialogView.findViewById(R.id.close);
        fillEntry(entries.get(currentIndex));

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentIndex>0){
                    currentIndex--;
                    fillEntry(entries.get(currentIndex));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentIndex< entries.size()-1){
                    currentIndex++;
                    fillEntry(entries.get(currentIndex));
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    //method to help print the list of triggers into strings
    public static String printTriggers(List<Trigger> triggers) {
        StringBuilder combinedNames = new StringBuilder();
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);
            combinedNames.append(trigger.getName());
            if (i < triggers.size() - 1) {
                combinedNames.append(" | ");
            }
        }
        return combinedNames.toString();
    }

    //method to print the current entry onto the dialog components and update page count accordingly
    private void fillEntry(Entry entry){

        category.setText(entry.getCategory());
        emotion.setText(entry.getEmotion());
        triggers.setText(printTriggers(entry.getTriggers()));
        note.setText(entry.getNote());
        index.setText(String.format(Locale.getDefault(), "%d/%d", currentIndex + 1, entries.size()));
    }

}

