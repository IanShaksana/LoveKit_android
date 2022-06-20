package com.example.testprod.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testprod.R;
import com.example.testprod.fragment.Frag_commitment_create_0;
import com.example.testprod.general.utility;
import com.example.testprod.testfrag.Frag_opening0;

import org.json.JSONObject;


public class CreateActivity extends AppCompatActivity {

    private TextView nama_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        final SharedPreferences preferences = this.getSharedPreferences("State", MODE_PRIVATE);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_commitment_create_0()).commit();
    }


    private JSONObject get_data(String STRING_SharedPreferences){
        try {
            JSONObject JObject = new JSONObject(STRING_SharedPreferences).getJSONArray("data").getJSONObject(0);
            Log.i("SharedPreferences",JObject.toString());
            return JObject;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean check(JSONObject JObject){
        try {
            return JObject.getString("testDetail").isEmpty();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private String getNama(){
        utility gu = new utility(CreateActivity.this);
        try {
            JSONObject JObject = new JSONObject(gu.get_data_string(gu.get_data("Login_State"),"detail"));
            return JObject.getString("name");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
