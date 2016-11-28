/**************************************
 *   file:HighScoreDialog.java
 *   @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
 *   Class: CS 245 - Graphical User Interface
 *
 *   Assignment: Android Memory Game
 *   @version v1.0
 *   Date Last Modified: 26 November 2016
 *   Purpose: Creates a class needed for an alert dialog asking for the user's initials
 *
 ***************************************/
package com.defaultusername.defaultusername;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by ejm on 11/28/16.
 */

public class HighScoreDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.high_score_prompt, null))
                // Add action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }
}
