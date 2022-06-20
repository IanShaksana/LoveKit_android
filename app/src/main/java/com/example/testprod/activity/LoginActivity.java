package com.example.testprod.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testprod.R;
import com.example.testprod.background.http_post;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_login;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by Adrian on 5/18/2018.
 */

public class LoginActivity extends AppCompatActivity {
    public static final int LOGIN_REQUEST = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText username = (EditText) findViewById(R.id.userInput);
        final EditText pass = (EditText) findViewById(R.id.passInput);
        ImageView img = findViewById(R.id.logo_vis);
        //Picasso.get().load(R.drawable.logo5).centerInside() .fit().into(img);

        utility gu = new utility(LoginActivity.this);

        if (!gu.get_data("Login_State").isEmpty()) {
            Log.i("loginact","sudah disini");
            Log.i("loginact",gu.get_data("Login_State"));
            Log.i("loginact1",gu.get_data_int(gu.get_data("Login_State"),"status")+"");
            Log.i("loginact2",gu.check_int(gu.get_data_int(gu.get_data("Login_State"),"status"),0)+"");
            Log.i("loginact3",gu.check_string(gu.get_data_string(gu.get_data("Login_State"),"testDetail"))+"");



            if(gu.check_int(gu.get_data_int(gu.get_data("Login_State"),"status"),0)){
                Log.i("loginact","sudah disini 2");

                if(gu.check_string(gu.get_data_string(gu.get_data("Login_State"),"testDetail"))){
                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, OpeningActivity.class).putExtra("first",1));
                    Log.i("loginact","sudah disini 3");
                    finish();
                }else{
                    LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), LOGIN_REQUEST);
                    Log.i("loginact","sudah disini 4");
                    finish();
                }
            }



        }

        final Button login = (Button) findViewById(R.id.loginButton);
        final Button reg = (Button) findViewById(R.id.RegButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login_local();
                login_real(login,reg,pass,username);
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegActivity.class));
            }
        });

    }

    private void login_local(){
        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences("State", MODE_PRIVATE);
        String login_state = "test";
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Login_State", login_state);
        editor.commit();
        LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), LOGIN_REQUEST);
        finish();
    }

    private void login_real(final Button login, final Button reg, EditText pass, EditText username){
        login.setEnabled(false);
        reg.setEnabled(false);
        if(username.getText().toString().matches("") || pass.getText().toString().matches("")  ){
            Toast.makeText(LoginActivity.this, "Isi Email dan Password", Toast.LENGTH_SHORT).show();
            login.setEnabled(true);
            reg.setEnabled(true);
        }else{
            try {
                http_post post = new http_post(LoginActivity.this);
                model_login newLogin = new model_login();
                newLogin.setEmail(username.getText().toString());
                newLogin.setPassword(pass.getText().toString());
                newLogin.setOnesignalid(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                //Log.i("onesig",OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(gson.toJson(newLogin));
                jsonObject.put("api","login");
                post.execute(jsonObject);
                post.getListener(new http_post.OnUpdateListener() {
                    @Override
                    public void onUpdate(JSONObject JObject) {
                        try{
                            login.setEnabled(true);
                            reg.setEnabled(true);
                            if(!JObject.getString("statuscode").equals("OK")){
                                Toast.makeText(LoginActivity.this, "Login gagal : "+JObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }else if(JObject.getString("statuscode").equals("no connection")){
                                Toast.makeText(LoginActivity.this,"Tidak ada koneksi ke server",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "Login sukses", Toast.LENGTH_SHORT).show();
                                SharedPreferences preferences = getSharedPreferences("State", MODE_PRIVATE);
                                JSONObject data_from_server = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0);
                                String login_state = data_from_server.toString();
                                String test_detail = data_from_server.getString("testDetail");
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("Login_State", login_state);
                                editor.commit();
                                //LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), LOGIN_REQUEST);
                                utility gu = new utility(LoginActivity.this);
                                if(gu.check_string(test_detail)){
                                    LoginActivity.this.startActivity(new Intent(LoginActivity.this, OpeningActivity.class).putExtra("first",1));
                                    finish();
                                }else{
                                    LoginActivity.this.startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), LOGIN_REQUEST);
                                    finish();
                                }


                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
