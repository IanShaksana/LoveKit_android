package com.example.testprod.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.testprod.R;

/**
 * Created by Adrian on 5/30/2018.
 */

public class dialog_number extends AppCompatDialogFragment {

    private dialogListener_worker listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_number, null);
        final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(2);
        np.setWrapSelectorWheel(false);
        //editText.setRawInputType(Configuration.KEYBOARD_12KEY);
        builder.setView(view).setTitle("Berapa Kali Pengulangan").setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String dialog = String.valueOf(np.getValue());
                listener.apply_invite(dialog);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (dialogListener_worker) getTargetFragment();
    }

    public interface dialogListener_worker {
        void apply_invite(String wasd);
    }


}
