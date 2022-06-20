package com.example.testprod.testfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.testprod.R;

public class Frag_test24 extends Fragment {

    private TextView pertanyaan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_test, container, false);
        final Bundle prevBundle = getArguments();
        Button button1 =  view.findViewById(R.id.yes);
        Button button2 =  view.findViewById(R.id.no);
        TextView numpage = view.findViewById(R.id.num_page);
        numpage.setText("24");
        pertanyaan = view.findViewById(R.id.pertanyaan);
        pertanyaan.setText(getResources().getString(R.string.pertanyaan24));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevBundle.putInt("24",1);
                open_frag(new Frag_test25(), prevBundle);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevBundle.putInt("24",0);
                open_frag(new Frag_test25(), prevBundle);
            }
        });
        return view;
    }

    private void open_frag(Fragment Frag, Bundle bundle){
        try {
            Frag.setArguments(bundle);
            assert getFragmentManager() != null;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
