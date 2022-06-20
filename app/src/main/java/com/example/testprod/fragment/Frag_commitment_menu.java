package com.example.testprod.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.activity.OpeningActivity;
import com.example.testprod.activity.TestActivity;
import com.example.testprod.dialog.dialog_date;
import com.example.testprod.dialog.dialog_yes_no_warn;
import com.example.testprod.general.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_menu extends Fragment implements dialog_yes_no_warn.dialogListener_yes_no_warn, DatePickerDialog.OnDateSetListener {

    EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_menu, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        Button button1 = (Button) view.findViewById(R.id.open_test);
        Button button2 = (Button) view.findViewById(R.id.personal_commitment);
        Button button3 = (Button) view.findViewById(R.id.result_info);

        button1.setVisibility(View.GONE);


        // done
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog_yes_no();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle newBundle = new Bundle();
                utility gu = new utility(getContext());
                newBundle.putString("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
                newBundle.putBoolean("local_or_not",true);
                Frag_commitment_list newCommitmentList = new Frag_commitment_list();
                newCommitmentList.setArguments(newBundle);
                open_frag(newCommitmentList);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle newBundle = new Bundle();
                utility gu = new utility(getContext());
                newBundle.putString("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
                newBundle.putBoolean("local_or_not",true);
                Frag_commitment_list_complete newCommitmentList = new Frag_commitment_list_complete();
                newCommitmentList.setArguments(newBundle);
                open_frag(newCommitmentList);
            }
        });

        return view;
    }


    private void open_frag(Fragment Frag){
        try {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void opendialog_date() {
        dialog_date dialogfragment = new dialog_date();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    private void opendialog_yes_no() {
        dialog_yes_no_warn dialogfragment = new dialog_yes_no_warn();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_warn(Boolean status) {
        if(status){
            getActivity().startActivity(new Intent(getActivity(), OpeningActivity.class));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");
        String STRING_date = format_1.format(c.getTime());
        editText.setText(STRING_date);
    }
}
