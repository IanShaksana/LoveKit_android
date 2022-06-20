package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_yes_no_general;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_task;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_create_0 extends Fragment{

    private FlexboxLayout word;
    private FlexboxLayout touch;
    private FlexboxLayout quality;
    private FlexboxLayout gift;
    private FlexboxLayout service;
    private Button custom_task;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_create0, container, false);

        word = view.findViewById(R.id.word);
        touch = view.findViewById(R.id.touch);
        quality = view.findViewById(R.id.quality);
        gift = view.findViewById(R.id.gift);
        service = view.findViewById(R.id.service);
        custom_task = view.findViewById(R.id.custom_task);

        final Bundle bundle = new Bundle();
        final Frag_commitment_create_1 newFrag = new Frag_commitment_create_1();

        word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("type",1);
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("type",2);
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("type",3);
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("type",4);
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putInt("type",5);
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        custom_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frag_commitment_create_2 newFrag2 = new Frag_commitment_create_2();
                bundle.putInt("type",0);
                newFrag2.setArguments(bundle);
                open_frag(newFrag2);
            }
        });


        return view;
    }

    private void open_frag(Fragment Frag){
        try {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
