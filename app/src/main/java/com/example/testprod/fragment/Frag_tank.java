package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testprod.R;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_login;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import me.itangqi.waveloadingview.WaveLoadingView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_tank extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeLayout;
    WaveLoadingView waveLoadingView;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tank, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        waveLoadingView = view.findViewById(R.id.heart_fill);
        getlovetank();

        return view;
    }

    private  void getlovetank(){
        http_get get = new http_get(getContext());
        utility gu = new utility(getContext());
        get.execute("profile",gu.get_data_string(gu.get_data("Login_State"),"id"));
        get.getListener(new http_get.OnUpdateListener() {
            @Override
            public void onUpdate(JSONObject JObject) {
                try {
                    if(JObject.getString("statuscode").equals("OK")){
                        JSONObject value = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0);
                        waveLoadingView.setProgressValue(value.getInt("hearttank"));
                        waveLoadingView.setBottomTitle(value.getInt("hearttank")+"%");
                    }else{
                        Toast.makeText(getContext(),"Jaringan Error",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("Exception",e.toString());
                    Toast.makeText(getContext(),"Jaringan Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    @Override
    public void onRefresh() {
        update();
    }

    private void update() {
        Fragment current = getFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_tank) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

}
