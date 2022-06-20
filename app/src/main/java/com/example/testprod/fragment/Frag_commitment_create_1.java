package com.example.testprod.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_create_1 extends Fragment{

    private Button suggestion1;
    private Button suggestion2;
    private Button suggestion3;
    private Button suggestion4;
    private Button suggestion5;
    private Button suggestion6;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_create1, container, false);

        suggestion1 = view.findViewById(R.id.suggestion1);
        suggestion2 = view.findViewById(R.id.suggestion2);
        suggestion3 = view.findViewById(R.id.suggestion3);
        suggestion4 = view.findViewById(R.id.suggestion4);
        suggestion5 = view.findViewById(R.id.suggestion5);
        suggestion6 = view.findViewById(R.id.suggestion6);

        final Bundle bundle = getArguments();

        switch (bundle.getInt("type")){
            case 1:
                suggestion1.setText(getResources().getString(R.string.title_word1));
                suggestion2.setText(getResources().getString(R.string.title_word2));
                suggestion3.setText(getResources().getString(R.string.title_word3));
                suggestion4.setText(getResources().getString(R.string.title_word4));
                suggestion5.setText(getResources().getString(R.string.title_word5));
                break;
            case 2:
                suggestion1.setText(getResources().getString(R.string.title_touch1));
                suggestion2.setText(getResources().getString(R.string.title_touch2));
                suggestion3.setText(getResources().getString(R.string.title_touch3));
                suggestion4.setText(getResources().getString(R.string.title_touch4));
                suggestion5.setText(getResources().getString(R.string.title_touch5));
                break;
            case 3:
                suggestion1.setText(getResources().getString(R.string.title_time1));
                suggestion2.setText(getResources().getString(R.string.title_time2));
                suggestion3.setText(getResources().getString(R.string.title_time3));
                suggestion4.setText(getResources().getString(R.string.title_time4));
                suggestion5.setText(getResources().getString(R.string.title_time5));
                break;
            case 4:
                suggestion1.setText(getResources().getString(R.string.title_gift1));
                suggestion2.setText(getResources().getString(R.string.title_gift2));
                suggestion3.setText(getResources().getString(R.string.title_gift3));
                suggestion4.setText(getResources().getString(R.string.title_gift4));
                suggestion5.setText(getResources().getString(R.string.title_gift5));
                break;
            case 5:
                suggestion1.setText(getResources().getString(R.string.title_service1));
                suggestion2.setText(getResources().getString(R.string.title_service2));
                suggestion3.setText(getResources().getString(R.string.title_service3));
                suggestion4.setText(getResources().getString(R.string.title_service4));
                suggestion5.setText(getResources().getString(R.string.title_service5));
                break;
            default:
                suggestion1.setText("No Suggestion");
                break;
        }
        final Frag_commitment_create_2 newFrag = new Frag_commitment_create_2();

        suggestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("template1",suggestion1.getText().toString());
                //bundle.putString("template2",suggestion1.getText().toString());
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        suggestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("template1",suggestion2.getText().toString());
                //bundle.putString("template2",suggestion2.getText().toString());
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        suggestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("template1",suggestion3.getText().toString());
                //bundle.putString("template2",suggestion3.getText().toString());
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        suggestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("template1",suggestion4.getText().toString());
                //bundle.putString("template2",suggestion4.getText().toString());
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        suggestion5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("template1",suggestion5.getText().toString());
                //bundle.putString("template2",suggestion5.getText().toString());
                newFrag.setArguments(bundle);
                open_frag(newFrag);
            }
        });

        suggestion6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("template1","no");
                //bundle.putString("template2","no");
                newFrag.setArguments(bundle);
                open_frag(newFrag);
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
