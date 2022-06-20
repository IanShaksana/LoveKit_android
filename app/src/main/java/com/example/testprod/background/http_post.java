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

public class http_post extends AsyncTask<JSONObject, Void, JSONObject> {
    Context currentAct;

    public http_post(Context currentAct) {
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
    protected JSONObject doInBackground(JSONObject... strings) {
        try {
            return postData(strings[0]);
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

    private JSONObject postData(JSONObject API) throws Exception {
        Log.println(Log.INFO,"wasd",API.toString());
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        JSONObject JObject = new JSONObject();

        try {


            //Create data to send to server
            JSONObject dataToSend = API;

            //Initialize and config request, then connect to server
            String urlpath = currentAct.getResources().getString(R.string.ip_address_do)+ API.getString("api");
            URL url = new URL(urlpath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json"); // set header
            urlConnection.connect();
            urlConnection.setConnectTimeout(5000);


            //Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(dataToSend.toString());
            bufferedWriter.flush();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            Log.i("httpcode",""+urlConnection.getResponseCode());
            JObject = new JSONObject(result.toString());
            Log.i("JSON RESULT",""+JObject.toString());


        } catch (Exception e) {
            e.printStackTrace();
            JObject.put("statuscode","no connection to server");
            Log.println(Log.INFO,"Exception", ""+e.toString());
        }finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
        return JObject;
    }


}
