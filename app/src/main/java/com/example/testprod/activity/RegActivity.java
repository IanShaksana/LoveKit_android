package com.example.testprod.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.testprod.R;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_date;
import com.example.testprod.model.model_login;
import com.example.testprod.model.model_profile;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RegActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText email;
    private EditText telepon;
    private EditText dob;
    private EditText gender;
    private EditText pass;
    private EditText pass_confirm;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        c = Calendar.getInstance();

        final Gson gson = new Gson();
        name = findViewById(R.id.reg_name);
        username = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_email);
        telepon = findViewById(R.id.reg_telp);
        dob = findViewById(R.id.reg_dob);
        gender = findViewById(R.id.reg_gender);
        pass = findViewById(R.id.reg_pass);
        pass_confirm = findViewById(R.id.reg_pass_confirm);


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
                new AlertDialog.Builder(RegActivity.this)
                        .setTitle("Jenis Kelamin")
                        .setSingleChoiceItems(singleChoiceItems, 3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                if (selectedIndex == 1) {
                                    gender.setText("Perempuan");
                                } else {
                                    gender.setText("Laki - Laki");
                                }
                            }
                        })
                        .setPositiveButton("Oke", null)
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });


        final Button button = findViewById(R.id.register);
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
                        Toast.makeText(RegActivity.this, "Lengkapi Data Yang Belum Lengkap", Toast.LENGTH_SHORT).show();
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
                                http_post post = new http_post(RegActivity.this);
                                post.execute(JSON_profile);
                                post.getListener(new http_post.OnUpdateListener() {
                                    @Override
                                    public void onUpdate(JSONObject JObject) {
                                        try {
                                            if (!JObject.getString("statuscode").equals("OK")) {
                                                Toast.makeText(RegActivity.this, "Buat Akun Gagal" , Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(RegActivity.this, "Buat Akun Sukses", Toast.LENGTH_SHORT).show();
                                                Log.i("here is", "Success");
                                                finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.i("Exception", e.toString());
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegActivity.this, "Please input correct email", Toast.LENGTH_SHORT).show();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                button.setEnabled(true);
            }
        });

    }


    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani1, R.anim.ani2);
    }

    private void opendialog_date() {
        DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat format_1 = new SimpleDateFormat("yyyy-MM-dd");
                String STRING_date = format_1.format(c.getTime());
                dob.setText(STRING_date);
            }
        };
        DatePickerDialog newDate = new DatePickerDialog(RegActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT ,  datePickerDialog, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        newDate.setButton(DatePickerDialog.BUTTON_POSITIVE,"OKE",newDate);
        newDate.setButton(DatePickerDialog.BUTTON_NEGATIVE,"BATAL",newDate);
        newDate.show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
