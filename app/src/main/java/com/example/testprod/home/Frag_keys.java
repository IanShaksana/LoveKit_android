package com.example.testprod.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.testprod.R;
import com.example.testprod.fragment.Frag_home;
import com.example.testprod.testfrag.Frag_test1;
import com.example.testprod.testfrag.Frag_test2;
import com.example.testprod.testfrag.Frag_test3;
import com.example.testprod.testfrag.Frag_test4;
import com.example.testprod.testfrag.Frag_test5;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_keys extends Fragment {
    SwipeRefreshLayout swipeLayout;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.keys, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabs);

        //ImageView img = view.findViewById(R.id.logo_vis);
        //Picasso.get().load(R.drawable.logo4).centerInside().fit().into(img);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);
        getChildFragmentManager().beginTransaction().replace(R.id.home_fragment, new Frag_keys_about()).commit();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        Log.i("pager","1");
                        selectedFragment = new Frag_keys_about();
                        clearBackStack();
                    break;
                    case 1:
                        Log.i("pager","2");
                        selectedFragment = new Frag_keys_word();
                        clearBackStack();
                    break;
                    case 2:
                        Log.i("pager","3");
                        selectedFragment = new Frag_keys_touch();
                        clearBackStack();
                    break;
                    case 3:
                        Log.i("pager","4");
                        selectedFragment = new Frag_keys_time();
                        clearBackStack();
                    break;
                    case 4:
                        Log.i("pager","5");
                        selectedFragment = new Frag_keys_gift();
                        clearBackStack();
                    break;
                    case 5:
                        Log.i("pager","6");
                        selectedFragment = new Frag_keys_service();
                        clearBackStack();
                    break;
                }
                if(selectedFragment != null){
                    getChildFragmentManager()
                            .beginTransaction()
                            //.setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                            .replace(R.id.home_fragment, selectedFragment)
                            .commit();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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


    private void clearBackStack() {
        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

}
