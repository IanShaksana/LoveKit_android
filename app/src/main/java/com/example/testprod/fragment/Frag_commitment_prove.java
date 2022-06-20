package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.model.model_login;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_prove extends Fragment {

    private ImageView imageView;
    private Integer rating_int;
    private Boolean rating_flag;
    private String id_task;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_prove, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        Button button1 = (Button) view.findViewById(R.id.finish);
        Button button2 = (Button) view.findViewById(R.id.retry);
        RatingBar rating = view.findViewById(R.id.rating_bar);
        imageView = view.findViewById(R.id.prove_image);

        Bundle bundle = getArguments();
        rating_flag = false;
        if (bundle != null) {
            id_task = bundle.getString("id");
            get_img(id_task);

        }else{
            // show spinner
        }

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_flag = true;
                rating_int = Math.round(rating);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rating_flag){
                    // dialog confirm
                    mark(id_task,3,rating_int,1);
                }else{
                    Toast.makeText(getContext(),"Beri Penilaian Dahulu",Toast.LENGTH_SHORT).show();
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog confirm
                mark(id_task,1,0,1);
            }
        });


        return view;
    }

    private void get_img(String id){
        try {
            http_get get = new http_get(getContext());
            get.execute("prove",id);
            get.getListener(new http_get.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            JSONObject relation_state = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0);
                            byte[] decodedString = Base64.decode(relation_state.getString("encodedimage"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                            imageView.invalidate();
                        }else{
                            Toast.makeText(getContext(),"No Image",Toast.LENGTH_SHORT).show();
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

    private void mark(String id,Integer status, Integer nilai,Integer reviewed){
        try {
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject();
            JObject.put("id",id);
            JObject.put("status",status);
            JObject.put("repeatable",1);
            JObject.put("nilai",nilai);
            JObject.put("reviewed",reviewed);
            JObject.put("api","review_task");
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
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private  void back(){
        getFragmentManager().popBackStack();
    }

}
