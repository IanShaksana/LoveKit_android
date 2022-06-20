package com.example.testprod.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_number;
import com.example.testprod.dialog.dialog_yes_no_general;
import com.example.testprod.general.utility;
import com.example.testprod.model.model_task;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_create_2 extends Fragment implements dialog_yes_no_general.dialogListener_yes_no_general, dialog_number.dialogListener_worker {

    private com.google.android.material.textfield.TextInputEditText title;
    private com.google.android.material.textfield.TextInputEditText desc;
    private com.google.android.material.textfield.TextInputEditText repeatable;
    private com.google.android.material.textfield.TextInputEditText rep;
    private com.google.android.material.textfield.TextInputEditText reviewed;
    private Integer type;

    private Integer repeat_int;
    private Integer review_int;

    private SharedPreferences preferences;

    private boolean cek_partner = false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_create, container, false);

        update_relation_state_shared_preferences();
        preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        repeatable = view.findViewById(R.id.repeatable);
        rep = view.findViewById(R.id.rep);
        reviewed = view.findViewById(R.id.reviewed);

        rep.setText("0");
        repeatable.setText("Tidak");
        repeat_int = 0;
        reviewed.setText("Tidak");
        review_int = 0;

        //rep.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if (!bundle.isEmpty()) {
            type = bundle.getInt("type");
            if (bundle.getInt("type") != 0 && !bundle.getString("template1").equals("no")) {
                title.setText(bundle.getString("template1"));
                title.setFocusable(false);
                title.setClickable(false);
                title.setCursorVisible(false);
                title.setFocusableInTouchMode(false);
                //desc.setText(bundle.getString("template2"));
                //desc.setFocusable(false);
                //desc.setClickable(false);
                //desc.setCursorVisible(false);
                //desc.setFocusableInTouchMode(false);
            }
        }

        Button button1 = (Button) view.findViewById(R.id.create_commitment);
        repeatable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] singleChoiceItems = {"Tidak", "Iya"};
                final int itemSelected = 0;
                new AlertDialog.Builder(getContext())
                        .setTitle("Apakah komitmen ini berulang ?")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                if (selectedIndex == 1) {
                                    repeatable.setText("Iya");
                                    rep.setText("2");
                                } else {
                                    repeatable.setText("Tidak");
                                    rep.setText("0");
                                }
                                repeat_int = selectedIndex;
                            }
                        })
                        .setPositiveButton("Oke", null)
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });

        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat_int == 1){
                    opendialog_number();
                }
            }
        });

        reviewed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cek_partner) {
                    String[] singleChoiceItems = {"Tidak", "Iya"};
                    final int itemSelected = 0;
                    new AlertDialog.Builder(getContext())
                            .setTitle("Apakah komitmen mau dievaluasi ? (butuh partner)")
                            .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                    if (selectedIndex == 1) {
                                        reviewed.setText("Iya");
                                    } else {
                                        reviewed.setText("Tidak");
                                    }
                                    review_int = selectedIndex;
                                }
                            })
                            .setPositiveButton("Oke", null)
                            .setNegativeButton("Batal", null)
                            .show();
                }else{
                    Toast.makeText(getContext(),"Kamu Belum Punya Partner",Toast.LENGTH_SHORT).show();
                }

            }
        });


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
                for (EditText lol : wasd) {
                    if (isEmpty(lol)) {
                        check = false;
                        break;
                    } else {
                        check = true;
                    }
                }
                if (check) {
                    opendialog_yes_no("Komitmen yang sudah dibuat tidak bisa diedit/dihapus.\n" +
                            "Apakah kamu yakin?");
                }else{
                    Toast.makeText(getContext(),"Data Komitmen Belum Lengkap",Toast.LENGTH_SHORT).show();
                }
                // dialog confirm
                // back
            }
        });

        return view;
    }

    private void create_task(String title, String description, Integer repeatable, Integer rep, Integer reviewed) {
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            model_task newTask = new model_task();
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setIdRelationship(gu.get_data_string(gu.get_data("Login_State"), "id"));
            newTask.setNilai(0);
            newTask.setStatus(1);
            newTask.setRepeatable(repeatable);
            newTask.setRep(rep);
            newTask.setCompletion(0);
            newTask.setReviewed(reviewed);
            newTask.setType(type);

            Gson gson = new Gson();
            JSONObject JObject = new JSONObject(gson.toJson(newTask));
            JObject.put("api", "create_task");
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if (JObject.getString("statuscode").equals("OK")) {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();

                        } else {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("Exception", e.toString());
                    }
                }
            });

        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    private void opendialog_yes_no(String message) {
        dialog_yes_no_general dialogfragment = new dialog_yes_no_general(message);
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    private void opendialog_number() {
        dialog_number dialogfragment = new dialog_number();
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }



    @Override
    public void apply_general(Boolean status) {
        if (status) {
            create_task(title.getText().toString(), desc.getText().toString(), repeat_int, Integer.parseInt(rep.getText().toString()), review_int);
        }
    }

    private boolean isEmpty(EditText test) {
        return test.getText().toString().trim().length() == 0;
    }

    @Override
    public void apply_invite(String wasd) {
        rep.setText(wasd);
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
                            cek_partner=true;
                            //Toast.makeText(context,relation_state,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getContext(),"Kamu Sudah Punya Partner",Toast.LENGTH_SHORT).show();
                            Log.i("Relation_State",relation_state);
                        }else{
                            cek_partner=false;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("Relation_State");
                            editor.apply();
                            //Toast.makeText(getContext(),"Kamu Belum Punya Partner",Toast.LENGTH_SHORT).show();
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
