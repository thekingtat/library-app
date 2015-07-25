package com.example.robbie.menuexamples;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by robbie on 25/7/2015.
 */
public class MyDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        theDialog.setTitle("Sample Dialog");

        return super.onCreateDialog(savedInstanceState);
    }
}
