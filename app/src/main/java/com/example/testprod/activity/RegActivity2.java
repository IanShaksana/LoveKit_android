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
import com.example.testprod.fragment.Frag_register2;
import com.example.testprod.home.Frag_keys;
import com.example.testprod.model.model_login;
import com.example.testprod.model.model_profile;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RegActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_register2()).commit();

    }

}
