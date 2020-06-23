package com.example.myapplication.wolit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.framents.FragmentCurrentStatus;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.framents.FragmentTransfer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FragmentFunc fragmentFunc = new FragmentFunc();
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.frag1:
                    fragmentFunc.loadFragment(FragmentTransfer.getInstance(), getSupportFragmentManager(), R.id.frameLayout);
                    return true;
                case R.id.frag2:
                    fragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(), getSupportFragmentManager(),  R.id.frameLayout);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.frag2);
        fragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(), getSupportFragmentManager(),  R.id.frameLayout);

    }

}
