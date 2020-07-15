package com.example.myapplication.wolit.realm;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;

import org.threeten.bp.Month;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmApdapter {
    private static Realm instance = null;
    private static RealmApdapter sharedValue = null;
    static RealmResults<WeeklyDetail> realmWeekly;
    static RealmResults<MonthlyDetail> realmMonthly;
    static RealmResults<EveryNDayDetail> realmEveryNDay;
    public static void initIntance(Context context) {
        Realm.init(context);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("myrealm3.realm")
                .schemaVersion(1)
                .build();
        instance = Realm.getInstance(myConfig);
    }
    public static Realm getInstance(){
        return instance;
    }
    public static RealmApdapter getSharedValue() {
        return sharedValue;
    }

    public static void updateRealmResult(){
        realmWeekly = RealmApdapter.getInstance().where(WeeklyDetail.class).findAll();
        realmMonthly = RealmApdapter.getInstance().where(MonthlyDetail.class).findAll();
        realmEveryNDay = RealmApdapter.getInstance().where(EveryNDayDetail.class).findAll();
    }
    public static List<TransactionDetail> getPendingList(){
        List<TransactionDetail> list = new ArrayList<>();

        updateRealmResult();

        List<WeeklyDetail> listWeekly = (RealmApdapter.getInstance().copyFromRealm(realmWeekly));
        for(TransactionDetail tmp : listWeekly){
            list.add(tmp);
        }
        List<MonthlyDetail> listMonthly = (RealmApdapter.getInstance().copyFromRealm(realmMonthly));
        for(TransactionDetail tmp : listMonthly){
            list.add(tmp);
        }
        List<EveryNDayDetail> listEveryNDay = (RealmApdapter.getInstance().copyFromRealm(realmEveryNDay));
        for(TransactionDetail tmp : listEveryNDay){
            list.add(tmp);
        }
        return list;
    }
    public static void removeFromCombiningList(int pos){
        if (realmWeekly.size() + realmMonthly.size() <= pos){
            realmEveryNDay.get(pos - (realmWeekly.size() + realmMonthly.size())).removeFromDatabase();
            return;
        }
        if (realmWeekly.size() <= pos){
            realmMonthly.get(pos - (realmWeekly.size())).removeFromDatabase();
            return;
        }
        realmWeekly.get(pos).removeFromDatabase();
    }


}
