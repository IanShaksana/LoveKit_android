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
import com.example.testprod.dialog.*;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_relation;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_partner_menu extends Fragment implements  dialog_invite.dialogListener_worker, dialog_yes_no_delete_partner.dialogListener_yes_no_warn {
    SharedPreferences preferences;
    utility gu;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.partner_menu, container, false);

        // done
        gu = new utility(getContext());
        preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        gu.update_relation_state_shared_preferences();

        // done
        button1 = (Button) view.findViewById(R.id.add_partner);
        // done
        button2 = (Button) view.findViewById(R.id.partner_profile);
        // done
        button3 = (Button) view.findViewById(R.id.partner_commitment);
        // done
        button4 = (Button) view.findViewById(R.id.accept_partner);
        // done
        button5 = (Button) view.findViewById(R.id.del_partner);

        update_relation_state_shared_preferences();


        // done
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog_invite();
            }
        });


        // done
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle newBundle = new Bundle();
                newBundle.putString("id",comparison());
                newBundle.putBoolean("local_or_not",false);
                Frag_profile wasd = new Frag_profile();
                wasd.setArguments(newBundle);
                open_frag(wasd);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle newBundle = new Bundle();
                newBundle.putString("id",comparison());
                newBundle.putBoolean("local_or_not",false);
                Frag_commitment_list newCommitmentList = new Frag_commitment_list();
                newCommitmentList.setArguments(newBundle);
                open_frag(newCommitmentList);
            }
        });

        // done
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utility gu = new utility(getContext());
                Frag_partner_list partner_list = new Frag_partner_list();
                Bundle newBundle = new Bundle();
                newBundle.putString("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
                partner_list.setArguments(newBundle);
                open_frag(partner_list);
            }
        });

        // done
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog_delete();
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

    private void opendialog_invite() {
        dialog_invite dialogfragment = new dialog_invite();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_invite(final String subject) {
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            model_relation newRelation = new model_relation();
            newRelation.setPasangan1(gu.get_data_string(gu.get_data("Login_State"),"id"));
            newRelation.setPasangan2(subject);

            Gson gson = new Gson();
            JSONObject JObject = new JSONObject(gson.toJson(newRelation));
            JObject.put("api","add_relation");
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

        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }

    private void update() {
        Fragment current = getFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_partner_menu) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

    private  void back(){
        getFragmentManager().popBackStack();
    }


    private void opendialog_delete() {
        dialog_yes_no_delete_partner dialogfragment = new dialog_yes_no_delete_partner();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_warn(Boolean status) {
        if(status){
            try {
                utility gu = new utility(getContext());
                http_post post = new http_post(getContext());
                JSONObject JObject = new JSONObject();
                JObject.put("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
                JObject.put("api","del_relation");
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
                e.printStackTrace();
            }
        }
    }

    private String comparison(){
        try {
            String local_id = gu.get_data_string(gu.get_data("Login_State"),"id");
            String state = gu.get_data("Relation_State");
            JSONObject JObject = new JSONObject(state);
            String pasangan1 = JObject.getString("pasangan1");
            String pasangan2 = JObject.getString("pasangan2");
            String partner_id = "";


            if(local_id.equals(pasangan1)){
                partner_id = pasangan2;
            }else{
                partner_id = pasangan1;
            }
            return  partner_id;

        }catch (Exception e){
            Log.e("Exception",e.toString());
            return null;
        }
    }

    public void update_relation_state_shared_preferences(){
        try {
            utility gu = new utility(getContext());
            http_get get = new http_get(getContext());
            get.execute("get_relation",gu.get_data_string(gu.get_data("Login_State"),"id"));
            get.getListener(new http_get.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            String relation_state = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0).toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Relation_State", relation_state);
                            editor.apply();
                            button1.setVisibility(View.GONE);
                            button4.setVisibility(View.GONE);
                            //Toast.makeText(context,relation_state,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(),"Kamu Sudah Punya Partner",Toast.LENGTH_SHORT).show();
                            Log.i("Relation_State",relation_state);
                        }else{
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("Relation_State");
                            editor.apply();
                            button3.setVisibility(View.GONE);
                            button2.setVisibility(View.GONE);
                            button5.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Kamu Belum Punya Partner",Toast.LENGTH_SHORT).show();
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


}
