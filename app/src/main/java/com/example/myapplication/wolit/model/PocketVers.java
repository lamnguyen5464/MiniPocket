package com.example.myapplication.wolit.model;

import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.tranferdetail.NonRepeatedDetail;

import io.realm.Realm;
import io.realm.RealmObject;

public class PocketVers extends RealmObject {
    private String label;

    private static PocketVers currentVers = null;

    public static PocketVers getCurrentVers(){
        if (currentVers == null){
            currentVers = new PocketVers();
        }
        return currentVers;
    }


    public PocketVers(String label){
        this.label = label;
    }
    public PocketVers(){
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static void addNewPocket(final String label){
        RealmApdapter.getInstancePocket().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PocketVers tmp = new PocketVers(label);
                RealmApdapter.getInstancePocket().copyToRealm(tmp);
            }
        });
        RealmApdapter.switchInstanceToVers(label);
//        NonRepeatedDetail.createAndAddToRealm(0, "Initially create", DateType.getToday());
    }
    public void removeFromDataBase(){
        RealmApdapter.getInstancePocket().beginTransaction();
        this.deleteFromRealm();
        RealmApdapter.getInstancePocket().commitTransaction();
    }
}
