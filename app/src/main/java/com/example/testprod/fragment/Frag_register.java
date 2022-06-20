package com.example.testprod.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_register extends Fragment implements DatePickerDialog.OnDateSetListener{
    private EditText name;
    private EditText username;
    private EditText email;
    private EditText telepon;
    private EditText dob;
    private EditText gender;
    private EditText pass;
    private EditText pass_confirm;
    private Button button1;

    private TextInputLayout t1;
    private TextInputLayout t2;
    private TextInputLayout t3;

    private TextInputLayout t4;
    private TextInputLayout t5;

    private TextInputLayout t6;
    private TextInputLayout t7;
    private TextInputLayout t8;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register, container, false);

        name = view.findViewById(R.id.reg_name);
        email = view.findViewById(R.id.reg_email);
        username = view.findViewById(R.id.reg_username);
        telepon = view.findViewById(R.id.reg_telp);
        dob = view.findViewById(R.id.reg_dob);
        gender = view.findViewById(R.id.reg_gender);
        pass = view.findViewById(R.id.reg_pass);
        pass_confirm = view.findViewById(R.id.reg_pass_confirm);
        button1 = view.findViewById(R.id.register);


        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);
        t6 = view.findViewById(R.id.t6);
        t7 = view.findViewById(R.id.t7);
        t8 = view.findViewById(R.id.t8);

        Bundle bundle = getArguments();

        if (bundle != null) {
            String choice = bundle.getString("choice");
            if(choice.equals("profile")){
                button1.setText("Edit Profil");
                JSONObject JObject;
                try {
                    JObject = new JSONObject(bundle.getString("old_profile"));
                    name.setText(JObject.getString("name"));
                    username.setText(JObject.getString("username"));
                    dob.setText(JObject.getString("DOB"));
                    gender.setText(JObject.getString("gender"));
                    telepon.setText(JObject.getString("telepon"));
                }catch (Exception e){
                    JObject = null;
                }


                t6.setVisibility(View.GONE);
                t7.setVisibility(View.GONE);
                t8.setVisibility(View.GONE);

                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opendialog_date();
                    }
                });
                gender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] singleChoiceItems = {"Laki - Laki","Perempuan"};
                        final int itemSelected = 0;
                        new AlertDialog.Builder(getContext())
                                .setTitle("Jenis Kelamin")
                                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                        if(selectedIndex == 1){
                                            gender.setText("Perempuan");
                                        }else{
                                            gender.setText("Laki - Laki");
                                        }
                                    }
                                })
                                .setPositiveButton("Oke", null)
                                .setNegativeButton("Batal", null)
                                .show();
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject newProfile = new JSONObject();
                        try {
                            newProfile.put("name",name.getText().toString());
                            newProfile.put("DOB",dob.getText().toString());
                            newProfile.put("gender",gender.getText().toString());
                            newProfile.put("username",username.getText().toString());
                            newProfile.put("telepon",telepon.getText().toString());
                            edit_profile(newProfile);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                button1.setText("Ubah Kata Sandi");
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);
                t4.setVisibility(View.GONE);
                t5.setVisibility(View.GONE);
                t6.setVisibility(View.GONE);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pass.getText().toString().trim().equals(pass_confirm.getText().toString().trim())){
                            Toast.makeText(getContext(),"Password Cocok",Toast.LENGTH_SHORT);
                            change_pass(pass.getText().toString());
                        }else{
                            Toast.makeText(getContext(),"Password Tidak Cocok",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

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

    private void edit_profile(final JSONObject newProfile){
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject();
            JObject.put("detail",newProfile.toString());
            JObject.put("api","edit_profile");
            JObject.put("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            utility gu = new utility(getContext());
                            JSONObject j1 = new JSONObject(gu.get_data("Login_State"));
                            j1.put("detail",newProfile.toString());
                            editor.putString("Login_State", j1.toString());
                            editor.commit();
                            back();
                        }else{
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void change_pass(String newPass){
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject();
            JObject.put("api","change_password");
            JObject.put("newPass",newPass);
            //JObject.put("encodedimage",encodeImage(bitmap_prove));
            JObject.put("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                            back();
                        }else{
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String StringToDate(String x) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
            SimpleDateFormat formatter2 = new SimpleDateFormat("MMM d, yyyy");
            Date date = formatter.parse(x);

            Calendar wad = Calendar.getInstance();
            wad.setTime(date);
            date = wad.getTime();
            // System.out.println(formatter.format(date));
            // return formatter.format(date);
            return formatter2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private  void back(){
        getFragmentManager().popBackStack();
    }

}
