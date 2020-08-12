package com.example.myapplication.wolit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.framents.main.FragmentCloudData;
import com.example.myapplication.wolit.framents.main.FragmentCurrentStatus;
import com.example.myapplication.wolit.framents.FragmentFunc;
import com.example.myapplication.wolit.framents.main.FragmentTransfer;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.PocketVers;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jakewharton.threetenabp.AndroidThreeTen;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.frag1:
                    FragmentFunc.loadFragment(new FragmentTransfer(), getSupportFragmentManager(), R.id.frameLayout);
                    return true;
                case R.id.frag2:
                    FragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(false, getSupportFragmentManager()), getSupportFragmentManager(),  R.id.frameLayout);
                    return true;
                case R.id.frag3:
                    FragmentFunc.loadFragment(new FragmentCloudData(), getSupportFragmentManager(), R.id.frameLayout);
                    return true;
            }
            return false;
        }
    };
    @Override
    public void onBackPressed() {
        //avoid backPress
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.frag2);
        FragmentFunc.loadFragment(FragmentCurrentStatus.getInstance(true, getSupportFragmentManager()), getSupportFragmentManager(),  R.id.frameLayout);



//        Log.d("@@@",CurrentStatus.getSharedValue().getUpToDate().getDate() + " " +CurrentStatus.getSharedValue().getUpToDate().getMonth());

//        DateType fromDate = new DateType(1, 7, 2020);
//        DateType toDate = new DateType(31, 7, 2020);
//        Log.d("@@@", fromDate.countDeltaDays(toDate)+"");
//        CurrentStatus.updateTransaction(fromDate, toDate);
//        RealmResults<NonRepeatedDetail> results = RealmApdapter.getInstance().where(NonRepeatedDetail.class).findAll();
//        for(int i = results.size()-1; i >= 0; i--){
//            results.get(i).removeFromDatabase();
//        }
    }

}
