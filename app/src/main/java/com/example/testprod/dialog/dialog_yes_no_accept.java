package com.example.testprod.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.testprod.R;

/**
 * Created by Adrian on 6/8/2018.
 */

public class dialog_yes_no_accept extends AppCompatDialogFragment {
    private dialogListener_yes_no_warn listener;

    private String ID_relation;
    public dialog_yes_no_accept(String ID_relation){
        this.ID_relation = ID_relation;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_yes_no_accept, null);

        builder.setView(view).setTitle("Terima Partner").setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Terima", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.apply_accept(2,ID_relation);
            }
        }).setNegativeButton("Tolak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.apply_accept(1,ID_relation);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (dialogListener_yes_no_warn) getTargetFragment();
    }

    public interface dialogListener_yes_no_warn {
        void apply_accept(Integer status, String ID_relation);
    }

}
