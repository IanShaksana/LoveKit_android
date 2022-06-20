package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_detail extends Fragment {

    private com.google.android.material.textfield.TextInputEditText title;
    private com.google.android.material.textfield.TextInputEditText desc;
    private com.google.android.material.textfield.TextInputEditText repeatable;
    private com.google.android.material.textfield.TextInputEditText rep;
    private com.google.android.material.textfield.TextInputEditText reviewed;
    private com.google.android.material.textfield.TextInputEditText created_at;
    private com.google.android.material.textfield.TextInputEditText finished_at;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_detail, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        Button button1 = view.findViewById(R.id.see_prove);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        repeatable = view.findViewById(R.id.repeatable);
        rep = view.findViewById(R.id.rep);
        reviewed = view.findViewById(R.id.reviewed);
        created_at = view.findViewById(R.id.created_at);
        finished_at = view.findViewById(R.id.finish_at);

        final Bundle bundle = getArguments();
        button1.setVisibility(View.GONE);

        if (!bundle.isEmpty()) {

            try {
                JSONObject JObject = new JSONObject(bundle.getString("id"));

                title.setText(JObject.getString("title"));
                desc.setText(JObject.getString("description"));
                if(JObject.getString("repeatable").equals("1")){
                    repeatable.setText("Iya");
                }else {
                    repeatable.setText("Tidak");
                }

                rep.setText(JObject.getString("rep"));

                if(JObject.getString("repeatable").equals("1")){
                    reviewed.setText("Iya");
                }else{
                    reviewed.setText("Tidak");
                }

                created_at.setText(format_date(JObject.getString("vcreatedAt")));
                finished_at.setText(format_date(JObject.getString("zdeletedAt")));

                if (!JObject.getString("prove").contains(null)) {
                    button1.setVisibility(View.VISIBLE);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private String format_date(String sdate) {
        String[] splittext = sdate.split("T");
        String newSdate = splittext[0];
        Log.i("format date", newSdate);
        String[] stime0 = splittext[1].split("\\.");
        String newStime = stime0[0];
        return newSdate + " " + newStime;
    }


}
