package com.example.testprod.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.activity.MainActivity;
import com.example.testprod.background.http_post;
import com.example.testprod.general.utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_result0 extends Fragment {
    private String type = "";
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.result0, container, false);
        final Bundle prevBundle = getArguments();
        final Integer first = getActivity().getIntent().getIntExtra("first",0);
        Log.i("first","first"+first);
        ImageView img = view.findViewById(R.id.img_result);
        result(prevBundle);

        switch (type){
            case "word":
                Picasso.get().load(R.drawable.visual_penjelasan_bk_word).centerInside().fit().into(img);
                break;
            case "gift":
                Picasso.get().load(R.drawable.visual_penjelasan_bk_gift).centerInside().fit().into(img);
                break;
            case "service":
                Picasso.get().load(R.drawable.visual_penjelasan_bk_pelayanan).centerInside().fit().into(img);
                break;
            case "time":
                Picasso.get().load(R.drawable.visual_penjelasan_bk_qtime).centerInside().fit().into(img);
                break;
            case "touch":
                Picasso.get().load(R.drawable.visual_penjelasan_bk_touch).centerInside().fit().into(img);
                break;
            default:
                break;
        }



        Button button1 =  view.findViewById(R.id.finish_result);

        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        final Frag_suggestion newFrag = new Frag_suggestion();
        newFrag.setArguments(bundle);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // http post kalo dah selesai
                try {
                    http_post post = new http_post(getContext());
                    final utility gu = new utility(getContext());
                    JSONObject JObject = new JSONObject(gu.get_data("Login_State"));
                    JObject.put("testDetail",type);
                    JObject.put("api","answer");
                    post.execute(JObject);
                    post.getListener(new http_post.OnUpdateListener() {
                        @Override
                        public void onUpdate(JSONObject JObject) {
                            try {
                                if(!JObject.getString("statuscode").equals("OK")){
                                    Toast.makeText(getContext(), "Tes Gagal : "+JObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(), "Tes Sukses", Toast.LENGTH_SHORT).show();

                                    if(first == 1){
                                        Log.i("first","first"+first);
                                        getActivity().startActivity(new Intent(getContext(), MainActivity.class).putExtra("first",1));
                                    }
                                    getActivity().finish();
                                }
                            }catch (Exception e){
                                Log.e("Exception",e.toString());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                /*
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                        .replace(R.id.main_fragment, new Frag_home())
                        .addToBackStack(null)
                        .commit();
                */
            }
        });

        return view;
    }

    private void result(Bundle prevBundle){
        JSONObject json = new JSONObject();
        Set<String> keys = prevBundle.keySet();
        for (String key : keys) {
            try {
                json.put(key,prevBundle.getInt(key));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("testresult",json.toString());

        Integer val_word = 0;
        Integer val_gift = 0;
        Integer val_service = 0;
        Integer val_times = 0;
        Integer val_touch = 0;


        try {
            // calculate word
            String[] words = {"1","6","11","16","21"};
            for (String word: words) {
                val_word = val_word + json.getInt(word);
            }
            // calculate


            // calculate gift
            String[] gifts = {"2","7","12","17","22"};
            for (String gift: gifts) {
                val_gift = val_gift + json.getInt(gift);
            }
            // calculate


            // calculate service
            String[] service = {"3","8","13","18","23"};
            for (String ser: service) {
                val_service = val_service + json.getInt(ser);
            }
            // calculate


            // calculate time
            String[] times = {"4","9","14","19","24"};
            for (String time: times) {
                val_times = val_times + json.getInt(time);
            }
            // calculate


            // calculate touch
            String[] touch = {"5","10","15","20","25"};
            for (String tou: touch) {
                val_touch = val_touch + json.getInt(tou);
            }
            // calculate


            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject(gu.get_data("Login_State"));
            JObject.put("api","answer");
            JObject.put("testDetail",compare(val_word,val_gift,val_service,val_times,val_touch));
            type = compare(val_word,val_gift,val_service,val_times,val_touch);
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.e("Exception",e.toString());
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

        Log.i("word",""+val_word);
        Log.i("gift",""+val_gift);
        Log.i("service",""+val_service);
        Log.i("time",""+val_times);
        Log.i("touch",""+val_touch);
        Log.i("largest", ""+compare(val_word,val_gift,val_service,val_times,val_touch));
    }

    private String compare(Integer val1,Integer val2,Integer val3,Integer val4,Integer val5){
        String largest = "placeholder";

        if(val1 >= val2 && val1 >= val3 && val1 >= val4 && val1 >= val5){
            largest = "word";
        }

        if(val2 >= val1 && val2 >= val3 && val2 >= val4 && val2 >= val5){
            largest = "gift";
        }

        if(val3 >= val1 && val3 >= val2 && val3 >= val4 && val1 >= val5){
            largest = "service";
        }

        if(val4 >= val1 && val4 >= val2 && val4 >= val3 && val1 >= val5){
            largest = "time";
        }

        if(val5 >= val1 && val5 >= val2 && val5 >= val3 && val5 >= val4){
            largest = "touch";
        }
        return largest;
    }

}
