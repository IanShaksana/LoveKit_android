package com.example.testprod.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.adapter.commitment_list_adapter;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.dialog.dialog_yes_no_general;
import com.example.testprod.general.utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_list_complete extends Fragment implements dialog_yes_no_general.dialogListener_yes_no_general {

    private ListView commitment_list;
    private ImageView empty;
    private TextView judul_atas;

    private JSONObject JObject_choosen;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_list, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        FloatingActionButton button1 = view.findViewById(R.id.add_commitment);
        judul_atas = view.findViewById(R.id.Judul_komitmen);
        judul_atas.setText("Pencapaianku");
        button1.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("id");
            Boolean local_or_not = bundle.getBoolean("local_or_not");
            if (!local_or_not) {
                // partner
                update_listview(view, id);
                button1.setVisibility(view.GONE);
            } else {
                // Toast.makeText(getContext(),"bundle not null",Toast.LENGTH_SHORT).show();
                // personal
                update_listview(view, id);
            }
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_frag(new Frag_commitment_create());
            }
        });

        return view;
    }

    private void open_frag(Fragment Frag) {
        try {
            assert getFragmentManager() != null;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void update_listview(final View view, String id) {
        http_get get = new http_get(getContext());
        get.execute("task_record", id);
        get.getListener(new http_get.OnUpdateListener() {
            @Override
            public void onUpdate(final JSONObject JObject) {
                try {
                    if (JObject.getString("statuscode").equals("OK")) {
                        JSONArray JArray = new JSONObject(JObject.toString()).getJSONArray("data");
                        List<JSONObject> resource = new ArrayList<>();
                        for (int i = 0; i < JArray.length(); i++) {
                            resource.add(JArray.getJSONObject(i));
                        }
                        commitment_list = view.findViewById(R.id.commitment_list);
                        empty = view.findViewById(R.id.empty);
                        commitment_list.setEmptyView(empty);
                        ListAdapter adapter = new commitment_list_adapter(getContext(), resource);
                        commitment_list.setAdapter(adapter);
                        commitment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    String choosen = String.valueOf(parent.getItemAtPosition(position));
                                    JObject_choosen = new JSONObject(choosen);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id",choosen);
                                    Frag_commitment_detail newDetail = new Frag_commitment_detail();
                                    newDetail.setArguments(bundle);
                                    open_frag(newDetail);
                                } catch (Exception e) {
                                    Log.e("Exception", e.toString());
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "update_list_failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void opendialog_yes_no(String message) {
        dialog_yes_no_general dialogfragment = new dialog_yes_no_general(message);
        dialogfragment.setTargetFragment(this, 0);
        dialogfragment.show(getFragmentManager(), "exa");
    }

    @Override
    public void apply_general(Boolean status) {
        if (status) {
            complete_task(JObject_choosen);
        }
    }

    private void complete_task(JSONObject JObject_choosen) {
        try {
            http_post post = new http_post(getContext());
            JSONObject JObject = JObject_choosen;
            JObject.put("api", "complete_task");
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if (JObject.getString("statuscode").equals("OK")) {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                            update();
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

    private void update() {
        Fragment current = getFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_commitment_list_complete) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

    private void review_task(JSONObject JObject_choosen) {
        try {
            http_post post = new http_post(getContext());
            JSONObject JObject = JObject_choosen;
            JObject.put("api", "complete_task");
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if (JObject.getString("statuscode").equals("OK")) {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                            update();
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


}
