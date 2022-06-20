package com.example.testprod.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testprod.R;

import org.json.JSONObject;

import java.util.List;


public class partner_pending_list_adapter extends ArrayAdapter<JSONObject> {

    public partner_pending_list_adapter(@NonNull Context context, List<JSONObject> resource) {
        super(context, R.layout.commitment_list, resource);
    }


    private TextView partner_name;
    private TextView partner_status;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cusView = inflater.inflate(R.layout.partner_pending_list_detail, parent, false);
        partner_name = cusView.findViewById(R.id.partner_list_detail_name);
        partner_status = cusView.findViewById(R.id.partner_list_detail_status);
        try{
            partner_name.setText("Nama : "+getItem(position).getString("nama1"));
            partner_status.setText("Status : menunggu");
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
        return cusView;

    }

}