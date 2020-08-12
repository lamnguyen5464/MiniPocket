package com.example.myapplication.wolit.database;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.wolit.model.PocketVers;
import com.example.myapplication.wolit.model.tranferdetail.EveryNDayDetail;
import com.example.myapplication.wolit.model.tranferdetail.MonthlyDetail;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;
import com.example.myapplication.wolit.model.tranferdetail.TransactionDetail;
import com.example.myapplication.wolit.model.tranferdetail.WeeklyDetail;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmApdapter {
    private static Realm instance = null;
    private static Realm instancePocket = null;
    static RealmResults<WeeklyDetail> realmWeekly;
    static RealmResults<MonthlyDetail> realmMonthly;
    static RealmResults<EveryNDayDetail> realmEveryNDay;
    static final String POCKET_TAG = "pocket";
    public static final String MAIN_POCKET = "Main pocket";
    public static void initIntance(Context context) {
        Realm.init(context);

        RealmConfiguration myConfigPockets = new RealmConfiguration.Builder()
                .name("my_pockets_list.realm")
                .schemaVersion(1)
                .build();

        instancePocket = Realm.getInstance(myConfigPockets);

        if (getPockets().size() == 0){
            PocketVers.addNewPocket(MAIN_POCKET);
        }
        switchInstanceToVers(MAIN_POCKET);
    }
    public static Realm getInstance(){
        return instance;
    }public static Realm getInstancePocket(){
        return instancePocket;
    }
    public static RealmResults<PocketVers> getPockets(){
        return getInstancePocket().where(PocketVers.class).findAll();
    }
    public static void eraseAll(Realm realm){
        RealmResults<WeeklyDetail> weeklyDetails = realm.where(WeeklyDetail.class).findAll();
        for(WeeklyDetail tmp : weeklyDetails){
            realm.beginTransaction();
            tmp.getStartDate().deleteFromRealm();
            tmp.getEndDate().deleteFromRealm();
            tmp.deleteFromRealm();
            realm.commitTransaction();
        }

        RealmResults<MonthlyDetail> monthlyDetails = realm.where(MonthlyDetail.class).findAll();
        for(MonthlyDetail tmp : monthlyDetails){
            realm.beginTransaction();
            tmp.getStartDate().deleteFromRealm();
            tmp.getEndDate().deleteFromRealm();
            tmp.deleteFromRealm();
            realm.commitTransaction();
        }

        RealmResults<EveryNDayDetail> everyNDayDetails = realm.where(EveryNDayDetail.class).findAll();
        for(EveryNDayDetail tmp : everyNDayDetails){
            realm.beginTransaction();
            tmp.getStartDate().deleteFromRealm();
            tmp.getEndDate().deleteFromRealm();
            tmp.deleteFromRealm();
            realm.commitTransaction();
        }
        eraseAllNonRepeated(realm);
    }

    public static void eraseAllNonRepeated(Realm realm){
        RealmResults<NonRepeatedDetail> curTransactions = realm.where(NonRepeatedDetail.class).findAll();

        for(NonRepeatedDetail trans : curTransactions){
            //delete from old
            realm.beginTransaction();

//            Log.d("@@@", trans.getValue() + " " +trans.getDate());

            trans.getDate().deleteFromRealm();
            trans.deleteFromRealm();

            realm.commitTransaction();
        }
    }
    public static void mergeAllToMain(PocketVers currentVers){
        switchInstanceToVers(MAIN_POCKET);
        Realm currentRealm = getRealm(currentVers.getLabel());
        RealmResults<NonRepeatedDetail> curTransactions = currentRealm.where(NonRepeatedDetail.class).findAll();

        for(NonRepeatedDetail trans : curTransactions){
            //add to main
            trans.saveToDatabase();
        }
        eraseAllNonRepeated(currentRealm);

    }
    public static Realm getRealm(String label){
        return Realm.getInstance(new RealmConfiguration.Builder()
                .name(POCKET_TAG + label +".realm")
                .schemaVersion(1)
                .build()
        );
    }
    public static void switchInstanceToVers(String label){
        instance = getRealm(label);
        PocketVers.getCurrentVers().setLabel(label);
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
    public static RealmResults<NonRepeatedDetail> getAllNonRepeatedList(){
        return RealmApdapter.getInstance().where(NonRepeatedDetail.class).sort("date.dateCode").findAll();
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
