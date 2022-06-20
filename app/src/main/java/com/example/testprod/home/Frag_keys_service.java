package com.example.testprod.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testprod.R;
import com.example.testprod.background.http_post;
import com.example.testprod.model.model_login;
import com.google.gson.Gson;

import org.json.JSONObject;

import me.itangqi.waveloadingview.WaveLoadingView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_keys_service extends Fragment {
    SwipeRefreshLayout swipeLayout;
    WaveLoadingView waveLoadingView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.keys_service, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);


        return view;
    }




}
