package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_yes_no_accept;
import com.example.testprod.dialog.dialog_yes_no_general;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_login;
import com.example.testprod.model.model_task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_create extends Fragment implements dialog_yes_no_general.dialogListener_yes_no_general {

    private com.google.android.material.textfield.TextInputEditText title;
    private com.google.android.material.textfield.TextInputEditText desc;
    private com.google.android.material.textfield.TextInputEditText repeatable;
    private com.google.android.material.textfield.TextInputEditText rep;
    private com.google.android.material.textfield.TextInputEditText reviewed;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_create, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        repeatable = view.findViewById(R.id.repeatable);
        rep = view.findViewById(R.id.rep);
        reviewed = view.findViewById(R.id.reviewed);

        Button button1 = (Button) view.findViewById(R.id.create_commitment);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = false;
                List<EditText> wasd = new ArrayList<>();
                wasd.add(title);
                wasd.add(desc);
                wasd.add(repeatable);
                wasd.add(rep);
                wasd.add(reviewed);
                for (EditText lol:wasd) {
                    if(isEmpty(lol)){
                        check = false;
                        break;
                    }else{
                        check = true;
                    }
                }
                if(check){
                    opendialog_yes_no("Apakah anda yakin dengan komitmen anda ?");
                }
                // dialog confirm
                // back
            }
        });

        return view;
    }

    private void create_task(String title, String description, Integer repeatable, Integer rep, Integer reviewed){
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            model_task newTask = new model_task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setIdRelationship(gu.get_data_string(gu.get_data("Login_State"),"id"));
            newTask.setNilai(0);
            newTask.setStatus(1);
            newTask.setRepeatable(repeatable);
            newTask.setRep(rep);
            newTask.setCompletion(0);
            newTask.setReviewed(reviewed);

            Gson gson = new Gson();
            JSONObject JObject = new JSONObject(gson.toJson(newTask));
            JObject.put("api","create_task");
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
                        Log.e("Exception",e.toString());
                    }
                }
            });

        }catch (Exception e){
            Log.e("Exception",e.toString());
        }

    }

    private void opendialog_yes_no(String message) {
        dialog_yes_no_general dialogfragment = new dialog_yes_no_general(message);
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    private  void back(){
        getFragmentManager().popBackStack();
    }

    @Override
    public void apply_general(Boolean status) {
        if(status){
            create_task(title.getText().toString(),desc.getText().toString(),Integer.parseInt(repeatable.getText().toString()),Integer.parseInt(rep.getText().toString()),Integer.parseInt(reviewed.getText().toString()));
        }
    }

    private boolean isEmpty(EditText test){
        return test.getText().toString().trim().length() == 0;
    }



}
