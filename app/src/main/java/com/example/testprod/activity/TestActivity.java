package com.example.testprod.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.testprod.R;
import com.example.testprod.fragment.*;
import com.example.testprod.testfrag.Frag_opening0;
import com.example.testprod.testfrag.Frag_test1;

import org.json.JSONArray;
import org.json.JSONObject;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final SharedPreferences preferences = this.getSharedPreferences("State", MODE_PRIVATE);
        String state = preferences.getString("Login_State", "");
        Log.i("SharedPreferences",check(get_data(state))+"");

        if (check(get_data(state))) {
            TestActivity.this.startActivity(new Intent(TestActivity.this, MainActivity.class));
            finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_test1()).commit();
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
}
