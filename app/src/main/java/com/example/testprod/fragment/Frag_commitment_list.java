package com.example.testprod.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testprod.R;
import com.example.testprod.activity.CreateActivity;
import com.example.testprod.activity.OpeningActivity;
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

public class Frag_commitment_list extends Fragment implements dialog_yes_no_general.dialogListener_yes_no_general, SwipeRefreshLayout.OnRefreshListener {

    private ListView commitment_list;
    private ImageView empty;
    private  TextView judu_komitmen;

    private JSONObject JObject_choosen;
    SwipeRefreshLayout swipeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_list, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        FloatingActionButton button1 = view.findViewById(R.id.add_commitment);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        swipeLayout.setOnRefreshListener(this);
        judu_komitmen = view.findViewById(R.id.Judul_komitmen);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("id");
            Boolean local_or_not = bundle.getBoolean("local_or_not");
            if (!local_or_not) {
                // partner
                judu_komitmen.setText("Komitmen Partner");
                update_listview(view, id);
                button1.setVisibility(view.GONE);
            } else {
                // Toast.makeText(getContext(),"bundle not null",Toast.LENGTH_SHORT).show();
                // personal
                judu_komitmen.setText("Komitmenku");
                update_listview(view, id);
            }
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(getActivity(), CreateActivity.class),100);
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
        get.execute("get_task", id);
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
                                    // open dialog
                                    utility gu = new utility(getContext());

                                    if (JObject_choosen.getString("idRelationship").equals(gu.get_data_string(gu.get_data("Login_State"), "id"))) {
                                        // personal
                                        if (JObject_choosen.getInt("repeatable") == 1) {

                                            if (JObject_choosen.getInt("reviewed") == 1) {
                                                // reviewed
                                                // buka upload image
                                                if (JObject_choosen.getInt("completion") + 1 == JObject_choosen.getInt("rep")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("id", JObject_choosen.getString("id"));
                                                    Frag_commitment_upload upload = new Frag_commitment_upload();
                                                    upload.setArguments(bundle);
                                                    open_frag(upload);
                                                } else if (JObject_choosen.getInt("status") == 0) {
                                                    Toast.makeText(getContext(), "Sedang Dalam Proses Evaluasi", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    opendialog_yes_no("Apakah kamu yakin ingin menyelesaikan komitmen ini ?");
                                                }


                                            } else {
                                                //done
                                                opendialog_yes_no("Apakah kamu yakin ingin menyelesaikan komitmen ini ?");
                                                // non reviewed
                                            }


                                        } else {
                                            // single
                                            if (JObject_choosen.getInt("reviewed") == 1 && JObject_choosen.getInt("status") == 1) {
                                                // reviewed
                                                // buka upload image
                                                Bundle bundle = new Bundle();
                                                bundle.putString("id", JObject_choosen.getString("id"));
                                                Frag_commitment_upload upload = new Frag_commitment_upload();
                                                upload.setArguments(bundle);
                                                open_frag(upload);

                                            } else if (JObject_choosen.getInt("status") == 0) {
                                                Toast.makeText(getContext(), "Sedang Dalam Proses Evaluasi", Toast.LENGTH_SHORT).show();
                                            } else {
                                                //done
                                                opendialog_yes_no("Apakah kamu yakin ingin menyelesaikan komitmen ini ?");
                                                // non reviewed
                                            }
                                        }

                                    } else {
                                        // punya e partner
                                        if (JObject_choosen.getInt("reviewed") == 1 && JObject_choosen.getInt("status") == 0) {
                                            // open frag prove
                                            Frag_commitment_prove prove = new Frag_commitment_prove();
                                            Bundle newBundle = new Bundle();
                                            newBundle.putString("id", JObject_choosen.getString("id"));
                                            prove.setArguments(newBundle);
                                            open_frag(prove);
                                        } else if (JObject_choosen.getInt("status") == 1 && JObject_choosen.getInt("status") == 1) {
                                            Toast.makeText(getContext(), "Komitmen Masih Belum Selesai", Toast.LENGTH_SHORT).show();
                                        } else if (JObject_choosen.getInt("reviewed") == 0) {
                                            Toast.makeText(getContext(), "Komitmen Partner Tidak Ingin di Evaluasi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
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
        if (current instanceof Frag_commitment_list) {
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


    @Override
    public void onRefresh() {
        update();
    }
}
