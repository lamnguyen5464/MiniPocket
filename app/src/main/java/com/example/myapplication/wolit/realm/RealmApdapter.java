package com.example.myapplication.wolit.realm;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmApdapter {
    private static Realm instance = null;

    public static void initIntance(Context context) {
        Realm.init(context);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("myrealm1.realm")
                .schemaVersion(1)
                .build();
        instance = Realm.getInstance(myConfig);
    }
    public static Realm getInstance(){
        return instance;
    }


}
