package edu.northeastern.moodtide.calendarView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;
import java.util.Locale;

import edu.northeastern.moodtide.R;
import edu.northeastern.moodtide.object.Entry;

public class EntryDialog extends DialogFragment {

    private TextView category, emotion, triggers, note, index;
    private Button previous, next, close;
    private List<Entry> entries;
    private int currentIndex=0;


    // Override onCreateDialog to customize the dialog appearance and behavior
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

    private void fillEntry(Entry entry){

        category.setText(entry.getCategory());
        emotion.setText(entry.getEmotion());
        triggers.setText(String.join(" | ", entry.getTriggers()));
        note.setText(entry.getNote());
        index.setText(String.format(Locale.getDefault(), "%d/%d", currentIndex + 1, entries.size()));
    }

}

