package com.example.testprod.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.testprod.R;
import com.example.testprod.activity.LoginActivity;
import com.example.testprod.activity.MainActivity;
import com.example.testprod.activity.TestActivity;
import com.example.testprod.background.http_post;
import com.example.testprod.general.utility;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Frag_suggestion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_suggestion extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Frag_suggestion() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sugesstion.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_suggestion newInstance(String param1, String param2) {
        Frag_suggestion fragment = new Frag_suggestion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        Button button =  view.findViewById(R.id.finish_suggestion);
        Bundle prevBundle = getArguments();
        final String type = prevBundle.getString("type");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // http post kalo dah selesai
                try {
                    http_post post = new http_post(getContext());
                    utility gu = new utility(getContext());
                    JSONObject JObject = new JSONObject(gu.get_data("Login_State"));
                    JObject.put("testDetail",type);
                    JObject.put("api","answer");
                    post.execute(JObject);
                    post.getListener(new http_post.OnUpdateListener() {
                        @Override
                        public void onUpdate(JSONObject JObject) {
                            try {
                                if(!JObject.getString("statuscode").equals("OK")){
                                    Toast.makeText(getContext(), "Tes Gagal : "+JObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                }else{
                                    Toast.makeText(getContext(), "Tes Sukses", Toast.LENGTH_SHORT).show();
                                    getActivity().startActivity(new Intent(getContext(), MainActivity.class).putExtra("first",1));
                                    getActivity().finish();
                                }
                            }catch (Exception e){
                                Log.e("Exception",e.toString());
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                /*
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                        .replace(R.id.main_fragment, new Frag_home())
                        .addToBackStack(null)
                        .commit();
                */
            }
        });
        return view;
    }
}
