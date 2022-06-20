package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.adapter.commitment_list_adapter;
import com.example.testprod.adapter.partner_pending_list_adapter;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_yes_no_accept;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_relation;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

// done
public class Frag_partner_list extends Fragment implements dialog_yes_no_accept.dialogListener_yes_no_warn{

    private ListView partner_list;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.partner_pending_list, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String i = bundle.getString("id");
            //Snackbar.make(container, i,500).show();
            update_listview(view,i);
        }

        return view;
    }

    private void update_listview(final View view, String id){
        http_get get =  new http_get(getContext());
        get.execute("get_relation_pending",id);
        get.getListener(new http_get.OnUpdateListener() {
            @Override
            public void onUpdate(JSONObject JObject) {
                try {
                    if(JObject.getString("statuscode").equals("OK")){
                        JSONArray JArray= new JSONObject(JObject.toString()).getJSONArray("data");
                        List<JSONObject> resource = new ArrayList<>();
                        for (int i = 0; i < JArray.length(); i++) {
                            resource.add(JArray.getJSONObject(i));
                        }
                        ListAdapter adapter = new partner_pending_list_adapter(getContext(),resource);
                        partner_list = (ListView) view.findViewById(R.id.partner_list);
                        partner_list.setAdapter(adapter);
                        partner_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String choosen = String.valueOf(parent.getItemAtPosition(position));
                                    JSONObject JObject = new JSONObject(choosen);
                                    opendialog_yes_no(JObject.getString("id"));
                                }catch (Exception e){
                                    Log.e("Exception",e.toString());
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.e("Exception",e.toString());
                }
            }
        });
    }

    private void opendialog_yes_no(String choosen) {
        dialog_yes_no_accept dialogfragment = new dialog_yes_no_accept(choosen);
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_accept(Integer status, String ID_relation) {
        try {
            http_post post = new http_post(getContext());
            model_relation newRelation = new model_relation();
            newRelation.setId(ID_relation);
            newRelation.setStatus(status);
            Gson gson = new Gson();
            JSONObject JObject = new JSONObject(gson.toJson(newRelation));
            JObject.put("api","accept_relation");
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {

                    try {

                        if(JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                            update();
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
        if (current instanceof Frag_partner_list) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

}
