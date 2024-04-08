package edu.northeastern.moodtide.calendarView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.northeastern.moodtide.R;

public class EntryDialog extends DialogFragment {

    private TextView test;


    // Override onCreateView to inflate the dialog layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_entry, container, false);

        return view;
    }

    // Override onCreateDialog to customize the dialog appearance and behavior
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Your Entry");
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.show_entry, null);
        builder.setView(dialogView);
        test=dialogView.findViewById(R.id.emotion);
        String date = getArguments().getString("date");
        test.setText(date);

        // Set positive button (Save)
        builder.setPositiveButton("next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        // Set negative button (Cancel)
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog without saving
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}

