package com.example.testprod.background;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.testprod.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class http_get extends AsyncTask<String, Void, JSONObject> {
    Context currentAct;

    public http_get(Context currentAct) {
        this.currentAct = currentAct;
    }

    public interface OnUpdateListener {
        public void onUpdate(JSONObject JObject);
    }

    OnUpdateListener listener;

    public void getListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            return getData(strings[0],strings[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
        listener.onUpdate(s);
    }

    private JSONObject getData(String API, String Header) throws Exception {
        Log.println(Log.INFO,"wasd",API);
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        JSONObject JObject = new JSONObject();

        try {


            //Initialize and config request, then connect to server
            String urlpath = currentAct.getResources().getString(R.string.ip_address_do)+ API;
            URL url = new URL(urlpath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("id_login", Header); // set header
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            Log.i("httpcode",""+urlConnection.getResponseCode());
            JObject = new JSONObject(result.toString());
            Log.i("JSON RESULT_GET",""+JObject.toString());


        } catch (Exception e) {
            e.printStackTrace();
            Log.println(Log.INFO,"Exception", ""+e.toString());
        }finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
        return JObject;
    }


}
