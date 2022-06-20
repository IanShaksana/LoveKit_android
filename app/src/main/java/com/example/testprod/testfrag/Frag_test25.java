package com.example.testprod.testfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.fragment.Frag_result;
import com.example.testprod.fragment.Frag_result0;

public class Frag_test25 extends Fragment {

    private TextView pertanyaan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_test, container, false);
        final Bundle prevBundle = getArguments();
        Button button1 =  view.findViewById(R.id.yes);
        Button button2 =  view.findViewById(R.id.no);
        TextView numpage = view.findViewById(R.id.num_page);
        numpage.setText("25");
        pertanyaan = view.findViewById(R.id.pertanyaan);
        pertanyaan.setText(getResources().getString(R.string.pertanyaan25));

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevBundle.putInt("25",1);
                open_frag(new Frag_result0(), prevBundle);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevBundle.putInt("25",0);
                open_frag(new Frag_result0(), prevBundle);
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
                    .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
