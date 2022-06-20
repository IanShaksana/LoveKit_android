package com.example.testprod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testprod.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;


public class commitment_list_adapter extends ArrayAdapter<JSONObject> {

    public commitment_list_adapter(@NonNull Context context, List<JSONObject> resource) {
        super(context, R.layout.commitment_list, resource);
    }


    private TextView commitment_name;
    private TextView commitment_desc;
    private TextView commitment_repetisi;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cusView = inflater.inflate(R.layout.commitment_list_detail, parent, false);
        commitment_name = cusView.findViewById(R.id.commitment_list_detail_name);
        commitment_desc = cusView.findViewById(R.id.commitment_list_detail_desc);
        commitment_repetisi = cusView.findViewById(R.id.repetisi);
        ImageView img = cusView.findViewById(R.id.type_vis);


        try {
            JSONObject JObject = getItem(position);

            if (JObject.getInt("type") == 1) {
                Picasso.get().load(R.drawable.icon_5bk_2_word).centerInside().fit().into(img);
            }
            if (JObject.getInt("type") == 2) {
                Picasso.get().load(R.drawable.icon_5bk_6_touch).centerInside().fit().into(img);
            }
            if (JObject.getInt("type") == 3) {
                Picasso.get().load(R.drawable.icon_5bk_5_qtime).centerInside().fit().into(img);
            }
            if (JObject.getInt("type") == 4) {
                Picasso.get().load(R.drawable.icon_5bk_3_gift).centerInside().fit().into(img);
            }
            if (JObject.getInt("type") == 5) {
                Picasso.get().load(R.drawable.icon_5bk_4_service).centerInside().fit().into(img);
            }
            if (JObject.getInt("type") == 0) {
                Picasso.get().load(R.drawable.icon_5bk_1_lovekit).centerInside().fit().into(img);
            }

            String task = "Judul : " + getItem(position).getString("title");
            commitment_name.setText(task);
            commitment_desc.setText("Deskripsi : " + getItem(position).getString("description"));
            if (getItem(position).getInt("repeatable") == 1) {
                commitment_repetisi.setText(JObject.getInt("completion") + " / " + JObject.getInt("rep"));
            } else {
                commitment_repetisi.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cusView;

    }

}