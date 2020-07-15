package com.example.myapplication.wolit.model.tranferdetail;

import android.util.Log;

import com.example.myapplication.wolit.model.CurrentStatus;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.realm.RealmApdapter;

import io.realm.Realm;
import io.realm.RealmObject;

public class NonRepeatedDetail extends RealmObject implements TransactionDetail {
    private double value;
    private String note = "";
    private DateType date;
    private static NonRepeatedDetail recentWeeklyDetail = null;
    public NonRepeatedDetail(){
        date = new DateType();
    }
    public static NonRepeatedDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new NonRepeatedDetail();
        }
        return recentWeeklyDetail;
    }
    @Override
    public void setBasicInfo(double value, String note){
        this.value = value;
        this.note = note;
    }

    @Override
    public boolean generateTransaction(DateType fromDate, DateType toDate) {
        return true;
    }

    @Override
    public DateType getStartDate() {
        return null;
    }

    @Override
    public DateType getEndDate() {
        return null;
    }

    @Override
    public String is() {
        return "NonRepeatedDetail";
    }

    @Override
    public void reset(){
        date.reset();
    }

    @Override
    public boolean isNotValid() {
//        Log.d("@@@",value+" " + note+ " " + date.getString());
//        Log.d("@@@",this.value+"_" + this.note+ " " + this.date.getString());
        if (this.value == 0 || this.note == "") return true;
        if (this.date.isEmptyDate()) return true;
        return false;
    }

    @Override
    public void saveToDatabase() {
        final NonRepeatedDetail tmp = this;
        RealmApdapter.getInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmApdapter.getInstance().copyToRealm(tmp);
            }
        });

        //update to currentMoney
        CurrentStatus currentStatus = CurrentStatus.getSharedValue();
        if (currentStatus != null) {
            currentStatus.updateMoney(currentStatus.getMyMoney() + this.value, currentStatus.getCurrencyType());
        }
    }

    @Override
    public void removeFromDatabase() {
        //update currentMoney
        CurrentStatus currentStatus = CurrentStatus.getSharedValue();
        if (currentStatus != null){
            currentStatus.updateMoney(currentStatus.getMyMoney() - this.value, currentStatus.getCurrencyType());
        }

        RealmApdapter.getInstance().beginTransaction();
        this.getDate().deleteFromRealm();
        this.deleteFromRealm();
        RealmApdapter.getInstance().commitTransaction();
    }
    public static void updateTransactionFromRealm(DateType fromDate, DateType toDate) {

    }
    public static void createAndAddToRealm(double value, String note, DateType date){
        NonRepeatedDetail tmp = new NonRepeatedDetail();
        tmp.setBasicInfo(value, note);
        tmp.date = date;
        tmp.saveToDatabase();
    }
    public DateType getDate() {
        return date;
    }

    public void setDate(DateType date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
