package com.example.testprod.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.activity.RegActivity;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_date;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_login;
import com.example.testprod.model.model_profile;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_register2 extends Fragment implements DatePickerDialog.OnDateSetListener{
    private EditText name;
    private EditText username;
    private EditText email;
    private EditText telepon;
    private EditText dob;
    private EditText gender;
    private EditText pass;
    private EditText pass_confirm;
    Calendar c;
    private Button button;



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register2, container, false);

        final Gson gson = new Gson();
        name = view.findViewById(R.id.reg_name);
        email = view.findViewById(R.id.reg_email);
        username = view.findViewById(R.id.reg_username);
        telepon = view.findViewById(R.id.reg_telp);
        dob = view.findViewById(R.id.reg_dob);
        gender = view.findViewById(R.id.reg_gender);
        pass = view.findViewById(R.id.reg_pass);
        pass_confirm = view.findViewById(R.id.reg_pass_confirm);
        button = view.findViewById(R.id.register);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog_date();
            }
        });


        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] singleChoiceItems = {"Laki - Laki", "Perempuan"};
                final int itemSelected = 0;
                new AlertDialog.Builder(getContext())
                        .setTitle("Select your gender")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                if (selectedIndex == 1) {
                                    gender.setText("Perempuan");
                                } else {
                                    gender.setText("Laki - Laki");
                                }
                            }
                        })
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


        button = view.findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                try {
                    if (
                            email.getText().toString().matches("")
                                    || pass.getText().toString().matches("")
                                    || name.getText().toString().matches("")
                                    || username.getText().toString().matches("")
                                    || telepon.getText().toString().matches("")
                                    || dob.getText().toString().matches("")
                                    || gender.getText().toString().matches("")
                                    || pass.getText().toString().matches("")
                                    || pass_confirm.getText().toString().matches("")
                    ) {
                        Toast.makeText(getContext(), "Lengkapi Data Yang Belum Lengkap", Toast.LENGTH_SHORT).show();
                        button.setEnabled(true);
                    } else {

                        if (isEmailValid(email.getText().toString())) {
                            if (pass.getText().toString().equals(pass_confirm.getText().toString())) {
                                model_login newUser = new model_login();
                                newUser.setEmail(email.getText().toString());
                                newUser.setPassword(pass_confirm.getText().toString());
                                newUser.setOnesignalid(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                                JSONObject JSON_profile = new JSONObject();
                                JSON_profile = new JSONObject(gson.toJson(newUser));

                                model_profile newProfile = new model_profile();
                                newProfile.setName(name.getText().toString());
                                newProfile.setDOB(dob.getText().toString());
                                newProfile.setGender(gender.getText().toString());
                                newProfile.setUsername(username.getText().toString());
                                newProfile.setTelepon(telepon.getText().toString());


                                JSONObject JSON_profile_detail = new JSONObject(gson.toJson(newProfile));
                                JSON_profile.put("detail", JSON_profile_detail.toString());
                                JSON_profile.put("api", "register");
                                http_post post = new http_post(getContext());
                                post.execute(JSON_profile);
                                post.getListener(new http_post.OnUpdateListener() {
                                    @Override
                                    public void onUpdate(JSONObject JObject) {
                                        try {
                                            if (!JObject.getString("statuscode").equals("OK")) {
                                                Toast.makeText(getContext(), "Register Failed : " + JObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Register Success", Toast.LENGTH_SHORT).show();
                                                Log.i("here is", "Success");
                                                getActivity().finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.i("Exception", e.toString());
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Password do not match", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please input correct email", Toast.LENGTH_SHORT).show();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                button.setEnabled(true);
            }
        });


        return view;
    }

    private void opendialog_date() {
        dialog_date dialogfragment = new dialog_date();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");
        String current_date = format_1.format(c.getTime());
        dob.setText(current_date);
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
