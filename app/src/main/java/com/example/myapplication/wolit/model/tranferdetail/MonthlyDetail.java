package com.example.myapplication.wolit.model.tranferdetail;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.realm.RealmApdapter;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MonthlyDetail extends RealmObject implements TransactionDetail {
    private double value;
    private String note = "";
    private DateType startDate, endDate;
    private int dateOfMonth;
    private static MonthlyDetail recentWeeklyDetail = null;
    public MonthlyDetail(){
        dateOfMonth = 1;
        startDate = new DateType();
        endDate = new DateType();
    }
    public static MonthlyDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new MonthlyDetail();
        }
        return recentWeeklyDetail;
    }
    @Override
    public String is() {
        return "MonthlyDetail";
    }
    @Override
    public void setBasicInfo(double value, String note){
        this.value = value;
        this.note = note;
    }

    @Override
    public void reset(){
        dateOfMonth = 1;
        startDate.reset();
        endDate.reset();
    }
    @Override
    public boolean isNotValid(){
        if (value == 0 || note == "") return true;
        if (startDate.isEmptyDate()) return true;
        if (!endDate.isEmptyDate() && endDate.getDateCode() < startDate.getDateCode()) return true;
        return false;
    }

    @Override
    public void saveToDatabase() {
        final MonthlyDetail tmp = this;
        RealmApdapter.getInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmApdapter.getInstance().copyToRealm(tmp);
            }
        });
    }

    @Override
    public void removeFromDatabase() {
        RealmApdapter.getInstance().beginTransaction();
        this.getStartDate().deleteFromRealm();
        this.getEndDate().deleteFromRealm();
        this.deleteFromRealm();
        RealmApdapter.getInstance().commitTransaction();
    }

    @Override
    public boolean generateTransaction(DateType fromDate, DateType toDate) {
        DateType markFromDate = new DateType(fromDate);
        while (fromDate.getDateCode() <= toDate.getDateCode()) {
            if (this.getEndDate().getDateCode() < fromDate.getDateCode()) {
                //delete
//                Log.d("@@@", "delete!");
                return false;
            } else {
                if (this.getStartDate().getDateCode() <= fromDate.getDateCode() && this.getDateOfMonth() == fromDate.getDate()) {
                    //the transaction happens on fromDate
                    NonRepeatedDetail.createAndAddToRealm(this.getValue(), this.getNote(), fromDate);
//                    Log.d("@@@", "----->"+fromDate.getDate() + " " + fromDate.getMonth() + " " + fromDate.getYear() + "-" + this.getValue());
                }
            }
            fromDate.goToTheNextDay();
        }
        fromDate.set(markFromDate);
        return true;
    }
    public static void updateTransactionFromRealm(DateType fromDate, DateType toDate){
        //Monthly
        RealmResults<MonthlyDetail> resultsMonthly = RealmApdapter.getInstance().where(MonthlyDetail.class).findAll();
        //log
//        Log.d("@@@", "Size of monthlyDetail: " + resultsMonthly.size());
//        for(MonthlyDetail tmp: resultsMonthly ){
//            Log.d("@@@", ") " + tmp.getValue() + " " + tmp.getNote() + " " +tmp.getDateOfMonth()+" " +tmp.getStartDate().getDateCode()+" "+tmp.getEndDate().getDateCode());
//        }
        for(int i = resultsMonthly.size()-1; i >= 0 ; i-- ){
            MonthlyDetail tmp = resultsMonthly.get(i);
            DateType tmpDate = new DateType(fromDate);
            if (tmpDate.isEmptyDate()) tmpDate.set(tmp.getStartDate());
            if (!tmp.generateTransaction(tmpDate, toDate)){
                tmp.removeFromDatabase();
            }
        }
    }
    public void pickDateFromCalendar(final Context context, final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                switch (btPicker.getId()){
                    case R.id.dateOfMonth:
                        dateOfMonth = dayOfMonth;
                        btPicker.setText(dayOfMonth+"");
                        break;
                    case R.id.startDate:
                        startDate.set(dayOfMonth, month+1, year);
                        break;
                    case R.id.endDate:
                        endDate.set(dayOfMonth, month+1, year);
                        break;
                }
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void eraseDate(Button bt){
        bt.setText("__/__/____");
        switch (bt.getId()){
            case R.id.dateOfMonth:
                dateOfMonth = 1;
                bt.setText("1");
                break;
            case R.id.startDate:
                startDate.reset();
                break;
            case R.id.endDate:
                endDate.reset();
                break;
        }
    }

    public int getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(int dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public DateType getStartDate() {
        return startDate;
    }

    public void setStartDate(DateType startDate) {
        this.startDate = startDate;
    }

    public DateType getEndDate() {
        return endDate;
    }

    public void setEndDate(DateType endDate) {
        this.endDate = endDate;
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
