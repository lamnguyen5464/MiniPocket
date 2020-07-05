package com.example.myapplication.wolit.model.tranferdetail;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.realm.RealmApdapter;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class WeeklyDetail extends RealmObject implements TransactionDetail {
    private double value;
    private String note = "";
    private static int NUM_OF_DAYS = 7;
    private String dayOfWeekCode = "";
    private DateType startDate, endDate;
    private static WeeklyDetail recentWeeklyDetail = null;
    public WeeklyDetail(){      //0 ->Sun
        this.dayOfWeekCode = "";
        for(int i = 0; i < NUM_OF_DAYS; i++) this.dayOfWeekCode += "0";
        startDate = new DateType();
        endDate = new DateType();
    }
    public static WeeklyDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new WeeklyDetail();
        }
        return recentWeeklyDetail;
    }
    public void switchDayOfWeek(int day){
        this.dayOfWeekCode = this.dayOfWeekCode.substring(0, day) + ((this.dayOfWeekCode.charAt(day) == '1')?"0":"1") + this.dayOfWeekCode.substring(day+1);
    }
    @Override
    public void setBasicInfo(double value, String note){
        this.value = value;
        this.note = note;
    }
    @Override
    public boolean generateTransaction(DateType fromDate, DateType toDate){
        DateType markFromDate = new DateType(fromDate);
        while (fromDate.getDateCode() <= toDate.getDateCode()) {
            if (this.getEndDate().getDateCode() < fromDate.getDateCode()) {
                //delete
                Log.d("@@@", "delete!");
                return false;
            } else {
                if (this.getStartDate().getDateCode() <= fromDate.getDateCode() && this.getDayOfWeekCode().charAt(fromDate.getDayOfWeek()) == '1') {
                    //the transaction happens on fromDate
                    NonRepeatedDetail.createAndAddToRealm(this.getValue(), this.getNote(), fromDate);
                    Log.d("@@@", "----->"+fromDate.getDate() + " " + fromDate.getMonth() + " " + fromDate.getYear() + "-" + this.getValue());
                }
            }
            fromDate.goToTheNextDay();
        }
        fromDate.set(markFromDate);
        return true;
    }
    public static void updateTransactionFromRealm(DateType fromDate, DateType toDate) {
//        //from Weeklydetail
        RealmResults<WeeklyDetail> resultsWeekly = RealmApdapter.getInstance().where(WeeklyDetail.class).findAll();
        Log.d("@@@", "Size of Weekly Details: " + resultsWeekly.size());
        for(int i = 0; i < resultsWeekly.size(); i++ ){
            WeeklyDetail st = resultsWeekly.get(i);
            Log.d("@@@", i + ") " + st.getValue() + " " + st.getNote() + " " + st.getDayOfWeekCode() + " " +st.getStartDate().getDateCode()+" "+st.getEndDate().getDateCode() );
        }
        for(int i = resultsWeekly.size()-1; i >= 0 ; i-- ) {
            WeeklyDetail tmp = resultsWeekly.get(i);
            if (!tmp.generateTransaction(fromDate, toDate)){
                tmp.removeFromDatabase();
            }
        }
    }

    @Override
    public void reset(){
        this.dayOfWeekCode = "";
        for(int i = 0; i < NUM_OF_DAYS; i++) this.dayOfWeekCode += "0";
        startDate.reset();
        endDate.reset();
    }
    @Override
    public boolean isNotValid(){
        if (this.value == 0 || this.note == "") return true;
        if (this.startDate.isEmptyDate()) return true;
        int countDays = 0;
        for(int i = 0; i < NUM_OF_DAYS; i++) if (this.dayOfWeekCode.charAt(i) == '1') countDays++;
        if (countDays == 0) return true;
        if (!this.endDate.isEmptyDate() && this.endDate.getDateCode() < this.startDate.getDateCode()) return true;
        return false;
    }

    @Override
    public void saveToDatabase() {
        final WeeklyDetail tmp = this;
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

    public void pickDateFromCalendar(final Context context, final Button btPicker){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (btPicker.getId() == R.id.startDate) startDate.set(dayOfMonth, month+1, year);
                else endDate.set(dayOfMonth, month+1, year);
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void eraseDate(Button btPicker){
        btPicker.setText("__/__/____");
        if (btPicker.getId() == R.id.startDate) startDate.reset();
        else endDate.reset();
    }

    public boolean getDayStatus(int day){
        if (day > 6) return false;
        return (this.dayOfWeekCode.charAt(day) == '1');
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

    public String getDayOfWeekCode() {
        return dayOfWeekCode;
    }

    public void setDayOfWeekCode(String dayOfWeekCode) {
        this.dayOfWeekCode = dayOfWeekCode;
    }
}
