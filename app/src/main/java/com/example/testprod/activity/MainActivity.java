package com.example.testprod.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.testprod.R;
import com.example.testprod.fragment.*;
import com.example.testprod.general.utility;
import com.example.testprod.home.Frag_keys;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    int counter;
    utility gu;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gu = new utility(MainActivity.this);
        gu.update_login_state_shared_preferences();


        bottomNav = findViewById(R.id.bottomNav);
        Integer first = getIntent().getIntExtra("first",0);
        if(first ==1){
            bottomNav.setSelectedItemId(R.id.nav_key);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_keys()).commit();
        }else {
            bottomNav.setSelectedItemId(R.id.nav_Home);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_home()).commit();
        }

        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_Home:
                    selectedFragment = new Frag_home();
                    clearBackStack();
                    break;

                case R.id.nav_Offer:
                    Bundle newBundle = new Bundle();
                    newBundle.putString("id",gu.get_data_string(gu.get_data("Login_State"),"id"));
                    newBundle.putBoolean("local_or_not",true);
                    selectedFragment = new Frag_profile();
                    selectedFragment.setArguments(newBundle);
                    clearBackStack();
                    break;

                case R.id.nav_tank:
                    selectedFragment = new Frag_tank();
                    clearBackStack();
                    break;

                case R.id.nav_key:
                    selectedFragment = new Frag_keys();
                    clearBackStack();
                    break;

            }
            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).replace(R.id.main_fragment, selectedFragment).commit();
            }
            return true;
        }
    };


    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ani1, R.anim.ani2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == Activity.RESULT_OK)){
            update();
        }
        if ((requestCode == 200) && (resultCode == Activity.RESULT_OK)){
            update_to_keys();
        }
    }

    private void update() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_commitment_list) {
            getSupportFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

    private void update_to_keys() {
        bottomNav.setSelectedItemId(R.id.nav_key);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new Frag_keys()).commit();
    }

}
