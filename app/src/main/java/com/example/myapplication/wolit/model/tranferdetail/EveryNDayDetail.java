package com.example.myapplication.wolit.model.tranferdetail;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.model.DateType;
import com.example.myapplication.wolit.database.RealmApdapter;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static java.lang.Integer.parseInt;

public class EveryNDayDetail extends RealmObject implements TransactionDetail {
    private double value;
    private String note = "";
    private DateType startDate, endDate;
    private int numOfDays;
    private static EveryNDayDetail recentWeeklyDetail = null;
    public EveryNDayDetail(){
        numOfDays = 1;
        startDate = new DateType();
        endDate = new DateType();
    }
    public static EveryNDayDetail getRecentDetail(){
        if (recentWeeklyDetail == null){
            recentWeeklyDetail = new EveryNDayDetail();
        }
        return recentWeeklyDetail;
    }

    @Override
    public String is() {
        return "EveryNDayDetail";
    }
    @Override
    public void setBasicInfo(double value, String note){
        this.value = value;
        this.note = note;
    }


    @Override
    public void reset(){
        numOfDays = 1;
        startDate.reset();
        endDate.reset();
    }
    public void onClickPickDays(TextView labelDays, int delta){
        numOfDays = Integer.parseInt((String)labelDays.getText());
        if (numOfDays + delta < 1 || numOfDays + delta > 30) return;
        numOfDays += delta;
        labelDays.setText(numOfDays+"");
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
        final EveryNDayDetail tmp = this;
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
        while (fromDate.getDateCode() <= toDate.getDateCode()){
            if (this.getEndDate().getDateCode() < fromDate.getDateCode()){
                //delete
//                Log.d("@@@", "delete!");
                return  false;
            }else{
                if (this.getStartDate().getDateCode() <= fromDate.getDateCode() && this.getStartDate().countDeltaDays(fromDate) % this.getNumOfDays() == 0){
                    //the transaction happens on fromDate
                    NonRepeatedDetail.createAndAddToRealm(this.getValue(), this.getNote(), fromDate);
//                    Log.d("@@@", "----->"+fromDate.getDate() + " " + fromDate.getMonth() + " " + fromDate.getYear()+ "-" + this.getValue());
                }
            }
            fromDate.goToTheNextDay();
        }
        fromDate.set(markFromDate);
        return true;
    }
    public static void updateTransactionFromRealm(DateType fromDate, DateType toDate){
        //Every N days
        RealmResults<EveryNDayDetail> resultsEveryNDays = RealmApdapter.getInstance().where(EveryNDayDetail.class).findAll();
//        Log.d("@@@", "Size of Every N day: " + resultsEveryNDays.size());
//        for(int i = 0; i < resultsEveryNDays.size(); i++ ){
//            EveryNDayDetail st = resultsEveryNDays.get(i);
//            Log.d("@@@", i + ") " + st.getValue() + " " + st.getNote() + " " +st.getStartDate().getDateCode()+" "+st.getEndDate().getDateCode());
//        }
        for(int i = resultsEveryNDays.size() - 1; i >= 0 ; i-- ){
            EveryNDayDetail tmp = resultsEveryNDays.get(i);
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
                switch (btPicker.getId()){
                    case R.id.startDate:
                        startDate.set(dayOfMonth, month+1, year);
                        break;
                    case R.id.endDate:
                        endDate.set(dayOfMonth, month+1, year);
                        break;
                }
                btPicker.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                Toast.makeText(context, "Hold button to remove this date", Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void eraseDate(Button bt){
        bt.setText("__/__/____");
        switch (bt.getId()){
            case R.id.startDate:
                startDate.reset();
                break;
            case R.id.endDate:
                endDate.reset();
                break;
        }
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

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
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
