package com.example.testprod.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.testprod.background.http_get;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class utility {
    private SharedPreferences preferences;
    private Context context;
    private boolean choice = false;
    public utility(Context context){
        this.context = context;
        preferences = this.context.getSharedPreferences("State", MODE_PRIVATE);
    }

    public String get_data(String key){
        //Log.i("key",preferences.getString(key, ""));
        return preferences.getString(key, "");
    }

    public String get_data_string(String STRING_SharedPreferences,String name){
        try {
            JSONObject JObject = new JSONObject(STRING_SharedPreferences);
            String data = JObject.getString(name);
            Log.i("SharedPreferences",data);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Integer get_data_int(String STRING_SharedPreferences,String name){
        try {
            JSONObject JObject = new JSONObject(STRING_SharedPreferences);
            Integer data = JObject.getInt(name);
            Log.i("SharedPreferences",data+"");
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean check_int(Integer data_int,Integer compare){
        try {
            return data_int != compare;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean check_string(String data_string){
        try {
            Log.i("data string",data_string);
            if(data_string.equals("null")){
                Log.i("loginact cek string","true");
                return  true;
            }else{
                Log.i("loginact cek string","false");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void update_login_state_shared_preferences(){
        try {
            http_get get = new http_get(context);
            get.execute("profile",get_data_string(get_data("Login_State"),"id"));
            get.getListener(new http_get.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            String login_state = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0).toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Login_State", login_state);
                            editor.apply();
                        }else{
                            Toast.makeText(context,"update_login_failed",Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                    }
                }
            });
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }


    public boolean update_relation_state_shared_preferences(){
        try {

            http_get get = new http_get(context);
            get.execute("get_relation",get_data_string(get_data("Login_State"),"id"));
            get.getListener(new http_get.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            String relation_state = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0).toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Relation_State", relation_state);
                            editor.apply();
                            //Toast.makeText(context,relation_state,Toast.LENGTH_SHORT).show();
                            Toast.makeText(context,"You have partner",Toast.LENGTH_SHORT).show();
                            Log.i("Relation_State",relation_state);
                            choice = true;
                        }else{
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("Relation_State");
                            editor.apply();
                            Toast.makeText(context,"No Relationship",Toast.LENGTH_SHORT).show();
                            choice = false;
                        }
                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                        choice = false;
                    }
                }
            });
        }catch (Exception e){
            Log.e("Exception",e.toString());
            choice = false;
        }
        return choice;
    }



}
