package com.example.testprod.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testprod.R;
import com.example.testprod.activity.LoginActivity;
import com.example.testprod.activity.MainActivity;
import com.example.testprod.activity.OpeningActivity;
import com.example.testprod.activity.TestActivity;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_yes_no_warn;
import com.example.testprod.model.model_login;
import com.github.jorgecastillo.FillableLoader;
import com.github.jorgecastillo.FillableLoaderBuilder;
import com.github.jorgecastillo.clippingtransforms.PlainClippingTransform;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import me.itangqi.waveloadingview.WaveLoadingView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_home extends Fragment implements dialog_yes_no_warn.dialogListener_yes_no_warn{
    SwipeRefreshLayout swipeLayout;
    WaveLoadingView waveLoadingView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        Button button2 = (Button) view.findViewById(R.id.LogOut);
        Button button3 = (Button) view.findViewById(R.id.open_partner);
        Button button4 = (Button) view.findViewById(R.id.open_commitment);
        SeekBar seekBar = view.findViewById(R.id.seek_bar);
        waveLoadingView = view.findViewById(R.id.heart_fill);
        waveLoadingView.setProgressValue(seekBar.getProgress());
        waveLoadingView.setBottomTitle(seekBar.getProgress()+"%");


        Button button1 = (Button) view.findViewById(R.id.open_test);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog_yes_no();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waveLoadingView.setProgressValue(progress);
                waveLoadingView.setBottomTitle(seekBar.getProgress()+"%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        waveLoadingView.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logout_real(preferences);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag_partner_menu partner_menu = new Frag_partner_menu();
                open_frag(partner_menu);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_frag(new Frag_commitment_menu());
            }
        });




        return view;
    }


    private void open_frag(Fragment Frag){
        try {
            assert getFragmentManager() != null;
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

    private void logout_local(final SharedPreferences preferences){
        Toast.makeText(getContext(), "Logout Success", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        getActivity().finish();
    }

    private void logout_real(final SharedPreferences preferences){
        try {
            http_post post = new http_post(getContext());
            String STRING_login_state = preferences.getString("Login_State", "");
            Log.i("logout",STRING_login_state);
            Gson gson = new Gson();
            model_login newLogin = gson.fromJson(STRING_login_state,model_login.class);
            JSONObject jsonObject = new JSONObject(gson.toJson(newLogin));
            jsonObject.put("api","logout");
            post.execute(jsonObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try{
                        if(!JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(), "Keluar Gagal : "+JObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Keluar Sukses", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.commit();
                            getActivity().finish();
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

    private void opendialog_yes_no() {
        dialog_yes_no_warn dialogfragment = new dialog_yes_no_warn();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_warn(Boolean status) {
        if(status){
            getActivity().startActivityForResult(new Intent(getActivity(), OpeningActivity.class),200);
        }
    }

}
